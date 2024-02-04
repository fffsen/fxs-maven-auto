package com.zycm.zybao.model.vo;

import java.io.Serializable;

public class UploadVo implements Serializable{

	private static final long serialVersionUID = 1L;
	private String relativePath;
	private Integer createrId;
	private boolean showProgress;
	private String uuid;

	public String getRelativePath() {
		return relativePath;
	}
	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}
	public Integer getCreaterId() {
		return createrId;
	}
	public void setCreaterId(Integer createrId) {
		this.createrId = createrId;
	}
	public boolean isShowProgress() {
		return showProgress;
	}
	public void setShowProgress(boolean showProgress) {
		this.showProgress = showProgress;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}
