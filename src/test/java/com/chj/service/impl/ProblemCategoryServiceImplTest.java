package com.chj.service.impl;

import com.chj.entity.model.ProblemCategory;
import com.chj.service.ProblemCategoryService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProblemCategoryServiceImplTest {

    @Resource
    private ProblemCategoryService problemCategoryService;

    @Test
    void problemAnalysis() {
        ProblemCategory problemCategory = problemCategoryService.problemAnalysis("没货了");
        System.out.println(problemCategory);
    }
}