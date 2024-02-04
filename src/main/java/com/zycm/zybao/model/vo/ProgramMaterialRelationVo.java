package com.zycm.zybao.model.vo;

import com.zycm.zybao.model.entity.ProgramMaterialRelationModel;

import java.io.Serializable;


/** 节目素材关系表
* @ClassName: ProgramMaterialRelationModel
* @Description: TODO
* @author sy
* @date 2017年8月15日 下午5:16:52
*
*/
public class ProgramMaterialRelationVo implements Serializable{

	private Integer programId;

    private Integer areaId;

    private Integer materialId;

	private Integer materialOrder;

    private Integer playTime;

    private String extend;

    public Integer getProgramId() {
        return programId;
    }

    public void setProgramId(Integer programId) {
        this.programId = programId;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public Integer getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }

    public Integer getMaterialOrder() {
        return materialOrder;
    }

    public void setMaterialOrder(Integer materialOrder) {
        this.materialOrder = materialOrder;
    }

    public Integer getPlayTime() {
        return playTime;
    }

    public void setPlayTime(Integer playTime) {
        this.playTime = playTime;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend == null ? null : extend.trim();
    }

    public ProgramMaterialRelationModel toModel(){
    	ProgramMaterialRelationModel pr = new ProgramMaterialRelationModel();
    	pr.setProgramId(programId);
    	pr.setAreaId(areaId);
    	pr.setMaterialId(materialId);
    	pr.setMaterialOrder(materialOrder);
    	pr.setPlayTime(playTime);
    	pr.setExtend(extend);
    	return pr;
    }
}
