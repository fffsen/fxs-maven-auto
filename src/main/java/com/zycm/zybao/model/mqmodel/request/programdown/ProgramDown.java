package com.zycm.zybao.model.mqmodel.request.programdown;

import java.math.BigDecimal;

/** 节目下刊消息体
* @ClassName: ProgramDown
* @Description: TODO
* @author sy
* @date 2017年9月13日 上午10:04:31
*
*/
public class ProgramDown {

	private Integer programId;

	private String programName;

	private Integer totalPlayTime;

	private Double programWidth;

	private Double programHeight;

	private Integer screenType;

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


}
