package com.chj.demo;

import cn.hutool.core.lang.UUID;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdvisorsDemoTest {

    @Resource
    private AdvisorsDemo advisorsDemo;

    /**
     * 测试自定义 Advisor
     */
    @Test
    void client1() {
        String chatId = UUID.randomUUID().toString(true);
        String response = advisorsDemo.client1("你是谁？", chatId);
        System.out.println(response);
        assertNotNull(response);
    }

    /**
     * 测试多轮对话记忆 Advisor
     */
    @Test
    void client2() {
        String chatId = UUID.randomUUID().toString(true);
        String response1 = advisorsDemo.client2("我是小王，请你记住我的名字", chatId);
        System.out.println(response1);
        String response2 = advisorsDemo.client2("我是谁？我刚刚告诉你了", chatId);
        System.out.println(response2);
    }

    /**
     * 多模态
     */
    @Test
    void client3() {
        String chatId = UUID.randomUUID().toString(true);
        String response = advisorsDemo.client3("帮我分析这张图片的内容", chatId);
        System.out.println(response);
        assertNotNull(response);
    }

    @Test
    void client4() {
        String chatId = UUID.randomUUID().toString(true);
        String response = advisorsDemo.client4(
                "我今天27岁，女朋友喜欢画画，有艺术细胞，" +
                "根据知识库的信息推断我的女友最可能是谁，并给出理由，" +
                "实在无法判断，请给出最有可能的 10 个人，并给出理由",
                chatId
        );
        System.out.println(response);
        assertNotNull(response);
    }

    @Test
    void client5() {
        String chatId = UUID.randomUUID().toString(true);
        String response = advisorsDemo.client5("刚刚谈恋爱，怎么跟女朋友维持感情呢", chatId);
        System.out.println(response);
        assertNotNull(response);
    }
}