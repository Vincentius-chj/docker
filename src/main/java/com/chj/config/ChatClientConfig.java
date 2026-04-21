package com.chj.config;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.chj.advisor.MyLoggerAdvisor;
import com.chj.repository.MySqlChatMemoryRepository;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    @Resource
    private MySqlChatMemoryRepository mySqlChatMemoryRepository;

    @Bean
    public ChatClient dashScopeChatClient(DashScopeChatModel chatModel) {
        // 对话记忆
        MessageChatMemoryAdvisor messageChatMemoryAdvisor = MessageChatMemoryAdvisor
                .builder(MessageWindowChatMemory
                        .builder()
//                        .chatMemoryRepository(mySqlChatMemoryRepository)
                        .maxMessages(20)
                        .build()
                )

                .build();
        return ChatClient.builder(chatModel)
                .defaultAdvisors(messageChatMemoryAdvisor, new MyLoggerAdvisor())
                .build();
    }

    @Bean
    public ChatClient deepSeekChatClient(DeepSeekChatModel chatModel) {
        return ChatClient.create(chatModel);
    }

    @Bean
    public ChatClient ollamaChatClient(OllamaChatModel chatModel) {
        return ChatClient.create(chatModel);
    }
}