package com.zycm.zybao.model.vo;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.zycm.zybao.model.entity.UserModel;

public class UserVo implements Serializable{

    private Integer uid;

    private String userName;

    private String password;

    private String realName;

    private String userMobphone;

    private String uPic;

    private Integer roleId;

    private Integer departmentId;

    private String info;

    private String mediaGroupIds;

    private Integer isAll;

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


    public String getUserMobphone() {
        return userMobphone;
    }

    public void setUserMobphone(String userMobphone) {
        this.userMobphone = userMobphone == null ? null : userMobphone.trim();
    }

    public String getuPic() {
        return uPic;
    }

    public void setuPic(String uPic) {
        this.uPic = uPic == null ? null : uPic.trim();
    }



    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
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


    public String getMediaGroupIds() {
		return mediaGroupIds;
	}

	public void setMediaGroupIds(String mediaGroupIds) {
		this.mediaGroupIds = mediaGroupIds;
	}

	public Integer getIsAll() {
		return isAll;
	}

	public void setIsAll(Integer isAll) {
		this.isAll = isAll;
	}



	public Integer getuGroupId() {
		return uGroupId;
	}

	public void setuGroupId(Integer uGroupId) {
		this.uGroupId = uGroupId;
	}



	public Integer getIsSyncUserGroup() {
		return isAll;
	}

	public void setIsSyncUserGroup(Integer isSyncUserGroup) {
		this.isSyncUserGroup = isAll;
	}

	public String isNotEmpty(){
    	String errormsg = "";
		if(StringUtils.isBlank(userName)){
			errormsg = "用户名不能为空";
			return errormsg;
		}
		if(StringUtils.isBlank(password)){
			errormsg = "用户密码不能为空";
			return errormsg;
		}
		if(null == roleId || roleId <= 0){
			errormsg = "用户角色不能为空";
			return errormsg;
		}
		if(null == uGroupId || uGroupId <= 0){
			errormsg = "用户组不能为空";
			return errormsg;
		}
		if(StringUtils.isBlank(userMobphone)){
			errormsg = "用户联系方式不能为空";
			return errormsg;
		}


		return errormsg;
    }

    public String isNotEmpty2(){
    	String errormsg = "";
		if(StringUtils.isBlank(userName)){
			errormsg = "用户名不能为空";
			return errormsg;
		}

		if(null == roleId || roleId <= 0){
			errormsg = "用户角色不能为空";
			return errormsg;
		}
		if(StringUtils.isBlank(userMobphone)){
			errormsg = "用户联系方式不能为空";
			return errormsg;
		}
		if(null == uGroupId || uGroupId <= 0){
			errormsg = "用户组不能为空";
			return errormsg;
		}

		return errormsg;
    }

    public String isNotEmpty3(){
    	String errormsg = "";
		if(StringUtils.isBlank(realName)){
			errormsg = "真实名不能为空";
			return errormsg;
		}

		if(StringUtils.isBlank(userMobphone)){
			errormsg = "用户联系方式不能为空";
			return errormsg;
		}


		return errormsg;
    }

    public UserModel toModel(){
    	UserModel userModel = new UserModel();
    	userModel.setUid(uid);
    	userModel.setUserName(userName);
    	userModel.setPassword(password);
    	userModel.setRealName(realName);
    	userModel.setUserMobphone(userMobphone);
    	userModel.setuPic(uPic);
    	//userModel.setRoleId(roleId);
    	userModel.setDepartmentId(departmentId);
    	userModel.setInfo(info);
    	userModel.setuGroupId(uGroupId);
    	userModel.setIsSyncUserGroup(isAll);
    	//userModel.setUserAddress(userAddress);
		return userModel;
    }
}
