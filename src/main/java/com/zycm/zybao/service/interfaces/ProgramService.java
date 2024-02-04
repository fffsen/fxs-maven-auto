package com.zycm.zybao.service.interfaces;


import com.zycm.zybao.model.mqmodel.request.publish.ProgramMsg;
import com.zycm.zybao.model.vo.ProgramLayoutVo;
import com.zycm.zybao.model.vo.ProgramMaterialRelationVo;
import com.zycm.zybao.model.vo.ProgramPublishVo;
import com.zycm.zybao.model.vo.ProgramVo;

import java.util.List;
import java.util.Map;

public interface ProgramService {

	/** 分页查询节目
	* @Title: selectPage
	* @Description: TODO
	* @author sy
	* @param @param map
	* @param @param pageSize
	* @param @param page
	* @param @return
	* @return Map<String,Object>
	*
	*/
	Map<String, Object> selectPage(Map<String, Object> map,Integer pageSize,Integer page);

	/**
	* @Title: selectPageProgTimeMode
	* @Description: 根据条件分页查询  节目的发布形式
	* @param map
	* @param pageSize
	* @param page
	* @return    参数
	* @author sy
	* @throws
	* @return Map<String,Object>    返回类型
	*
	*/
	Map<String, Object> selectPageProgTimeMode(Map<String, Object> map,Integer pageSize,Integer page);

	/**
	* @Title: selectPageNoOrders
	* @Description: 分页查询未配置订单的节目
	* @param map
	* @param pageSize
	* @param page
	* @return    参数
	* @author sy
	* @throws
	* @return Map<String,Object>    返回类型
	*
	*/
	Map<String, Object> selectPageNoOrders(Map<String, Object> map,Integer pageSize,Integer page);

	/** 保存节目
	* @Title: saveProgram
	* @Description: TODO
	* @author sy
	* @param @param programVo  节目信息
	* @param @param programLayoutVo  布局信息
	* @param @param programMaterialRelationVo   素材信息
	* @return void
	*
	*/
	Integer saveProgram(ProgramVo programVo, List<ProgramLayoutVo> programLayoutList, List<ProgramMaterialRelationVo> programMaterialRelationList) throws Exception;

	/** 修改节目
	* @Title: updateProgram
	* @Description: TODO
	* @author sy
	* @param @param programVo  节目信息
	* @param @param programLayoutVo  布局信息
	* @param @param programMaterialRelationVo   素材信息
	* @return void
	*
	*/
	void updateProgram(ProgramVo programVo,List<ProgramLayoutVo> programLayoutList,List<ProgramMaterialRelationVo> programMaterialRelationList) throws Exception;

	/** 删除节目
	* @Title: deleteProgram
	* @Description: TODO
	* @author sy
	* @param @param programId
	* @return void
	*
	*/
	void deleteProgram(Integer programId) throws Exception;

	/** 根据节目id查询节目的详细信息、布局信息、素材信息
	* @Title: selectByProgramId
	* @Description: TODO
	* @author sy
	* @param @param programId
	* @param @return
	* @return Map<String,Object>
	*
	*/
	Map<String,Object> selectByProgramId(Integer programId);

    /** 根据节目id  修改节目名
    * @Title: updateName
    * @Description: TODO
    * @author sy
    * @param @param programId
    * @param @param programName
    * @return void
    *
    */
    void updateName(Integer programId,String programName);

	/** 根据节目id  审核节目
	* @Title: updateAudit
	* @Description: TODO
	* @author sy
	* @param @param programId
	* @param @param auditStatus
	* @param @param auditRemark
	* @return void
	*
	*/
	void updateAudit(Integer programId,Integer auditStatus,String auditRemark) throws Exception;

	/**
	* @Title: updateProgramInfo
	* @Description:修改节目的基本信息及审核节目信息
	* @param programId
	* @param programName
	* @param auditStatus
	* @param auditRemark
	* @throws Exception    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	void updateProgramInfo(Integer programId,String programName,Integer auditStatus,String auditRemark) throws Exception;

	/** 分页查询  节目信息与布局信息 用于选择节目展示
	* @Title: selectPageProgramAndLayout
	* @Description: TODO
	* @author sy
	* @param @param map
	* @param @param pageSize
	* @param @param page
	* @param @return
	* @return Map<String,Object>
	*
	*/
	Map<String, Object> selectPageProgramAndLayout(Map<String, Object> map,Integer pageSize,Integer page);

	/**
	* 说明：根据节目id  组装节目的基本信息、布局信息、素材信息
	* @Title: structurePublishCommond
	* @Description: TODO
	* @author sy
	* @param @param programPublishVo
	* @param @return
	* @return List<ProgramMsg>
	*
	*/
	List<ProgramMsg> structurePublishCommond(ProgramPublishVo programPublishVo);

	/** 根据节目id查询节目基本信息
	* @Title: selectByPrimaryKey
	* @Description:
	* @author sy
	* @param @param programId
	* @param @return
	* @return Map<String,Object>
	*
	*/
	List<Map<String, Object>> selectByPrimaryKeys(Integer[] programIds);

	/** 根据终端组id  查询节目单 包括继承父节点的节目
	* @Title: selectProgByGroupId
	* @Description:
	* @author sy
	* @param @param mediaGroupId
	* @param @return
	* @return List<ProgramMsg>
	*
	*/
	List<ProgramMsg> selectProgByGroupId(Integer mediaGroupId);

	/**
	* 说明：根据节目名称查询
	* <br>1 只传节目名用于验证节目添加时重复名称
	* <br>2 传节目名与节目id用于修改节目时验证重复名称
	* @Title: checkProgName
	* @Description: TODO
	* @author sy
	* @param @param programName
	* @param @param programId
	* @param @return
	* @return List<Map<String,Object>>
	*
	*/
	List<Map<String,Object>> checkProgName(String programName,Integer programId);

	/** 根据素材id查询数据
	* @Title: selectByMaterialId
	* @Description:
	* @author sy
	* @param @param materialId
	* @param @return
	* @return List<Map<String,Object>>
	*
	*/
	List<Map<String,Object>> selectByMaterialId(Integer materialId);

	/** 根据素材id删除数据
	* @Title: deleteByMaterialId
	* @Description:
	* @author sy
	* @param @param materialId
	* @return void
	*
	*/
	void deleteByMaterialId(Integer materialId);

	/** 删除素材  调整总时长
	* @Title: deleteMaterialIdReduceTime
	* @Description:
	* @author sy
	* @param @param list
	* @return void
	*
	*/
	void deleteMaterialIdReduceTime(List<Map<String,Object>> list);

	/**
	* @Title: oneKeyMakeProgram
	* @Description: 一键节目制作
	* @param materialIds
	* @param programName
	* @param screenType
	* @param p_width
	* @param p_height
	* @param playTime    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	void oneKeyMakeProgram(Integer[] materialIds, String programName, Integer screenType, String p_width,
			String p_height, Integer playTime,Integer creatorId);
}
