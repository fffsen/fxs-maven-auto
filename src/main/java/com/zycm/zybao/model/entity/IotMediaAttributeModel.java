package com.zycm.zybao.model.entity;

import java.util.Date;

/**
* @ClassName: IotMediaAttributeModel
* @Description: 终端物联卡属性
* @author sy
* @date 2019年3月29日
*
*/
public class IotMediaAttributeModel {

    private String machineCode;

    private String iccid;

    private String imsi;

    private String accessNumber;

    private Integer iotType;

    private Long curDataUsage;

    private Long totalDataUsage;

    private String cardType;

    private Integer cardStatus;

    private Date createTime;

    private String remark;

    private Integer isMain;

    private Integer isDelete;

    private Integer iccidUpdateType;

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode == null ? null : machineCode.trim();
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid == null ? null : iccid.trim();
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi == null ? null : imsi.trim();
    }

    public String getAccessNumber() {
        return accessNumber;
    }

    public void setAccessNumber(String accessNumber) {
        this.accessNumber = accessNumber == null ? null : accessNumber.trim();
    }

    public Integer getIotType() {
        return iotType;
    }

    public void setIotType(Integer iotType) {
        this.iotType = iotType;
    }

    public Long getCurDataUsage() {
        return curDataUsage;
    }

    public void setCurDataUsage(Long curDataUsage) {
        this.curDataUsage = curDataUsage;
    }

    public Long getTotalDataUsage() {
        return totalDataUsage;
    }

    public void setTotalDataUsage(Long totalDataUsage) {
        this.totalDataUsage = totalDataUsage;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType == null ? null : cardType.trim();
    }

    public Integer getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(Integer cardStatus) {
        this.cardStatus = cardStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getIsMain() {
        return isMain;
    }

    public void setIsMain(Integer isMain) {
        this.isMain = isMain;
    }

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Integer getIccidUpdateType() {
		return iccidUpdateType;
	}

	public void setIccidUpdateType(Integer iccidUpdateType) {
		this.iccidUpdateType = iccidUpdateType;
	}


}
