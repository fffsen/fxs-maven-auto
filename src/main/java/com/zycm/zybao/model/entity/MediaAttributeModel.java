package com.zycm.zybao.model.entity;

import java.math.BigDecimal;
import java.util.Date;

/** 媒体机属性表
* @ClassName: MediaAttributeModel
* @Description: TODO
* @author sy
* @date 2017年8月15日 下午5:02:17
*
*/
public class MediaAttributeModel {

    private Integer mid;

    private String machineCode;

    private String clientNumber;

    private String clientName;

    private String sysVersion;

    private String clientVersion;

    private String hardware;

    private String registerCode;

    private Integer light;

    private Integer voice;

    private Integer maxVoice;

    private Double cpuTemp;

    private BigDecimal diskSpace;

    private BigDecimal diskFreeSpace;

    private Integer expireFileNum;

    private Integer playFileNum;

    private Integer playProgramNum;

    private String phoneNumber;

    private Integer ftpId;

    private Date downloadStartTime;

    private Date downloadEndTime;

    private Integer cleanFileDay;

    private String mediaIp;

    private Date createTime;

    private Integer adStatus;

    private Integer adDelete;

    private Double downloadProgress;

    private Date lastReceiveTime;

    private Integer netType;

    private Date workStartTime;

    private Date workEndTime;

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode == null ? null : machineCode.trim();
    }

    public String getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber == null ? null : clientNumber.trim();
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName == null ? null : clientName.trim();
    }

    public String getSysVersion() {
        return sysVersion;
    }

    public void setSysVersion(String sysVersion) {
        this.sysVersion = sysVersion == null ? null : sysVersion.trim();
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion == null ? null : clientVersion.trim();
    }

    public String getHardware() {
        return hardware;
    }

    public void setHardware(String hardware) {
        this.hardware = hardware == null ? null : hardware.trim();
    }

    public String getRegisterCode() {
        return registerCode;
    }

    public void setRegisterCode(String registerCode) {
        this.registerCode = registerCode == null ? null : registerCode.trim();
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber == null ? null : phoneNumber.trim();
    }

    public Integer getFtpId() {
        return ftpId;
    }

    public void setFtpId(Integer ftpId) {
        this.ftpId = ftpId;
    }

	public Date getDownloadStartTime() {
		return downloadStartTime;
	}

	public void setDownloadStartTime(Date downloadStartTime) {
		this.downloadStartTime = downloadStartTime;
	}

	public Date getDownloadEndTime() {
		return downloadEndTime;
	}

	public void setDownloadEndTime(Date downloadEndTime) {
		this.downloadEndTime = downloadEndTime;
	}

	public Integer getCleanFileDay() {
		return cleanFileDay;
	}

	public void setCleanFileDay(Integer cleanFileDay) {
		this.cleanFileDay = cleanFileDay;
	}

	public String getMediaIp() {
		return mediaIp;
	}

	public void setMediaIp(String mediaIp) {
		this.mediaIp = mediaIp;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getAdStatus() {
		return adStatus;
	}

	public void setAdStatus(Integer adStatus) {
		this.adStatus = adStatus;
	}

	public Integer getAdDelete() {
		return adDelete;
	}

	public void setAdDelete(Integer adDelete) {
		this.adDelete = adDelete;
	}

	public Double getDownloadProgress() {
		return downloadProgress;
	}

	public void setDownloadProgress(Double downloadProgress) {
		this.downloadProgress = downloadProgress;
	}

	public Date getLastReceiveTime() {
		return lastReceiveTime;
	}

	public void setLastReceiveTime(Date lastReceiveTime) {
		this.lastReceiveTime = lastReceiveTime;
	}

	public Integer getNetType() {
		return netType;
	}

	public void setNetType(Integer netType) {
		this.netType = netType;
	}

	public Date getWorkStartTime() {
		return workStartTime;
	}

	public void setWorkStartTime(Date workStartTime) {
		this.workStartTime = workStartTime;
	}

	public Date getWorkEndTime() {
		return workEndTime;
	}

	public void setWorkEndTime(Date workEndTime) {
		this.workEndTime = workEndTime;
	}

 }
