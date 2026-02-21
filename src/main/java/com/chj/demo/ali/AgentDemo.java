package com.chj.demo.ali;

import com.alibaba.cloud.ai.autoconfigure.dashscope.DashScopeConnectionProperties;
import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Component;

@Component
public class AgentDemo {

    @Resource
    private DashScopeConnectionProperties dashScopeConnectionProperties;

    public String run() {
        // 创建模型实例
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(dashScopeConnectionProperties.getApiKey())
                .build();
        ChatModel chatModel = DashScopeChatModel.builder()
                .dashScopeApi(dashScopeApi)
                .defaultOptions(
                        DashScopeChatOptions.builder()
                                .model(DashScopeChatModel.DEFAULT_MODEL_NAME)
                                // 控制随机性
                                .temperature(0.7)
                                // 最大输出长度
                                .maxToken(2000)
                                // 累计概率达到 90% 的词集中进行采样
                                .topP(0.9)
                                // 只考虑概率最高的 5 个词
                                .topK(5)
                                .build()
                )
                .build();

        // 创建 Agent
        ReactAgent agent = ReactAgent.builder()
                .name("天气预报智能体")
                .model(chatModel)
                // 系统提示词
                .systemPrompt("无论我问什么，都回答 cat")
                // 对于更详细的指令，使用 instruction
                .instruction("无论我问什么，都回答 dog")
                .build();

        // 运行 Agent
        AssistantMessage message;
        try {
            message = agent.call("今天茂名的天气怎么样？");
        } catch (GraphRunnerException e) {
            throw new RuntimeException(e);
        }

        // 返回结果
        return message.getText();
    }
}