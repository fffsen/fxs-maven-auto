package com.zycm.zybao.model.entity;

import java.util.Date;

public class UserModel {

    private Integer uid;

    private String userName;

    private String password;

    private String realName;

    private String email;

    private String userMobphone;

    private Date regTime;

    private String uPic;

    private Date lastTime;

   /* private Integer roleId;*/

    private Integer userStatus;

    private Integer departmentId;

    private String info;

    private Integer isDelete;

    private Integer uGroupId;

    private Integer isSyncUserGroup;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getUserMobphone() {
        return userMobphone;
    }

    public void setUserMobphone(String userMobphone) {
        this.userMobphone = userMobphone == null ? null : userMobphone.trim();
    }

    public Date getRegTime() {
        return regTime;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }

    public String getuPic() {
        return uPic;
    }

    public void setuPic(String uPic) {
        this.uPic = uPic == null ? null : uPic.trim();
    }


    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

   /* public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }*/

    public Integer getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Integer userStatus) {
        this.userStatus = userStatus;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info == null ? null : info.trim();
    }

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Integer getuGroupId() {
		return uGroupId;
	}

	public void setuGroupId(Integer uGroupId) {
		this.uGroupId = uGroupId;
	}

	public Integer getIsSyncUserGroup() {
		return isSyncUserGroup;
	}

	public void setIsSyncUserGroup(Integer isSyncUserGroup) {
		this.isSyncUserGroup = isSyncUserGroup;
	}


}
