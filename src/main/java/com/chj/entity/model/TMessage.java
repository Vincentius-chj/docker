package com.chj.entity.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("t_message")
public class TMessage {

    /**
     * 主键 ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 消息对话 ID
     */
    @TableField(value = "chat_id")
    private String chatId;

    /**
     * 消息类型
     */
    @TableField(value = "message_type")
    private String messageType;

    /**
     * 消息内容
     */
    @TableField(value = "message")
    private String message;

    /**
     * 媒体内容
     */
    @TableField(value = "media")
    private String media;

    /**
     * 工具调用内容
     */
    private String toolCalls;

    /**
     * 工具响应内容
     */
    @TableField(value = "responses")
    private String responses;

    /**
     * 元数据
     */
    @TableField(value = "metadata")
    private String metadata;
}