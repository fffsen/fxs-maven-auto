package com.zycm.zybao.dao;

import com.zycm.zybao.model.entity.UserRolesModel;

import java.util.List;
import java.util.Map;

public interface UserRolesDao {

	/**新增用户角色关系数据
	 * @param record
	 * @return
	 */
	int insert(UserRolesModel record);

	/**删除用户角色信息
	 * @param uid
	 * @return
	 */
	int deleteByUid(Integer[] uid);

	/**
	* @Title: selectUserByRole
	* @Description: 根据角色查询有效的用户
	* @param roleId
	* @return    参数
	* @author sy
	* @throws
	* @return List<Map<String,Object>>    返回类型
	*
	*/
	List<Map<String,Object>> selectUserByRole(Integer roleId);
}
