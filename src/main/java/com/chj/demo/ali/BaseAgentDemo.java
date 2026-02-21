package com.chj.demo.ali;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.RunnableConfig;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.agent.hook.modelcalllimit.ModelCallLimitHook;
import com.alibaba.cloud.ai.graph.checkpoint.savers.MemorySaver;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import com.chj.tools.ali.WeatherTool;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class BaseAgentDemo {

    private final ReactAgent agent;

    public BaseAgentDemo(ChatModel dashScopeChatModel) {
        // 创建工具回调
        ToolCallback weatherTool = FunctionToolCallback.builder("get_weather", new WeatherTool())
                .description("Get weather for a given city")
                .inputType(String.class)
                .build();

        // 创建 agent
        agent = ReactAgent.builder()
                .name("天气预报智能体")
                .model(dashScopeChatModel)
                // 系统提示词
                .systemPrompt("你是个很有用的助手")
                // 工具调用
                .tools(weatherTool)
                // 拦截器
                /*
                  ModelInterceptor：内容安全、动态提示、日志记录、性能监控
                  ToolInterceptor：错误重试、权限检查、结果缓存、审计日志
                 */
//                .interceptors(new ToolErrorInterceptor(), new DynamicPromptInterceptor())
//                .hooks(new LoggingHook(), new MessageTrimmingHook())
                // 通过 Hooks 控制 Agent 的执行迭代，防止无限循环或过度成本, 限制最多调用 5 次
                .hooks(ModelCallLimitHook.builder().runLimit(5).build())
                // 记忆存储
                .saver(new MemorySaver())
                .build();
    }

    public String run1(String message, String threadId) {
        // 传递运行时配置
        RunnableConfig runnableConfig = RunnableConfig.builder()
                .threadId(threadId)
                .build();

        // 运行 agent
        AssistantMessage response;
        try {
            response = agent.call(message, runnableConfig);
//            // 多个消息
//            List<Message> messages = List.of(
//                    new UserMessage("我想了解 Java 多线程"),
//                    new UserMessage("特别是线程池的使用")
//            );
//            response = agent.call(messages);
        } catch (GraphRunnerException e) {
            throw new RuntimeException(e);
        }

        // 返回结果
        return response.getText();
    }

    public String run2(String message, String threadId) {
        RunnableConfig runnableConfig = RunnableConfig.builder()
                .threadId(threadId)
                .addMetadata("user_role", "expert")
                .build();

        Optional<OverAllState> result;

        try {
            // 使用 invoke 方法获取完整的执行状态
            result = agent.invoke(message, runnableConfig);
        } catch (GraphRunnerException e) {
            throw new RuntimeException(e);
        }
        if (result.isPresent()) {
            OverAllState state = result.get();

            // 访问消息历史
            Optional<Object> messages = state.value("messages");
            List<Message> messageList = (List<Message>) messages.get();

//            // 访问自定义状态
//            Optional<Object> customData = state.value("custom_key");

            return messageList.stream().map(Message::getText).collect(Collectors.joining("\n"));
        }
        return "fail";
    }

}
