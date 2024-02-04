package com.zycm.zybao.service.interfaces;

import com.zycm.zybao.model.entity.MediaAttributeModel;
import com.zycm.zybao.model.vo.MediaAttributeVo;

import java.util.List;
import java.util.Map;

public interface MediaAttributeService {

	/** 根据终端id 删除终端
	* @Title: deleteByMids
	* @Description: TODO
	* @author sy
	* @param @param mids
	* @return void
	*
	*/
	void updateIsDelete(Integer[] mids);

	/** 批量添加终端
	* @Title: batchInsert
	* @Description: TODO
	* @author sy
	* @param @param list
	* @return void
	*
	*/
	void addMedia(Integer media_group_id,Integer num,Integer start) throws Exception;

	/** 修改分组
	* @Title: updateGroup
	* @Description: TODO
	* @author sy
	* @param @param mediaGroupId
	* @param @param mids
	* @return void
	*
	*/
	void updateGroup(Integer mediaGroupId, Integer[] mids);

	/** 分页查询终端机信息
	* @Title: selectPage
	* @Description:
	* @author sy
	* @param @param map
	* @param @return
	* @return Map<String,Object>
	*
	*/
	Map<String, Object> selectPage(Map<String, Object> map,Integer pageSize,Integer page);
	Map<String, Object> selectPageByPrimaryKeys(Map<String, Object> map,Integer pageSize,Integer page);
	/** 根据终端id查询终端详细信息
	* @Title: selectByPrimaryKey
	* @Description:
	* @author sy
	* @param @param mid
	* @param @return
	* @return Map<String,Object>
	*
	*/
	Map<String,Object> selectByPrimaryKey(Integer mid);

	/** 根据终端id修改终端名称、闲时时段、清理天数、ftp
	* @Title: updateMedia
	* @Description:
	* @author sy
	* @param @param mediaAttributeModel
	* @return void
	*
	*/
	void updateMedia(MediaAttributeVo mediaAttributeVo);

	/** 终端升级
	* @Title: mediaUpgrade
	* @Description:
	* @author sy
	* @param @param mids
	* @param @param materialId
	* @return void
	*
	*/
	void mediaUpgrade(String[] machineCodes,Integer materialId) throws Exception;

	/** 多个终端远程截屏
	* @Title: screenshot
	* @Description:
	* @author sy
	* @param @param mids
	* @param @return
	* @return List<Map<String,Object>>
	*
	*/
	List<Map<String,Object>> screenshot(Integer[] mids);

	/** 根据机器码获取媒体机的文件列表
	* @Title: getClientFile
	* @Description:
	* @author sy
	* @param @param machineCode
	* @param @return
	* @return List<Map<String,Object>>
	*
	*/
	Map<String,Object> getClientFile(String machineCode);

	/** 删除终端文件
	* @Title: deleteClientFile
	* @Description:
	* @author sy
	* @param @param machineCode
	* @param @param fileName
	* @return void
	*
	*/
	void deleteClientFile(String machineCode,String[] fileName);

	/**
	* @Title: deleteExpireClientFile
	* @Description: 批量清理过期文件
	* @param machineCodes    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	void deleteExpireClientFile(String[] machineCodes);

	/** 重启终端
	* @Title: clientRestart
	* @Description:
	* @author sy
	* @param @param machineCode
	* @return void
	*
	*/
	void clientRestart(String[] machineCode);


	/**
	* 说明：根据机器码修改终端机的在线状态
	* @Title: updateAdStatus
	* @Description: TODO
	* @author sy
	* @param @param mediaAttributeModel
	* @return void
	*
	*/
	void updateAdStatus(String machineCode,Integer adStatus);

	/** 批量添加终端
	* @Title: batchInsert
	* @Description: TODO
	* @author sy
	* @param @param list
	* @return void
	*
	*/
	void batchInsert(List<MediaAttributeModel> list);

