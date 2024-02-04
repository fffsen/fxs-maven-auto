package com.zycm.zybao.service.impl;

import com.zycm.zybao.common.config.FtpConfig;
import com.zycm.zybao.common.utils.NumUtil;
import com.zycm.zybao.dao.*;
import com.zycm.zybao.model.entity.ProgramLayoutModel;
import com.zycm.zybao.model.entity.ProgramMaterialRelationModel;
import com.zycm.zybao.model.entity.ProgramModel;
import com.zycm.zybao.model.mqmodel.request.publish.ProgramMsg;
import com.zycm.zybao.model.vo.ProgramLayoutVo;
import com.zycm.zybao.model.vo.ProgramMaterialRelationVo;
import com.zycm.zybao.model.vo.ProgramPublishVo;
import com.zycm.zybao.model.vo.ProgramVo;
import com.zycm.zybao.service.interfaces.ProgramPublishRecordService;
import com.zycm.zybao.service.interfaces.ProgramPublishRecordService2;
import com.zycm.zybao.service.interfaces.ProgramService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service("programService")
public class ProgramServiceImpl implements ProgramService {

    @Autowired(required = false)
	private ProgramDao programDao;
    @Autowired(required = false)
   	private ProgramMaterialRelationDao programMaterialRelationDao;
    @Autowired(required = false)
   	private ProgramLayoutDao programLayoutDao;
    @Autowired(required = false)
    private ProgramMaterialDao programMaterialDao;
    @Autowired(required = false)
    private ProgramPublishRecordService2 programPublishRecordService2;
	@Autowired(required = false)
	private ProgramPublishRecordService programPublishRecordService;
    @Autowired(required = false)
    private ProgramListDetailDao programListDetailDao;
    @Autowired(required = false)
    private ProgramListTimeSetDao programListTimeSetDao;
    @Autowired(required = false)
    private MediaAttributeDao mediaAttributeDao;


    @Override
	public Map<String, Object> selectPage(Map<String, Object> map,Integer pageSize,Integer page) {
		Map<String, Object> returndata = new HashMap<String, Object>();
		String totalCount = programDao.selectPageCount(map);
		if(!"0".equals(totalCount)){
			//总页数
	        Double totalPage = Math.ceil(Integer.parseInt(totalCount)/Double.parseDouble(pageSize+""));
	        int startRow = (page-1)*pageSize;
	        map.put("startRow", startRow);
	        map.put("pageSize", pageSize);
	        List<Map<String,Object>> list = programDao.selectPage(map);
	        for (int i = 0; i < list.size(); i++) {
	        	//大小转换处理
	    		String size_kb = list.get(i).get("totalSize") == null?"":list.get(i).get("totalSize").toString();
	    		if(StringUtils.isNotBlank(size_kb)){
	    			list.get(i).put("totalSize",NumUtil.sizeToStr(new BigDecimal(size_kb)));
	    		}else{
	    			list.get(i).put("totalSize","");
	    		}
			}
	        returndata.put("dataList", list);
	        returndata.put("totalPage", totalPage);

		}else{
			returndata.put("dataList", new ArrayList());
	        returndata.put("totalPage", 0);
		}
		returndata.put("page", page);
		returndata.put("pageSize", pageSize);
		returndata.put("total", Integer.parseInt(totalCount));

		return returndata;
	}

    @Override
   	public Map<String, Object> selectPageProgTimeMode(Map<String, Object> map,Integer pageSize,Integer page) {
   		Map<String, Object> returndata = new HashMap<String, Object>();
   		Integer totalCount = programDao.selectPageProgTimeModeCount(map);
   		if(totalCount.intValue() > 0){
   			//总页数
   	        Double totalPage = Math.ceil(totalCount.intValue()/Double.parseDouble(pageSize+""));
   	        int startRow = (page-1)*pageSize;
   	        map.put("startRow", startRow);
   	        map.put("pageSize", pageSize);
   	        List<Map<String,Object>> list = programDao.selectPageProgTimeMode(map);
   	        returndata.put("dataList", list);
   	        returndata.put("totalPage", totalPage);

   		}else{
   			returndata.put("dataList", new ArrayList());
   	        returndata.put("totalPage", 0);
   		}
   		returndata.put("page", page);
   		returndata.put("pageSize", pageSize);
   		returndata.put("total", totalCount);

   		return returndata;
   	}

