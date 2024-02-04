package com.zycm.zybao.model.entity;

import java.io.Serializable;
import java.util.Date;

/**
* @ClassName: RolesModel
* @Description: 角色表
* @author sy
* @date 2017年11月6日 下午4:21:30
*
*/
public class RolesModel implements Serializable{
    private Integer roleId;

    private String roleName;

    private String roleIntroduce;

    private Date roleCreateTime;

    private Integer isDelete;

    private String roleToProject;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }


    public String getRoleIntroduce() {
		return roleIntroduce;
	}

	public void setRoleIntroduce(String roleIntroduce) {
		this.roleIntroduce = roleIntroduce;
	}

	public Date getRoleCreateTime() {
        return roleCreateTime;
    }

    public void setRoleCreateTime(Date roleCreateTime) {
        this.roleCreateTime = roleCreateTime;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public String getRoleToProject() {
        return roleToProject;
    }

    public void setRoleToProject(String roleToProject) {
        this.roleToProject = roleToProject == null ? null : roleToProject.trim();
    }
}
