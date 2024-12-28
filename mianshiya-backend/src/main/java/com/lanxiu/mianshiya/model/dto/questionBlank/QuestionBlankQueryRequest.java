package com.lanxiu.mianshiya.model.dto.questionBlank;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.lanxiu.mianshiya.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 查询题库请求
 *
 * 蓝朽
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionBlankQueryRequest extends PageRequest implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    private String title;
    /**
     * 创建用户 id
     */
    private Long userId;
    /**
     * 描述
     */
    private String description;

    /**
     * 状态：0-待审核, 1-通过, 2-拒绝
     */
    private Integer reviewStatus;


    private static final long serialVersionUID = 1L;
}