	@Override
	public Integer saveProgram(ProgramVo programVo, List<ProgramLayoutVo> programLayoutList,
							   List<ProgramMaterialRelationVo> programMaterialRelationList) throws Exception {
		//校验节目名称重复
    	Map<String,Object> param = new HashMap<String,Object>();
    	param.put("programName", programVo.getProgramName());
    	param.put("programId", null);
    	List<Map<String,Object>> proglist = programDao.checkProgName(param);
    	if(proglist.size() > 0){
    		throw new Exception("节目名称重复！");
    	}

    	Date date = new Date();
		//计算总大小
    	BigDecimal totalSize = new BigDecimal(0);
    	if(programMaterialRelationList.size() > 0){
    		Integer[] materialIds = new Integer[programMaterialRelationList.size()];
    		int aa = 0;
    		for (ProgramMaterialRelationVo pmv : programMaterialRelationList) {
    			materialIds[aa] = pmv.getMaterialId();
    			aa++;
			}
        	List<Map<String,Object>> malist = programMaterialDao.selectByPrimaryKeys(materialIds);
        	if(malist.size() > 0 ){
        		for (int i = 0; i < malist.size(); i++) {
        			String m_size = malist.get(i).get("size") != null?malist.get(i).get("size").toString():"0";
        			totalSize = totalSize.add(new BigDecimal(m_size));
				}
        	}
    	}

		//节目表 新增数据
		ProgramModel programModel = programVo.toModel();
		programModel.setTotalPlayTime(0);
		programModel.setTotalSize(totalSize);
		programModel.setAuditStatus(0);
		programModel.setCreateTime(date);
		programModel.setUpdateTime(date);
		programDao.insert(programModel);
		//节目素材关系
		List<ProgramMaterialRelationModel> pmrModelList = new ArrayList<ProgramMaterialRelationModel>();
		for(ProgramMaterialRelationVo pmr : programMaterialRelationList){
			pmr.setProgramId(programModel.getProgramId());
			pmrModelList.add(pmr.toModel());
		}
		if(pmrModelList.size() > 0){
			programMaterialRelationDao.insertOfBatch(pmrModelList);
		}else{
			log.error("节目id【"+programModel.getProgramId()+"】没有配置素材信息！");
		}

		//布局表新增数据
		List<ProgramLayoutModel> plModelList = new ArrayList<ProgramLayoutModel>();
		for(ProgramLayoutVo pl : programLayoutList){
			pl.setProgramId(programModel.getProgramId());
			plModelList.add(pl.toModel());
		}
		if(plModelList.size() > 0){
			programLayoutDao.insertOfBatch(plModelList);
		}else{
			log.error("节目id【"+programModel.getProgramId()+"】没有布局信息！");
		}

		//计算总时长
		//根据节目id  重新得出计算后的节目时长信息
		List<Map<String, Object>> protimelist = programMaterialRelationDao.selectProgramTotalTime(programModel.getProgramId());
		if(protimelist.size() > 0){
			//获取最新的节目总时长
			String newPlayTime = protimelist.get(0).get("playTime").toString();
			ProgramModel pm = new ProgramModel();
			pm.setProgramId(programModel.getProgramId());
			pm.setTotalPlayTime(StringUtils.isNotBlank(newPlayTime)?Integer.parseInt(newPlayTime):0);
			programDao.updateTotalPlayTime(pm);
		}

		return programModel.getProgramId();

	}


