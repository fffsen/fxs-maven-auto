package com.zycm.zybao.model.mqmodel.request.publish;

import java.io.Serializable;
import java.util.List;

/** 节目信息消息体
* @ClassName: ProgramMsg
* @Description: TODO
* @author sy
* @date 2017年9月12日 下午6:06:02
*
*/
public class ProgramMsg implements Serializable{

	private Integer programId;

	private String programName;

	private Integer totalPlayTime;

	private Double programWidth;

	private Double programHeight;

	private Integer screenType;

	private Integer programOrder;

	private ProgramDateSetMsg programDateSetMsg;

	private List<ProgramLayoutMsg> programLayoutMsgList;

	private ProgramBackgroundMsg programBackgroundMsg;

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

	public ProgramDateSetMsg getProgramDateSetMsg() {
		return programDateSetMsg;
	}

	public void setProgramDateSetMsg(ProgramDateSetMsg programDateSetMsg) {
		this.programDateSetMsg = programDateSetMsg;
	}

	public List<ProgramLayoutMsg> getProgramLayoutMsgList() {
		return programLayoutMsgList;
	}

	public void setProgramLayoutMsgList(List<ProgramLayoutMsg> programLayoutMsgList) {
		this.programLayoutMsgList = programLayoutMsgList;
	}

	public ProgramBackgroundMsg getProgramBackgroundMsg() {
		return programBackgroundMsg;
	}

	public void setProgramBackgroundMsg(ProgramBackgroundMsg programBackgroundMsg) {
		this.programBackgroundMsg = programBackgroundMsg;
	}

	public Integer getProgramOrder() {
		return programOrder;
	}

	public void setProgramOrder(Integer programOrder) {
		this.programOrder = programOrder;
	}

}
