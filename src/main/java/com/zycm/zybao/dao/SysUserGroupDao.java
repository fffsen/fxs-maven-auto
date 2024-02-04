package com.zycm.zybao.dao;

import com.zycm.zybao.model.entity.SysUserGroupModel;

import java.util.List;
import java.util.Map;


public interface SysUserGroupDao {

	/**
	* @Title: selectPage
	* @Description: 分页查询
	* @param param
	* @return    参数
	* @author sy
	* @throws
	* @return List<Map<String,Object>>    返回类型
	*
	*/
	List<Map<String,Object>> selectPage(Map<String,Object> param);
	Integer selectPageCount(Map<String,Object> param);

	/**
	* @Title: selectList
	* @Description: 不分页
	* @param param
	* @return    参数
	* @author sy
	* @throws
	* @return List<Map<String,Object>>    返回类型
	*
	*/
	List<Map<String,Object>> selectList(Map<String,Object> param);

	/**
	* @Title: deleteByPrimaryKey
	* @Description: 删除
	* @param uGroupId
	* @return    参数
	* @author sy
	* @throws
	* @return int    返回类型
	*
	*/
	int deleteByPrimaryKey(Integer uGroupId);

    /**
    * @Title: insert
    * @Description: 新增
    * @return    参数
    * @author sy
    * @throws
    * @return int    返回类型
    *
    */
    int insert(SysUserGroupModel sysUserGroupModel);

    /**
    * @Title: updateGroupName
    * @Description: 修改用户组名
    * @param sysUserGroupModel
    * @return    参数
    * @author sy
    * @throws
    * @return int    返回类型
    *
    */
    int updateGroupName(SysUserGroupModel sysUserGroupModel);

    /**
    * @Title: validName
    * @Description: 验证用户组名称重复
    * @param param
    * @return    参数
    * @author sy
    * @throws
    * @return List<Map<String,Object>>    返回类型
    *
    */
    List<Map<String,Object>> validName(Map<String,Object> param);
}