	@Override
	public void updateProgram(ProgramVo programVo, List<ProgramLayoutVo> programLayoutList,
			List<ProgramMaterialRelationVo> programMaterialRelationList) throws Exception {
		Date date = new Date();
		//计算总大小
    	BigDecimal totalSize = new BigDecimal(0);
    	if(programMaterialRelationList.size() > 0){
    		Integer[] materialIds = new Integer[programMaterialRelationList.size()];
    		int aa = 0;
    		for (ProgramMaterialRelationVo pmv : programMaterialRelationList) {
    			materialIds[aa] = pmv.getMaterialId();
    			aa++;
			}
        	List<Map<String,Object>> malist = programMaterialDao.selectByPrimaryKeys(materialIds);
        	if(malist.size() > 0 ){
        		for (int i = 0; i < malist.size(); i++) {
        			String m_size = malist.get(i).get("size") != null?malist.get(i).get("size").toString():"0";
        			totalSize = totalSize.add(new BigDecimal(m_size));
				}
        	}
    	}
		//节目表 修改数据
		ProgramModel programModel = programVo.toModel();
		programModel.setTotalPlayTime(0);
		programModel.setTotalSize(totalSize);
		programModel.setUpdateTime(date);
		programDao.updateProgram(programModel);

		//删除节目素材关系表中数据  再添加
		List<ProgramMaterialRelationModel> pmrModelList = new ArrayList<ProgramMaterialRelationModel>();
		for(ProgramMaterialRelationVo pmr : programMaterialRelationList){
			pmr.setProgramId(programModel.getProgramId());
			pmrModelList.add(pmr.toModel());
		}
		programMaterialRelationDao.deleteByProgramId(programModel.getProgramId());
		if(pmrModelList.size() > 0){
			programMaterialRelationDao.insertOfBatch(pmrModelList);
		}else{
			log.error("节目id【"+programModel.getProgramId()+"】没有配置素材信息！");
		}

		//删除布局表数据 再新加数据
		List<ProgramLayoutModel> plModelList = new ArrayList<ProgramLayoutModel>();
		for(ProgramLayoutVo pl : programLayoutList){
			pl.setProgramId(programModel.getProgramId());
			plModelList.add(pl.toModel());
		}
		programLayoutDao.deleteByProgramId(programModel.getProgramId());
		if(plModelList.size() > 0){
			programLayoutDao.insertOfBatch(plModelList);
		}else{
			log.error("节目id【"+programModel.getProgramId()+"】没有布局信息！");
		}

		//计算总时长
		//根据节目id  重新得出计算后的节目时长信息
		List<Map<String, Object>> protimelist = programMaterialRelationDao.selectProgramTotalTime(programModel.getProgramId());
		if(protimelist.size() > 0){
			//获取最新的节目总时长
			String newPlayTime = protimelist.get(0).get("playTime").toString();
			ProgramModel pm = new ProgramModel();
			pm.setProgramId(programModel.getProgramId());
			pm.setTotalPlayTime(StringUtils.isNotBlank(newPlayTime)?Integer.parseInt(newPlayTime):0);
			programDao.updateTotalPlayTime(pm);
		}

		//修改了节目后  需要重新发布节目单  同步节目
		List<Map<String, Object>> grouplist = programPublishRecordService2.selectPageGroupByProg2(programVo.getProgramId());
		if(grouplist.size() > 0){
			log.info("修改的节目【"+programVo.getProgramId()+"】已发布终端组："+grouplist.size()+" 个");
			Integer[] mediaGroupIds = new Integer[grouplist.size()];
			for (int i = 0; i < grouplist.size(); i++) {
				String mediaGroupId = grouplist.get(i).get("mediaGroupId")==null?"":grouplist.get(i).get("mediaGroupId").toString();
				if(StringUtils.isNotBlank(mediaGroupId)) mediaGroupIds[i] = Integer.parseInt(mediaGroupId);
			}
			if(mediaGroupIds.length > 0){
				//如果是分批发布需要创建任务
			/*	PublishTaskModel publishTaskModel = null;
				String programIds_str = "";
				if("batch".equals(ProgramPublishRecordServiceImpl.SYS_PUBLISH_MODEL)){//分批发布
					publishTaskModel = new PublishTaskModel();

					publishTaskModel.setTaskName(programVo.getProgramName());
					programIds_str = ","+programVo.getProgramId()+",";
					publishTaskModel.setProgramIds(programIds_str);
					Map<String, Object> param22 = new HashMap<String, Object>();
					param22.put("mediaGroupIds", mediaGroupIds);
					List<Map<String, Object>> media = mediaAttributeDao.selectMediaByGroupId(param22);
					publishTaskModel.setMediaCount(media.size());
					publishTaskModel.setMediaCompleteCount(0);
					publishTaskModel.setCreateTime(date);
					//生成主任务
					publishTaskDao.insert(publishTaskModel);
				}

				programPublishRecordService2.publishProgByGroup(mediaGroupIds,publishTaskModel.getpTaskId());*/
				programPublishRecordService.publishProgByGroup(mediaGroupIds);
			}
		}
	}

