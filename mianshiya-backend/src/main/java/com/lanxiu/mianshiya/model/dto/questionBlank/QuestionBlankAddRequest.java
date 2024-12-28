package com.lanxiu.mianshiya.model.dto.questionBlank;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 创建题库请求
 *
 * 蓝朽
 *
 */
@Data
public class QuestionBlankAddRequest implements Serializable {


    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String description;

    /**
     * 图片
     */
    private String picture;



    private static final long serialVersionUID = 1L;
}