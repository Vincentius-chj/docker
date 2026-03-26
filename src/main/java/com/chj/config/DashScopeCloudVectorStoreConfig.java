package com.chj.config;

import com.alibaba.cloud.ai.autoconfigure.dashscope.DashScopeConnectionProperties;
import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeCloudStore;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeStoreOptions;
import jakarta.annotation.Resource;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DashScopeCloudVectorStoreConfig {

    @Resource
    private DashScopeConnectionProperties dashScopeConnectionProperties;

    @Bean
    public VectorStore dashScopeCloudVectorStore() {
        final String KNOWLEDGE_INDEX = "恋爱大师";
        return new DashScopeCloudStore(
                DashScopeApi.builder().apiKey(dashScopeConnectionProperties.getApiKey()).build(),
                new DashScopeStoreOptions(KNOWLEDGE_INDEX)
        );
    }
}