	@Override
	public void deleteProgram(Integer programId) throws Exception{
		List<Map<String, Object>> grouplist = programPublishRecordService2.selectPageGroupByProg2(programId);
		//先删除节目表
		programDao.deleteByPrimaryKey(programId);
		//删除节目素材关系表中数据
		programMaterialRelationDao.deleteByProgramId(programId);
		//删除布局表数据
		programLayoutDao.deleteByProgramId(programId);
		//删除所有节目单中包含改节目的数据
		programListTimeSetDao.deleteByProgramId(programId);
		programListDetailDao.deleteByProgramId(programId);
		//下刊删除的节目
		//查询节目所在的所有终端组id
		if(grouplist.size() > 0){
			Integer[] mediaGroupIds = new Integer[grouplist.size()];
			for (int i = 0; i < grouplist.size(); i++) {
				String mediaGroupId = grouplist.get(i).get("mediaGroupId")==null?"":grouplist.get(i).get("mediaGroupId").toString();
				if(StringUtils.isNotBlank(mediaGroupId)) mediaGroupIds[i] = Integer.parseInt(mediaGroupId);
			}

			//下刊节目
			if(mediaGroupIds.length > 0){
				programPublishRecordService2.progDownForProg(programId, mediaGroupIds);
			}
		}
	}

	@Override
	public Map<String, Object> selectByProgramId(Integer programId) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		String prefix = FtpConfig.getImgServerPath();//预览地址前缀
		//查询节目基本信息
		List<Map<String, Object>> programMapList = programDao.selectByPrimaryKeys(new Integer[]{programId});
		Map<String, Object> programMap = programMapList.size() > 0?programMapList.get(0):null;
		if(null != programMap && programMap.get("backgroundId") != null){
			String name = programMap.get("materialName").toString();
			String path = programMap.get("materialPath").toString();
			programMap.put("backgroundPath", prefix+path+name);
		}else{
			programMap.put("backgroundPath", "");
		}

		//查询节目的素材信息
		List<Map<String, Object>> pmrList = programMaterialRelationDao.selectByProgramId(programId);

		for(Map<String, Object> map : pmrList){
			String materialName = map.get("materialName").toString();
			String materialPath = map.get("materialPath").toString();
			map.put("previewPath", prefix+materialPath+materialName);
		}
		//查询节目的布局信息
		List<Map<String, Object>> pllist = programLayoutDao.selectByProgramId(programId);
		returnMap.put("program", programMap);
		returnMap.put("material", pmrList);
		returnMap.put("layout", pllist);

