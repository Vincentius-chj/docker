package com.chj.demo.ali;

import com.chj.demo.ali.AgentDemo;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AgentDemoTest {

    @Resource
    private AgentDemo agentDemo;

    @Test
    void testRun() {
        String run = agentDemo.run();
        System.out.println(run);
        assertNotNull(run);
    }
}