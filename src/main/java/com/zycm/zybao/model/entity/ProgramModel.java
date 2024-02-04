package com.zycm.zybao.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/** 节目表
* @ClassName: ProgramModel
* @Description: TODO
* @author sy
* @date 2017年8月15日 下午5:18:04
*
*/
public class ProgramModel implements Serializable{
    private Integer programId;

    private String programName;

    private Integer totalPlayTime;

    private BigDecimal totalSize;

    private Double programWidth;

    private Double programHeight;

    private Integer screenType;

    private Integer auditStatus;

    private String auditRemark;

    private Integer creatorId;

    private Date createTime;

    private Date updateTime;

    private Integer backgroundId;

    private String backgroundColor;

    //数据处理字段
    private ProgramMaterialModel programMaterialModel;
    private String backgroundPath;
    private List<ProgramLayoutModel> plmodel;

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


    public BigDecimal getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(BigDecimal totalSize) {
		this.totalSize = totalSize;
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

    public String getAuditRemark() {
		return auditRemark;
	}

	public void setAuditRemark(String auditRemark) {
		this.auditRemark = auditRemark;
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

	public ProgramMaterialModel getProgramMaterialModel() {
		return programMaterialModel;
	}

	public void setProgramMaterialModel(ProgramMaterialModel programMaterialModel) {
		this.programMaterialModel = programMaterialModel;
	}

	public List<ProgramLayoutModel> getPlmodel() {
		return plmodel;
	}

	public void setPlmodel(List<ProgramLayoutModel> plmodel) {
		this.plmodel = plmodel;
	}

	public String getBackgroundPath() {
		return backgroundPath;
	}

	public void setBackgroundPath(String backgroundPath) {
		this.backgroundPath = backgroundPath;
	}


}
