package com.zycm.zybao.model.mqmodel.task.runlog;

import java.util.List;

/** 定时反馈运行日志消息体
* @ClassName: RunLogTask
* @Description: TODO
* @author sy
* @date 2017年9月15日 下午6:03:34
*
*/
public class RunLogTask {

	private String machineCode;//终端机器码

	private List<RunLogList> runLogList;//运行日志列表

	public String getMachineCode() {
		return machineCode;
	}

	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}

	public List<RunLogList> getRunLogList() {
		return runLogList;
	}

	public void setRunLogList(List<RunLogList> runLogList) {
		this.runLogList = runLogList;
	}


}
