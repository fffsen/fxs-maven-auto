package com.zycm.zybao.model.vo;

import com.zycm.zybao.model.entity.ProgramLayoutModel;

import java.io.Serializable;


/** 节目布局
* @ClassName: ProgramLayoutModel
* @Description: TODO
* @author sy
* @date 2017年8月15日 下午5:14:58
*
*/
public class ProgramLayoutVo implements Serializable{

	private Integer programId;

    private Integer areaId;

    private Integer areaType;

    private String areaInfo;

    private Integer layer;

    private Double x;

    private Double y;

    private Double width;

    private Double height;



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

    public Integer getAreaType() {
        return areaType;
    }

    public void setAreaType(Integer areaType) {
        this.areaType = areaType;
    }

    public String getAreaInfo() {
        return areaInfo;
    }

    public void setAreaInfo(String areaInfo) {
        this.areaInfo = areaInfo == null ? null : areaInfo.trim();
    }

    public Integer getLayer() {
        return layer;
    }

    public void setLayer(Integer layer) {
        this.layer = layer;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public ProgramLayoutModel toModel(){
    	ProgramLayoutModel programLayoutModel = new ProgramLayoutModel();
    	programLayoutModel.setProgramId(programId);
    	programLayoutModel.setAreaId(areaId);
    	programLayoutModel.setAreaType(areaType);
    	programLayoutModel.setAreaInfo(areaInfo);
    	programLayoutModel.setLayer(layer);
    	programLayoutModel.setX(x);
    	programLayoutModel.setY(y);
    	programLayoutModel.setWidth(width);
    	programLayoutModel.setHeight(height);
    	return programLayoutModel;
    }
}
