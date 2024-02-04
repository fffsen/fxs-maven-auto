package com.zycm.zybao.dao;

import com.zycm.zybao.model.entity.RolesModel;

import java.util.List;
import java.util.Map;

public interface RolesDao {

	/**
	* @Title: selectPage
	* @Description: 分页查询
	* @author sy
	* @param @param param
	* @param @return
	* @return List<Map<String,Object>>
	* @throws
	*/
	List<Map<String,Object>> selectPage(Map<String,Object> param);


	/**
	* @Title: selectRoleInfo
	* @Description: 不分页
	* @author sy
	* @param @param param
	* @param @return
	* @return List<Map<String,Object>>
	* @throws
	*/
	List<Map<String,Object>> selectRoleInfo(Map<String,Object> param);

	/**
	* @Title: selectPageCount
	* @Description: 分页绑定统计总数
	* @author sy
	* @param @param param
	* @param @return
	* @return Integer
	* @throws
	*/
	Integer selectPageCount(Map<String,Object> param);

	/**
	* @Title: selectByPrimaryKey
	* @Description: 根据id查询详细
	* @author sy
	* @param @param roleId
	* @param @return
	* @return Map<String,Object>
	* @throws
	*/
	Map<String,Object> selectByPrimaryKey(Integer roleId);

	/**
	* @Title: insert
	* @Description: 新增角色
	* @author sy
	* @param @param rolesModel
	* @return void
	* @throws
	*/
	void insert(RolesModel rolesModel);

	/**
	* @Title: updateByPrimaryKey
	* @Description: 修改角色
	* @author sy
	* @param @param rolesModel
	* @return void
	* @throws
	*/
	void updateByPrimaryKey(RolesModel rolesModel);

	/**
	* @Title: deleteByPrimaryKey
	* @Description: 删除角色
	* @author sy
	* @param @param roleId
	* @return void
	* @throws
	*/
	void deleteByPrimaryKey(Integer roleId);

	/**
	* @Title: selectByName
	* @Description: 校验角色名重复
	* @author sy
	* @param @param param
	* @param @return
	* @return List<Map<String,Object>>
	* @throws
	*/
	List<Map<String,Object>> selectByName(Map<String,Object> param);

	/**
	* @Title: selectByUserName
	* @Description: 根据用户名查角色
	* @param userName
	* @return    参数
	* @author sy
	* @throws
	* @return List<RolesModel>    返回类型
	*
	*/
	List<RolesModel> selectByUserName(String userName);
}
