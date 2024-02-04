package com.zycm.zybao.service.interfaces;

import java.util.List;
import java.util.Map;

public interface RolesFunctionService {
	/** 根据角色id查询权限
	* @Title: selectByRole
	* @Description:
	* @author sy
	* @param @param roleId
	* @param @return
	* @return List<Map<String,Object>>
	*
	*/
	List<Map<String,Object>> selectByRole(Integer roleId);


	/** 保存权限
	* @Title: batchInsert
	* @Description:
	* @author sy
	* @param @param list
	* @return void
	*
	*/
	void savePrivilege(Integer roleId,Integer[] functionIds) throws Exception;
}
