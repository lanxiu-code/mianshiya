package com.lanxiu.mianshiya.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lanxiu.mianshiya.common.ErrorCode;
import com.lanxiu.mianshiya.constant.CommonConstant;
import com.lanxiu.mianshiya.exception.ThrowUtils;
import com.lanxiu.mianshiya.mapper.QuestionBlankMapper;
import com.lanxiu.mianshiya.model.dto.questionBlank.QuestionBlankQueryRequest;
import com.lanxiu.mianshiya.model.entity.QuestionBlank;
import com.lanxiu.mianshiya.model.entity.User;
import com.lanxiu.mianshiya.model.vo.QuestionBlankVO;
import com.lanxiu.mianshiya.model.vo.UserVO;
import com.lanxiu.mianshiya.service.QuestionBlankService;
import com.lanxiu.mianshiya.service.UserService;
import com.lanxiu.mianshiya.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 题库服务实现
 *
 * 蓝朽
 *
 */
@Service
@Slf4j
public class QuestionBlankServiceImpl extends ServiceImpl<QuestionBlankMapper, QuestionBlank> implements QuestionBlankService {

    @Resource
    private UserService userService;

    /**
     * 校验数据
     *
     * @param questionBlank
     * @param add      对创建的数据进行校验
     */
    @Override
    public void validQuestionBlank(QuestionBlank questionBlank, boolean add) {
        ThrowUtils.throwIf(questionBlank == null, ErrorCode.PARAMS_ERROR);
        String title = questionBlank.getTitle();
        String picture = questionBlank.getPicture();
        // 创建数据时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isBlank(title), ErrorCode.PARAMS_ERROR);
            ThrowUtils.throwIf(StringUtils.isBlank(picture), ErrorCode.PARAMS_ERROR);
        }
        // 修改数据时，有参数则校验
        if (StringUtils.isNotBlank(title)) {
            ThrowUtils.throwIf(title.length() > 80, ErrorCode.PARAMS_ERROR, "标题过长");
        }
    }

    /**
     * 获取查询条件
     *
     * @param questionBlankQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionBlank> getQueryWrapper(QuestionBlankQueryRequest questionBlankQueryRequest) {
        QueryWrapper<QuestionBlank> queryWrapper = new QueryWrapper<>();
        if (questionBlankQueryRequest == null) {
            return queryWrapper;
        }
        Long id = questionBlankQueryRequest.getId();
        String title = questionBlankQueryRequest.getTitle();
        String description = questionBlankQueryRequest.getDescription();
        String sortField = questionBlankQueryRequest.getSortField();
        String sortOrder = questionBlankQueryRequest.getSortOrder();
        // 从多字段中搜索
        if (StringUtils.isNotBlank(title)) {
            // 需要拼接查询条件
            queryWrapper.and(qw -> qw.like("title", title).or().like("description", title));
        }
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        // 精确查询
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取题库封装
     *
     * @param questionBlank
     * @param request
     * @return
     */
    @Override
    public QuestionBlankVO getQuestionBlankVO(QuestionBlank questionBlank, HttpServletRequest request) {
        // 对象转封装类
        QuestionBlankVO questionBlankVO = QuestionBlankVO.objToVo(questionBlank);
        // region 可选
        // 1. 关联查询用户信息
        Long userId = questionBlank.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        questionBlankVO.setUser(userVO);
        // endregion
        return questionBlankVO;
    }

    /**
     * 分页获取题库封装
     *
     * @param questionBlankPage
     * @param request
     * @return
     */
    @Override
    public Page<QuestionBlankVO> getQuestionBlankVOPage(Page<QuestionBlank> questionBlankPage, HttpServletRequest request) {
        List<QuestionBlank> questionBlankList = questionBlankPage.getRecords();
        Page<QuestionBlankVO> questionBlankVOPage = new Page<>(questionBlankPage.getCurrent(), questionBlankPage.getSize(), questionBlankPage.getTotal());
        if (CollUtil.isEmpty(questionBlankList)) {
            return questionBlankVOPage;
        }
        // 对象列表 => 封装对象列表
        List<QuestionBlankVO> questionBlankVOList = questionBlankList.stream().map(questionBlank -> {
            return QuestionBlankVO.objToVo(questionBlank);
        }).collect(Collectors.toList());

        // region 可选
        // 1. 关联查询用户信息
        Set<Long> userIdSet = questionBlankList.stream().map(QuestionBlank::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 填充信息
        questionBlankVOList.forEach(questionBlankVO -> {
            Long userId = questionBlankVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            questionBlankVO.setUser(userService.getUserVO(user));
        });
        // endregion
        questionBlankVOPage.setRecords(questionBlankVOList);
        return questionBlankVOPage;
    }

}
