package com.zycm.zybao.dao;

import com.zycm.zybao.model.entity.FtpInfoModel;

import java.util.List;
import java.util.Map;

public interface FtpInfoDao {

	/**
	* @Title: selectPage
	* @Description: 分页查询
	* @author sy
	* @param @param param
	* @param @return
	* @return List<Map<String,Object>>
	* @throws
	*/
	List<Map<String,Object>> selectPage(Map<String,Object> param);

	/**
	* @Title: selectPageCount
	* @Description: 分页查询  统计总数
	* @author sy
	* @param @param param
	* @param @return
	* @return Integer
	* @throws
	*/
	Integer selectPageCount(Map<String,Object> param);

	/**
	* @Title: insert
	* @Description:新增ftp
	* @author sy
	* @param @param ftpInfoModel
	* @return void
	* @throws
	*/
	void insert(Map<String, Object> param);

	/**
	* @Title: updateByPrimaryKey
	* @Description: 修改ftp信息
	* @author sy
	* @param @param ftpInfoModel
	* @return void
	* @throws
	*/
	void updateByPrimaryKey(Map<String, Object> param);

	/**
	* @Title: deleteByPrimaryKey
	* @Description: 删除ftp信息
	* @author sy
	* @param @param ftpId
	* @return void
	* @throws
	*/
	void deleteByPrimaryKey(Integer ftpId);

	/**
	* @Title: selectByPrimaryKey
	* @Description: 根据id查询ftp信息详细
	* @author sy
	* @param @param ftpId
	* @param @return
	* @return Map<String,Object>
	* @throws
	*/
	Map<String,Object> selectByPrimaryKey(Integer ftpId);

	/**
	* @Title: updateDefaultById
	* @Description: 修改默认ftp
	* @author sy
	* @param @param ftpInfoModel
	* @return void
	* @throws
	*/
	void updateDefaultById(Map<String,Object> param);

	/**
	* @Title: selectInfo
	* @Description: 条件查询
	* @author sy
	* @param @param param
	* @param @return
	* @return List<Map<String,Object>>
	* @throws
	*/
	List<Map<String,Object>> selectInfo(Map<String,Object> param);

	/**
	* @Title: selectDefaultFpt
	* @Description: 查询默认ftp
	* @return    参数
	* @author sy
	* @throws
	* @return FtpInfoModel    返回类型
	*
	*/
	FtpInfoModel selectDefaultFpt();

	/**
	* @Title: validRepeat
	* @Description: 验证重复数据
	* @param param
	* @return    参数
	* @author sy
	* @throws
	* @return List<Map<String,Object>>    返回类型
	*
	*/
	List<Map<String,Object>> validRepeat(Map<String,Object> param);
}
