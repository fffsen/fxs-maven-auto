package com.zycm.zybao.model.mqmodel.task.heartbeat;

import java.io.Serializable;
import java.util.Date;

/** 心跳反馈消息体
* @ClassName: HeartbeatTask
* @Description: TODO
* @author sy
* @date 2017年9月15日 下午5:32:29
*
*/
public class HeartbeatTask implements Serializable{
	private String machineCode;//终端机器码

	private String clientNumber;//终端编码名称

	private String ip;//终端ip

	private String playProgramIds;//终端播放的多个节目的id,逗号隔开

	private String playMaterialIds;//终端播放的多个素材的id,逗号隔开

	//private Integer runStatus;//1正常运行    0运行异常（节目播放不正常）

	private Date receiveTime;//接收消息时间

	private Integer isUpdateToDB;//是否需要更新到数据库 0不需要  1需要

	public String getMachineCode() {
		return machineCode;
	}

	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}

	/*public Integer getRunStatus() {
		return runStatus;
	}

	public void setRunStatus(Integer runStatus) {
		this.runStatus = runStatus;
	}*/

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPlayProgramIds() {
		return playProgramIds;
	}

	public void setPlayProgramIds(String playProgramIds) {
		this.playProgramIds = playProgramIds;
	}

	public String getClientNumber() {
		return clientNumber;
	}

	public void setClientNumber(String clientNumber) {
		this.clientNumber = clientNumber;
	}

	public String getPlayMaterialIds() {
		return playMaterialIds;
	}

	public void setPlayMaterialIds(String playMaterialIds) {
		this.playMaterialIds = playMaterialIds;
	}

	public Date getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}

	public Integer getIsUpdateToDB() {
		return isUpdateToDB;
	}

	public void setIsUpdateToDB(Integer isUpdateToDB) {
		this.isUpdateToDB = isUpdateToDB;
	}


}
