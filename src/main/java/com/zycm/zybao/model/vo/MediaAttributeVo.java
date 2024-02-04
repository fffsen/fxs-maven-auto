package com.zycm.zybao.model.vo;

import com.zycm.zybao.model.entity.MediaAttributeModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;

@Slf4j
public class MediaAttributeVo implements Serializable{
	private Integer mid;

	private String machineCode;//终端机器码

	private String clientName;

	private Integer ftpId;

	private String downloadStartTime;

	private String downloadEndTime;

	private Integer cleanFileDay;

	private Integer light;//终端的亮度 0-100

	private Integer voice;//终端的音量

	private Integer netType;

	private String workStartTime;

	private String workEndTime;

	public Integer getMid() {
		return mid;
	}

	public void setMid(Integer mid) {
		this.mid = mid;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public Integer getFtpId() {
		return ftpId;
	}

	public void setFtpId(Integer ftpId) {
		this.ftpId = ftpId;
	}

	public String getDownloadStartTime() {
		return downloadStartTime;
	}

	public void setDownloadStartTime(String downloadStartTime) {
		this.downloadStartTime = downloadStartTime;
	}

	public String getDownloadEndTime() {
		return downloadEndTime;
	}

	public void setDownloadEndTime(String downloadEndTime) {
		this.downloadEndTime = downloadEndTime;
	}

	public Integer getCleanFileDay() {
		return cleanFileDay;
	}

	public void setCleanFileDay(Integer cleanFileDay) {
		this.cleanFileDay = cleanFileDay;
	}

	public String getMachineCode() {
		return machineCode;
	}

	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}

	public Integer getLight() {
		return light;
	}

	public void setLight(Integer light) {
		this.light = light;
	}

	public Integer getVoice() {
		return voice;
	}

	public void setVoice(Integer voice) {
		this.voice = voice;
	}

	public Integer getNetType() {
		return netType;
	}

	public void setNetType(Integer netType) {
		this.netType = netType;
	}

	public String getWorkStartTime() {
		return workStartTime;
	}

	public void setWorkStartTime(String workStartTime) {
		this.workStartTime = workStartTime;
	}

	public String getWorkEndTime() {
		return workEndTime;
	}

	public void setWorkEndTime(String workEndTime) {
		this.workEndTime = workEndTime;
	}

	public MediaAttributeModel toModel(){
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		MediaAttributeModel mediaAttributeModel = new MediaAttributeModel();
		mediaAttributeModel.setMid(mid);
		mediaAttributeModel.setFtpId(ftpId);
		mediaAttributeModel.setClientName(clientName);
		try {
			mediaAttributeModel.setDownloadEndTime(sdf.parse(downloadEndTime));
			mediaAttributeModel.setDownloadStartTime(sdf.parse(downloadStartTime));
			if(StringUtils.isNoneBlank(workEndTime,workStartTime)){
				mediaAttributeModel.setWorkEndTime(sdf.parse(workEndTime));
				mediaAttributeModel.setWorkStartTime(sdf.parse(workStartTime));
			}
		} catch (Exception e) {
			log.error("字符串转时间格式错误", e);
		}
		mediaAttributeModel.setCleanFileDay(cleanFileDay);
		mediaAttributeModel.setMachineCode(machineCode);
		mediaAttributeModel.setLight(light);
		mediaAttributeModel.setVoice(voice);
		mediaAttributeModel.setNetType(netType);
		return mediaAttributeModel;
	}
}
