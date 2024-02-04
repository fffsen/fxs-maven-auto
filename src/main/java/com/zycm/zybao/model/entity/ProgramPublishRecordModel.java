package com.zycm.zybao.model.entity;

import java.util.Date;

/** 节目发布表记录表
* @ClassName: ProgramPublishRecordModel
* @Description: TODO
* @author sy
* @date 2017年8月15日 下午5:19:21
*
*/
public class ProgramPublishRecordModel {
    private Integer publishId;

    private Integer programId;

    private Integer mediaGroupId;

    private Integer publisherId;

    private Integer publishStatus;

    private Integer timeMode;

    private Integer switchMode;

    private Integer downloadMode;

    private Date playStartDate;

    private Date playStartTime;

    private Date playEndDate;

    private Date playEndTime;

    private String weekSet;

    private Integer playLevel;

    private Integer frequency;

    private Integer programOrder;

    private Date createTime;

    public Integer getPublishId() {
        return publishId;
    }

    public void setPublishId(Integer publishId) {
        this.publishId = publishId;
    }

    public Integer getProgramId() {
        return programId;
    }

    public void setProgramId(Integer programId) {
        this.programId = programId;
    }

    public Integer getMediaGroupId() {
        return mediaGroupId;
    }

    public void setMediaGroupId(Integer mediaGroupId) {
        this.mediaGroupId = mediaGroupId;
    }

    public Integer getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Integer publisherId) {
        this.publisherId = publisherId;
    }

    public Integer getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(Integer publishStatus) {
        this.publishStatus = publishStatus;
    }

    public Integer getTimeMode() {
        return timeMode;
    }

    public void setTimeMode(Integer timeMode) {
        this.timeMode = timeMode;
    }

    public Integer getSwitchMode() {
        return switchMode;
    }

    public void setSwitchMode(Integer switchMode) {
        this.switchMode = switchMode;
    }

    public Integer getDownloadMode() {
        return downloadMode;
    }

    public void setDownloadMode(Integer downloadMode) {
        this.downloadMode = downloadMode;
    }

    public Date getPlayStartDate() {
        return playStartDate;
    }

    public void setPlayStartDate(Date playStartDate) {
        this.playStartDate = playStartDate;
    }

    public Date getPlayStartTime() {
        return playStartTime;
    }

    public void setPlayStartTime(Date playStartTime) {
        this.playStartTime = playStartTime;
    }

    public Date getPlayEndDate() {
        return playEndDate;
    }

    public void setPlayEndDate(Date playEndDate) {
        this.playEndDate = playEndDate;
    }

    public Date getPlayEndTime() {
        return playEndTime;
    }

    public void setPlayEndTime(Date playEndTime) {
        this.playEndTime = playEndTime;
    }

    public String getWeekSet() {
        return weekSet;
    }

    public void setWeekSet(String weekSet) {
        this.weekSet = weekSet == null ? null : weekSet.trim();
    }

    public Integer getPlayLevel() {
        return playLevel;
    }

    public void setPlayLevel(Integer playLevel) {
        this.playLevel = playLevel;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

	public Integer getProgramOrder() {
		return programOrder;
	}

	public void setProgramOrder(Integer programOrder) {
		this.programOrder = programOrder;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


}
