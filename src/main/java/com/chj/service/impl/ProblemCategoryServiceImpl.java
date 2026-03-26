package com.chj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chj.entity.model.ProblemCategory;
import com.chj.mapper.ProblemCategoryMapper;
import com.chj.service.ProblemCategoryService;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProblemCategoryServiceImpl extends ServiceImpl<ProblemCategoryMapper, ProblemCategory> implements ProblemCategoryService  {

    @Resource
    private ChatClient dashScopeChatClient;

    @Override
    public ProblemCategory problemAnalysis(String problem) {
        List<ProblemCategory> problemCategories = list();
        String message = "你是一名原因分析师，接下来我给出内容和所有的原因分类，你帮我进行归因，" +
                "也就是在原因分类集合中找到最符合内容的原因，最后返回该原因分类的信息即可。" +
                "内容：" + problem + "原因分类集合：" + problemCategories +
                "，如果实在无法确定，也要找到最符合原因分类下的一个‘其他’。";
        return dashScopeChatClient.prompt()
                .user(message)
                .call()
                .entity(ProblemCategory.class);
    }
}