	/**
	* @Title: selectMediaProgram
	* @Description: 根据id或名称 查节目档期数据
	* @return    参数
	* @author sy
	* @throws
	* @return List<Map<String,Object>>    返回类型
	*
	*/
	List<Map<String,Object>> selectMediaProgram(Integer[] mids,String clientName);

	/**
	* @Title: selectPageGroupProgNumZero
	* @Description: 分页查询节目数为0的终端组
	* @param map
	* @param pageSize
	* @param page
	* @return    参数
	* @author sy
	* @throws
	* @return Map<String,Object>    返回类型
	*
	*/
	Map<String, Object> selectPageGroupProgNumZero(Map<String, Object> map,Integer pageSize,Integer page);

	/**
	* @Title: selectPageMediaProgNumZero
	* @Description: 分页查询节目数为0的终端设备
	* @param param
	* @param pageSize
	* @param page
	* @return    参数
	* @author sy
	* @throws
	* @return Map<String,Object>    返回类型
	*
	*/
	Map<String, Object> selectPageMediaProgNumZero(Map<String, Object> param, Integer pageSize,Integer page);
	/**
	* @Title: selectPageOfflineMedia
	* @Description: 分页查询离线终端的离线时长
	* @param map
	* @param pageSize
	* @param page
	* @return    参数
	* @author sy
	* @throws
	* @return Map<String,Object>    返回类型
	*
	*/
	Map<String, Object> selectPageOfflineMedia(Map<String, Object> map,Integer pageSize,Integer page);

	/**
	* @Title: 激活设备
	* @Description: TODO(这里用一句话描述这个方法的作用)
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	void activeMedia(String[] machineCodes, Integer status) throws Exception;

	/**
	* @Title: changeFtp
	* @Description: 批量切换ftp
	* @param mids
	* @param ftpId    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	void changeFtp(Integer[] mids,Integer ftpId);

	/**
	* @Title: selectPageMediaByUids
	* @Description: 根据用户id与终端组id分页查询终端
	* @param map
	* @return    参数
	* @author sy
	* @throws
	* @return List<Map<String,Object>>    返回类型
	*
	*/
	Map<String, Object> selectPageMediaByUids(Map<String, Object> map,Integer pageSize,Integer page);

	/**
	* @Title: updateIccidAndIccidUpdateType
	* @Description: 手动修改iccid
	* @param machineCode
	* @param iccid
	* @param iccidUpdateType    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	void updateIccidAndIccidUpdateType(String machineCode,String iccid,Integer iccidUpdateType) throws Exception;

	/**
	* @Title: selectUidByMachineCodes
	* @Description: 根据机器码查询需要发送通知的用户
	* @return    参数
	* @author sy
	* @throws
	* @return List<Map<String,Object>>    返回类型
	*
	*/
	List<Map<String, Object>> selectNoticeUserByMachineCodes(Map<String,Object> param);

	List<Map<String, Object>> selectGroupIdByMachineCodes(String[] machineCodes);


	/**
	* @Title: syncClientTime
	* @Description: 同步指定终端组时间
	* @param machineCodes
	* @throws Exception    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	void syncClientTime(String[] machineCodes) throws Exception;

	/**
	* @Title: syncAllClientTime
	* @Description: 同步所有终端组时间
	* @throws Exception    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	void syncAllClientTime() throws Exception;

	/**
	* @Title: selectMediaByGroupId
	* @Description: 根据终端组id查询终端信息
	* @param mediaGroupIds
	* @return    参数
	* @author sy
	* @throws
	* @return List<Map<String,Object>>    返回类型
	*
	*/
	List<Map<String,Object>> selectMediaByGroupId(Integer[] mediaGroupIds);

	Map<String, Object> selectClientInfo(Map<String, Object> map,Integer pageSize,Integer page);

	Map<String, Object> selectIotInfo(Map<String, Object> param, Integer pageSize, Integer page);

	List<Map<String,Object>> selectByCondition(Map<String, Object> param);
}
