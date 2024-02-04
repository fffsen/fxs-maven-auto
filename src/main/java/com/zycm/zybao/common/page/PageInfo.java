package com.zycm.zybao.common.page;

import net.sf.json.JSONObject;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.List;


public class PageInfo<T> implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Transient
    private Integer
    total = 0, //总数
    page = 1,//当前页
	totalPage = 1,
	startRow = 0,
    pageSize = 10;//每页数目
    private List<T> dataList;
    public PageInfo(){
    }
    public PageInfo(Integer page,Integer pageSize){
    	this.startRow = (page -1)*pageSize;
    }

    public Integer getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}
	public Integer getPageSize() {
        return pageSize;
    }
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
    public Integer getTotal() {
        return total;
    }
    public void setTotal(Integer total) {
        this.total = total;
    }
    public Integer getPage() {
        return page;
    }
    public void setPage(Integer page) {
        this.page = page;
    }
    public List<T> getDataList() {
        return dataList;
    }
    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    public String toJson(){
        String json = JSONObject.fromObject(this).toString();
        return json;
    }
}

