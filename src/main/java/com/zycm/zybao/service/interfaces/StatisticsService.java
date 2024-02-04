package com.zycm.zybao.service.interfaces;

import java.util.Map;

public interface StatisticsService {
	/**
	* @Title: selectMediaStatistics
	* @Description: 统计设备的总数、在线、离线、异常、拆机、无节目终端数
	* @param param
	* @return    参数
	* @author sy
	* @throws
	* @return Map<String,Object>    返回类型
	*
	*/
	Map<String,Object> selectMediaStatistics(Map<String,Object> param);

	/**
	* @Title: pageStatisticsRunLog
	* @Description: 统计异常设备
	* @param param
	* @param page
	* @param pageSize
	* @return    参数
	* @author sy
	* @throws
	* @return Map<String,Object>    返回类型
	*
	*/
	Map<String, Object> pageStatisticsRunLog(Map<String,Object> param,Integer page,Integer pageSize);
}
