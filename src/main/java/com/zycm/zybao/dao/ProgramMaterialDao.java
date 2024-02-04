package com.zycm.zybao.dao;

import com.zycm.zybao.model.entity.ProgramMaterialModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ProgramMaterialDao {

	/** 分页查询
	* @Title: selectPage
	* @Description: TODO
	* @author sy
	* @param @param map
	* @param @return
	* @return List<Map<String,Object>>
	* @throws
	*/
	List<Map<String,Object>> selectPage(Map<String,Object> map);

	/** 分页查询的统计总数据条数
	* @Title: selectPageCount
	* @Description: TODO
	* @author sy
	* @param @param map
	* @param @return
	* @return String
	* @throws
	*/
	String selectPageCount(Map<String,Object> map);

	/**
	* @Title: selectFolderByMaterial
	* @Description: 查询当前目录下包含模糊匹配素材的子文件夹
	* @param map
	* @return    参数
	* @author sy
	* @throws
	* @return List<Map<String,Object>>    返回类型
	*
	*/
	Map<String,Object> selectFolderByMaterial(Map<String,Object> map);

	/** 根据素材id查询基本信息
	* @Title: selectByPrimaryKey
	* @Description: TODO
	* @author sy
	* @param @param materialId
	* @param @return
	* @return List<Map<String,Object>>
	* @throws
	*/
	Map<String,Object> selectByPrimaryKey(Integer materialId);

	/**
	* @Title: selectByApkVersion
	* @Description: 根据版本号查询素材信息
	* @param apkVersion
	* @return    参数
	* @author sy
	* @throws
	* @return List<Map<String,Object>>    返回类型
	*
	*/
	List<Map<String,Object>> selectByApkVersion(String apkVersion);
	/**
	* @Title: selectByPrimaryKeys
	* @Description: 根据多个素材id查询信息
	* @param materialIds
	* @return    参数
	* @author sy
	* @throws
	* @return List<Map<String,Object>>    返回类型
	*
	*/
	List<Map<String,Object>> selectByPrimaryKeys(Integer[] materialIds);

	/** 批量添加素材信息
	* @Title: batchInsert
	* @Description: TODO
	* @author sy
	* @param @param list
	* @return void
	* @throws
	*/
	void batchInsert(List<ProgramMaterialModel> list);

	/** 修改素材的名称、有效时间、是否共享
	* @Title: updateMaterial
	* @Description: TODO
	* @author sy
	* @param @param programMaterialModel
	* @return void
	* @throws
	*/
	void updateMaterial(ProgramMaterialModel programMaterialModel);

	/** 修改素材的名称、审核信息
	* @Title: updateMaterial
	* @Description: TODO
	* @author sy
	* @param @param programMaterialModel
	* @return void
	* @throws
	*/
	void updateMaterial2(ProgramMaterialModel programMaterialModel);

	void updateMaterialPath(ProgramMaterialModel programMaterialModel);

	/** 修改审核状态、审核人、审核备注
	* @Title: updateAuditStatus
	* @Description: TODO
	* @author sy
	* @param @param programMaterialModel
	* @return void
	* @throws
	*/
	void updateAuditStatus(ProgramMaterialModel programMaterialModel);

	/** 根据素材id  逻辑删除
	* @Title: updateIsDelete
	* @Description: TODO
	* @author sy
	* @param @param materialId
	* @return void
	* @throws
	*/
	void updateIsDelete(ProgramMaterialModel programMaterialModel);

	/**
	* @Title: updateAiAudit
	* @Description: TODO(修改智能审核结果)
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	void updateAiAudit(ProgramMaterialModel programMaterialModel);

	/**
	* @Title: selectApk
	* @Description: 分页查询安装包信息  不带文件夹
	* @author sy
	* @param @param map
	* @param @return
	* @return List<Map<String,Object>>
	* @throws
	*/
	List<Map<String,Object>> selectApk(Map<String,Object> map);

	/**
	* @Title: selectApkCount
	* @Description: 查询总数  绑定分页查询安装包信息方法
	* @author sy
	* @param @param map
	* @param @return
	* @return String
	* @throws
	*/
	String selectApkCount(Map<String,Object> map);

	/**
	* @Title: selectByMaterialName
	* @Description: 根据素材名称查询数据
	* @author sy
	* @param @param materialName
	* @param @return
	* @return List<Map<String,Object>>
	* @throws
	*/
	List<Map<String,Object>> selectByMaterialName(String[] materialName);

	/**
	* @Title: checkMaterialName
	* @Description: 验证节目是否有效
	* @author sy
	* @param @param materialNames
	* @param @param type
	* @param @param materialIds
	* @param @return
	* @return List<Map<String,Object>>
	* @throws
	*/
	List<Map<String,Object>> checkMaterialName(@Param("materialNames") String[] materialNames, @Param("type") Integer type, @Param("materialIds") Integer[] materialIds);

	/**
	* @Title: selectNullMD5
	* @Description: 不分页查询md5为空的素材 不包括文件夹、apk
	* @return    参数
	* @author sy
	* @throws
	* @return List<Map<String,Object>>    返回类型
	*
	*/
	List<Map<String,Object>> selectNullMD5();

	/**
	* @Title: updateMD5
	* @Description: 修改md5值
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	void updateMD5(ProgramMaterialModel programMaterialModel);

	/**
	* @Title: selectInspectorByMaterialId
	* @Description: 根据素材id查询终端管理人员
	* @param param
	* @return    参数
	* @author sy
	* @throws
	* @return List<Map<String,Object>>    返回类型
	*
	*/
	List<Map<String,Object>> selectInspectorByMaterialId(Map<String,Object> param);
}
