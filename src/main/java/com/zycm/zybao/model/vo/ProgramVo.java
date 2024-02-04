package com.zycm.zybao.model.vo;

import com.zycm.zybao.model.entity.ProgramModel;

import java.io.Serializable;
import java.util.Date;


/** 节目表
* @ClassName: ProgramModel
* @Description: TODO
* @author sy
* @date 2017年8月15日 下午5:18:04
*
*/
public class ProgramVo implements Serializable{
    private Integer programId;

    private String programName;

    private Integer totalPlayTime;

    private Double programWidth;

    private Double programHeight;

    private Integer screenType;

    private Integer auditStatus;

    private Integer creatorId;

    private Date createTime;

    private Date updateTime;

    private Integer backgroundId;

    private String backgroundColor;

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
        this.programName = programName == null ? null : programName.trim();
    }

    public Integer getTotalPlayTime() {
        return totalPlayTime;
    }

    public void setTotalPlayTime(Integer totalPlayTime) {
        this.totalPlayTime = totalPlayTime;
    }

    public Double getProgramWidth() {
        return programWidth;
    }

    public void setProgramWidth(Double programWidth) {
        this.programWidth = programWidth;
    }

    public Double getProgramHeight() {
        return programHeight;
    }

    public void setProgramHeight(Double programHeight) {
        this.programHeight = programHeight;
    }

    public Integer getScreenType() {
        return screenType;
    }

    public void setScreenType(Integer screenType) {
        this.screenType = screenType;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getBackgroundId() {
        return backgroundId;
    }

    public void setBackgroundId(Integer backgroundId) {
        this.backgroundId = backgroundId;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor == null ? null : backgroundColor.trim();
    }

    public ProgramModel toModel(){
    	ProgramModel programModel= new ProgramModel();
    	programModel.setProgramId(programId);
    	programModel.setProgramName(programName);
    	programModel.setTotalPlayTime(totalPlayTime);
    	programModel.setProgramWidth(programWidth);
    	programModel.setProgramHeight(programHeight);
    	programModel.setScreenType(screenType);
    	programModel.setAuditStatus(auditStatus);
    	programModel.setCreatorId(creatorId);
    	programModel.setCreateTime(createTime);
    	programModel.setUpdateTime(updateTime);
    	programModel.setBackgroundId(backgroundId);
    	programModel.setBackgroundColor(backgroundColor);

    	return programModel;
    }
}
