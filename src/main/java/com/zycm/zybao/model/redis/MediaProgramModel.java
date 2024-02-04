package com.zycm.zybao.model.redis;

import java.io.Serializable;

public class MediaProgramModel implements Serializable{


	private static final long serialVersionUID = 1L;

	private String machineCode;//机器码

	private String programIds;//多个发布节目的id

	private long publishDate;//发布时间

	private Integer pubStatus;//执行状态 0待发送 1已发送 2已完成 3无响应

	private String programJsonStr;//节目单

	public String getMachineCode() {
		return machineCode;
	}

	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}

	public String getProgramIds() {
		return programIds;
	}

	public void setProgramIds(String programIds) {
		this.programIds = programIds;
	}

	public long getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(long publishDate) {
		this.publishDate = publishDate;
	}

	public Integer getPubStatus() {
		return pubStatus;
	}

	public void setPubStatus(Integer pubStatus) {
		this.pubStatus = pubStatus;
	}

	public String getProgramJsonStr() {
		return programJsonStr;
	}

	public void setProgramJsonStr(String programJsonStr) {
		this.programJsonStr = programJsonStr;
	}

}
