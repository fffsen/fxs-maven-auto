package com.zycm.zybao.dao;

import com.zycm.zybao.model.entity.ModuleFunctionModel;

import java.util.List;
import java.util.Map;

public interface ModuleFunctionDao {


	/**
	* @Title: selectByRoleIds
	* @Description: 查询多个角色的权限合集
	* @param roleId
	* @return    参数
	* @author sy
	* @throws
	* @return List<ModuleFunctionModel>    返回类型
	*
	*/
	List<ModuleFunctionModel> selectByRoleIds(Integer[] roleId);
}
