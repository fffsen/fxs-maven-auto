package com.zycm.zybao.service.interfaces;

import com.zycm.zybao.model.entity.ProgramMaterialModel;

import java.util.List;
import java.util.Map;

public interface ProgramMaterialService {

	/** 分页查询
	* @Title: selectPage
	* @Description: TODO
	* @author sy
	* @param @param map
	* @param @return
	* @return List<Map<String,Object>>
	*
	*/
	Map<String,Object> selectPage(Map<String,Object> map,Integer pageSize,Integer page);

	/** 根据素材id查询基本信息
	* @Title: selectByPrimaryKey
	* @Description: TODO
	* @author sy
	* @param @param materialId
	* @param @return
	* @return Map<String,Object>
	*
	*/
	Map<String,Object> selectByPrimaryKey(Integer materialId);

	/** 批量添加素材信息
	* @Title: batchInsert
	* @Description: TODO
	* @author sy
	* @param @param list
	* @return void
	*
	*/
	List<ProgramMaterialModel> batchInsert(List<ProgramMaterialModel> list);

	/** 修改素材的名称、有效时间、是否共享
	* @Title: updateMaterial
	* @Description: TODO
	* @author sy
	* @param @param programMaterialModel
	* @return void
	*
	*/
	void updateMaterial(ProgramMaterialModel programMaterialModel) throws Exception;

	/**
	* @Title: updateMaterialPath
	* @Description: 修改素材的路径
	* @param materialId
	* @param materialPath    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	void updateMaterialPath(Integer materialId,String materialPath);
	/** 修改审核状态、审核人、审核备注
	* @Title: updateAuditStatus
	* @Description: TODO
	* @author sy
	* @param @param programMaterialModel
	* @return void
	*
	*/
	void updateAuditStatus(ProgramMaterialModel programMaterialModel);

	/** 根据素材id  逻辑删除
	* @Title: updateIsDelete
	* @Description: TODO
	* @author sy
	* @param @param materialId
	* @return void
	*
	*/
	void updateIsDelete(Integer materialId);

	/** 分页查询安装包信息
	* @Title: selectApk
	* @Description:
	* @author sy
	* @param @param map
	* @param @return
	* @return List<Map<String,Object>>
	*
	*/
	Map<String,Object> selectApk(Map<String,Object> map,Integer pageSize,Integer page);

	/** 根据素材名称查询数据
	* @Title: selectByMaterialName
	* @Description:
	* @author sy
	* @param @param materialName
	* @param @return
	* @return List<Map<String,Object>>
	*
	*/
	List<Map<String,Object>> selectByMaterialName(String[] materialName);

	/** 验证节目是否有效
	* @Title: checkMaterialName
	* @Description:
	* @author sy
	* @param @param materialNames
	* @param @param type
	* @param @param materialIds
	* @param @return
	* @return List<Map<String,Object>>
	*
	*/
	List<Map<String,Object>> checkMaterialName(String[] materialNames,Integer type,Integer[] materialIds);

	/**
	* @Title: readRemoteTxt
	* @Description: 获取远程txt的内容
	* @param path
	* @return    参数
	* @author sy
	* @throws
	* @return String    返回类型
	*
	*/
	String readRemoteTxt(String path) throws Exception;

	/**
	* @Title: checkMaterialInProg
	* @Description: 判断素材是否已存在节目中
	* @param materialIds
	* @return    参数
	* @author sy
	* @throws
	* @return List<Map<String,Object>>    返回类型
	*
	*/
	List<Map<String,Object>> checkMaterialInProg(Integer[] materialIds);

	/**
	* @Title: syncMaterialMD5
	* @Description: 同步有效素材的md5值
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	String syncMaterialMD5();

}
