package com.zycm.zybao.dao;

import com.zycm.zybao.model.entity.ProgramMaterialRelationModel;

import java.util.List;
import java.util.Map;

public interface ProgramMaterialRelationDao {

	void insertOfBatch(List<ProgramMaterialRelationModel> pmrList);

	void deleteByProgramId(Integer programId);

	/**
	* @Title: selectByProgramId
	* @Description: 根据单个节目id查询节目的素材      能返回素材的播放设置信息
	* @param programId
	* @return    参数
	* @author sy
	* @throws
	* @return List<Map<String,Object>>    返回类型
	*
	*/
	List<Map<String,Object>> selectByProgramId(Integer programId);

	/**
	* @Title: selectByProgramIds
	* @Description: 根据多个节目id查询包含的素材     只能返回素材信息   播放设置不能返回  因为一个素材可以在不同节目中有不同的播放设置
	* @param programIds
	* @return    参数
	* @author sy
	* @throws
	* @return List<Map<String,Object>>    返回类型
	*
	*/
	List<Map<String,Object>> selectByProgramIds(Integer[] programIds);

	/**
	* @Title: selectByMaterialId
	* @Description:根据素材id查询数据
	* @author sy
	* @param @param materialId
	* @param @return
	* @return List<Map<String,Object>>
	* @throws
	*/
	List<Map<String,Object>> selectByMaterialId(Integer materialId);

	/**
	* @Title: deleteByMaterialId
	* @Description: 根据素材id删除数据
	* @author sy
	* @param @param materialId
	* @return void
	* @throws
	*/
	void deleteByMaterialId(Integer materialId);

	/**
	* @Title: selectProgramTotalTime
	* @Description: 计算出节目的总时长
	* @author sy
	* @param @param programId
	* @param @return
	* @return List<Map<String,Object>>
	* @throws
	*/
	List<Map<String,Object>> selectProgramTotalTime(Integer programId);

	/**
	* @Title: checkMaterialInProg
	* @Description: 判断素材是否已存在节目中
	* @param materialIds
	* @return    参数
	* @author sy
	* @throws
	* @return List<Map<String,Object>>    返回类型
	*
	*/
	List<Map<String,Object>> checkMaterialInProg(Integer[] materialIds);

}
