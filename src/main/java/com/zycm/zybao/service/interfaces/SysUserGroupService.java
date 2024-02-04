package com.zycm.zybao.service.interfaces;

import java.util.List;
import java.util.Map;

public interface SysUserGroupService {

	/** 分页查询ftp信息
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
	* @Title: selectList
	* @Description: 不分页
	* @param param
	* @return    参数
	* @author sy
	* @throws
	* @return List<Map<String,Object>>    返回类型
	*
	*/
	List<Map<String,Object>> selectList(Map<String,Object> param);

	/**
	* @Title: deleteByPrimaryKey
	* @Description: 删除
	* @param uGroupId
	* @return    参数
	* @author sy
	* @throws
	* @return int    返回类型
	*
	*/
	void deleteByPrimaryKey(Integer uGroupId) throws Exception;

    /**
    * @Title: insert
    * @Description: 新增
    * @return    参数
    * @author sy
    * @throws
    * @return int    返回类型
    *
    */
    void insert(String userGroupName) throws Exception;

    /**
    * @Title: updateGroupName
    * @Description: 修改用户组名
    * @return    参数
    * @author sy
    * @throws
    * @return int    返回类型
    *
    */
    void updateGroupName(Integer uGroupId,String userGroupName) throws Exception;
    /**
     * @Title: validName
     * @Description: 验证用户组名称重复
     * @param param
     * @return    参数
     * @author sy
     * @throws
     * @return List<Map<String,Object>>    返回类型
     *
     */
     List<Map<String,Object>> validName(Map<String,Object> param);

     /**
    * @Title: addMediaGroupOfUserGroup
    * @Description:给用户组配置终端组
    * @param uGroupId
    * @param groudIds    参数
    * @author sy
    * @throws
    * @return void    返回类型
    *
    */
    void confMediaGroupToUserGroup(Integer uGroupId,Integer[] groudIds) throws Exception;

    /**
    * @Title: getGroupIdByUGroupId
    * @Description: 根据用户组id查询终端组数据
    * @param uGroupId
    * @return    参数
    * @author sy
    * @throws
    * @return List<Map<String,Object>>    返回类型
    *
    */
    List<Map<String,Object>> getGroupIdByUGroupId(Integer uGroupId);
}
