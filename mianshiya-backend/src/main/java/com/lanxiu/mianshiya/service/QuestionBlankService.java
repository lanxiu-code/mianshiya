package com.lanxiu.mianshiya.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lanxiu.mianshiya.model.dto.questionBlank.QuestionBlankQueryRequest;
import com.lanxiu.mianshiya.model.entity.QuestionBlank;
import com.lanxiu.mianshiya.model.vo.QuestionBlankVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 题库服务
 *
 * 蓝朽
 *
 */
public interface QuestionBlankService extends IService<QuestionBlank> {

    /**
     * 校验数据
     *
     * @param questionBlank
     * @param add 对创建的数据进行校验
     */
    void validQuestionBlank(QuestionBlank questionBlank, boolean add);

    /**
     * 获取查询条件
     *
     * @param questionBlankQueryRequest
     * @return
     */
    QueryWrapper<QuestionBlank> getQueryWrapper(QuestionBlankQueryRequest questionBlankQueryRequest);
    
    /**
     * 获取题库封装
     *
     * @param questionBlank
     * @param request
     * @return
     */
    QuestionBlankVO getQuestionBlankVO(QuestionBlank questionBlank, HttpServletRequest request);

    /**
     * 分页获取题库封装
     *
     * @param questionBlankPage
     * @param request
     * @return
     */
    Page<QuestionBlankVO> getQuestionBlankVOPage(Page<QuestionBlank> questionBlankPage, HttpServletRequest request);
}
