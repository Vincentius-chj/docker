package com.chj.demo;

import cn.hutool.core.lang.UUID;
import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.chj.entity.ActorFilms;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplateActions;
import org.springframework.ai.chat.prompt.PromptTemplateMessageActions;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ChatClientDemoTest {

    @Resource
    private ChatClientDemo chatClientDemo;

    /**
     * 大模型调用
     */
    @Test
    void client1() {
        String chatId = UUID.randomUUID().toString(true);
        String response = chatClientDemo.client1("今天茂名的天气怎么样？", chatId);
        System.out.println(response);
        assertNotNull(response);
    }

    /**
     * 使用专用模板类构造系统提示词
     */
    @Test
    void client2() {
        String chatId = UUID.randomUUID().toString(true);

        String userText = "明天茂名的天气怎么样？";
        UserMessage userMessage = new UserMessage(userText);
        // 使用专用模板类构造系统提示词
        String systemText = "You are an expert in {job}, and you can {action}.";
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemText);
        Message systemMessage = systemPromptTemplate.createMessage(Map.of("job", "weather", "action", "forecast"));

        Prompt prompt = new Prompt(systemMessage, userMessage);

        String response = chatClientDemo.client2(prompt, chatId);
        System.out.println(response);
        assertNotNull(response);
    }

    /**
     * 结构化输出
     */
    @Test
    void client3() {
        String chatId = UUID.randomUUID().toString(true);
        ActorFilms actorFilms = chatClientDemo.client3("Generate the filmography for a random actor.", chatId);
        System.out.println(actorFilms);
        assertNotNull(actorFilms);
    }

    /**
     * 流式处理，返回 String
     */
    @Test
    void client4() {
        String chatId = UUID.randomUUID().toString(true);
        Flux<String> response = chatClientDemo.client4("你是谁？", chatId);
        response.doOnNext(System.out::println) // 每当流中产生一个新元素时，将其打印到控制台
                .doOnComplete(() -> System.out.println("流处理完成"))
                .doOnError(error -> System.err.println("流处理出错: " + error.getMessage()))
                .blockLast(); // 阻塞当前线程，直到流处理结束，并返回最后一个元素
        assertNotNull(response);
    }

    /**
     * 流式处理，返回 ChatResponse
     */
    @Test
    void client5() {
        String chatId = UUID.randomUUID().toString(true);
        Flux<ChatResponse> response = chatClientDemo.client5("你是谁？", chatId);
        response.doOnNext(chatResponse -> System.out.println(chatResponse.getResult().getOutput().getText()))
                .doOnComplete(() -> System.out.println("流处理完成"))
                .doOnError(error -> System.err.println("流处理出错: " + error.getMessage()))
                .blockLast();
        assertNotNull(response);
    }

    /**
     * 流式结构化输出
     */
    @Test
    void client6() {
        String chatId = UUID.randomUUID().toString(true);
        String message1 = """
                  Generate the filmography for a random actor.
                  {format}
                """;
        String message2 = """
                  Generate the filmography for a random actor.
                  <format>
                """;
        List<ActorFilms> actorFilms = chatClientDemo.client6(message2, chatId);
        System.out.println(actorFilms);
    }
}