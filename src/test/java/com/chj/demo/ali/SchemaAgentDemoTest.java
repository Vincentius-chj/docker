package com.chj.demo.ali;

import cn.hutool.json.JSONUtil;
import com.chj.demo.ali.SchemaAgentDemo;
import com.chj.entity.PoemOutput;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SchemaAgentDemoTest {

    @Resource
    private SchemaAgentDemo schemaAgentDemo;

    @Test
    void testRun() {
        String run = schemaAgentDemo.run();
        PoemOutput poemOutput = JSONUtil.toBean(run, PoemOutput.class);
        System.out.println(poemOutput);
        assertNotNull(poemOutput);
    }
}