package com.zycm.zybao.model.entity;

import java.util.Date;

public class ProgramListTimeSetModel {
    private Integer programListTimeSetId;

    private Integer listDetailId;

    private Date startDate;

    private Date startTime;

    private Date endDate;

    private Date endTime;

    private Integer frequency;

    public Integer getProgramListTimeSetId() {
        return programListTimeSetId;
    }

    public void setProgramListTimeSetId(Integer programListTimeSetId) {
        this.programListTimeSetId = programListTimeSetId;
    }

    public Integer getListDetailId() {
        return listDetailId;
    }

    public void setListDetailId(Integer listDetailId) {
        this.listDetailId = listDetailId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }
}
