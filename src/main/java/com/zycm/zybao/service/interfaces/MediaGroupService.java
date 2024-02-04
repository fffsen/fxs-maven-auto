package com.zycm.zybao.service.interfaces;

import java.util.List;
import java.util.Map;

public interface MediaGroupService {

	/**
	 *
	* 终端组树形结构数据
	* @Title: selectGroupTree
	* @Description: TODO
	* @author sy
	* @param @return
	* @return List<Map<String,Object>>
	*
	*/
	Map<String, Object> selectGroupTree(Integer uid,boolean spread);

	/**
	 *
	* 终端组树形结构数据
	* @Title: selectGroupTree
	* @Description: TODO
	* @author sy
	* @param @return
	* @return List<Map<String,Object>>
	*
	*/
	Map<String, Object> selectGroupXTree(Integer uid);

	/**
	* @Title: selectGroupXTreeUser
	* @Description: 根据用户id  查询用户所在用户组的终端组信息
	* @param uid
	* @return    参数
	* @author sy
	* @throws
	* @return Map<String,Object>    返回类型
	*
	*/
	Map<String, Object> selectGroupXTreeUser(Integer uid);

	/**
	 * 添加子节点
	* @Title: insert
	* @Description: TODO
	* @author sy
	* @param @param mediaGroupModel
	* @return void
	*
	*/
	void insert(String mediaGroupName,Integer parentId,Integer uid,Integer uGroupId) throws Exception;

	/** 删除子节点
	* @Title: deleteByPrimaryKey
	* @Description: TODO
	* @author sy
	* @param @param mediaGroupId
	* @return void
	*
	*/
	void deleteByPrimaryKey(Integer mediaGroupId) throws Exception;

	/**
	* @Title: selectMediaByGroupIdAndChild
	* @Description: 根据终端组id查询终端组及其所有子节点分组的终端
	* @param mediaGroupId
	* @return    参数
	* @author sy
	* @throws
	* @return List<Map<String,Object>>    返回类型
	*
	*/
	List<Map<String,Object>> selectMediaByGroupIdAndChild(Integer mediaGroupId);

	/**
	* @Title: selectChildByGroupId
	* @Description: 根据终端组id查询子节点id
	* @param mediaGroupId
	* @return    参数
	* @author sy
	* @throws
	* @return List<Integer>    返回类型
	*
	*/
	List<Integer> selectChildByGroupId(Integer mediaGroupId);
	/** 修改节点名称
	* @Title: updateGroupName
	* @Description: TODO
	* @author sy
	* @param @param mediaGroupModel
	* @return void
	*
	*/
	void updateGroupName(String mediaGroupName,Integer mediaGroupId) throws Exception;

	/** 根据终端组名称查询  用于验证
	* @Title: selectByGroupName
	* @Description:
	* @author sy
	* @param @param mediaGroupName
	* @param @return
	* @return List<Map<String,Object>>
	*
	*/
	List<Map<String,Object>> selectByGroupName(Map<String,Object> param);

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

	/** 分页查询最末终端组信息
	* @Title: selectPage
	* @Description:
	* @author sy
	* @param @param param
	* @param @return
	* @return List<Map<String,Object>>
	*
	*/
	Map<String,Object> selectPageLastGroup(Integer page,Integer pageSize,Map<String, Object> param);
}
