package com.zycm.zybao.service.interfaces;

import com.zycm.zybao.model.entity.ModuleFunctionModel;
import com.zycm.zybao.model.entity.ModuleModel;

import java.util.List;

public interface ModuleService {

	/** 查询发布系统所有模块的功能点
	* @Title: selectPublishModule
	* @Description:
	* @author sy
	* @param @return
	* @return List<ModuleModel>
	*
	*/
	public List<ModuleModel> selectPublishModule();

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
