package com.zycm.zybao.model.mqmodel.request.light;

/**
* @ClassName: light
* @Description: TODO(调节终端亮度)
* @author sy
* @date 2021年10月7日
*
*/
public class Light {
	private String machineCode;
	private Integer lightRatio;

	public String getMachineCode() {
		return machineCode;
	}
	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}
	public Integer getLightRatio() {
		return lightRatio;
	}
	public void setLightRatio(Integer lightRatio) {
		this.lightRatio = lightRatio;
	}

}
