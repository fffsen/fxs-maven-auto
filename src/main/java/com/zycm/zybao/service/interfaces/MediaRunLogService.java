package com.zycm.zybao.service.interfaces;

import com.zycm.zybao.model.entity.MediaRunLogModel;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface MediaRunLogService {

	/** 分页查询终端机运行日志
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

	/**
	* @Title: statisticsRunLog
	* @Description: 根据时间段统计日志
	* @param param
	* @return    参数
	* @author sy
	* @throws
	* @return List<Map<String,Object>>    返回类型
	*
	*/
	Map<String,Object> pageStatisticsRunLog(Map<String,Object> param,Integer page,Integer pageSize);

	/**
	* @Title: getOffCountByCondition
	* @Description: 根据机器码查询指定时段的离线频率
	* @param param
	* @return    参数
	* @author sy
	* @throws
	* @return List<Map<String,Object>>    返回类型
	*
	*/
	List<Map<String,Object>> getOffCountByCondition(Map<String,Object> param);

	/**
	 * 新增运行日志 合并日志处理
	 * @param machineCode
	 * @param logInfo
	 * @param logLevel
	 * @param date
	 * @return
	 */
	MediaRunLogModel insert(String machineCode, String logInfo, Integer logLevel, Date date);

	/**
	 * @Title: batchInsert
	 * @Description: 批量增加运行日志
	 * @author sy
	 * @param @param list
	 * @return void
	 * @throws
	 */
	void batchInsert(List<MediaRunLogModel> list);
}
