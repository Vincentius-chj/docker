package com.chj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
        org.springframework.ai.model.chat.client.autoconfigure.ChatClientAutoConfiguration.class
})
public class ChjSpringAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChjSpringAiApplication.class, args);
    }

}
