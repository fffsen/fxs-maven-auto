package com.zycm.zybao.model.mqmodel.task.runlog;

import java.util.Date;

public class RunLogList {

	private String logInfo;//日志描述

	private Integer logLevel;//日志级别（1普通 2 错误异常）

	private String logTime;//日志时间  年月日时分秒

	public String getLogInfo() {
		return logInfo;
	}

	public void setLogInfo(String logInfo) {
		this.logInfo = logInfo;
	}

	public Integer getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(Integer logLevel) {
		this.logLevel = logLevel;
	}

	public String getLogTime() {
		return logTime;
	}

	public void setLogTime(String logTime) {
		this.logTime = logTime;
	}


}
