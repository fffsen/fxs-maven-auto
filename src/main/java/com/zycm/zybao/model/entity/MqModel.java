package com.zycm.zybao.model.entity;

public class MqModel {
    private Integer mqId;

    private String mqIp;

    private Integer mqPort;

    private String mqAccount;

    private String mqPwd;

    private Integer isDelete;

    private Integer creatorId;

    private String receiverTopName;//服务端订阅并接收终端反馈信息的主题名称 zhibo_respose_PTP

    private String senderTopName;//服务端订阅并发送信息到终端的主题名称 zhibo_request_PTP

    private String info;

    private String mqProtocol;

    public Integer getMqId() {
        return mqId;
    }

    public void setMqId(Integer mqId) {
        this.mqId = mqId;
    }

    public String getMqIp() {
        return mqIp;
    }

    public void setMqIp(String mqIp) {
        this.mqIp = mqIp == null ? null : mqIp.trim();
    }

    public Integer getMqPort() {
        return mqPort;
    }

    public void setMqPort(Integer mqPort) {
        this.mqPort = mqPort;
    }

    public String getMqAccount() {
        return mqAccount;
    }

    public void setMqAccount(String mqAccount) {
        this.mqAccount = mqAccount == null ? null : mqAccount.trim();
    }

    public String getMqPwd() {
        return mqPwd;
    }

    public void setMqPwd(String mqPwd) {
        this.mqPwd = mqPwd == null ? null : mqPwd.trim();
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

	public String getReceiverTopName() {
		return receiverTopName;
	}

	public void setReceiverTopName(String receiverTopName) {
		this.receiverTopName = receiverTopName;
	}

	public String getSenderTopName() {
		return senderTopName;
	}

	public void setSenderTopName(String senderTopName) {
		this.senderTopName = senderTopName;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getMqProtocol() {
		return mqProtocol;
	}

	public void setMqProtocol(String mqProtocol) {
		this.mqProtocol = mqProtocol;
	}


}
