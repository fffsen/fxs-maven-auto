package com.zycm.zybao.service.interfaces;


import java.util.List;
import java.util.Map;

public interface ProgramPublishRecordService2 {


	/**
	 * 根据节目id 查询发布到哪些终端组 不分页
	* @Title: selectPageGroupByProg2
	* @Description:
	* @author sy
	* @param @param programId
	* @param @return
	* @return List<Map<String,Object>>
	*
	*/
	List<Map<String, Object>> selectPageGroupByProg2(Integer programId);

	/**
	 * @Title: publishProgByGroup
	 * @Description: 发布最新的节目单  按终端组
	 * @param mediaGroupIds
	 * @param pTaskId    参数  任务id
	 * @author sy
	 * @throws
	 * @return void    返回类型
	 *
	 */
	void publishProgByGroup(Integer[] mediaGroupIds,Integer pTaskId);

	/**
	 * 多个终端组下刊一个节目
	* @Title: progDownForProg
	* @Description:
	* @author sy
	* @param @param programId
	* @param @param mediaGroupIds
	* @return void
	*
	*/
	void progDownForProg(Integer programId,Integer[] mediaGroupIds) throws Exception;

}
