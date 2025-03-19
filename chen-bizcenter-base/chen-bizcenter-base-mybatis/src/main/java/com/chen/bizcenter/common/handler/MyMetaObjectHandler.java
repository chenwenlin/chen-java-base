package com.chen.bizcenter.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.chen.bizcenter.common.SnowflakeIdWorker;
import com.chen.bizcenter.common.constants.BasePoFieldConstants;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * mybatis plus全局填充字段
 *
 * @Author: chenille
 * @Date: 2024-10-30 21:06
 **/
public class MyMetaObjectHandler implements MetaObjectHandler {

    private final SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker();

    /**
     * 插入填充
     *
     * @param metaObject metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        //公共字段
        mySetFieldValByName(BasePoFieldConstants.ID, snowflakeIdWorker.nextId(), metaObject);
        mySetFieldValByName(BasePoFieldConstants.CREATE_TIME, LocalDateTime.now(), metaObject);
        mySetFieldValByName(BasePoFieldConstants.CREATE_USER_ID, LocalDateTime.now(), metaObject);
        mySetFieldValByName(BasePoFieldConstants.MODIFY_TIME, LocalDateTime.now(), metaObject);
        mySetFieldValByName(BasePoFieldConstants.MODIFY_USER_ID, LocalDateTime.now(), metaObject);
        mySetFieldValByName(BasePoFieldConstants.DELETED, false, metaObject);
    }

    /**
     * 更新填充
     *
     * @param metaObject metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        mySetFieldValByName(BasePoFieldConstants.MODIFY_TIME, LocalDateTime.now(), metaObject);
        mySetFieldValByName(BasePoFieldConstants.MODIFY_USER_ID, LocalDateTime.now(), metaObject);
    }

    /**
     * 是否是空字段
     *
     * @param fieldName  字段名称
     * @param fieldVal   字段值
     * @param metaObject metaObject
     */
    private void mySetFieldValByName(String fieldName, Object fieldVal, MetaObject metaObject) {
        if (metaObject.hasSetter(fieldName) && getFieldValByName(fieldName, metaObject) == null && Objects.nonNull(fieldVal)) {
            metaObject.setValue(fieldName, fieldVal);
        }
    }
}
