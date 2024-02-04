package com.zycm.zybao.model.vo;

import java.io.Serializable;

public class MediaVo implements Serializable{
	private Integer mid;	//广告位id
	private String machine_code;	//机器码
	private String name;	//广告位
	private Integer size;	//尺寸 (0表示52寸 1为55寸 2为42寸 3为21.5寸)
	private Integer model;	//机型（0安卓 1高 2海 3海5）
	private Integer way;	//上刊方式(0无线，1U盘）
	public Integer getMid() {
		return mid;
	}
	public void setMid(Integer mid) {
		this.mid = mid;
	}
	public String getMachine_code() {
		return machine_code;
	}
	public void setMachine_code(String machine_code) {
		this.machine_code = machine_code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public Integer getModel() {
		return model;
	}
	public void setModel(Integer model) {
		this.model = model;
	}
	public Integer getWay() {
		return way;
	}
	public void setWay(Integer way) {
		this.way = way;
	}

}
