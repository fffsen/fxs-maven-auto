package com.zycm.zybao.dao;

import com.zycm.zybao.model.entity.MediaRunLogModel;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;


public interface MediaRunLogDao {

	/**
	* @Title: selectPage
	* @Description: 分页查询运行日志
	* @author sy
	* @param @param param
	* @param @return
	* @return List<Map<String,Object>>
	* @throws
	*/
	List<Map<String,Object>> selectPage(Map<String,Object> param);

	/**
	* @Title: selectPageCount
	* @Description: 绑定分页查询 查询总数
	* @author sy
	* @param @param param
	* @param @return
	* @return Integer
	* @throws
	*/
	Integer  selectPageCount(Map<String,Object> param);

	/**
	* @Title: batchInsert
	* @Description: 批量增加运行日志
	* @author sy
	* @param @param list
	* @return void
	* @throws
	*/
	void batchInsert(List<MediaRunLogModel> list);

	/**
	* @Title: pageStatisticsRunLog
	* @Description: 根据时间段统计日志
	* @param param
	* @return    参数
	* @author sy
	* @throws
	* @return List<Map<String,Object>>    返回类型
	*
	*/
	List<Map<String,Object>> pageStatisticsRunLog(Map<String,Object> param);
	Integer  pageStatisticsRunLogCount(Map<String,Object> param);

	/**
	* @Title: selectMediaMinAndMaxDate
	* @Description: 定时任务  每天算出每个终端前一天的日志的最早时间与最晚时间
	* @param param
	* @return    参数
	* @author sy
	* @throws
	* @return List<Map<String,Object>>    返回类型
	*
	*/
	List<Map<String,Object>> selectMediaMinAndMaxDate(Map<String,Object> param);

	/**
	* @Title: updateInfoByPrimaryKey
	* @Description: 根据id修改日志内容、日志时间、创建时间
	* @param mediaRunLogModel    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	void updateInfoByPrimaryKey(MediaRunLogModel mediaRunLogModel);

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
	* @Title: insert
	* @Description: TODO(单个新增)
	* @return    参数
	* @author sy
	* @throws
	* @return MediaRunLogModel    返回类型
	*
	*/
	MediaRunLogModel insert(MediaRunLogModel mediaRunLogModel);
}
