package com.zycm.zybao.dao;

import com.zycm.zybao.model.entity.ProgramModel;
import com.zycm.zybao.model.mqmodel.request.publish.ProgramMsg;

import java.util.List;
import java.util.Map;


public interface ProgramDao {

	 public void insert(ProgramModel programModel);

	 public void updateProgram(ProgramModel programModel);

	 /**
	* @Title: updateProgramInfo
	* @Description: 修改节目基本信息与审核信息
	* @param programModel    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	void updateProgramInfo(ProgramModel programModel);
	 /**
	* @Title: selectPage
	* @Description: 分页查询节目
	* @param map
	* @return    参数
	* @author sy
	* @throws
	* @return List<Map<String,Object>>    返回类型
	*
	*/
	List<Map<String,Object>> selectPage(Map<String,Object> map);

	String selectPageCount(Map<String,Object> map);

	/**
	* @Title: selectPageNoOrders
	* @Description: TODO(这里用一句话描述这个方法的作用) 分页查询未配置订单的节目
	* @param map
	* @return    参数
	* @author sy
	* @throws
	* @return List<Map<String,Object>>    返回类型
	*
	*/
	List<Map<String,Object>> selectPageNoOrders(Map<String,Object> map);

	Integer selectPageNoOrdersCount(Map<String,Object> map);

	 /**
	* @Title: deleteByPrimaryKey
	* @Description: 删除节目
	* @param programId    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	void deleteByPrimaryKey(Integer programId);

	 /**
	* @Title: selectByPrimaryKeys
	* @Description: 查询多个节目的详细信息及背景信息
	* @param programIds
	* @return    参数
	* @author sy
	* @throws
	* @return List<Map<String,Object>>    返回类型
	*
	*/
	List<Map<String,Object>> selectByPrimaryKeys(Integer[] programIds);

	 /**
	* @Title: updateName
	* @Description: 重命名节目
	* @param programModel    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	void updateName(ProgramModel programModel);

	 /**
	* @Title: updateAudit
	* @Description: 节目审核
	* @param programModel    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	void updateAudit(ProgramModel programModel);

	 /**
	* @Title: selectPageProgramAndLayout
	* @Description: 分页查询节目信息及布局信息
	* @param map
	* @return    参数
	* @author sy
	* @throws
	* @return List<ProgramModel>    返回类型
	*
	*/
	List<ProgramModel> selectPageProgramAndLayout(Map<String,Object> map);

	 String selectPageProgramAndLayoutCount(Map<String,Object> map);

	 /**
	* @Title: selectPorgAndLayoutAndMaterial
	* @Description: 根据多个节目id  查询多个节目的节目信息、布局信息 、素材信息
	* @param programIds
	* @return    参数
	* @author sy
	* @throws
	* @return List<ProgramMsg>    返回类型
	*
	*/
	List<ProgramMsg> selectPorgAndLayoutAndMaterial(Integer[] programIds);

	 /**
	* @Title: selectProgByMachineCode
	* @Description: 根据机器码  查询终端机的分组的多个节目的节目单脚本   主要用于app的定时节目校对
	* @param machineCode
	* @return    参数
	* @author sy
	* @throws
	* @return List<ProgramMsg>    返回类型
	*
	*/
	List<ProgramMsg> selectProgByMachineCode(String machineCode);

	 /**
	* @Title: selectProgByGroupId
	* @Description: 根据终端组id  查询终端组的多个节目脚本
	* @param mediaGroupId
	* @return    参数
	* @author sy
	* @throws
	* @return List<ProgramMsg>    返回类型
	*
	*/
	List<ProgramMsg> selectProgByGroupId(Integer mediaGroupId);

	 /**
	* @Title: selectProgByProgramIds
	* @Description: 根据多个节目 查询多个节目的脚本格式
	* @param programIds
	* @return    参数
	* @author sy
	* @throws
	* @return List<ProgramMsg>    返回类型
	*
	*/
	List<ProgramMsg> selectProgByProgramIds(Integer[] programIds);
	 /**
	* @Title: checkProgName
	* @Description: 验证节目名有效性
	* @author sy
	* @param @param param
	* @param @return
	* @return List<Map<String,Object>>
	* @throws
	*/
	List<Map<String,Object>> checkProgName(Map<String,Object> param );

	 /**
	* @Title: updateTotalPlayTime
	* @Description: 修改节目的总播放时长
	* @param programModel    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	void updateTotalPlayTime(ProgramModel programModel);

	/**
	* @Title: checkNullArea
	* @Description: 查询节目是否存在空的播放区
	* @param programId    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	List<Map<String, Object>> checkNullArea(Integer programId);

	/**
	* @Title: selectPageProgTimeMode
	* @Description: 根据条件分页查询  节目的发布形式
	* @param map
	* @return    参数
	* @author sy
	* @throws
	* @return List<Map<String,Object>>    返回类型
	*
	*/
	List<Map<String,Object>> selectPageProgTimeMode(Map<String,Object> map);

	Integer selectPageProgTimeModeCount(Map<String,Object> map);

}
