package com.zycm.zybao.model.entity;

import java.util.Date;

/**
* @ClassName: MediaRunLogModel
* @Description: 终端运行日志
* @author sy
* @date 2017年9月30日 下午2:51:22
*
*/
public class MediaRunLogModel {
    private Integer runLogId;

    private Integer mid;

    private String machineCode;

    private Date logTime;

    private String logInfo;

    private Integer logLevel;

    private Date createTime;

    public Integer getRunLogId() {
        return runLogId;
    }

    public void setRunLogId(Integer runLogId) {
        this.runLogId = runLogId;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public String getMachineCode() {
		return machineCode;
	}

	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}

	public Date getLogTime() {
        return logTime;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

    public String getLogInfo() {
        return logInfo;
    }

    public void setLogInfo(String logInfo) {
        this.logInfo = logInfo == null ? null : logInfo.trim();
    }

    public Integer getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(Integer logLevel) {
        this.logLevel = logLevel;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
