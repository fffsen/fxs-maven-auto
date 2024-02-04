package com.zycm.zybao.dao;

import com.zycm.zybao.model.entity.MediaPlayLogModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface MediaPlayLogDao {

	/**
	* @Title: selectPage
	* @Description: 分页查询播放日志
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

	/** 批量新增
	* @Title: batchInsert
	* @Description: TODO
	* @author sy
	* @param @param list
	* @return void
	* @throws
	*/
	void batchInsert(List<MediaPlayLogModel> list);

	/** 删除终端当天播放记录中指定的节目播放记录
	* @Title: deleteByMachineCodeForDay
	* @Description: TODO
	* @author sy
	* @param @param machineCode
	* @param @param programIds
	* @return void
	* @throws
	*/
	void deleteByMachineCodeForDay(@Param("machineCode") String machineCode, @Param("programIds") Integer[] programIds, @Param("currentDate") String currentDate);

	/**
	* @Title: statisticsPlayTime
	* @Description: 分页统计 素材的播放次数
	* @param param
	* @return    参数
	* @author sy
	* @throws
	* @return List<Map<String,Object>>    返回类型
	*
	*/
	List<Map<String,Object>> pageStatisticsPlayTime(Map<String,Object> param);
	Integer  pageStatisticsPlayTimeCount(Map<String,Object> param);
	/**
	* @Title: selectPlayTimeDetail
	* @Description: 查看播放次数详细  分页
	* @param param
	* @return    参数
	* @author sy
	* @throws
	* @return List<Map<String,Object>>    返回类型
	*
	*/
	List<Map<String,Object>> selectPagePlayTimeDetail(Map<String,Object> param);
	Integer  selectPagePlayTimeDetailCount(Map<String,Object> param);
}
