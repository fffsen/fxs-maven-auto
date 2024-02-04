package com.zycm.zybao.dao;

import com.zycm.zybao.model.entity.RolesFunctionModel;

import java.util.List;
import java.util.Map;

public interface RolesFunctionDao {
	/**
	* @Title: selectByRole
	* @Description: 根据角色id查询权限
	* @author sy
	* @param @param roleId
	* @param @return
	* @return List<Map<String,Object>>
	* @throws
	*/
	List<Map<String,Object>> selectByRole(Integer roleId);

	/**
	* @Title: deleteByPrimaryKey
	* @Description: 根据角色删除权限
	* @author sy
	* @param @param roleId
	* @return void
	* @throws
	*/
	void deleteByPrimaryKey(Integer roleId);

	/**
	* @Title: batchInsert
	* @Description: 批量新增权限
	* @author sy
	* @param @param list
	* @return void
	* @throws
	*/
	void batchInsert(List<RolesFunctionModel> list);
}
