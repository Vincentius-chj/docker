package com.chj.advisor;

import com.chj.entity.ActorFilms;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.ai.chat.client.ChatClientMessageAggregator;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import org.springframework.core.Ordered;
import reactor.core.publisher.Flux;

/**
 * 自定义日志拦截器，日志级别为 Info，仅打印请求和响应
 */
@Slf4j
public class MyLoggerAdvisor implements CallAdvisor, StreamAdvisor {

    private void before(ChatClientRequest chatClientRequest) {
        log.info("AI Request: {}", chatClientRequest.prompt().getUserMessage().getText());
    }

    private void observeAfter(ChatClientResponse chatClientResponse) {
        log.info("AI Response: {}", chatClientResponse.chatResponse().getResult().getOutput().getText());
    }

    @NotNull
    @Override
    public ChatClientResponse adviseCall(@NotNull ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        this.before(chatClientRequest);
        ChatClientResponse advisedResponse = callAdvisorChain.nextCall(chatClientRequest);
        this.observeAfter(advisedResponse);
        return advisedResponse;
    }

    @NotNull
    @Override
    public Flux<ChatClientResponse> adviseStream(@NotNull ChatClientRequest chatClientRequest, StreamAdvisorChain streamAdvisorChain) {
        this.before(chatClientRequest);
        Flux<ChatClientResponse> advisedResponse = streamAdvisorChain.nextStream(chatClientRequest);
        return new ChatClientMessageAggregator().aggregateChatClientResponse(advisedResponse, this::observeAfter);
    }

    @NotNull
    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int getOrder() {
//        return Ordered.LOWEST_PRECEDENCE - 1;
        return -1;
    }
}
