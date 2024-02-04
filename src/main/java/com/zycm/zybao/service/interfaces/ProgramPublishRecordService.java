package com.zycm.zybao.service.interfaces;


import com.zycm.zybao.model.vo.ProgramPublishVo;

import java.util.List;
import java.util.Map;

public interface ProgramPublishRecordService {


	/** 选择终端组  查询终端组的节目信息
	* @Title: selectPage
	* @Description: 分页查询
	* @author sy
	* @param @param map
	* @param @return
	* @return Map<String,Object>
	*
	*/
	Map<String, Object> selectPage(Integer mediaGroupId,Integer pageSize,Integer page);

	Map<String, Object> selectPage2(Integer mid,Integer pageSize,Integer page) throws Exception;

	/**
	 * 根据节目id 查询发布到哪些终端组
	* @Title: selectGroupByProg
	* @Description:
	* @author sy
	* @param @param programId
	* @param @return
	* @return List<Map<String,Object>>
	*
	*/
	Map<String, Object> selectPageGroupByProg(Map<String,Object> param,Integer pageSize,Integer page);
	/**
	 * 根据节目id 查询发布到哪些终端组 不分页
	* @Title: selectPageGroupByProg2
	* @Description:
	* @author sy
	* @param @param programId
	* @param @return
	* @return List<Map<String,Object>>
	*
	*/
	List<Map<String, Object>> selectPageGroupByProg2(Integer programId);

	/**
	 * 节目发布
	* @Title: publishProg
	* @Description:
	* @author sy
	* @param @param programIds
	* @param @param mediaGroupIds
	* @return void
	*
	*/
	void publishProg(ProgramPublishVo programPublishVo) throws Exception;

	/**
	* @Title: publishProgByGroup
	* @Description: 发布最新的节目单  按终端组
	* @param mediaGroupIds    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	void publishProgByGroup(Integer[] mediaGroupIds);
	/**
	* @Title: publishProgByGroup
	* @Description: 发布最新的节目单  按终端组
	* @param mediaGroupIds
	* @param pTaskId    参数  任务id
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	void publishProgByGroup(Integer[] mediaGroupIds,Integer pTaskId);
	/**
	 * 多个终端组下刊一个节目
	* @Title: progDownForProg
	* @Description:
	* @author sy
	* @param @param programId
	* @param @param mediaGroupIds
	* @return void
	*
	*/
	void progDownForProg(Integer programId,Integer[] mediaGroupIds) throws Exception;

	/**
	 * 多个终端组下刊一个节目
	* @Title: progDownForGroup
	* @Description:
	* @author sy
	* @param @param programId
	* @param @param mediaGroupIds
	* @return void
	*
	*/
	void progDownForGroup(Integer mediaGroupId,Integer[] pubIds) throws Exception;

	/**
	 * 同步节目信息 节目变动后需同步的情况
	* @Title: syncProg
	* @Description:
	* @author sy
	* @param @param programIds
	* @return void
	*
	*/
	void syncProg(Integer[] programIds);

	/**
	 * 根据素材id  查询素材有没有被发布
	* @Title: selectPublishByMaterialId
	* @Description:
	* @author sy
	* @param @param materialIds
	* @param @return
	* @return List<Map<String,Object>>
	*
	*/
	List<Map<String,Object>> selectPublishByMaterialId(Integer[] materialIds);

	/**
	* @Title: updateProgramOrderByKey
	* @Description: 更新节目排序
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	void updateProgramOrderByKey(String publishIdAndOrder,String publishIdAndOrder2,Integer mediaGroupId,Integer direction) throws Exception;

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

	/**
	* @Title: selectMaxPlayTime
	* @Description: 根据多个终端组  查询其中播放时间最长的时间
	* @param mediaGroupIds
	* @return    参数
	* @author sy
	* @throws
	* @return Integer    返回类型
	*
	*/
	Map<String,Object> selectMaxPlayTime(Integer[] mediaGroupIds);

	/**
	* @Title: checkFirstProgByMediaGroupId
	* @Description: 验证多个终端组是否存在优先节目
	* @param mediaGroupIds
	* @return    参数
	* @author sy
	* @throws
	* @return List<Map<String,Object>>    返回类型
	*
	*/
	List<Map<String, Object>> checkFirstProgByMediaGroupId(Integer[] mediaGroupIds);
}
