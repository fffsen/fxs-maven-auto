package com.zycm.zybao.dao;

import com.zycm.zybao.model.entity.ProgramListTimeSetModel;

import java.util.List;


public interface ProgramListTimeSetDao {

   	/**
	* @Title: batchInsert
	* @Description: 批量添加
	* @param list    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	void batchInsert(List<ProgramListTimeSetModel> list);


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

}
