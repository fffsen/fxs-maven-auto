package com.zycm.zybao.dao;

import com.zycm.zybao.model.entity.SysuserGroupMediagroupModel;

import java.util.List;
import java.util.Map;

public interface SysuserGroupMediagroupDao {

    /**批量增加用户管理的分组
     * @param list
     */
    public void batchInsert(List<SysuserGroupMediagroupModel> list);

    /**根据用户id删除用户管理的终端组信息
     * @param
     */
    void deleteByUgroupId(Integer[] uGroupIds);

    /**
    * @Title: deleteByMediaGroupId
    * @Description:根据多个终端组id删除信息
    * @param
    * @author sy
    * @throws
    * @return void    返回类型
    *
    */
    void deleteByMediaGroupIds(Integer[] mediaGroupIds);

    /**根据用户id查询分组信息
     * @param
     * @return
     */
    List<Map<String,Object>> selectByUgroupId(Integer uGroupId);

    /**
    * @Title: selectByMediaGroupId
    * @Description: 根据终端组id  查询所属的用户组信息
    * @param
    * @return    参数
    * @author sy
    * @throws
    * @return List<Map<String,Object>>    返回类型
    *
    */
    List<Map<String,Object>> selectByMediaGroupId(Integer mediaGroupId);

    /**根据用户id查询  用户是否管理所有分组
     * @param
     * @return
     */
    Map<String,Object> selectIsallByUgroupId(Integer uGroupId);

    /**
    * @Title: selectMediaGroupByUserGroup
    * @Description: 根据用户id 查询用户能操作的终端组
    * @param param
    * @return    参数
    * @author sy
    * @throws
    * @return List<Map<String,Object>>    返回类型
    *
    */
    List<Map<String,Object>> selectMediaGroupByUserGroup(Map<String,Object> param);

    /**
    * @Title: selectMediaGroupByUGroupId
    * @Description:根据用户组id查询能操作的终端组
    * @param param
    * @return    参数
    * @author sy
    * @throws
    * @return List<Map<String,Object>>    返回类型
    *
    */
    List<Map<String,Object>> selectMediaGroupByUGroupId(Map<String,Object> param);

    /**
    * @Title: selectUserGroupOfFixed
    * @Description: 根据用户组id  查询固定组和不固定组信息
    * @param param
    * @return    参数
    * @author sy
    * @throws
    * @return List<Map<String,Object>>    返回类型
    *
    */
    List<Map<String,Object>> selectUserGroupOfFixed(Map<String,Object> param);

    /**
    * @Title: vaildNoGroup
    * @Description: 验证用户组配置的终端组是否只有一个固定的未分组
    * @param
    * @return    参数
    * @author sy
    * @throws
    * @return List<Map<String,Object>>    返回类型
    *
    */
    List<Map<String,Object>> vaildNoGroup(Map<String,Object> param);

    /**
    * @Title: selectNoGroupByUgroupId
    * @Description: 根据用户组id查询未分组的组信息
    * @param uGroupId
    * @return    参数
    * @author sy
    * @throws
    * @return List<Map<String,Object>>    返回类型
    *
    */
    List<Map<String,Object>> selectNoGroupByUgroupId(Integer uGroupId);
}
