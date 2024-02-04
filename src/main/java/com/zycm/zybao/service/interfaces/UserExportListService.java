package com.zycm.zybao.service.interfaces;

import java.util.List;
import java.util.Map;

public interface UserExportListService {
    /**
    * @Title: batchInsert
    * @Description: 批量添加
    * @author sy
    * @throws
    * @return void    返回类型
    *
    */
    void batchInsert(Integer[] mids,Integer uid);

    /**
    * @Title: selectPage
    * @Description: 分页查询
    * @return    参数
    * @author sy
    * @throws
    * @return List<Map<String,Object>>    返回类型
    *
    */
    Map<String, Object> selectPage(Map<String, Object> map,Integer page,Integer pageSize);

    /**
    * @Title: deleteByUidAndMid
    * @Description: 根据uid与mid删除
    * @param param    参数
    * @author sy
    * @throws
    * @return void    返回类型
    *
    */
    void deleteByUidAndMid(Map<String,Object> param);

    /**
     * @Title: selectByUidAndMids
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param param
     * @return    参数
     * @author sy
     * @throws
     * @return List<Map<String,Object>>    返回类型
     *
     */
     List<Map<String,Object>> selectByUidAndMids(Map<String,Object> param);

     /**
      * @Title: selectUserExportByUid
      * @Description:根据用户 查询用户的导出列表
      * @param uid
      * @return    参数
      * @author sy
      * @throws
      * @return List<Integer>    返回类型
      *
      */
      List<Integer> selectUserExportByUid(Integer uid);
}
