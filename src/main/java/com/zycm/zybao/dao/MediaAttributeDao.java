package com.zycm.zybao.dao;

import com.zycm.zybao.model.entity.MediaAttributeModel;
import com.zycm.zybao.model.mqmodel.respose.clientconf.ClientConfRespose;

import java.util.List;
import java.util.Map;

public interface MediaAttributeDao {

	/** 根据终端id 删除终端 物理删除
	* @Title: deleteByMids
	* @Description: TODO
	* @author sy
	* @param @param mids
	* @return void
	* @throws
	*/
	void deleteByMids(Integer[] mids);

	/** 根据终端id 删除终端     逻辑删除
	* @Title: updateIsDelete
	* @Description: TODO
	* @author sy
	* @param @param mids
	* @return void
	* @throws
	*/
	void updateIsDelete(Integer[] mids);

	/** 批量添加终端
	* @Title: batchInsert
	* @Description: TODO
	* @author sy
	* @param @param list
	* @return void
	* @throws
	*/
	void batchInsert(List<MediaAttributeModel> list);

	/** 验证终端编号的唯一性
	* @Title: selectByClientNumber
	* @Description: TODO
	* @author sy
	* @param @param clientNumber
	* @param @return
	* @return List<Map<String,Object>>
	* @throws
	*/
	List<Map<String,Object>> selectByClientNumber(String[] clientNumber);

	/**
	* @Title: selectPage
	* @Description: 分页查询终端信息
	* @author sy
	* @param @param map
	* @param @return
	* @return List<Map<String,Object>>
	* @throws
	*/
	List<Map<String,Object>> selectPage(Map<String,Object> map);
	/**
	* @Title: selectPageByPrimaryKeys
	* @Description: 根据多个终端id 分页查询终端信息
	* @author sy
	* @param @param map
	* @param @return
	* @return List<Map<String,Object>>
	* @throws
	*/
	List<Map<String,Object>> selectPageByPrimaryKeys(Map<String,Object> map);
	/**
	* @Title: selectPageCount
	* @Description: 查询总数据数
	* @author sy
	* @param @param map
	* @param @return
	* @return String
	* @throws
	*/
	String selectPageCount(Map<String,Object> map);
	/**
	* @Title: selectPageCount
	* @Description: 查询总数据数
	* @author sy
	* @param @param map
	* @param @return
	* @return String
	* @throws
	*/
	Integer selectPageByPrimaryKeysCount(Map<String,Object> map);

	/**
	* @Title: getOnlineStatistics
	* @Description: 获取在线统计信息
	* @author sy
	* @param @return
	* @return Map<String,Object>
	* @throws
	*/
	List<Map<String, Object>> getOnlineStatistics(Map<String,Object> map);

	/**
	* @Title: selectByPrimaryKey
	* @Description: 根据终端id查询终端详细信息
	* @author sy
	* @param @param mid
	* @param @return
	* @return Map<String,Object>
	* @throws
	*/
	List<Map<String, Object>> selectByPrimaryKeys(Integer[] mids);

	/**
	* @Title: updateMedia
	* @Description: 根据终端id修改终端名称、闲时时段、清理天数、ftp
	* @author sy
	* @param @param mediaAttributeModel
	* @return void
	* @throws
	*/
	void updateMedia(MediaAttributeModel mediaAttributeModel);

	/**
	* @Title: selectMediaByGroupId
	* @Description: 根据终端组id查询所有终端信息
	* @author sy
	* @param @param mediaGroupId
	* @param @return
	* @return List<Map<String,Object>>
	* @throws
	*/
	List<Map<String,Object>> selectMediaByGroupId(Map<String,Object> param);

	/**
	* @Title: selectByCondition
	* @Description: 根据条件查询  不分页
	* @author sy
	* @param @param condition
	* @param @return
	* @return List<Map<String,Object>>
	* @throws
	*/
	List<Map<String,Object>> selectByCondition(Map<String,Object> condition);

	/**
	* @Title: updateByCode
	* @Description: 心跳的数据修改
	* @author sy
	* @param @param mediaAttributeModel
	* @return void
	* @throws
	*/
	void updateByCode(MediaAttributeModel mediaAttributeModel);

	/**
	* @Title: updateAdStatus
	* @Description: 心跳的状态跟踪
	* @author sy
	* @param @param mediaAttributeModel
	* @return void
	*
	*/
	void updateAdStatus(MediaAttributeModel mediaAttributeModel);
	/**
	* @Title: updateAdStatusByConf
	* @Description: 按条件修改媒体机的在线状态
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	void updateAdStatusByConf(Map<String,Object> param);

	/**
	* @Title: downloadProgress
	* @Description: 更新终端下载素材的进度
	* @author sy
	* @param @param mediaAttributeModel
	* @return void
	*
	*/
	void updateDownloadProgress(MediaAttributeModel mediaAttributeModel);
	/**
	* @Title: selectAllConfByMachineCode
	* @Description: 根据机器码查询终端的配置信息
	* @author sy
	* @param @param machineCode
	* @param @return
	* @return Map<String,Object>
	*
	*/
	Map<String,Object> selectAllConfByMachineCode(String machineCode);

