package com.chj.demo.ali;

import cn.hutool.core.lang.UUID;
import com.chj.demo.ali.BaseAgentDemo;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BaseAgentDemoTest {

    @Resource
    private BaseAgentDemo baseAgentDemo;

    @Test
    void testRun1() {
        String threadId = UUID.randomUUID().toString(true);
        String run = baseAgentDemo.run1("茂名今天的天气怎么样？", threadId);
        System.out.println(run);
        assertNotNull(run);
    }

    @Test
    void testRun2() {
        String threadId = UUID.randomUUID().toString(true);
        String run = baseAgentDemo.run2("茂名今天的天气怎么样？", threadId);
        System.out.println(run);
        assertNotNull(run);
    }

    @Test
    void testRun3() {
        String threadId = UUID.randomUUID().toString(true);
        baseAgentDemo.run2("我叫张三", threadId);
        String message = baseAgentDemo.run2("我叫什么名字？", threadId);
        System.out.println(message);
    }
}