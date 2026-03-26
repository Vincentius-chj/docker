package com.chj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chj.entity.model.ProblemCategory;

public interface ProblemCategoryService extends IService<ProblemCategory> {

    ProblemCategory problemAnalysis(String problem);
}
