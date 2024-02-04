package com.zycm.zybao.model.mqmodel.respose.publishprogram;

import java.io.Serializable;

/** 终端节目下载状态
* @ClassName: ProgramDownloadRespose
* @Description: TODO
* @author sy
* @date 2022年01月13日 上午11:37:27
*
*/
public class ProgramDownloadRespose implements Serializable{

	private String machineCode;//终端机器码

	private String finishProgramIds;//下载完成的节目id ,多个逗号隔开

	private String notFinishProgramIds;//未下载完成的节目id ,多个逗号隔开

	public String getMachineCode() {
		return machineCode;
	}

	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}

	public String getFinishProgramIds() {
		return finishProgramIds;
	}

	public void setFinishProgramIds(String finishProgramIds) {
		this.finishProgramIds = finishProgramIds;
	}

	public String getNotFinishProgramIds() {
		return notFinishProgramIds;
	}

	public void setNotFinishProgramIds(String notFinishProgramIds) {
		this.notFinishProgramIds = notFinishProgramIds;
	}

	public String toString2(){
		return this.machineCode+"#"+this.finishProgramIds;
	}

}
