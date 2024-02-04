package com.zycm.zybao.model.mqmodel.request.publish;

import java.io.Serializable;
import java.util.List;

/** 节目布局消息体
* @ClassName: ProgramLayoutMsg
* @Description: TODO
* @author sy
* @date 2017年8月15日 下午5:14:58
*
*/
public class ProgramLayoutMsg implements Serializable{


    private Integer areaId;

    private Integer areaType;

    private Integer layer;

    private Double x;

    private Double y;

    private Double areaWidth;

    private Double areaHeight;

    private List<ProgramFileMsg> programFileMsgList;


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

	public Double getAreaWidth() {
		return areaWidth;
	}

	public void setAreaWidth(Double areaWidth) {
		this.areaWidth = areaWidth;
	}

	public Double getAreaHeight() {
		return areaHeight;
	}

	public void setAreaHeight(Double areaHeight) {
		this.areaHeight = areaHeight;
	}

	public List<ProgramFileMsg> getProgramFileMsgList() {
		return programFileMsgList;
	}

	public void setProgramFileMsgList(List<ProgramFileMsg> programFileMsgList) {
		this.programFileMsgList = programFileMsgList;
	}


}
