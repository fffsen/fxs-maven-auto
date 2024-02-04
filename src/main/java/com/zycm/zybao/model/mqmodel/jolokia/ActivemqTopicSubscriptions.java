package com.zycm.zybao.model.mqmodel.jolokia;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

public class ActivemqTopicSubscriptions implements Serializable{

	private String objectName;

	private String machineCode;

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
		this.machineCode = StringUtils.isNotBlank(objectName)?objectName.split(",")[1].replace("clientId=", "").replace("&amp;", "="):null;
	}

	public String getMachineCode() {
		return machineCode;
	}



}
