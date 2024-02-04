package com.zycm.zybao.dao;

import com.zycm.zybao.model.entity.ProgramListDetailModel;

import java.util.List;
import java.util.Map;


public interface ProgramListDetailDao {

	/**
	* @Title: selectPage
	* @Description: 分页查询节目单详细
	* @author sy
	* @param @param param
	* @param @return
	* @return List<Map<String,Object>>
	* @throws
	*/
	List<Map<String,Object>> selectPage(Map<String,Object> param);

	Integer selectPageCount(Map<String,Object> param);

	/**
	* @Title: batchInsert
	* @Description: 批量添加节目单详细
	* @author sy
	* @param @param list
	* @return void
	* @throws
	*/
	void batchInsert(List<ProgramListDetailModel> list);


	/**
	* @Title: selectByListId
	* @Description: 根据节目单id查询节目单详细
	* @author sy
	* @param @param listId
	* @param @return
	* @return List<Map<String,Object>>
	* @throws
	*/
	List<Map<String,Object>> selectByListId(Integer listId);
	/**
	* @Title: selectByListId
	* @Description: 根据节目单id查询节目单详细
	* @author sy
	* @param @param listId
	* @param @return
	* @return List<Map<String,Object>>
	* @throws
	*/
	List<ProgramListDetailModel> selectByListId2(Integer listId);

	/**
	* @Title: deleteByProgramId
	* @Description: 根据节目id删除
	* @param programId    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	void deleteByProgramId(Integer programId);

	/**
	* @Title: selectByCondition
	* @Description: 根据条件查询节目单信息
	* @return    参数
	* @author sy
	* @throws
	* @return List<Map<String,Object>>    返回类型
	*
	*/
	List<Map<String,Object>> selectByCondition(Map<String,Object> param);
}
