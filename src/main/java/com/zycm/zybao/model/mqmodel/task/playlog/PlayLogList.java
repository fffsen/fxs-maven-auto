package com.zycm.zybao.model.mqmodel.task.playlog;

import java.util.Date;

public class PlayLogList {

	private Integer programId;//节目id

	private String programName;//节目id

	private Integer playtime;//播放次数

	private String logDate;//日志时间    年月日

	public Integer getProgramId() {
		return programId;
	}

	public void setProgramId(Integer programId) {
		this.programId = programId;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public Integer getPlaytime() {
		return playtime;
	}

	public void setPlaytime(Integer playtime) {
		this.playtime = playtime;
	}

	public String getLogDate() {
		return logDate;
	}

	public void setLogDate(String logDate) {
		this.logDate = logDate;
	}



}
