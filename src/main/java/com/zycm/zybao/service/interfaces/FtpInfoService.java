package com.zycm.zybao.service.interfaces;

import java.util.List;
import java.util.Map;


public interface FtpInfoService {

	/** 分页查询ftp信息
	* @Title: selectPage
	* @Description:
	* @author sy
	* @param @param param
	* @param @param page
	* @param @param pageSize
	* @param @return
	* @return Map<String,Object>
	*
	*/
	Map<String,Object> selectPage(Map<String,Object> param,Integer page,Integer pageSize);

	/** 新增ftp
	* @Title: insert
	* @Description:
	* @author sy
	* @param @param ftpInfoModel
	* @return void
	*
	*/
	void insert(Map<String,Object> param) throws Exception;

	/** 修改ftp信息
	* @Title: updateByPrimaryKey
	* @Description:
	* @author sy
	* @param @param ftpInfoModel
	* @return void
	*
	*/
	void updateByPrimaryKey(Map<String, Object> param) throws Exception;

	/** 删除ftp信息
	* @Title: deleteByPrimaryKey
	* @Description:
	* @author sy
	* @param @param ftpId
	* @return void
	*
	*/
	void deleteByPrimaryKey(Integer ftpId) throws Exception;

	/** 根据id查询ftp信息详细
	* @Title: selectByPrimaryKey
	* @Description:
	* @author sy
	* @param @param ftpId
	* @param @return
	* @return Map<String,Object>
	*
	*/
	Map<String,Object> selectByPrimaryKey(Integer ftpId);

	/** 修改默认ftp
	* @Title: updateDefaultById
	* @Description:
	* @author sy
	* @param @param ftpInfoModel
	* @return void
	*
	*/
	void updateDefaultById(Integer ftpId,Integer userId,Integer[] sameGroupUserId,boolean b) throws Exception;

	/** 条件查询
	* @Title: selectInfo
	* @Description:
	* @author sy
	* @param @param param
	* @param @return
	* @return List<Map<String,Object>>
	*
	*/
	List<Map<String,Object>> selectInfo(Integer uGroupId,Integer sId,Integer[] sameGroupUserId);
}
