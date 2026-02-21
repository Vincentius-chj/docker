package com.chj.entity;

import lombok.Data;

/**
 * 诗词实体类
 */
@Data
public class PoemOutput {

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 风格
     */
    private String style;
}