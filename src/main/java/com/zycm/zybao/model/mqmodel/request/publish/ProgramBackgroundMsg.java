package com.zycm.zybao.model.mqmodel.request.publish;

import java.io.Serializable;
import java.math.BigDecimal;

/** 节目背景设置消息体
* @ClassName: ProgramBackgroundMsg
* @Description: TODO
* @author sy
* @date 2017年9月12日 下午6:06:21
*
*/
public class ProgramBackgroundMsg implements Serializable{

	private Integer backgroundId;

    private String backgroundColor;

    private String backName;

    private BigDecimal backSize;

    private String backPath;

    private Integer backType;

    private BigDecimal backHeight;

    private BigDecimal backWidth;

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
		this.backgroundColor = backgroundColor;
	}

	public String getBackName() {
		return backName;
	}

	public void setBackName(String backName) {
		this.backName = backName;
	}

	public BigDecimal getBackSize() {
		return backSize;
	}

	public void setBackSize(BigDecimal backSize) {
		this.backSize = backSize;
	}

	public String getBackPath() {
		return backPath;
	}

	public void setBackPath(String backPath) {
		this.backPath = backPath;
	}

	public Integer getBackType() {
		return backType;
	}

	public void setBackType(Integer backType) {
		this.backType = backType;
	}

	public BigDecimal getBackHeight() {
		return backHeight;
	}

	public void setBackHeight(BigDecimal backHeight) {
		this.backHeight = backHeight;
	}

	public BigDecimal getBackWidth() {
		return backWidth;
	}

	public void setBackWidth(BigDecimal backWidth) {
		this.backWidth = backWidth;
	}



}
