package com.zycm.zybao.model.mqmodel.respose.clientconf;

import java.math.BigDecimal;

public class ClientConfRespose {

		private String machineCode;//终端机器码

		private String clientNumber;//终端编号

		private String clientVersion;//终端app版本

		private String dogVersion;//看门狗版本

		private String hardware;//终端主板型号

		private String sysVersion;//终端系统版本

		private Double cpuTemp;//cpu温度

		private BigDecimal diskSpace;//硬盘空间kb

		private BigDecimal diskFreeSpace;//空闲空间kb

		private Integer expireFileNum;//终端过期文件数

		private Integer playFileNum;//终端档期中文件数

		private Integer playProgramNum;//终端档期中节目数

	    private String ftpId;//ftp 的id

	    private Integer light;//终端的亮度

	    private Integer voice;//终端的音量

	    private Integer maxVoice;//终端的音量

	    private String iccid;//终端的物联卡信息iccid

	    private String connectAddr;//终端连接mq的地址

	    private String ftpAddrAndPort;//终端使用的ftp的地址信息及端口 如：192.168.2.169:2233

	    private Integer videoOutMode;//视频输出状态(0关闭 1开启)

	    private String recordScreenInfo;//录屏app相关信息包括版本 app名称等
	    //oss新加反馈字段
	    private Integer ftpType;//存储类型 1 ftp服务 2 阿里云存储

	    private String bucketName;//根目录下的主目录


		public String getClientNumber() {
			return clientNumber;
		}

		public void setClientNumber(String clientNumber) {
			this.clientNumber = clientNumber;
		}

		public String getSysVersion() {
			return sysVersion;
		}

		public void setSysVersion(String sysVersion) {
			this.sysVersion = sysVersion;
		}

		public String getMachineCode() {
			return machineCode;
		}

		public void setMachineCode(String machineCode) {
			this.machineCode = machineCode;
		}

		public String getClientVersion() {
			return clientVersion;
		}

		public void setClientVersion(String clientVersion) {
			this.clientVersion = clientVersion;
		}

		public String getHardware() {
			return hardware;
		}

		public void setHardware(String hardware) {
			this.hardware = hardware;
		}

		public Double getCpuTemp() {
			return cpuTemp;
		}

		public void setCpuTemp(Double cpuTemp) {
			this.cpuTemp = cpuTemp;
		}

		public BigDecimal getDiskSpace() {
			return diskSpace;
		}

		public void setDiskSpace(BigDecimal diskSpace) {
			this.diskSpace = diskSpace;
		}

		public BigDecimal getDiskFreeSpace() {
			return diskFreeSpace;
		}

		public void setDiskFreeSpace(BigDecimal diskFreeSpace) {
			this.diskFreeSpace = diskFreeSpace;
		}

		public Integer getExpireFileNum() {
			return expireFileNum;
		}

		public void setExpireFileNum(Integer expireFileNum) {
			this.expireFileNum = expireFileNum;
		}

		public Integer getPlayFileNum() {
			return playFileNum;
		}

		public void setPlayFileNum(Integer playFileNum) {
			this.playFileNum = playFileNum;
		}

		public Integer getPlayProgramNum() {
			return playProgramNum;
		}

		public void setPlayProgramNum(Integer playProgramNum) {
			this.playProgramNum = playProgramNum;
		}

		public String getFtpId() {
			return ftpId;
		}

		public void setFtpId(String ftpId) {
			this.ftpId = ftpId;
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

		public Integer getMaxVoice() {
			return maxVoice;
		}

		public void setMaxVoice(Integer maxVoice) {
			this.maxVoice = maxVoice;
		}

		public String getDogVersion() {
			return dogVersion;
		}

		public void setDogVersion(String dogVersion) {
			this.dogVersion = dogVersion;
		}

		public String getIccid() {
			return iccid;
		}

		public void setIccid(String iccid) {
			this.iccid = iccid;
		}

		public String getConnectAddr() {
			return connectAddr;
		}

		public void setConnectAddr(String connectAddr) {
			this.connectAddr = connectAddr;
		}

		public String getFtpAddrAndPort() {
			return ftpAddrAndPort;
		}

		public void setFtpAddrAndPort(String ftpAddrAndPort) {
			this.ftpAddrAndPort = ftpAddrAndPort;
		}

		public Integer getVideoOutMode() {
			return videoOutMode;
		}

		public void setVideoOutMode(Integer videoOutMode) {
			this.videoOutMode = videoOutMode;
		}

		public String getRecordScreenInfo() {
			return recordScreenInfo;
		}

		public void setRecordScreenInfo(String recordScreenInfo) {
			this.recordScreenInfo = recordScreenInfo;
		}

		public Integer getFtpType() {
			return ftpType;
		}

		public void setFtpType(Integer ftpType) {
			this.ftpType = ftpType;
		}

		public String getBucketName() {
			return bucketName;
		}

		public void setBucketName(String bucketName) {
			this.bucketName = bucketName;
		}

}
