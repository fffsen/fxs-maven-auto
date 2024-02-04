package com.zycm.zybao.dao;

import com.zycm.zybao.model.entity.SysUserNotifyModel;

import java.util.List;
import java.util.Map;


public interface SysUserNotifyDao {

	/**
	* @Title: selectPage
	* @Description: 根据用户分页查询
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
    * @Title: updateReadStatusByKeys
    * @Description: 批量修改读取状态
    * @param param    参数
    * @author sy
    * @throws
    * @return void    返回类型
    *
    */
    void updateReadStatusByKeys(Map<String,Object> param);

    /**
    * @Title: deleteByKeys
    * @Description: 批量删除
    * @param param    参数
    * @author sy
    * @throws
    * @return void    返回类型
    *
    */
    void deleteByKeys(Map<String,Object> param);

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