		return returnMap;
	}

	@Override
	public List<Map<String, Object>> selectByPrimaryKeys(Integer[] programIds) {
		return programDao.selectByPrimaryKeys(programIds);
	}

	@Override
	public void updateName(Integer programId, String programName) {
		ProgramModel programModel = new ProgramModel();
		programModel.setProgramId(programId);
		programModel.setProgramName(programName);
		programModel.setUpdateTime(new Date());
		programDao.updateName(programModel);
	}

	@Override
	public void updateAudit(Integer programId, Integer auditStatus, String auditRemark) throws Exception{
		//验证节目播放区是否有空素材的情况
		List<Map<String,Object>> list = programDao.checkNullArea(programId);
		if(list.size() > 0){
			throw new Exception("节目存在素材为空的播放区！");
		}

		if(auditStatus.intValue() == 0 ||auditStatus.intValue() == 1 ||auditStatus.intValue() == 2){
			ProgramModel programModel = new ProgramModel();
			programModel.setProgramId(programId);
			programModel.setAuditStatus(auditStatus);
			programModel.setAuditRemark(auditRemark);
			programDao.updateAudit(programModel);
		}else{
			log.error("节目的审核状态值不正确！");
		}

	}

	@Override
	public void updateProgramInfo(Integer programId, String programName, Integer auditStatus, String auditRemark)
			throws Exception {
		List<Map<String,Object>> programList = programDao.selectByPrimaryKeys(new Integer[]{programId});
		if(programList.size() == 1){
			Integer auditStatus_db = Integer.parseInt(programList.get(0).get("auditStatus").toString());
			if(auditStatus_db.intValue() == 1){//审核通过后 不能再审核了 只能改名称
				ProgramModel programModel = new ProgramModel();
				programModel.setProgramId(programId);
				programModel.setProgramName(programName);
				programModel.setUpdateTime(new Date());
				programDao.updateName(programModel);
			}else{
				//验证节目播放区是否有空素材的情况
				List<Map<String,Object>> list = programDao.checkNullArea(programId);
				if(list.size() > 0){
					throw new Exception("节目存在素材为空的播放区！");
				}
				if(auditStatus.intValue() == 0 ||auditStatus.intValue() == 1 ||auditStatus.intValue() == 2){
					ProgramModel programModel = new ProgramModel();
					programModel.setProgramId(programId);
					programModel.setProgramName(programName);
					programModel.setUpdateTime(new Date());
					programModel.setAuditStatus(auditStatus);
					programModel.setAuditRemark(auditRemark);
					programDao.updateProgramInfo(programModel);
				}else{
					log.error("节目的审核状态值不正确！");
					throw new Exception("节目的审核状态值不正确！");
				}

			}
		}else{
			throw new Exception("根据节目id【"+programId+"】查询节目信息为空！");
		}

	}

	@Override
	public Map<String, Object> selectPageProgramAndLayout(Map<String, Object> map, Integer pageSize, Integer page) {
		//查询节目信息
		Map<String, Object> returndata = new HashMap<String, Object>();
		String totalCount = programDao.selectPageProgramAndLayoutCount(map);
		if(!"0".equals(totalCount)){
			//总页数
	        Double totalPage = Math.ceil(Integer.parseInt(totalCount)/Double.parseDouble(pageSize+""));
	        int startRow = (page-1)*pageSize;
	        map.put("startRow", startRow);
	        map.put("pageSize", pageSize);
	        List<ProgramModel> list = programDao.selectPageProgramAndLayout(map);
	        String prefix = FtpConfig.getImgServerPath();//预览地址前缀
	        for (ProgramModel ptm : list) {
	        	if(ptm.getBackgroundId() != null && ptm.getBackgroundId() > 0){
	        		String materialName = ptm.getProgramMaterialModel().getMaterialName();
		    		String materialPath = ptm.getProgramMaterialModel().getMaterialPath();
		    		ptm.setBackgroundPath(prefix+materialPath+materialName);
	        	}
			}
	        returndata.put("dataList", list);
	        returndata.put("totalPage", totalPage);

		}else{
			returndata.put("dataList", new ArrayList());
	        returndata.put("totalPage", 0);
		}
		returndata.put("page", page);
		returndata.put("pageSize", pageSize);
		returndata.put("total", Integer.parseInt(totalCount));

		return returndata;
	}

	@Override
	public List<ProgramMsg> structurePublishCommond(ProgramPublishVo programPublishVo) {
		//组装指令数据
		//根据多个节目id查询节目信息  组装节目信息、节目的布局信息、节目的素材信息
		List<ProgramMsg> programMsglist = programDao.selectPorgAndLayoutAndMaterial(programPublishVo.getProgramIds());
		return programMsglist;
	}

	@Override
	public List<ProgramMsg> selectProgByGroupId(Integer mediaGroupId) {
		return programDao.selectProgByGroupId(mediaGroupId);
	}

	@Override
	public List<Map<String, Object>> checkProgName(String programName,Integer programId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("programName", programName);
		map.put("programId", programId);
		return programDao.checkProgName(map);
	}

	@Override
	public List<Map<String, Object>> selectByMaterialId(Integer materialId) {
		return programMaterialRelationDao.selectByMaterialId(materialId);
	}

	@Override
	public void deleteByMaterialId(Integer materialId) {
		programMaterialRelationDao.deleteByMaterialId(materialId);
	}

	public void deleteMaterialIdReduceTime(List<Map<String,Object>> list){
		if(list.size() > 0){
			for (int i = 0; i < list.size(); i++) {
				//取出删除素材涉及的节目信息  包括节目id、分区、素材在节目中的时长（素材在不同节目中的时长设置是不一样的）
				String programId = list.get(i).get("programId").toString();
				String areaId = list.get(i).get("areaId").toString();
				//String playTime = list.get(i).get("playTime").toString();
				//根据节目id  重新得出计算后的节目时长信息
				List<Map<String, Object>> protimelist = programMaterialRelationDao.selectProgramTotalTime(Integer.parseInt(programId));
				if(protimelist.size() > 0){
					if(protimelist.size() > 1){
						//多分区 并且有2个以上分区总时长相同并且最大 不需要处理删除素材带来的节目总时长的改变
					}else{
						//多分区只有1个分区总时长最大   需要处理删除素材带来的节目总时长的改变
						String areaId_s = list.get(i).get("areaId").toString();
						String playTime_s = list.get(i).get("playTime").toString();
						if(areaId.equals(areaId_s)){
							//如果删除的素材所在的布局分区id等于 统计节目总时长用的分区id  那么删除的素材与节目总时长相关需要做处理
							ProgramModel programModel = new ProgramModel();
							programModel.setProgramId(Integer.parseInt(programId));
							programModel.setTotalPlayTime(Integer.parseInt(playTime_s));
							programDao.updateTotalPlayTime(programModel);

						}
						//修改分区的时长

					}
				}

			}
		}else{
			log.error("删除素材 调整总时长处理  接收的list为空");
		}
	}

	@Override
	public void oneKeyMakeProgram(Integer[] materialIds, String programName, Integer screenType, String p_width,
			String p_height, Integer playTime1,Integer creatorId) {
		//查询素材是否存在
		List<Map<String,Object>> m_list = programMaterialDao.selectByPrimaryKeys(materialIds);
		if(materialIds.length == m_list.size()){
			Integer totalTime = 0;
			BigDecimal totalSize = new BigDecimal(0);
			if(m_list.size() > 1){
				//计算节目的总时长与总大小
				for (Map<String, Object> map : m_list) {
					String ptype = map.get("type").toString();
					String s = map.get("size")==null?"0":map.get("size").toString();
					totalSize = totalSize.add(new BigDecimal(s));
					if("1".equals(ptype)){
						totalTime += 10;//图片默认10s
					}else if("2".equals(ptype)){
						Integer t = map.get("timeLenth")==null?0:Integer.parseInt(map.get("timeLenth").toString());
						totalTime += t;//视频默认为源视频本身的时长
					}
				}
			}else if(m_list.size() == 1){
				//计算节目的总时长与总大小
				String s = m_list.get(0).get("size")==null?"0":m_list.get(0).get("size").toString();
				String ptype = m_list.get(0).get("type").toString();
				totalSize = totalSize.add(new BigDecimal(s));
				if("1".equals(ptype)){
					totalTime = 10;//图片默认10s
				}else if("2".equals(ptype)){
					Integer t = m_list.get(0).get("timeLenth")==null?0:Integer.parseInt(m_list.get(0).get("timeLenth").toString());
					totalTime = t;//视频默认为源视频本身的时长
				}
				//totalTime = playTime;

			}
			//加入节目
			Date date = new Date();
			ProgramModel programModel = new ProgramModel();
			programModel.setProgramName(programName);
			programModel.setTotalPlayTime(totalTime);
			programModel.setTotalSize(totalSize);
			programModel.setProgramHeight(Double.parseDouble(p_height) );
			programModel.setProgramWidth(Double.parseDouble(p_width));
			programModel.setScreenType(screenType);
			programModel.setAuditStatus(0);
			programModel.setCreatorId(creatorId);
			programModel.setCreateTime(date);
			programModel.setUpdateTime(date);
			programDao.insert(programModel);
			//布局表加入数据
			List<ProgramLayoutModel> list = new ArrayList<ProgramLayoutModel>();
			ProgramLayoutModel programLayoutModel = new ProgramLayoutModel();
			programLayoutModel.setProgramId(programModel.getProgramId());
			programLayoutModel.setAreaId(1);
			programLayoutModel.setAreaType(9);//混合区
			programLayoutModel.setHeight(Double.parseDouble(p_height));
			programLayoutModel.setWidth(Double.parseDouble(p_width));
			programLayoutModel.setLayer(1);
			programLayoutModel.setX(Double.parseDouble("0"));
			programLayoutModel.setY(Double.parseDouble("0"));
			list.add(programLayoutModel);
			programLayoutDao.insertOfBatch(list);
			//播放设置加入数据
			List<ProgramMaterialRelationModel> pmrList = new ArrayList<ProgramMaterialRelationModel>();
			if(m_list.size() > 0){
				int order = 1;
				for (Map<String, Object> map : m_list) {
					String ptype = map.get("type").toString();
					Integer materialId = Integer.parseInt(map.get("materialId").toString());
					ProgramMaterialRelationModel programMaterialRelationModel = new ProgramMaterialRelationModel();
					programMaterialRelationModel.setProgramId(programModel.getProgramId());
					programMaterialRelationModel.setAreaId(1);
					programMaterialRelationModel.setMaterialId(materialId);
					programMaterialRelationModel.setMaterialOrder(order);
					if("1".equals(ptype)){//图片
						programMaterialRelationModel.setPlayTime(10);
						programMaterialRelationModel.setExtend("{'effect':0}");
					}else if("2".equals(ptype)){//视频
						Integer t = map.get("timeLenth")==null?0:Integer.parseInt(map.get("timeLenth").toString());
						programMaterialRelationModel.setPlayTime(t);

						programMaterialRelationModel.setExtend("{'voice':40}");
					}
					pmrList.add(programMaterialRelationModel);
					order++;
				}
			}
			programMaterialRelationDao.insertOfBatch(pmrList);

		}else{
			log.error("一键生成节目时，选择的素材数据与数据库数据不一致！");
		}
	}

	@Override
	public Map<String, Object> selectPageNoOrders(Map<String, Object> map, Integer pageSize, Integer page) {
		Map<String, Object> returndata = new HashMap<String, Object>();
		Integer totalCount = programDao.selectPageNoOrdersCount(map);
		if(!"0".equals(totalCount)){
			//总页数
	        Double totalPage = Math.ceil(totalCount/Double.parseDouble(pageSize+""));
	        int startRow = (page-1)*pageSize;
	        map.put("startRow", startRow);
	        map.put("pageSize", pageSize);
	        List<Map<String,Object>> list = programDao.selectPageNoOrders(map);
	        for (int i = 0; i < list.size(); i++) {
	        	//大小转换处理
	    		String size_kb = list.get(i).get("totalSize") == null?"":list.get(i).get("totalSize").toString();
	    		if(StringUtils.isNotBlank(size_kb)){
	    			list.get(i).put("totalSize",NumUtil.sizeToStr(new BigDecimal(size_kb)));
	    		}else{
	    			list.get(i).put("totalSize","");
	    		}
			}
	        returndata.put("dataList", list);
	        returndata.put("totalPage", totalPage);

		}else{
			returndata.put("dataList", new ArrayList());
	        returndata.put("totalPage", 0);
		}
		returndata.put("page", page);
		returndata.put("pageSize", pageSize);
		returndata.put("total", totalCount);

		return returndata;
	}


}
