package com.zycm.zybao.model.entity;

/** 终端组与终端关系表
* @ClassName: MediaGroupRelationModel
* @Description: TODO
* @author sy
* @date 2017年8月15日 下午5:07:55
*
*/
public class MediaGroupRelationModel {
    private Integer mediaGroupId;

    private Integer mid;

    public Integer getMediaGroupId() {
        return mediaGroupId;
    }

    public void setMediaGroupId(Integer mediaGroupId) {
        this.mediaGroupId = mediaGroupId;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }
}
