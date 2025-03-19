package com.chen.bizcenter.common.vo.req;

import lombok.Data;

import java.io.Serializable;

/**
 * 分页请求对象VO
 *
 * @Author: chenille
 * @Date: 2024-10-25 22:24
 **/
@Data
public class PageRequestVO implements Serializable {
    private static final long serialVersionUID = 1251835782021005190L;

    /**
     * 每页数量 分页查每次最多200
     */
    private Integer size;

    /**
     * 当前页 分页查每次最小1
     */
    private Integer currentPage;
}

