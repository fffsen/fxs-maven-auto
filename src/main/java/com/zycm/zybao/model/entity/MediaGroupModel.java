package com.zycm.zybao.model.entity;

/** 媒体机终端组
* @ClassName: MediaGroupModel
* @Description: TODO
* @author sy
* @date 2017年8月15日 下午5:06:53
*
*/
public class MediaGroupModel {
    private Integer mediaGroupId;

    private String mediaGroupName;

    private Integer timeLongWarn;

    private Integer parentId;

    private Integer isFixed;

    public Integer getMediaGroupId() {
        return mediaGroupId;
    }

    public void setMediaGroupId(Integer mediaGroupId) {
        this.mediaGroupId = mediaGroupId;
    }

    public String getMediaGroupName() {
        return mediaGroupName;
    }

    public void setMediaGroupName(String mediaGroupName) {
        this.mediaGroupName = mediaGroupName == null ? null : mediaGroupName.trim();
    }

    public Integer getTimeLongWarn() {
        return timeLongWarn;
    }

    public void setTimeLongWarn(Integer timeLongWarn) {
        this.timeLongWarn = timeLongWarn;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

	public Integer getIsFixed() {
		return isFixed;
	}

	public void setIsFixed(Integer isFixed) {
		this.isFixed = isFixed;
	}


}
