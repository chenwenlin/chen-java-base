package com.chen.bizcenter.common.vo.res;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页返回对象VO
 *
 * @Author: chenille
 * @Date: 2024-10-25 22:24
 **/
@Data
public class PageResponseVO<T> implements Serializable {
    private static final long serialVersionUID = -9200122821274720605L;

    /**
     * 每页数量
     */
    private Integer size;

    /**
     * 总数
     */
    private Long total;

    /**
     * 总页数
     */
    private Integer pageSize;

    /**
     * 当前页
     */
    private Integer currentPage;

    /**
     * 返回数据集合
     */
    private List<T> list;
}
