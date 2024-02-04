package com.zycm.zybao.model.mqmodel.request.deletefiie;

/**
* @ClassName: DeleteExpireFile
* @Description: 清理过期文件实体类
* @author sy
* @date 2019年3月16日
*
*/
public class DeleteExpireFile {

	private String machineCode;//机器码

	private String startDate;//开始时间  yyyy-MM-dd

	private Integer interval;//隔多少天清理一次

	private Integer mode;//0立即清理  1按设置每隔x天清理

	public String getMachineCode() {
		return machineCode;
	}

	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public Integer getInterval() {
		return interval;
	}

	public void setInterval(Integer interval) {
		this.interval = interval;
	}

	public Integer getMode() {
		return mode;
	}

	public void setMode(Integer mode) {
		this.mode = mode;
	}




}
