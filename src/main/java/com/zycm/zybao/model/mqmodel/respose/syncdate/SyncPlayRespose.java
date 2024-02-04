package com.zycm.zybao.model.mqmodel.respose.syncdate;

/**
* @ClassName: SyncDateRespose
* @Description: 同步播放接收指令
* @author sy
* @date 2017年11月30日 下午5:23:38
*
*/
public class SyncPlayRespose {

	private String machineCode;//终端机器码

	private String programName;//节目名称

	private long playProgress;//节目已播放的毫秒数

	public String getMachineCode() {
		return machineCode;
	}

	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public long getPlayProgress() {
		return playProgress;
	}

	public void setPlayProgress(long playProgress) {
		this.playProgress = playProgress;
	}



}
