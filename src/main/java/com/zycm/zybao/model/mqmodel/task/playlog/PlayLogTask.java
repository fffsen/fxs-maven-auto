package com.zycm.zybao.model.mqmodel.task.playlog;

import java.util.List;

/** 终端播放日志消息体
* @ClassName: PlayLogTask
* @Description: TODO
* @author sy
* @date 2017年9月15日 下午6:14:28
*
*/
public class PlayLogTask {
	private String machineCode;//终端机器码

	private List<PlayLogList> playLogList;//播放日志列表

	public String getMachineCode() {
		return machineCode;
	}

	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}

	public List<PlayLogList> getPlayLogList() {
		return playLogList;
	}

	public void setPlayLogList(List<PlayLogList> playLogList) {
		this.playLogList = playLogList;
	}





}
