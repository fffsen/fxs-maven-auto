package com.zycm.zybao.service.interfaces;

import java.util.List;
import java.util.Map;

public interface AttributeDictionaryService {

	/**
	 * 查询部门  数据字典
	* @Title: selectDepartment
	* @Description:
	* @author sy
	* @param @param param
	* @param @return
	* @return List<Map<String,Object>>
	*
	*/
	List<Map<String,Object>> selectDepartment(Map<String,Object> param);

	/**
	* @Title: loadSyncPlayGroup
	* @Description: TODO(载入同步播放组信息)
	* @param masterMachineCode
	* @param slaveMachineCode    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	/*void loadSyncPlayGroup(String masterMachineCode,String slaveMachineCode,String type);*/
}
