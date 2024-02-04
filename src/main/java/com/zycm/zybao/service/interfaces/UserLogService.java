package com.zycm.zybao.service.interfaces;

import java.util.Map;

public interface UserLogService {

	/** 分页查询用户操作日志
	* @Title: selectPage
	* @Description:
	* @author sy
	* @param @param map
	* @param @return
	* @return Map<String,Object>
	*
	*/
	Map<String, Object> selectPage(Map<String, Object> map,Integer pageSize,Integer page);

	/**
	* 说明：添加用户操作日志
	* @Title: insert
	* @Description: TODO
	* @author sy
	* @param uid
	* @param logLevel
	* @param info
	* @return void
	*
	*/
	void insert(Integer uid,Integer logLevel,String info);

	/**
	* @Title: selectLoginFailByUid
	* @Description: 获取用户当日登录失败次数
	* @param uid
	* @return    参数
	* @author sy
	* @throws
	* @return List<Map<String,Object>>    返回类型
	*
	*/
	int selectLoginFailByUid(Integer uid);

}
