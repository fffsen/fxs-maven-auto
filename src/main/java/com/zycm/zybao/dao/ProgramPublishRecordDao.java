package com.zycm.zybao.dao;

import com.zycm.zybao.model.entity.ProgramPublishRecordModel;

import java.util.List;
import java.util.Map;

public interface ProgramPublishRecordDao {
	/**
	* @Title: selectProgramByGroupId
	* @Description: 分页查询终端组的节目信息
	* @author sy
	* @param @param mediaGroupId
	* @param @return
	* @return List<Map<String,Object>>
	* @throws
	*/
	List<Map<String,Object>> selectProgramByGroupId(Map<String,Object> param);

	/**
	* @Title: selectProgramByGroupIdCount
	* @Description: TODO 绑定分页查询  查总数
	* @author sy
	* @param @param param
	* @param @return
	* @return String
	* @throws
	*/
	String selectProgramByGroupIdCount(Map<String,Object> param);

	/**
	* @Title: selectGroupProg
	* @Description: 查询终端组的节目 不包括继承的节目
	* @author sy
	* @param @param mediaGroupId
	* @param @return
	* @return List<ProgramPublishRecordModel>
	* @throws
	*/
	List<ProgramPublishRecordModel> selectGroupProg(Integer mediaGroupId);

	/**
	* @Title: batchInsert
	* @Description: 批量发布节目
	* @author sy
	* @param @param list
	* @return void
	* @throws
	*/
	void batchInsert(List<ProgramPublishRecordModel> list);

	/**
	* @Title: selectGroupByProg
	* @Description: 根据节目id 查询发布到哪些终端组  分页
	* @author sy
	* @param @param programId
	* @param @return
	* @return List<Map<String,Object>>
	* @throws
	*/
	List<Map<String,Object>> selectGroupByProg(Map<String,Object> param);
	/**
	* @Title: selectGroupByProg2
	* @Description: 根据节目id 查询发布到哪些终端组  不分页
	* @author sy
	* @param @param param
	* @param @return
	* @return List<Map<String,Object>>
	* @throws
	*/
	List<Map<String,Object>> selectGroupByProg2(Map<String,Object> param);
	Integer selectGroupByProgCount(Map<String,Object> param);

	/**
	* @Title: progDownForProg
	* @Description: 一个节目在多个终端组上下刊
	* @author sy
	* @param @param param
	* @return void
	* @throws
	*/
	void progDownForProg(Map<String,Object> param);

	/**
	* @Title: progDownForGroup
	* @Description:  在一个终端组上下刊多个节目
	* @author sy
	* @param @param param
	* @return void
	* @throws
	*/
	void progDownForGroup(Map<String,Object> param);

	/**
	* @Title: deleteByGroupId
	* @Description: 根据分组id删除所有节目
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	void deleteByGroupIds(Integer[] mediaGroupIds);

	/**
	* @Title: selectPublishByMaterialId
	* @Description: 根据素材id  查询素材有没有被发布
	* @author sy
	* @param @param materialIds
	* @param @return
	* @return List<Map<String,Object>>
	* @throws
	*/
	List<Map<String,Object>> selectPublishByMaterialId(Integer[] materialIds);

	/**
	* @Title: updateProgramOrderByKey
	* @Description: 修改节目排序
	* @param programPublishRecordModel    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	void updateProgramOrderByKey(ProgramPublishRecordModel programPublishRecordModel);

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
	* @Title: selectGroupMaxNum
	* @Description: 统计指定终端组的最大序号
	* @param mediaGroupIds
	* @return    参数
	* @author sy
	* @throws
	* @return List<Map<String,Object>>    返回类型
	*
	*/
	List<Map<String,Object>> selectGroupMaxNum(List<Integer> mediaGroupIds);

	/**
	* @Title: checkFirstProgByMediaGroupId
	* @Description: 验证多个终端组是否存在优先的节目
	* @param mediaGroupIds
	* @return    参数
	* @author sy
	* @throws
	* @return List<Map<String,Object>>    返回类型
	*
	*/
	List<Map<String,Object>> checkFirstProgByMediaGroupId(Integer[] mediaGroupIds);

	/**
	* @Title: updateGroupProgByGroupId
	* @Description: 切换终端组的节目
	* @param param    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	void updateGroupProgByGroupId(Map<String,Object> param);

}
