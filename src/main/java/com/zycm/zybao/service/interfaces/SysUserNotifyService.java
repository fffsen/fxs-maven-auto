package com.zycm.zybao.service.interfaces;

import com.zycm.zybao.model.entity.SysUserNotifyModel;

import java.util.List;
import java.util.Map;

public interface SysUserNotifyService {
	/**
    * @Title: selectPage
    * @Description: 分页查询
    * @return    参数
    * @author sy
    * @throws
    * @return List<Map<String,Object>>    返回类型
    *
    */
    Map<String, Object> selectPage(Map<String, Object> map,Integer page,Integer pageSize);

	/**
	* @Title: updateReadStatusByKeys
	* @Description: 修改读取状态
	* @param receiverId
	* @param notifyIds
	* @throws Exception    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	void updateReadStatusByKeys(Integer receiverId,Integer[] notifyIds) throws Exception;

	/**
	* @Title: deleteByKeys
	* @Description: 批量删除
	* @param receiverId
	* @param notifyIds
	* @throws Exception    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	void deleteByKeys(Integer receiverId,Integer[] notifyIds) throws Exception;

	/**
	* @Title: selectNotRead
	* @Description: 查询用户未读取的信息总数
	* @param receiverId
	* @return    参数
	* @author sy
	* @throws
	* @return Integer    返回类型
	*
	*/
	Map<String, Object> selectCount(Integer receiverId);

	/**
    * @Title: batchInsert
    * @Description: 批量添加
    * @param list    参数
    * @author sy
    * @throws
    * @return void    返回类型
    *
    */
    void batchInsert(List<SysUserNotifyModel> list);
}
