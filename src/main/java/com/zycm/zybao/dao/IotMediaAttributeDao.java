package com.zycm.zybao.dao;

import com.zycm.zybao.model.entity.IotMediaAttributeModel;

import java.util.List;
import java.util.Map;

public interface IotMediaAttributeDao {
    void insert(IotMediaAttributeModel iotMediaAttributeModel);

    /**
	* @Title: selectPage
	* @Description: 分页查询
	* @author sy
	* @param @param param
	* @param @return
	* @return List<Map<String,Object>>
	* @throws
	*/
	List<Map<String,Object>> selectPage(Map<String,Object> param);
	/**
	* @Title: selectPageCount
	* @Description: 分页查询  统计总数
	* @author sy
	* @param @param param
	* @param @return
	* @return Integer
	* @throws
	*/
	Integer selectPageCount(Map<String,Object> param);

	/**
	* @Title: selectByIccid
	* @Description: 根据iccid查询
	* @param iccid
	* @return    参数
	* @author sy
	* @throws
	* @return Map<String,Object>    返回类型
	*
	*/
	Map<String,Object> selectByIccid(String iccid);

	/**
	* @Title: selectByMachineCode
	* @Description: 根据机器码查询物联卡信息
	* @param machineCode
	* @return    参数
	* @author sy
	* @throws
	* @return List<Map<String,Object>>    返回类型
	*
	*/
	Map<String,Object> selectByMachineCode(String machineCode);

	/**
	* @Title: updateData
	* @Description: 更新流量信息
	* @param iotMediaAttributeModel    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	void updateData(IotMediaAttributeModel iotMediaAttributeModel);

	/**
	* @Title: updateMachineCode
	* @Description: 更改终端绑定  根据iccid修改机器码
	* @param param    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	void updateMachineCode(Map<String,Object> param);

	/**
	* @Title: updateIccid
	* @Description: 根据机器码修改iccid
	* @param param    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	void updateIccid(Map<String,Object> param);

	/**
	* @Title: deleteByIccid
	* @Description: 根据iccid删除
	* @param iccid    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	void deleteByIccid(String iccid);
	void deleteByMachineCode(String machineCode);

	/**
	* @Title: selectByIotType
	* @Description: 查询每个运营商的物联卡信息
	* @param iotType
	* @return    参数
	* @author sy
	* @throws
	* @return List<Map<String,Object>>    返回类型
	*
	*/
	List<IotMediaAttributeModel> selectByIotType(Integer iotType);

	List<Map<String,Object>> selectClientInfo(Map<String,Object> param);
	Integer selectClientInfoCount(Map<String,Object> param);

	List<Map<String,Object>> selectIotInfo(Map<String,Object> param);
	Integer selectIotInfoCount(Map<String,Object> param);




}
