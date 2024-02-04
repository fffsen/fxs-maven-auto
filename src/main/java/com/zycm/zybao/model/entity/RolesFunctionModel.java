package com.zycm.zybao.model.entity;

/**
* @ClassName: RolesFunctionModel
* @Description: 角色功能点关系表
* @author sy
* @date 2017年11月6日 下午4:20:58
*
*/
public class RolesFunctionModel {
    private Integer roleId;

    private Integer functionId;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getFunctionId() {
        return functionId;
    }

    public void setFunctionId(Integer functionId) {
        this.functionId = functionId;
    }
}
