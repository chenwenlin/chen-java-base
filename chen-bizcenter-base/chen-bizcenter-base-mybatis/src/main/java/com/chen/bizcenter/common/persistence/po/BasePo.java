package com.chen.bizcenter.common.persistence.po;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 公共字段对象
 *
 * @Author: chenille
 * @Date: 2024-10-25 23:00
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class BasePo<T extends Model<T>> extends Model<T> implements Serializable {

    private static final long serialVersionUID = -7968916792631120754L;

    /**
     * IdType.AUTO：数据库ID自增
     * IdType.INPUT：用户输入ID
     * IdType.ID_WORKER：全局唯一ID，内容为空自动填充（默认配置）
     * IdType.UUID：全局唯一ID，内容为空自动填充
     * global-config:
     * #主键类型  0:"数据库ID自增", 1:"用户输入ID",2:"全局唯一ID (数字类型唯一ID)", 3:"全局唯一ID UUID"
     * id-type: 2
     */
    @TableId(value = "id", type = IdType.INPUT)
    protected Long id;

    /**
     * 创建人ID
     */
    @TableField(fill = FieldFill.INSERT)
    protected Long createUserId;


    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime createTime;


    /**
     * 修改人ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected Long modifyUserId;

    /**
     * 修改时间
     */
    @TableField(update = "now()", fill = FieldFill.INSERT_UPDATE)
    protected LocalDateTime modifyTime;

    /**
     * 删除标记
     */
    @TableLogic(value = "false", delval = "true")
    @TableField(fill = FieldFill.INSERT)
    protected Boolean deleted;
}
