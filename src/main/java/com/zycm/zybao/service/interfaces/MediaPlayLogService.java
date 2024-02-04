package com.zycm.zybao.service.interfaces;

import com.zycm.zybao.model.entity.MediaPlayLogModel;

import java.util.List;
import java.util.Map;


public interface MediaPlayLogService {

	/** 分页查询终端机播放日志
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

	/** 批量新增  需要自己设置  播放日志id 规则是 节目id-机器码-添加时间年月日 如：'2-sdaffsfdsfsd=-20170930'
	* @Title: batchInsert
	* @Description: TODO
	* @author sy
	* @param @param list
	* @return void
	*
	*/
	void batchInsert(List<MediaPlayLogModel> list);

	/**
	* @Title: pageStatisticsPlayTime
	* @Description: 统计素材的播放次数 分页
	* @param param
	* @param page
	* @param pageSize
	* @return    参数
	* @author sy
	* @throws
	* @return Map<String,Object>    返回类型
	*
	*/
	Map<String,Object> pageStatisticsPlayTime(Map<String,Object> param,Integer page,Integer pageSize);
	/**
	* @Title: selectPagePlayTimeDetail
	* @Description: 查询素材次数的详细 分页
	* @param param
	* @param page
	* @param pageSize
	* @return    参数
	* @author sy
	* @throws
	* @return Map<String,Object>    返回类型
	*
	*/
	Map<String,Object> selectPagePlayTimeDetail(Map<String,Object> param,Integer page,Integer pageSize);

}
