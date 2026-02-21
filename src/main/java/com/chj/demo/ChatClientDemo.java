package com.chj.demo;

import com.chj.entity.ActorFilms;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.AdvisorParams;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.template.NoOpTemplateRenderer;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ChatClientDemo {

    @Resource
    private ChatClient dashScopeChatClient;


    public String client1(String message, String chatId) {
        return dashScopeChatClient.prompt()
                .user(message)
                .call()
                .content();
    }

    public String client2(Prompt prompt, String chatId) {
        return dashScopeChatClient.prompt(prompt)
                .call()
                .content();
    }

    public ActorFilms client3(String message, String chatId) {
        return dashScopeChatClient.prompt()
                // 启用原生结构化输出
//                .advisors(AdvisorParams.ENABLE_NATIVE_STRUCTURED_OUTPUT)
                .user(message)
                .call()
                .entity(ActorFilms.class);
    }

    public Flux<String> client4(String message, String chatId) {
        return dashScopeChatClient.prompt()
                .user(message)
                .stream()
                .content();
    }

    public Flux<ChatResponse> client5(String message, String chatId) {
        return dashScopeChatClient.prompt()
                .user(message)
                .stream()
                .chatResponse();
    }

    public List<ActorFilms> client6(String message, String chatId) {
        BeanOutputConverter<List<ActorFilms>> converter = new BeanOutputConverter<>(new ParameterizedTypeReference<>() {
        });

        Flux<String> response = this.dashScopeChatClient.prompt()
//                .templateRenderer(new NoOpTemplateRenderer())
                .user(u -> u.text(message).param("format", converter.getFormat()))
                .templateRenderer(StTemplateRenderer.builder().startDelimiterToken('<').endDelimiterToken('>').build())
                .stream()
                .content();

        String content = String.join("", Objects.requireNonNull(response.collectList().block()));

        return converter.convert(content);
    }
}
