package com.zycm.zybao.model.mqmodel.respose.screenshot;

/** 远程截屏  响应消息体
* @ClassName: Screenshot
* @Description: TODO
* @author sy
* @date 2017年9月13日 上午10:37:41
*
*/
public class ScreenshotRespose {

	/** 处理结果状态标志 0失败  1成功
	* @Fields result : TODO
	*/
	private Integer result;

	/**  处理结果说明信息
	* @Fields message : TODO
	*/
	private String message;

	/** 终端机器码
	* @Fields machineCode : TODO
	*/
	private String machineCode;

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMachineCode() {
		return machineCode;
	}

	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}

}
