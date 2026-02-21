package com.chj.demo.ali;

import com.chj.demo.ali.StreamAgentDemo;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StreamAgentDemoTest {

    @Resource
    private StreamAgentDemo streamAgentDemo;

    @Test
    void testRun() {
        streamAgentDemo.run();
    }
}