package com.zycm.zybao.model.entity;

import java.util.Date;

/**
* @ClassName: SysUserGroupModel
* @Description: 用户组实体
* @author sy
* @date 2018年12月20日
*
*/
public class SysUserGroupModel {

    private Integer uGroupId;

    private String userGroupName;

    private Date createTime;

    public Integer getuGroupId() {
        return uGroupId;
    }

    public void setuGroupId(Integer uGroupId) {
        this.uGroupId = uGroupId;
    }

    public String getUserGroupName() {
        return userGroupName;
    }

    public void setUserGroupName(String userGroupName) {
        this.userGroupName = userGroupName == null ? null : userGroupName.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
