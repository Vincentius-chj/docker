package com.chj.demo;

import com.alibaba.cloud.ai.autoconfigure.dashscope.DashScopeConnectionProperties;
import com.chj.advisor.MyLoggerAdvisor;
import com.chj.advisor.ReReadingAdvisor;
import com.chj.config.DashScopeCloudAdvisor;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.content.Media;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

import java.net.URI;

@Component
public class AdvisorsDemo {

    @Resource
    private ChatClient dashScopeChatClient;
    @Resource
    private ChatClient deepSeekChatClient;
    @Resource
    private ChatClient ollamaChatClient;
    @Resource
    private VectorStore dashScopeCloudVectorStore;
    @Resource
    private DashScopeConnectionProperties dashScopeConnectionProperties;
    @Resource
    private SimpleVectorStore simpleVectorStore;

    public String client1(String message, String chatId) {
        return dashScopeChatClient.prompt()
                .advisors(new ReReadingAdvisor(), new MyLoggerAdvisor())
                .user(message)
                .call()
                .content();
    }

    public String client2(String message, String chatId) {
        return dashScopeChatClient.prompt()
                .advisors(advisorSpec -> {
                    advisorSpec.param(ChatMemory.CONVERSATION_ID, chatId);
                })
                .user(message)
                .call()
                .content();
    }

    private final String url = "https://bilibili-video-core.oss-cn-beijing.aliyuncs.com/012e010a-79dc-4920-92e9-068a0dc47451.png";

    public String client3(String message, String chatId) {
        ClassPathResource imageResource = new ClassPathResource("static/img1.png");
        UserMessage userMessage = UserMessage.builder()
                .text(message)
                .media(Media.builder().mimeType(MimeTypeUtils.IMAGE_PNG).data(URI.create(url)).build())
                .build();
//        return dashScopeChatClient.prompt()
//                .messages(userMessage)
//                .call()
//                .content();
        return dashScopeChatClient.prompt()
                .user(u -> u.text(message)
                        .media(Media.builder().mimeType(MimeTypeUtils.IMAGE_PNG).data(URI.create(url)).build())
                )
                .call()
                .content();
    }

    public String client4(String message, String chatId) {
        return dashScopeChatClient.prompt()
                .user(message)
                .advisors(QuestionAnswerAdvisor.builder(dashScopeCloudVectorStore).build())
                .call()
                .content();
    }

    public String client5(String message, String chatId) {
        Advisor advisor = DashScopeCloudAdvisor.createDashScopeCloudAdvisor("恋爱", dashScopeConnectionProperties.getApiKey());
        return dashScopeChatClient.prompt()
                .user(message)
                .advisors(advisor)
                .advisors(advisorSpec -> {
                    advisorSpec.param(ChatMemory.CONVERSATION_ID, chatId);
                })
                .call()
                .content();
    }

    /**
     * 使用 SimpleVectorStore 的示例
     * SimpleVectorStore 是一个简单的内存向量存储，适合开发和测试使用
     */
    public String client6(String message, String chatId) {
        return dashScopeChatClient.prompt()
                .user(message)
                .advisors(QuestionAnswerAdvisor.builder(simpleVectorStore).build())
                .call()
                .content();
    }
}
