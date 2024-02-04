package com.zycm.zybao.dao;

import com.zycm.zybao.model.entity.ProgramListModel;

import java.util.List;
import java.util.Map;


public interface ProgramListDao {

	/**
	* @Title: selectPage
	* @Description: 分页查询节目单
	* @author sy
	* @param @param param
	* @param @return
	* @return List<Map<String,Object>>
	* @throws
	*/
	List<Map<String,Object>> selectPage(Map<String,Object> param);

	Integer selectPageCount(Map<String,Object> param);

	/**
	* @Title: insert
	* @Description:添加节目单
	* @author sy
	* @param @param programListModel
	* @return void
	* @throws
	*/
	void insert(ProgramListModel programListModel);

	/**
	* @Title: deleteByPrimaryKey
	* @Description: 删除节目单
	* @author sy
	* @param @param listId
	* @return void
	* @throws
	*/
	void deleteByPrimaryKey(Integer listId);


}
