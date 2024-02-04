package com.zycm.zybao.dao;

import com.zycm.zybao.model.entity.MediaGroupRelationModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface MediaGroupRelationDao {

	void updateGroup(@Param("mediaGroupId") Integer mediaGroupId, @Param("mids") Integer[] mids);


	void batchInsert(List<MediaGroupRelationModel> list);

	/**
	* @Title: selectMidByGroupId
	* @Description: 根据多个终端组id查询终端
	* @return    参数
	* @author sy
	* @throws
	* @return List<Map<String,Object>>    返回类型
	*
	*/
	List<Map<String,Object>> selectMidByGroupId(Integer[] mediaGroupIds);

	/**
	* @Title: selectGroupIdByMids
	* @Description: 根据终端id查询组id
	* @param mids
	* @return    参数
	* @author sy
	* @throws
	* @return List<Map<String,Object>>    返回类型
	*
	*/
	List<Map<String,Object>> selectGroupIdByMids(Integer[] mids);

	/**
	* @Title: updateGroupMediaByGroupId
	* @Description: 切换分组的终端
	* @param param    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	void updateGroupMediaByGroupId(Map<String,Object> param);

	/**
	* @Title: deleteByMid
	* @Description: 清理终端的关联数据
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	void deleteByMid(Integer[] mids);
}
