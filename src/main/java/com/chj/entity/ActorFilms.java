package com.chj.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

/**
 * 演员电影集实体类
 */
@JsonPropertyOrder({"filmTitle", "actorName", "releaseYear", "genre"})
@Data
public class ActorFilms {

    /**
     * 演员姓名
     */
    private String actorName;

    /**
     * 电影标题
     */
    private String filmTitle;

    /**
     * 电影发布年份
     */
    private int releaseYear;

    /**
     * 电影类型
     */
    private String genre;

}