	/**
	* @Title: updateAllConfByMachineCode
	* @Description: 根据机器码修改终端的配置信息
	* @author sy
	* @param @param clientConfRespose
	* @return void
	* @throws
	*/
	void updateAllConfByMachineCode(ClientConfRespose clientConfRespose);

	/**
	* @Title: selectGroupProgNum
	* @Description: 根据多个终端组id 统计终端组每个的节目数
	* @author sy
	* @param @param mediaGroupId
	* @param @return
	* @return List<Map<String,Object>>
	* @throws
	*/
	List<Map<String,Object>> selectGroupProgNum(Integer[] mediaGroupId);

	/**
	* @Title: selectMaxNumber
	* @Description: 查询最大的终端编号
	* @author sy
	* @param @param prefix
	* @param @return
	* @return Map<String,Object>
	* @throws
	*/
	Map<String,Object> selectMaxNumber(String prefix);

	/**
	* @Title: selectMediaProgram
	* @Description: 根据id或名称 查节目档期数据
	* @param param
	* @return    参数
	* @author sy
	* @throws
	* @return List<Map<String,Object>>    返回类型
	*
	*/
	List<Map<String,Object>> selectMediaProgram(Map<String,Object> param);

	/**
	* @Title: selectByFtpId
	* @Description: 根据ftpId查询终端信息
	* @param ftpId
	* @return    参数
	* @author sy
	* @throws
	* @return List<Map<String,Object>>    返回类型
	*
	*/
	List<Map<String,Object>> selectByFtpId(Integer ftpId);

	/**
	* @Title: selectGroupIdByMachineCode
	* @Description: 根据机器码查询终端及终端组id
	* @param machineCode
	* @return    参数
	* @author sy
	* @throws
	* @return Map<String,Object>    返回类型
	*
	*/
	List<Map<String, Object>> selectGroupIdByMachineCodes(String[] machineCode);

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

	/**
	* @Title: batchUpdateLastTime
	* @Description: 批量更新最后通信时间
	* @param pList    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	void batchUpdateLastTime(List<Map<String, Object>> pList);

	/**
	* @Title: selectOfflineMedia
	* @Description: 查询用户管理的离线终端 统计离线时长 秒
	* @param map
	* @return    参数
	* @author sy
	* @throws
	* @return List<Map<String,Object>>    返回类型
	*
	*/
	List<Map<String,Object>> selectPageOfflineMedia(Map<String,Object> map);
	Integer selectPageOfflineMediaCount(Map<String,Object> map);

	/**
	* @Title: selectPageGroupProgNumZero
	* @Description: 分页查询 节目数为0的终端组
	* @param map
	* @return    参数
	* @author sy
	* @throws
	* @return List<Map<String,Object>>    返回类型
	*
	*/
	List<Map<String,Object>> selectPageGroupProgNumZero(Map<String,Object> map);
	Integer selectPageGroupProgNumZeroCount(Map<String,Object> map);

	/**
	* @Title: selectPageMediaProgNumZero
	* @Description: 分页查询 节目数为0的终端设备
	* @param map
	* @return    参数
	* @author sy
	* @throws
	* @return List<Map<String,Object>>    返回类型
	*
	*/
	List<Map<String,Object>> selectPageMediaProgNumZero(Map<String,Object> map);
	Integer selectPageMediaProgNumZeroCount(Map<String,Object> map);


	/**
	* @Title: updateMediaNetSpeed
	* @Description: 修改终端网速信息
	* @param param    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	void updateMediaNetSpeed(Map<String,Object> param);

	/**
	* @Title: updateWorkStatus
	* @Description: 批量修改工作状态
	* @param param    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	void updateWorkStatus(Map<String,Object> param);

	/**
	* @Title: updateFtp
	* @Description: 批量修改ftp
	* @param param    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	void updateFtp(Map<String,Object> param);

	/**
	* @Title: selectMediaForRedis
	* @Description: 不分页  用于同步redis的数据
	* @param param
	* @return    参数
	* @author sy
	* @throws
	* @return List<Map<String,Object>>    返回类型
	*
	*/
	List<Map<String,Object>> selectMediaForRedis(Map<String,Object> param);

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
	List<Map<String,Object>> selectPageMediaByUids(Map<String,Object> map);
	Integer selectPageMediaByUidsCount(Map<String,Object> map);

}
