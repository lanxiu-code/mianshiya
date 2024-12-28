package com.lanxiu.mianshiya.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lanxiu.mianshiya.annotation.AuthCheck;
import com.lanxiu.mianshiya.common.BaseResponse;
import com.lanxiu.mianshiya.common.DeleteRequest;
import com.lanxiu.mianshiya.common.ErrorCode;
import com.lanxiu.mianshiya.common.ResultUtils;
import com.lanxiu.mianshiya.constant.UserConstant;
import com.lanxiu.mianshiya.exception.BusinessException;
import com.lanxiu.mianshiya.exception.ThrowUtils;
import com.lanxiu.mianshiya.model.dto.questionBlank.QuestionBlankAddRequest;
import com.lanxiu.mianshiya.model.dto.questionBlank.QuestionBlankEditRequest;
import com.lanxiu.mianshiya.model.dto.questionBlank.QuestionBlankQueryRequest;
import com.lanxiu.mianshiya.model.dto.questionBlank.QuestionBlankUpdateRequest;
import com.lanxiu.mianshiya.model.entity.QuestionBlank;
import com.lanxiu.mianshiya.model.entity.User;
import com.lanxiu.mianshiya.model.vo.QuestionBlankVO;
import com.lanxiu.mianshiya.service.QuestionBlankService;
import com.lanxiu.mianshiya.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 题库接口
 *
 * 蓝朽
 *
 */
@RestController
@RequestMapping("/questionBlank")
@Slf4j
public class QuestionBlankController {

    @Resource
    private QuestionBlankService questionBlankService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建题库
     *
     * @param questionBlankAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addQuestionBlank(@RequestBody QuestionBlankAddRequest questionBlankAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(questionBlankAddRequest == null, ErrorCode.PARAMS_ERROR);
        QuestionBlank questionBlank = new QuestionBlank();
        BeanUtils.copyProperties(questionBlankAddRequest, questionBlank);
        // 数据校验
        questionBlankService.validQuestionBlank(questionBlank, true);
        User loginUser = userService.getLoginUser(request);
        questionBlank.setUserId(loginUser.getId());
        // 写入数据库
        boolean result = questionBlankService.save(questionBlank);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newQuestionBlankId = questionBlank.getId();
        return ResultUtils.success(newQuestionBlankId);
    }

    /**
     * 删除题库
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteQuestionBlank(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        QuestionBlank oldQuestionBlank = questionBlankService.getById(id);
        ThrowUtils.throwIf(oldQuestionBlank == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldQuestionBlank.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = questionBlankService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新题库（仅管理员可用）
     *
     * @param questionBlankUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateQuestionBlank(@RequestBody QuestionBlankUpdateRequest questionBlankUpdateRequest) {
        if (questionBlankUpdateRequest == null || questionBlankUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QuestionBlank questionBlank = new QuestionBlank();
        BeanUtils.copyProperties(questionBlankUpdateRequest, questionBlank);
        // 数据校验
        questionBlankService.validQuestionBlank(questionBlank, false);
        // 判断是否存在
        long id = questionBlankUpdateRequest.getId();
        QuestionBlank oldQuestionBlank = questionBlankService.getById(id);
        ThrowUtils.throwIf(oldQuestionBlank == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = questionBlankService.updateById(questionBlank);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取题库（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<QuestionBlankVO> getQuestionBlankVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        QuestionBlank questionBlank = questionBlankService.getById(id);
        ThrowUtils.throwIf(questionBlank == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(questionBlankService.getQuestionBlankVO(questionBlank, request));
    }

    /**
     * 分页获取题库列表（仅管理员可用）
     *
     * @param questionBlankQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<QuestionBlank>> listQuestionBlankByPage(@RequestBody QuestionBlankQueryRequest questionBlankQueryRequest) {
        long current = questionBlankQueryRequest.getCurrent();
        long size = questionBlankQueryRequest.getPageSize();
        // 查询数据库
        Page<QuestionBlank> questionBlankPage = questionBlankService.page(new Page<>(current, size),
                questionBlankService.getQueryWrapper(questionBlankQueryRequest));
        return ResultUtils.success(questionBlankPage);
    }

    /**
     * 分页获取题库列表（封装类）
     *
     * @param questionBlankQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<QuestionBlankVO>> listQuestionBlankVOByPage(@RequestBody QuestionBlankQueryRequest questionBlankQueryRequest,
                                                               HttpServletRequest request) {
        long current = questionBlankQueryRequest.getCurrent();
        long size = questionBlankQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<QuestionBlank> questionBlankPage = questionBlankService.page(new Page<>(current, size),
                questionBlankService.getQueryWrapper(questionBlankQueryRequest));
        // 获取封装类
        return ResultUtils.success(questionBlankService.getQuestionBlankVOPage(questionBlankPage, request));
    }

    /**
     * 分页获取当前登录用户创建的题库列表
     *
     * @param questionBlankQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<QuestionBlankVO>> listMyQuestionBlankVOByPage(@RequestBody QuestionBlankQueryRequest questionBlankQueryRequest,
                                                                 HttpServletRequest request) {
        ThrowUtils.throwIf(questionBlankQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);
        questionBlankQueryRequest.setUserId(loginUser.getId());
        long current = questionBlankQueryRequest.getCurrent();
        long size = questionBlankQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<QuestionBlank> questionBlankPage = questionBlankService.page(new Page<>(current, size),
                questionBlankService.getQueryWrapper(questionBlankQueryRequest));
        // 获取封装类
        return ResultUtils.success(questionBlankService.getQuestionBlankVOPage(questionBlankPage, request));
    }

    /**
     * 编辑题库（给用户使用）
     *
     * @param questionBlankEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editQuestionBlank(@RequestBody QuestionBlankEditRequest questionBlankEditRequest, HttpServletRequest request) {
        if (questionBlankEditRequest == null || questionBlankEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QuestionBlank questionBlank = new QuestionBlank();
        BeanUtils.copyProperties(questionBlankEditRequest, questionBlank);
        // 数据校验
        questionBlankService.validQuestionBlank(questionBlank, false);
        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        long id = questionBlankEditRequest.getId();
        QuestionBlank oldQuestionBlank = questionBlankService.getById(id);
        ThrowUtils.throwIf(oldQuestionBlank == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldQuestionBlank.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = questionBlankService.updateById(questionBlank);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    // endregion
}
