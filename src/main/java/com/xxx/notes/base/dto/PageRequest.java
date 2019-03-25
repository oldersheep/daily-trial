package com.xxx.notes.base.dto;

import com.xxx.notes.base.util.StringUtils;

import java.io.Serializable;

/**
 * @ClassName PageRequest
 * @Description 分页对象
 * @Author l17561
 * @Date 2018/8/15 10:05
 * @Version V1.0
 */
public class PageRequest implements Serializable {

    //pageNum 当前记录起始索引
    private Integer pageNum;

    //pageSize 每页显示记录数
    private Integer pageSize;

    // 排序列
    private String orderCols;

    //排序 "desc" 或者 "asc"
    private String byRule;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setOrderCols(String orderCols) {
        this.orderCols = orderCols;
    }

    public void setByRule(String byRule) {
        this.byRule = byRule;
    }

    public String getOrderBy() {
        // 没有排序列，直接忽略排序规则
        if (this.orderCols == null || "".equals(this.orderCols)) {
            return "";
        }
        // 排序规则为空时，默认倒序
        if (this.byRule == null || "".equals(this.byRule)) {
            this.byRule = "desc";
        }
        return StringUtils.humpToLine(orderCols) + " " + byRule;
    }

}
