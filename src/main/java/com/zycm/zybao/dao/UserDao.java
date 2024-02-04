package com.zycm.zybao.dao;

import com.zycm.zybao.model.entity.UserModel;

import java.util.List;
import java.util.Map;

public interface UserDao {

	List<Map<String,Object>> selectPage(Map<String,Object> param);

	Integer selectPageCount(Map<String,Object> param);

	void insert(UserModel userModel);

	Map<String,Object> selectByPrimaryKey(Integer uid);

	List<Map<String,Object>> selectByUserName(Map<String,Object> param);

	Map<String,Object> selectByOpenId(String openId);

	List<Map<String,Object>> selectByCondition(Map<String,Object> param);

	void updateByPrimaryKey(UserModel userModel);

	void updatePwd(Map<String,Object> param);

	void updateStatus(Map<String,Object> param);

	void updateIsDelete(Map<String,Object> param);

	Map<String,Object> selectUserAndPwd(Map<String,Object> param);

	/**
	* @Title: selectUsersByUgroupId
	* @Description: 根据用户id 查询与用户同用户组的多个用户id
	* @param uid
	* @return    参数
	* @author sy
	* @throws
	* @return String    返回类型  多个用户id 用逗号隔开的
	*
	*/
	String selectUsersByUgroupId(Integer uid);

	/**
	* @Title: selectAdmin
	* @Description: 查询管理者
	* @param uGroupId
	* @return    参数
	* @author sy
	* @throws
	* @return String    返回类型
	*
	*/
	String selectAdmin(Integer uGroupId);

	Map<String,Object> selectUidAndPwd(Map<String,Object> param);

	/**
	* @Title: selectSyncUser
	* @Description: 查询用户组下的同步用户
	* @param param
	* @return    参数
	* @author sy
	* @throws
	* @return List<Map<String,Object>>    返回类型
	*
	*/
	List<Map<String,Object>> selectSyncUser(Map<String,Object> param);

	/**
	* @Title: cancelSync
	* @Description: 取消用户的用户组终端组同步设置
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	void updateSync(Map<String,Object> param);

	/**
	* @Title: updateWarnNotice
	* @Description: 修改用户预警通知状态
	* @param param    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	void updateWarnNotice(Map<String,Object> param);

}
