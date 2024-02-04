package com.zycm.zybao.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AttributeDictionaryDao {

	/**
	* @Title: selectDepartment
	* @Description: 查询部门  数据字典
	* @author sy
	* @param @param param
	* @param @return
	* @return List<Map<String,Object>>
	* @throws
	*/
 	List<Map<String,Object>> selectDepartment(Map<String,Object> param);

}
