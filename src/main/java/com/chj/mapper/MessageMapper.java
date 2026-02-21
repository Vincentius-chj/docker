package com.chj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chj.entity.model.TMessage;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageMapper extends BaseMapper<TMessage> {
}
