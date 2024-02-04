package com.zycm.zybao.dao;

import com.zycm.zybao.model.entity.SysuserMediagroupModel;

import java.util.List;
import java.util.Map;

public interface SysuserMediagroupDao {

    /**批量增加用户管理的分组
     * @param list
     */
    void batchInsert(List<SysuserMediagroupModel> list);

    /**根据用户id删除用户管理的终端组信息
     */
    void deleteByUid(Integer[] uids);

    /**
    * @Title: deleteByMediaGroupId
    * @Description: 根据多个终端组id 删除数据
    * @param mediaGroupId    参数
    * @author sy
    * @throws
    * @return void    返回类型
    *
    */
    void deleteByMediaGroupIds(Integer[] mediaGroupId);

    /**根据用户id查询分组信息
     * @param uid
     * @return
     */
    List<Map<String,Object>> selectByUid(Integer uid);

    /**根据用户id查询  用户是否管理所有分组
     * @param uid
     * @return
     */
    Map<String,Object> selectIsallByUid(Integer uid);

    /**
    * @Title: deleteMediaGroupByUserGroup
    * @Description: 根据用户组id、多个终端组id按条件删除
    * @param param    参数
    * @author sy
    * @throws
    * @return void    返回类型
    *
    */
    void deleteMediaGroupByUserGroup(Map<String,Object> param);

    /**
    * @Title: selectByMediaGroupId
    * @Description: 根据终端组id  查询用户配置的终端组信息
    * @param mediaGroupId
    * @return    参数
    * @author sy
    * @throws
    * @return List<Map<String,Object>>    返回类型
    *
    */
    List<Map<String,Object>> selectByMediaGroupId(Integer mediaGroupId);
}
