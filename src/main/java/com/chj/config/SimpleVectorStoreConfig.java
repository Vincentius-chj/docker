package com.chj.config;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * SimpleVectorStore 配置类
 * 简单的内存向量存储，适合开发和测试使用
 */
@Configuration
public class SimpleVectorStoreConfig {

    /**
     * 创建 SimpleVectorStore Bean
     * 使用 EmbeddingModel 进行向量嵌入
     */
    @Bean
    public SimpleVectorStore simpleVectorStore(EmbeddingModel dashscopeEmbeddingModel) {
        SimpleVectorStore vectorStore = SimpleVectorStore.builder(dashscopeEmbeddingModel).build();
        // 初始化示例文档
        vectorStore.add(createSampleDocuments());
        return vectorStore;
    }

    private List<Document> createSampleDocuments() {
        return List.of(
                new Document("Spring AI 是一个用于构建 AI 应用的 Spring 框架扩展，它简化了与各种 AI 模型的集成。", Map.of("source", "spring-ai-intro")),
                new Document("SimpleVectorStore 是 Spring AI 提供的一个简单的内存向量存储实现，适合开发和测试环境使用。", Map.of("source", "simple-vector-store")),
                new Document("QuestionAnswerAdvisor 可以结合向量存储实现基于知识库的问答功能，通过检索相关文档来增强 AI 回答。", Map.of("source", "qa-advisor"))
        );
    }
}
