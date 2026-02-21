package com.chj.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.messages.UserMessage;

@Getter
@AllArgsConstructor
public enum MessageEnum {

    USER_MESSAGE(1, "user", UserMessage.class),
    SYSTEM_MESSAGE(2, "system", SystemMessage.class),
    ASSISTANT_MESSAGE(3, "assistant", AssistantMessage.class),
    TOOL_RESPONSE_MESSAGE(4, "tool_response", ToolResponseMessage.class);

    private final Integer code;

    private final String message;

    private final Class<?> clazz;

    public static MessageEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (MessageEnum value : MessageEnum.values()) {
            if (code.equals(value.code)) {
                return value;
            }
        }
        return null;
    }

    public static Class<?> getClassByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (MessageEnum value : MessageEnum.values()) {
            if (code.equals(value.code)) {
                return value.clazz;
            }
        }
        return null;
    }
}
