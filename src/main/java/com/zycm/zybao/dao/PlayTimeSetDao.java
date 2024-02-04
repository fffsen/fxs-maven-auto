package com.zycm.zybao.dao;

import com.zycm.zybao.model.entity.PlayTimeSetModel;

import java.util.List;
import java.util.Map;

public interface PlayTimeSetDao {

	/**
	* @Title: batchInsert
	* @Description: 批量添加
	* @param psList    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	void batchInsert(List<PlayTimeSetModel> psList);

	/**
	* @Title: progDownForProgAfterDetele
	* @Description: 根据一个节目id与多个组id 删除时段设置
	* @param param    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	void progDownForProgAfterDetele(Map<String,Object> param);

	/**
	* @Title: progDownForGroupAfterDetele
	* @Description: 根据一个组id与多个节目id 删除时段设置
	* @param param    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	void progDownForGroupAfterDetele(Map<String,Object> param);

	/**
	* @Title: selectConflictTime
	* @Description: 根据多个指定组id及时间段 查询定时连播类型中存在时间段有交叉的数据
	* 				一般同时段不允许2个定时连播的节目
	* @return    参数
	* @author sy
	* @throws
	* @return List<Map<String,Object>>    返回类型
	*
	*/
	List<Map<String,Object>> selectConflictTime(Map<String, Object> param);

}
