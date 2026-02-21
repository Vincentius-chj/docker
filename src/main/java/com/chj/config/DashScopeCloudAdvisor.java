package com.chj.config;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetriever;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetrieverOptions;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;

import java.util.HashMap;
import java.util.List;

public class DashScopeCloudAdvisor {

    public static Advisor createDashScopeCloudAdvisor(String status, String apiKey) {
        String KNOWLEDGE_INDEX = "恋爱大师";
        HashMap<String, Object> map = new HashMap<>();
        map.put("tag", status);
        DocumentRetriever retriever = new DashScopeDocumentRetriever(
                DashScopeApi.builder().apiKey(apiKey).build(),
                DashScopeDocumentRetrieverOptions.builder()
                        .searchFilters(List.of(map))
                        .indexName(KNOWLEDGE_INDEX)
                        .build()
        );
        return RetrievalAugmentationAdvisor.builder()
                .documentRetriever(retriever)
                .build();
    }
}
