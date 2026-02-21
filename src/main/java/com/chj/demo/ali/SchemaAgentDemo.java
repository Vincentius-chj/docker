package com.chj.demo.ali;

import cn.hutool.json.JSONUtil;
import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.checkpoint.savers.MemorySaver;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import com.chj.entity.PoemOutput;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.stereotype.Component;

@Component
public class SchemaAgentDemo {

    @Resource
    private ChatModel dashScopeChatModel;

    /**
     * 选择建议：
     * outputType：类型安全，适合结构固定的场景（推荐）
     * outputSchema：使用 BeanOutputConverter 生成时提供类型安全，手动提供字符串时灵活性高，适合动态或复杂的输出格式
     */
    public String run() {
        // 使用 BeanOutputConverter 生成 outputSchema
        BeanOutputConverter<PoemOutput> outputConverter = new BeanOutputConverter<>(PoemOutput.class);
        String format = outputConverter.getFormat();

        ReactAgent agent = ReactAgent.builder()
                .name("poem_agent")
                .model(dashScopeChatModel)
                // 结构化输出
//                .outputType(PoemOutput.class)
                .outputSchema(format)
                .saver(new MemorySaver())
                .build();

        AssistantMessage response;
        try {
            response = agent.call("写一首关于春天的诗");
        } catch (GraphRunnerException e) {
            throw new RuntimeException(e);
        }
        return response.getText();
    }
}
