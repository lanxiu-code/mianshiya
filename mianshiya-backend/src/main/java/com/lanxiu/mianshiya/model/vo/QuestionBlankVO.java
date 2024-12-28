package com.lanxiu.mianshiya.model.vo;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.lanxiu.mianshiya.model.entity.QuestionBlank;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 题库视图
 *
 * 蓝朽
 *
 */
@Data
public class QuestionBlankVO implements Serializable {

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
     * 描述
     */
    private String description;

    /**
     * 图片
     */
    private String picture;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 浏览量
     */
    private Integer viewNum;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 编辑时间
     */
    private Date editTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;



    /**
     * 状态：0-待审核, 1-通过, 2-拒绝
     */
    private Integer reviewStatus;

    /**
     * 审核信息
     */
    private String reviewMessage;

    /**
     * 审核人 id
     */
    private Long reviewerId;

    /**
     * 审核时间
     */
    private Date reviewTime;

    /**
     * 创建用户信息
     */
    private UserVO user;

    /**
     * 封装类转对象
     *
     * @param questionBlankVO
     * @return
     */
    public static QuestionBlank voToObj(QuestionBlankVO questionBlankVO) {
        if (questionBlankVO == null) {
            return null;
        }
        QuestionBlank questionBlank = new QuestionBlank();
        BeanUtils.copyProperties(questionBlankVO, questionBlank);
        return questionBlank;
    }

    /**
     * 对象转封装类
     *
     * @param questionBlank
     * @return
     */
    public static QuestionBlankVO objToVo(QuestionBlank questionBlank) {
        if (questionBlank == null) {
            return null;
        }
        QuestionBlankVO questionBlankVO = new QuestionBlankVO();
        BeanUtils.copyProperties(questionBlank, questionBlankVO);
        return questionBlankVO;
    }
}
