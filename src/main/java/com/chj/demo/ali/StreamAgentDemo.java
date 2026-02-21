package com.chj.demo.ali;

import com.alibaba.cloud.ai.graph.NodeOutput;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.checkpoint.savers.MemorySaver;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import com.alibaba.cloud.ai.graph.streaming.OutputType;
import com.alibaba.cloud.ai.graph.streaming.StreamingOutput;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class StreamAgentDemo {

    @Resource
    private ChatModel dashScopeChatModel;

    public void run() {
        ReactAgent agent = ReactAgent.builder()
                .name("poem_agent")
                .model(dashScopeChatModel)
                .saver(new MemorySaver())
                .build();

        Flux<NodeOutput> stream;
        try {
            stream = agent.stream("你是谁？");
        } catch (GraphRunnerException e) {
            throw new RuntimeException(e);
        }
        /*
         * 在 Agent 场景中，流式输出的核心是处理 StreamingOutput 类型
         *           无论是模型推理、工具调用还是 Hook 节点，输出都统一为这个类型
         */
        /*
         * 对于 Hook 等通常非流式的节点，直接使用 AGENT_HOOK_FINISHED 读取内容即可
         * 并非所有节点输出都有意义，尤其是 Hook 节点可能产生无效输出，建议通过 OutputType 进行过滤
         */
        stream.subscribe(
                output -> {
                    // 检查是否为 StreamingOutput 类型
                    if (output instanceof StreamingOutput streamingOutput) {
                        OutputType type = streamingOutput.getOutputType();

                        // 处理模型推理的流式输出
                        if (type == OutputType.AGENT_MODEL_STREAMING) {
                            // 流式增量内容，逐步显示
                            System.out.print(streamingOutput.message().getText());
                        } else if (type == OutputType.AGENT_MODEL_FINISHED) {
                            // 模型推理完成，可获取完整响应
                            System.out.println("\n模型输出完成");
                        }

                        // 处理工具调用完成（目前不支持 STREAMING）
                        if (type == OutputType.AGENT_TOOL_FINISHED) {
                            System.out.println("工具调用完成: " + output.node());
                        }

                        // 对于 Hook 节点，通常只关注完成事件（如果Hook没有有效输出可以忽略）
                        if (type == OutputType.AGENT_HOOK_FINISHED) {
                            System.out.println("Hook 执行完成: " + output.node());
                        }
                    }
                },
                error -> System.err.println("错误: " + error),
                () -> System.out.println("Agent 执行完成")
        );
    }
}
