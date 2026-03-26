package com.chj.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chj.entity.model.Message;
import com.chj.mapper.MessageMapper;
import jakarta.annotation.Resource;
import org.jetbrains.annotations.NotNull;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.*;
import org.springframework.ai.content.Media;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * 自定义 MySQL 聊天记录存储
 */
@Component
public final class MySqlChatMemoryRepository implements ChatMemoryRepository {

    @Resource
    private MessageMapper messageMapper;

    @NotNull
    @Override
    public List<String> findConversationIds() {
        List<Message> messages = messageMapper.selectList(new LambdaQueryWrapper<>());
        if (CollUtil.isEmpty(messages)) {
            return CollUtil.newArrayList();
        }
        return messages.stream().map(Message::getMessage).toList();
    }

    @NotNull
    @Override
    public List<org.springframework.ai.chat.messages.Message> findByConversationId(@NotNull String conversationId) {
        List<Message> messages = messageMapper.selectList(new LambdaQueryWrapper<Message>().eq(Message::getChatId, conversationId));
        if (CollUtil.isEmpty(messages)) {
            return CollUtil.newArrayList();
        }
        return messages.stream().map(this::convertToMessage).toList();
    }

    @Override
    public void saveAll(@NotNull String conversationId, @NotNull List<org.springframework.ai.chat.messages.Message> messages) {
        List<Message> tMessages = messages.stream().map(message -> {
            Message.TMessageBuilder tMessageBuilder = Message.builder()
                    .chatId(conversationId)
                    .messageType(message.getMessageType().getValue())
                    .message(message.getText());
            return switch (message) {
                case UserMessage userMessage -> tMessageBuilder
                        .media(JSONUtil.toJsonStr(userMessage.getMedia()))
                        .metadata(JSONUtil.toJsonStr(userMessage.getMetadata()))
                        .build();
                case AssistantMessage assistantMessage -> tMessageBuilder
                        .media(JSONUtil.toJsonStr(assistantMessage.getMedia()))
                        .toolCalls(JSONUtil.toJsonStr(assistantMessage.getToolCalls()))
                        .metadata(JSONUtil.toJsonStr(assistantMessage.getMetadata()))
                        .build();
                case SystemMessage systemMessage -> tMessageBuilder
                        .metadata(JSONUtil.toJsonStr(systemMessage.getMetadata()))
                        .build();
                case ToolResponseMessage toolResponseMessage -> tMessageBuilder
                        .responses(JSONUtil.toJsonStr(toolResponseMessage.getResponses()))
                        .metadata(JSONUtil.toJsonStr(toolResponseMessage.getMetadata()))
                        .build();
                default -> null;
            };
        }).filter(Objects::nonNull).toList();
        messageMapper.insert(tMessages);
    }

    @Override
    public void deleteByConversationId(@NotNull String conversationId) {
        messageMapper.delete(new LambdaQueryWrapper<Message>().eq(Message::getChatId, conversationId));
    }

    /**
     * 将 TMessage 转换为对应的 Message 对象
     */
    private org.springframework.ai.chat.messages.Message convertToMessage(Message message) {
        MessageType messageType = MessageType.fromValue(message.getMessageType());
        return switch (messageType) {
            case MessageType.USER -> UserMessage.builder()
                    .text(message.getMessage())
                    .media(JSONUtil.toList(message.getMedia(), Media.class))
                    .metadata(JSONUtil.toBean(message.getMetadata(), new TypeReference<>() {
                    }, false))
                    .build();
            case MessageType.ASSISTANT -> AssistantMessage.builder()
                    .content(message.getMessage())
                    .media(JSONUtil.toList(message.getMedia(), Media.class))
                    .properties(JSONUtil.toBean(message.getMetadata(), new TypeReference<>() {
                    }, false))
                    .toolCalls(JSONUtil.toList(message.getToolCalls(), AssistantMessage.ToolCall.class))
                    .build();
            case MessageType.SYSTEM -> SystemMessage.builder()
                    .text(message.getMessage())
                    .metadata(JSONUtil.toBean(message.getMetadata(), new TypeReference<>() {
                    }, false))
                    .build();
            case MessageType.TOOL -> ToolResponseMessage.builder()
                    .responses(JSONUtil.toList(message.getResponses(), ToolResponseMessage.ToolResponse.class))
                    .metadata(JSONUtil.toBean(message.getMetadata(), new TypeReference<>() {
                    }, false))
                    .build();
        };
    }
}
