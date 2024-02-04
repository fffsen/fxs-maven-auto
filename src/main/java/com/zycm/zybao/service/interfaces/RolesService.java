package com.zycm.zybao.service.interfaces;

import com.zycm.zybao.model.entity.RolesModel;

import java.util.List;
import java.util.Map;

public interface RolesService {

	/** 分页查询角色信息
	* @Title: selectPage
	* @Description:
	* @author sy
	* @param @param param
	* @param @return
	* @return List<Map<String,Object>>
	*
	*/
	Map<String,Object> selectPage(Integer page,Integer pageSize,Map<String, Object> param);

	/** 不分页查询角色信息
	* @Title: selectRoleInfo
	* @Description: 不分页
	* @author sy
	* @param @param param
	* @param @return
	* @return List<Map<String,Object>>
	*
	*/
	List<Map<String,Object>> selectRoleInfo(Map<String,Object> param);


	/** 根据角色id 查询详细
	* @Title: selectByPrimaryKey
	* @Description:
	* @author sy
	* @param @param roleId
	* @param @return
	* @return Map<String,Object>
	*
	*/
	Map<String,Object> selectByPrimaryKey(Integer roleId);

	/** 新增角色
	* @Title: insert
	* @Description:
	* @author sy
	* @param @param rolesModel
	* @return void
	*
	*/
	void insert(String roleName,String roleIntroduce);

	/** 修改角色
	* @Title: updateByPrimaryKey
	* @Description:
	* @author sy
	* @param @param rolesModel
	* @return void
	*
	*/
	void updateByPrimaryKey(Integer roleId,String roleName,String roleIntroduce);

	/**  删除角色
	* @Title: deleteByPrimaryKey
	* @Description:
	* @author sy
	* @param @param roleId
	* @return void
	*
	*/
	void deleteByPrimaryKey(Integer roleId) throws Exception;

	/** 校验角色名重复
	* @Title: selectByName
	* @Description:
	* @author sy
	* @param @param param
	* @param @return
	* @return List<Map<String,Object>>
	*
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
