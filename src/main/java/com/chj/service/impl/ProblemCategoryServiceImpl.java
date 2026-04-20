package com.chj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chj.entity.model.ProblemCategory;
import com.chj.mapper.ProblemCategoryMapper;
import com.chj.service.ProblemCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProblemCategoryServiceImpl extends ServiceImpl<ProblemCategoryMapper, ProblemCategory> implements ProblemCategoryService  {

    private final ChatClient dashScopeChatClient;

    // 从外部文件加载系统提示词模板
    @Value("classpath:/templates/cause-analysis-system.prompt")
    private Resource systemPromptTemplate;

    // 从外部文件加载用户提示词模板
    @Value("classpath:/templates/cause-analysis-user.prompt")
    private Resource userPromptTemplate;

    @Override
    public ProblemCategory problemAnalysis(String problem) {
        List<ProblemCategory> problemCategories = list();

        // 渲染用户提示词，绑定变量
        PromptTemplate userTemplate = new PromptTemplate(userPromptTemplate);
        String userText = userTemplate.create(Map.of(
                "problem", problem,
                "categories", problemCategories.toString()
        )).getContents();

        return dashScopeChatClient.prompt()
                .system(systemPromptTemplate)
                .user(userText)
                .call()
                .entity(ProblemCategory.class);
    }
}
