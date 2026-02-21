package com.chj.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

/**
 * 演员电影集实体类
 */
@JsonPropertyOrder({"filmTitle", "actorName", "releaseYear", "genre"})
@Data
public class ActorFilms {

    private String actorName; // 演员姓名
    private String filmTitle; // 电影标题
    private int releaseYear; // 电影上映年份
    private String genre; // 电影类型


}
