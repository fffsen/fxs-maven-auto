package com.zycm.zybao.model.entity;

import java.util.Date;

/** z终端播放日志
* @ClassName: MediaPlayLogModel
* @Description: TODO
* @author sy
* @date 2017年9月30日 下午2:50:38
*
*/
public class MediaPlayLogModel {
    private String playLogId;//播放日志id 规则是 节目id-机器码-添加时间年月日 如：'2-sdaffsfdsfsd=-20170930'

    private Integer mid;

    private String machineCode;

    private Integer programId;

    private String programName;

    private Integer playTime;

    private Date logDate;

    private Date updateTime;

    public String getPlayLogId() {
        return playLogId;
    }

    public void setPlayLogId(String playLogId) {
        this.playLogId = playLogId;
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

	public Integer getPlayTime() {
        return playTime;
    }

    public void setPlayTime(Integer playTime) {
        this.playTime = playTime;
    }

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
