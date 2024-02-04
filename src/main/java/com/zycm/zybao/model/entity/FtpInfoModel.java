package com.zycm.zybao.model.entity;

import java.io.Serializable;

/** ftp配置信息表
* @ClassName: FtpInfoModel
* @Description: TODO
* @author sy
* @date 2017年8月15日 下午5:01:22
*
*/
public class FtpInfoModel implements Serializable{
    private Integer ftpId;

    private String ipAddr;

    private Integer port;

    private String ftpUser;

    private String ftpPwd;

    private Integer isDefault;

    private Integer creatorId;

    private String previewPath;

    private Integer previewPort;

    private Integer ftpType;

    private String bucketName;

    private Integer openHttp;

    public Integer getFtpId() {
        return ftpId;
    }

    public void setFtpId(Integer ftpId) {
        this.ftpId = ftpId;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr == null ? null : ipAddr.trim();
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getFtpUser() {
        return ftpUser;
    }

    public void setFtpUser(String ftpUser) {
        this.ftpUser = ftpUser == null ? null : ftpUser.trim();
    }

    public String getFtpPwd() {
        return ftpPwd;
    }

    public void setFtpPwd(String ftpPwd) {
        this.ftpPwd = ftpPwd == null ? null : ftpPwd.trim();
    }

	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}

	public Integer getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
	}

	public String getPreviewPath() {
		return previewPath;
	}

	public void setPreviewPath(String previewPath) {
		this.previewPath = previewPath;
	}

	public Integer getPreviewPort() {
		return previewPort;
	}

	public void setPreviewPort(Integer previewPort) {
		this.previewPort = previewPort;
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

	public Integer getOpenHttp() {
		return openHttp;
	}

	public void setOpenHttp(Integer openHttp) {
		this.openHttp = openHttp;
	}


}
