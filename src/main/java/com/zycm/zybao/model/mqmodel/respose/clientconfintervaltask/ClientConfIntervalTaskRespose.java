package com.zycm.zybao.model.mqmodel.respose.clientconfintervaltask;

/**
* @ClassName: ClientConfIntervalTaskRespose
* @Description: 每十分钟接收终端的定时任务反馈的字段
* @author sy
* @date 2020年12月10日
*
*/
public class ClientConfIntervalTaskRespose {

	private String machineCode;//终端机器码

	private Integer recordScreenStatus;//录屏状态 1开启中 2已开启 3开启失败 11关闭中 12已关闭 13关闭失败 0未知


	public String getMachineCode() {
		return machineCode;
	}

	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}

	public Integer getRecordScreenStatus() {
		return recordScreenStatus;
	}

	public void setRecordScreenStatus(Integer recordScreenStatus) {
		this.recordScreenStatus = recordScreenStatus;
	}

}
