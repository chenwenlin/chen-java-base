package com.chen.bizcenter.common.persistence.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: chenille
 * @Date: 2024-11-19 21:45
 */
@Slf4j
public class BasePRepository<M extends BaseMapper<P>, P> extends ServiceImpl<M, P> implements IPRepository<P> {

    /**
     * 构造方法
     *
     * @param baseMapper baseMapper
     */
    public BasePRepository(M baseMapper) {
        log.debug();
        this.baseMapper = baseMapper;
    }
}
