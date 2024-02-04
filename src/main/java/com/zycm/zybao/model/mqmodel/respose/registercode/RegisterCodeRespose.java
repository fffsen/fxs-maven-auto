package com.zycm.zybao.model.mqmodel.respose.registercode;

/** 注册消息体
* @ClassName: RegisterCode
* @Description: TODO
* @author sy
* @date 2017年9月13日 下午2:47:31
*
*/
public class RegisterCodeRespose {

	private String machineCode;//终端机器码

	private String code;//注册码

	private Integer result;//校验结果

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public String getMachineCode() {
		return machineCode;
	}

	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}



}
