package com.zycm.zybao.model.mqmodel.respose.downloadprogress;

/**
* @ClassName: DownloadProgressRespose
* @Description: 终端响应素材下载进度
* @author sy
* @date 2018年4月16日 下午2:40:55
*
*/
public class DownloadProgressRespose {
	private String machineCode;//终端机器码
	private String materialName;//素材文件名称
    private Double downloadProgress;//下载进度  小数型
    private Double downloadSpeed;//下载速度  小数型  kb

	public String getMachineCode() {
		return machineCode;
	}
	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}
	public String getMaterialName() {
		return materialName;
	}
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	public Double getDownloadProgress() {
		return downloadProgress;
	}
	public void setDownloadProgress(Double downloadProgress) {
		this.downloadProgress = downloadProgress;
	}
	public Double getDownloadSpeed() {
		return downloadSpeed;
	}
	public void setDownloadSpeed(Double downloadSpeed) {
		this.downloadSpeed = downloadSpeed;
	}


}
