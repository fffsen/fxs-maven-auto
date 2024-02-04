package com.zycm.zybao.model.mqmodel.respose.clientfile;

import java.io.Serializable;
import java.math.BigDecimal;

public class ClientFileList implements Serializable{

    private String materialName;//素材文件名称
    //private BigDecimal size;//素材大小kb
    private Double size;//素材大小kb
    private Integer fileStatus;//素材状态  1表示档期中   0表示已过期

	public String getMaterialName() {
		return materialName;
	}
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	public Double getSize() {
		return size;
	}
	public void setSize(Double size) {
		this.size = size;
	}
	public Integer getFileStatus() {
		return fileStatus;
	}
	public void setFileStatus(Integer fileStatus) {
		this.fileStatus = fileStatus;
	}




}
