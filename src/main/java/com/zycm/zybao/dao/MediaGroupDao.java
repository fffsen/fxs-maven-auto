package com.zycm.zybao.dao;

import com.zycm.zybao.model.entity.MediaGroupModel;

import java.util.List;
import java.util.Map;

public interface MediaGroupDao {

	/** 根据父节点id查子节点信息
	* @Title: selectByParentId
	* @Description: TODO
	* @author sy
	* @param @param parentId
	* @param @return
	* @return List<Map<String,Object>>
	* @throws
	*/
	List<Map<String,Object>> selectByParentId(Map<String,Object> param);

	/**
	* @Title: selectAllChild
	* @Description: 根据终端组节点id查询所有子节点id 包括终端组节点id自己
	* @param mediaGroupId
	* @return    参数
	* @author sy
	* @throws
	* @return String    返回类型
	*
	*/
	String selectAllChild(Integer mediaGroupId);

	/** 查询父节点不为0的所有子节点
	* @Title: selectChild
	* @Description: TODO
	* @author sy
	* @param @return
	* @return List<Map<String,Object>>
	* @throws
	*/
	List<Map<String,Object>> selectChild(Map<String,Object> param);

	/** 添加子节点
	* @Title: insert
	* @Description: TODO
	* @author sy
	* @param @param mediaGroupModel
	* @return void
	* @throws
	*/
	void insert(MediaGroupModel mediaGroupModel);

	/**
	* @Title: batchInsert
	* @Description: 批量添加
	* @param list    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	void batchInsert(List<MediaGroupModel> list);
	/** 删除子节点
	* @Title: deleteByPrimaryKey
	* @Description: TODO
	* @author sy
	* @param @param mediaGroupId
	* @return void
	* @throws
	*/
	void deleteByPrimaryKey(Integer mediaGroupId);

	/**
	* @Title: deleteByPrimaryKeys
	* @Description: 批量删除终端组
	* @param mediaGroupIds    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	void deleteByPrimaryKeys(Integer[] mediaGroupIds);
	/** 修改节点名称
	* @Title: updateGroupName
	* @Description: TODO
	* @author sy
	* @param @param mediaGroupModel
	* @return void
	* @throws
	*/
	void updateGroupName(MediaGroupModel mediaGroupModel);


	/**
	* @Title: selectByGroupName
	* @Description: 根据终端组名称查询  用于验证
	* @author sy
	* @param @param mediaGroupName
	* @param @return
	* @return List<Map<String,Object>>
	* @throws
	*/
	List<Map<String,Object>> selectByGroupName(Map<String,Object> param);

	Map<String,Object> selectByPrimaryKey(Integer mediaGroupId);

	/**
	* @Title: selectDefaultGroup
	* @Description: 判断分组是不是默认的未分组
	* @param mediaGroupId
	* @return    参数
	* @author sy
	* @throws
	* @return Map<String,Object>    返回类型
	*
	*/
	Map<String,Object> selectDefaultGroup(Integer mediaGroupId);

	/**
	* @Title: selectPage
	* @Description: 分页查询最末节点终端组
	* @author sy
	* @param @param param
	* @param @return
	* @return List<Map<String,Object>>
	* @throws
	*/
	List<Map<String,Object>> selectPageLastGroup(Map<String,Object> param);
	Integer selectPageLastGroupCount(Map<String,Object> param);
}
