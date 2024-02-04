package com.zycm.zybao.model.entity;

import java.util.Date;

/**
* @ClassName: ProgramListDetailModel
* @Description: 节目单详细
* @author sy
* @date 2017年9月30日 下午2:52:07
*
*/
public class ProgramListDetailModel {
    private Integer listDetailId;

    private Integer listId;

    private Integer programId;

    private Integer timeMode;

    private Integer switchMode;

    private Integer downloadMode;

    private Integer programOrder;

    private Date playStartDate;

    private Date playStartTime;

    private Date playEndDate;

    private Date playEndTime;

    private String weekSet;

    private Integer playLevel;

    private Integer frequency;

    public Integer getListDetailId() {
        return listDetailId;
    }

    public void setListDetailId(Integer listDetailId) {
        this.listDetailId = listDetailId;
    }

    public Integer getListId() {
        return listId;
    }

    public void setListId(Integer listId) {
        this.listId = listId;
    }

    public Integer getProgramId() {
        return programId;
    }

    public void setProgramId(Integer programId) {
        this.programId = programId;
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

    public Integer getProgramOrder() {
        return programOrder;
    }

    public void setProgramOrder(Integer programOrder) {
        this.programOrder = programOrder;
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
}
