package com.chj.interceptor;

import com.alibaba.cloud.ai.graph.agent.interceptor.ToolCallHandler;
import com.alibaba.cloud.ai.graph.agent.interceptor.ToolCallRequest;
import com.alibaba.cloud.ai.graph.agent.interceptor.ToolCallResponse;
import com.alibaba.cloud.ai.graph.agent.interceptor.ToolInterceptor;
import lombok.extern.slf4j.Slf4j;

/**
 * 工具错误处理拦截器
 */
@Slf4j
public class ToolErrorInterceptor extends ToolInterceptor {
    @Override
    public ToolCallResponse interceptToolCall(ToolCallRequest request, ToolCallHandler handler) {
        try {
            // 调用工具之前
            log.info("Tool call: {}", request);
            ToolCallResponse response = handler.call(request);
            // 调用工具之后
            log.info("Tool call successful: {}", response);
            return response;
        } catch (Exception e) {
            return ToolCallResponse.of(
                    request.getToolCallId(),
                    request.getToolName(),
                    "Tool failed: " + e.getMessage()
            );
        }
    }

    @Override
    public String getName() {
        return "ToolErrorInterceptor";
    }
}