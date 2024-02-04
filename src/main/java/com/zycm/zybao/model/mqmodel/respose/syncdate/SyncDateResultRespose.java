package com.zycm.zybao.model.mqmodel.respose.syncdate;

/**
* @ClassName: SyncDateRespose
* @Description: 同步时间后的结果反馈 判断时间是否校准  终端到服务器的通信
* @author sy
* @date 2017年11月30日 下午5:23:38
*
*/
public class SyncDateResultRespose {

	private String machineCode;//终端机器码

	private String clientDatetime;//终端时间 做校准

	public String getMachineCode() {
		return machineCode;
	}

	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}

	public String getClientDatetime() {
		return clientDatetime;
	}

	public void setClientDatetime(String clientDatetime) {
		this.clientDatetime = clientDatetime;
	}

}
