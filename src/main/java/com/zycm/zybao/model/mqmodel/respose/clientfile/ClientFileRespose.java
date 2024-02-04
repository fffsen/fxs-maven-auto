package com.zycm.zybao.model.mqmodel.respose.clientfile;

import java.io.Serializable;
import java.util.List;

/** 终端文件响应消息体
* @ClassName: ClientFile
* @Description: TODO
* @author sy
* @date 2017年9月13日 上午11:37:27
*
*/
public class ClientFileRespose implements Serializable{

	private String machineCode;//终端机器码

	private List<ClientFileList> clientFileList;//终端文件列表

	public String getMachineCode() {
		return machineCode;
	}

	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}

	public List<ClientFileList> getClientFileList() {
		return clientFileList;
	}

	public void setClientFileList(List<ClientFileList> clientFileList) {
		this.clientFileList = clientFileList;
	}

}
