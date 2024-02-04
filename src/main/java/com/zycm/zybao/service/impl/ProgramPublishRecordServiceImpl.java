package com.zycm.zybao.service.impl;

import com.zycm.zybao.amq.topic.producer.FBTopicSender;
import com.zycm.zybao.common.config.SysConfig;
import com.zycm.zybao.common.enums.MessageEvent;
import com.zycm.zybao.common.redis.RedisHandle;
import com.zycm.zybao.common.utils.NumUtil;
import com.zycm.zybao.dao.MediaAttributeDao;
import com.zycm.zybao.dao.PlayTimeSetDao;
import com.zycm.zybao.dao.ProgramPublishRecordDao;
import com.zycm.zybao.model.entity.PlayTimeSetModel;
import com.zycm.zybao.model.entity.ProgramPublishRecordModel;
import com.zycm.zybao.model.mqmodel.ProtocolModel;
import com.zycm.zybao.model.mqmodel.request.publish.ProgramMsg;
import com.zycm.zybao.model.vo.ProgramPublishVo;
import com.zycm.zybao.service.interfaces.ProgramPublishRecordService;
import com.zycm.zybao.service.interfaces.ProgramService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service("programPublishRecordService")
public class ProgramPublishRecordServiceImpl implements ProgramPublishRecordService {

	public static final String SYS_PUBLISH_MODEL = SysConfig.sysPublishModel;

	@Autowired(required = false)
	private ProgramPublishRecordDao programPublishRecordDao;
	@Autowired(required = false)
	private MediaAttributeDao mediaAttributeDao;
	@Autowired(required = false)
	private ProgramService programService;
	@Autowired(required = false)
	private PlayTimeSetDao playTimeSetDao;
	@Autowired(required = false)
	private RedisHandle redisHandle;
	@Autowired(required = false)
	private FBTopicSender fBTopicSender;

	@Override
	public Map<String, Object> selectPage(Integer mediaGroupId,Integer pageSize,Integer page) {
		Map<String, Object> returndata = new HashMap<String, Object>();
		Map<String, Object> param = new HashMap<String, Object>();

		param.put("mediaGroupIds", new Integer[]{mediaGroupId});

		String totalCount = programPublishRecordDao.selectProgramByGroupIdCount(param);
		if(!"0".equals(totalCount)){
			//总页数
	        Double totalPage = Math.ceil(Integer.parseInt(totalCount)/Double.parseDouble(pageSize+""));
	        int startRow = (page-1)*pageSize;
	        param.put("startRow", startRow);
	        param.put("pageSize", pageSize);
	        List<Map<String,Object>> list = programPublishRecordDao.selectProgramByGroupId(param);
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
	public Map<String, Object> selectPage2(Integer mid, Integer pageSize, Integer page) throws Exception{
		Map<String, Object> returndata = new HashMap<String, Object>();
		Map<String, Object> param = new HashMap<String, Object>();

		//根据组id查询设备id
		Integer mediaGroupId = null;
		List<Map<String, Object>> list2 = mediaAttributeDao.selectByPrimaryKeys(new Integer[]{mid});
		if(list2.size() >0){
			mediaGroupId = Integer.parseInt(list2.get(0).get("mediaGroupId").toString());
		} else {
			throw new Exception("未查询到终端组信息");
		}

		param.put("mediaGroupIds", new Integer[]{mediaGroupId});

		String totalCount = programPublishRecordDao.selectProgramByGroupIdCount(param);
		if(!"0".equals(totalCount)){
			//总页数
	        Double totalPage = Math.ceil(Integer.parseInt(totalCount)/Double.parseDouble(pageSize+""));
	        int startRow = (page-1)*pageSize;
	        param.put("startRow", startRow);
	        param.put("pageSize", pageSize);
	        List<Map<String,Object>> list = programPublishRecordDao.selectProgramByGroupId(param);
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
	public Map<String, Object> selectPageGroupByProg(Map<String,Object> param, Integer pageSize, Integer page) {
		Map<String, Object> returndata = new HashMap<String, Object>();
		Integer totalCount = programPublishRecordDao.selectGroupByProgCount(param);
		if(null != totalCount && totalCount > 0){
			//总页数
	        Double totalPage = Math.ceil(totalCount/Double.parseDouble(pageSize+""));
	        int startRow = (page-1)*pageSize;
	        param.put("startRow", startRow);
	        param.put("pageSize", pageSize);
	        List<Map<String,Object>> list = programPublishRecordDao.selectGroupByProg(param);

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
	public void publishProg(ProgramPublishVo programPublishVo) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
		List<ProgramPublishRecordModel> pprlist = new ArrayList<ProgramPublishRecordModel>();
		List<PlayTimeSetModel> ptslist = new ArrayList<PlayTimeSetModel>();
		//转换终端组数组成list
		List<Integer> tmp_mediaGroupIdList = new ArrayList<Integer>(programPublishVo.getMediaGroupIds().length);
		Collections.addAll(tmp_mediaGroupIdList, programPublishVo.getMediaGroupIds());

		//计算终端组的最大节目序号
		Map<String,Integer> maxNumMap = new HashMap<String,Integer>();
		List<Map<String,Object>> maxNumList = programPublishRecordDao.selectGroupMaxNum(tmp_mediaGroupIdList);
		if(maxNumList.size() > 0){
			for (Map<String, Object> mnmap : maxNumList) {
				maxNumMap.put(mnmap.get("mediaGroupId").toString(), Integer.parseInt(mnmap.get("maxNum").toString()));
			}
		}


		//计算多时段时间设置的  获取起始时段
		List<Date> s_e_date = programPublishVo.toOrderTimeSet();
		Date s_date = s_e_date.get(0);
		Date e_date = s_e_date.get(s_e_date.size()-1);
		Date date = new Date();
		if(programPublishVo.getTimeMode() == 1){//永久
			programPublishVo.setPlayStartDate("1970-01-01");
			programPublishVo.setPlayEndDate("2199-01-01");
		}

		//把有效的终端组上加上发布信息
		for (Integer mediaGroupId : tmp_mediaGroupIdList) {
			int aa = 1;
			Integer groupMaxNum = maxNumMap.get(mediaGroupId.toString())==null?0:maxNumMap.get(mediaGroupId.toString());
			for (int i = 0; i < programPublishVo.getProgramIds().length; i++) {
				ProgramPublishRecordModel pprModel = new ProgramPublishRecordModel();
				pprModel.setProgramId(programPublishVo.getProgramIds()[i]);
				pprModel.setProgramOrder(groupMaxNum.intValue()+aa);
				pprModel.setMediaGroupId(mediaGroupId);
				pprModel.setPublisherId(programPublishVo.getPublisherId());
				//pprModel.setPublishStatus(publishStatus);
				pprModel.setTimeMode(programPublishVo.getTimeMode());
				pprModel.setSwitchMode(programPublishVo.getSwitchMode());
				pprModel.setDownloadMode(programPublishVo.getDownloadMode());

				if(programPublishVo.getTimeMode() == 3){
					pprModel.setWeekSet(programPublishVo.getWeekSet());
				}else{
					pprModel.setPlayStartDate(sdf.parse(programPublishVo.getPlayStartDate()));
					pprModel.setPlayStartTime(s_date);
					//pprModel.setPlayStartTime(sdf2.parse(programPublishVo.getPlayStartTime()));
					pprModel.setPlayEndDate(sdf.parse(programPublishVo.getPlayEndDate()));
					pprModel.setPlayEndTime(e_date);
					//pprModel.setPlayEndTime(sdf2.parse(programPublishVo.getPlayEndTime()));
				}
				pprModel.setPlayLevel(programPublishVo.getPlayLevel());
				pprModel.setFrequency(programPublishVo.getFrequency());
				pprModel.setCreateTime(date);
				pprlist.add(pprModel);
				aa++;
			}
		}
		programPublishRecordDao.batchInsert(pprlist);//与以下操作应同事务处理

		if(programPublishVo.getTimeMode() != 3){//按星期的 暂时不支持多时段设置
			//组装多时段设置表信息
			for (int i = 0; i < pprlist.size(); i++) {
				for (int j = 0; j < programPublishVo.getTimesetdata().length; j++) {
					String _s_e_time = programPublishVo.getTimesetdata()[j];
					PlayTimeSetModel pts = new PlayTimeSetModel();
					pts.setPublishId(pprlist.get(i).getPublishId());
					pts.setStartDate(sdf.parse(programPublishVo.getPlayStartDate()));
					pts.setEndDate(sdf.parse(programPublishVo.getPlayEndDate()));
					pts.setStartTime(sdf2.parse(_s_e_time.split("#")[0]));
					pts.setEndTime(sdf2.parse(_s_e_time.split("#")[1]));
					pts.setFrequency(Integer.parseInt(_s_e_time.split("#")[2]));
					ptslist.add(pts);
				}
			}

			playTimeSetDao.batchInsert(ptslist);
		}


		//组装发布指令消息体
		//根据节目id 查询节目的详细信息

		//暂时处理  循环终端组id  获取每个终端组的节目
		Map<String,List<ProgramMsg>> programMsglistmap = new HashMap<String,List<ProgramMsg>>();
		List<ProgramMsg> programMsglist = new ArrayList<ProgramMsg>();
		for (int i = 0; i < programPublishVo.getMediaGroupIds().length; i++) {
			//先判断是不是同步组
			/*Map<String,Object> masterMedia = syncGroupDetailsDao.selectMasterByGroupId(programPublishVo.getMediaGroupIds()[i]);
			if(masterMedia != null){//发布节目时同步组第一次是不发布节目单指令的  只标记变动行为等定时任务来触发 因为就算这里发送节目单也是老的节目单
				//标记终端组节目变动
				redisHandle.set(Constants.REDIS_SYNC_MASTER_PUBLISH_PREFIX+programPublishVo.getMediaGroupIds()[i], date.getTime());
			}else{
				programMsglist = programService.selectProgByGroupId(programPublishVo.getMediaGroupIds()[i]);
			}*/
			programMsglist = programService.selectProgByGroupId(programPublishVo.getMediaGroupIds()[i]);
			if(programMsglist.size() > 0){
				programMsglistmap.put(programPublishVo.getMediaGroupIds()[i].toString(), programMsglist);
			}
		}

		//发送指令
		ProtocolModel protocolModel = new ProtocolModel();
		//List<PublishTaskDetailModel> ptdList = null;
		Map<String, Object> param22 = new HashMap<String, Object>();
		param22.put("mediaGroupIds", programPublishVo.getMediaGroupIds());
		List<Map<String, Object>> media = mediaAttributeDao.selectMediaByGroupId(param22);
		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d1 = new Date();

		//PublishTaskModel publishTaskModel = null;
		String programIds_str = "";
		if("batch".equals(SYS_PUBLISH_MODEL)){//分批发布
			throw new Exception("版本不支持分批发布模式");
			/*publishTaskModel = new PublishTaskModel();
			ptdList = new ArrayList<PublishTaskDetailModel>();
			//处理节目名称
			List<ProgramMsg> pmList = programMsglistmap.get(programMsglistmap.keySet().toArray()[0]);
			String taskName = "";
			for (Integer proId : programPublishVo.getProgramIds()) {
				for (ProgramMsg programMsg : pmList) {
					if(programMsg.getProgramId().intValue() == proId.intValue()){
						taskName += ","+programMsg.getProgramName();
						break;
					}
				}
			}

			publishTaskModel.setTaskName(StringUtils.isNotBlank(taskName)?taskName.substring(1):taskName);
			programIds_str = ","+StringUtils.join(programPublishVo.getProgramIds(), ",")+",";
			publishTaskModel.setProgramIds(programIds_str);
			publishTaskModel.setMediaCount(media.size());
			publishTaskModel.setMediaCompleteCount(0);
			publishTaskModel.setCreateTime(d1);
			//生成主任务
			publishTaskDao.insert(publishTaskModel);*/
		}

		log.info("开始发布，发送指令,开始时间"+sdf3.format(d1)+">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		for (Map<String, Object> map : media) {
			String machineCode = map.get("machineCode")==null?"":map.get("machineCode").toString();
			String mediaGroupId = map.get("mediaGroupId")==null?"":map.get("mediaGroupId").toString();
			if(StringUtils.isNotBlank(machineCode)){
				protocolModel.ServerToClient(MessageEvent.PUBLISH.event, programMsglistmap.get(mediaGroupId));
				protocolModel.setToClient(machineCode);
				JSONObject json = JSONObject.fromObject(protocolModel);
				if("batch".equals(SYS_PUBLISH_MODEL)){//分批发布
					//分批发布详细
				/*	PublishTaskDetailModel publishTaskDetailModel = new PublishTaskDetailModel();
					publishTaskDetailModel.setMachineCode(machineCode);
					publishTaskDetailModel.setpTaskId(publishTaskModel.getpTaskId());
					publishTaskDetailModel.setPublishStatus(0);
					publishTaskDetailModel.setProgramJson(json.toString());
					publishTaskDetailModel.setCreateTime(d1);
					publishTaskDetailModel.setSendTime(0);
					ptdList.add(publishTaskDetailModel);*/

				}else{
					fBTopicSender.sendMsg(json.toString(), machineCode);
				}
			}else{
				log.error("机器码为空，不发送指令");
			}
		}

		if("batch".equals(SYS_PUBLISH_MODEL)){
			/*if(ptdList.size() > 0)
			publishTaskDetailDao.batchInsert(ptdList);*/
		}else{
			try {
				fBTopicSender.session.commit();
			} catch (Exception e) {
				log.error("提交发布指令事务失败！", e);
			}
		}
		Date d2 = new Date();
		log.info("发送"+media.size()+"个指令结束,开始时间"+sdf3.format(d1)+",结束时间"+sdf3.format(d2)+",耗时"+(d2.getTime()-d1.getTime())/1000+"秒>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		//计入用户日志
		redisHandle.set("refresh_iot", d2.getTime());

		//缓存清理
		programMsglistmap = null;
		media = null;
		//ptdList = null;
	}

	@Override
	public void publishProgByGroup(Integer[] mediaGroupIds) {
		publishProgByGroup(mediaGroupIds,null);
	}

	@Override
	public void publishProgByGroup(Integer[] mediaGroupIds,Integer pTaskId) {
		//给每个终端组下的所有媒体机发送  最新的节目单
		//暂时处理  循环终端组id  获取每个终端组的节目
		Map<String,List<ProgramMsg>> programMsglistmap = new HashMap<String,List<ProgramMsg>>();
		List<ProgramMsg> programMsglist = new ArrayList<ProgramMsg>();
		for (int i = 0; i < mediaGroupIds.length; i++) {
			/*//先判断是不是同步组
			Map<String,Object> masterMedia = syncGroupDetailsDao.selectMasterByGroupId(mediaGroupIds[i]);
			if(masterMedia != null){//发布节目时同步组第一次是不发布节目单指令的  只标记变动行为等定时任务来触发 因为就算这里发送节目单也是老的节目单
				Object programMsgObj = redisHandle.get(Constants.REDIS_SYNC_MASTER_PROGRAM_PREFIX+mediaGroupIds[i]);
				if(programMsgObj != null){
					ProgramMsg programMsg= (ProgramMsg)JSONObject.toBean(JSONObject.fromObject(programMsgObj), ProgramMsg.class);
					programMsglist.add(programMsg);
				}else{//第一次不发送节目单指令 等定时任务触发
					log.info("同步组【"+mediaGroupIds[i]+"】节目单还未生成");
				}
			}else{
				programMsglist = programService.selectProgByGroupId(mediaGroupIds[i]);
			}*/
			programMsglist = programService.selectProgByGroupId(mediaGroupIds[i]);
			if(programMsglist.size() > 0){
				programMsglistmap.put(mediaGroupIds[i].toString(), programMsglist);
			}
		}
		//查询终端组下的所有终端
		Map<String, Object> param22 = new HashMap<String, Object>();
		param22.put("mediaGroupIds", mediaGroupIds);
		List<Map<String, Object>> media = mediaAttributeDao.selectMediaByGroupId(param22);
		//准备给每个终端发送信息   因为一个节目可以在不同分组中  所以每个终端机的节目单信息也不会相同  需要给每个终端重新组装节目单  并发送出去
		List<ProgramMsg> emptyarray =  new ArrayList<ProgramMsg>();
		ProtocolModel protocolModel = new ProtocolModel();
		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d1 = new Date();
		//List<PublishTaskDetailModel> ptdList = new ArrayList<PublishTaskDetailModel>();
		for (Map<String, Object> map : media) {
			String machineCode = map.get("machineCode")==null?"":map.get("machineCode").toString();
			String mediaGroupId = map.get("mediaGroupId")==null?"":map.get("mediaGroupId").toString();
			if(StringUtils.isNotBlank(machineCode)){
				//组装消息协议
				protocolModel.ServerToClient(MessageEvent.PUBLISH.event,
						programMsglistmap.get(mediaGroupId) == null ? emptyarray : programMsglistmap.get(mediaGroupId));
				protocolModel.setToClient(machineCode);
				JSONObject json = JSONObject.fromObject(protocolModel);
				if(pTaskId != null && "batch".equals(SYS_PUBLISH_MODEL)){//分批发布
					//分批发布详细
					/*PublishTaskDetailModel publishTaskDetailModel = new PublishTaskDetailModel();
					publishTaskDetailModel.setMachineCode(machineCode);
					publishTaskDetailModel.setpTaskId(pTaskId);
					publishTaskDetailModel.setPublishStatus(0);
					publishTaskDetailModel.setProgramJson(json.toString());
					publishTaskDetailModel.setCreateTime(d1);
					publishTaskDetailModel.setSendTime(0);
					ptdList.add(publishTaskDetailModel);*/
				}else{
					log.debug("发布消息："+json);
					fBTopicSender.sendMsg(json.toString(), machineCode);
				}

			}else{
				log.error("机器码为空，不发送指令");
			}
		}

	/*	if(pTaskId != null && "batch".equals(SYS_PUBLISH_MODEL) && ptdList.size() > 0){
			publishTaskDetailDao.batchInsert(ptdList);
		}else{
			try {
				fBTopicSender.session.commit();
			} catch (Exception e) {
				log.error("发布最新的节目单的指令失败！", e);
			}
		}*/
		try {
			fBTopicSender.session.commit();
		} catch (Exception e) {
			log.error("发布最新的节目单的指令失败！", e);
		}
		Date d2 = new Date();
		log.info("发送最新的节目单的指令"+media.size()+"个指令结束,开始时间"+sdf3.format(d1)+",结束时间"+sdf3.format(d2)+",耗时"+(d2.getTime()-d1.getTime())/1000+"秒>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		//计入用户日志
		redisHandle.set("refresh_iot", d2.getTime());

		media = null;
		programMsglistmap = null;
		//ptdList = null;
	}

	@Override
	public void progDownForProg(Integer programId, Integer[] mediaGroupIds) throws Exception {

		//针对一个节目在多个终端组下刊的情况
		//删除多个终端组上该节目的发布数据
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("programId", programId);
		param.put("mediaGroupIds", mediaGroupIds);
		//清理时段设置表
		playTimeSetDao.progDownForProgAfterDetele(param);
		programPublishRecordDao.progDownForProg(param);

		//给每个终端组下的所有媒体机发送  最新的节目单
		//暂时处理  循环终端组id  获取每个终端组的节目
		Map<String,List<ProgramMsg>> programMsglistmap = new HashMap<String,List<ProgramMsg>>();
		for (int i = 0; i < mediaGroupIds.length; i++) {
			List<ProgramMsg> programMsglist = programService.selectProgByGroupId(mediaGroupIds[i]);
			if(programMsglist.size() > 0){
				programMsglistmap.put(mediaGroupIds[i].toString(), programMsglist);
			}
		}
		//查询终端组下的所有终端
		Map<String, Object> param22 = new HashMap<String, Object>();
		param22.put("mediaGroupIds", mediaGroupIds);
		List<Map<String, Object>> media = mediaAttributeDao.selectMediaByGroupId(param22);
		//准备给每个终端发送信息   因为一个节目可以在不同分组中  所以每个终端机的节目单信息也不会相同  需要给每个终端重新组装节目单  并发送出去
		List<ProgramMsg> emptyarray =  new ArrayList<ProgramMsg>();
		ProtocolModel protocolModel = new ProtocolModel();
		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d1 = new Date();
		for (Map<String, Object> map : media) {
			String machineCode = map.get("machineCode")==null?"":map.get("machineCode").toString();
			String mediaGroupId = map.get("mediaGroupId")==null?"":map.get("mediaGroupId").toString();
			if(StringUtils.isNotBlank(machineCode)){
				//组装消息协议
				protocolModel.ServerToClient(MessageEvent.PROGRAMDOWN.event,
						programMsglistmap.get(mediaGroupId) == null ? emptyarray : programMsglistmap.get(mediaGroupId));
				protocolModel.setToClient(machineCode);
				JSONObject json = JSONObject.fromObject(protocolModel);
				log.debug("发布消息："+json);
				fBTopicSender.sendMsg(json.toString(), machineCode);
			}else{
				log.error("机器码为空，不发送指令");
			}
		}
		try {
			fBTopicSender.session.commit();
		} catch (Exception e) {
			log.error("提交一个节目在多个组上下刊的指令事务失败！", e);
		}

		Date d2 = new Date();
		log.info("发送一个节目在多个组上下刊的指令"+media.size()+"个指令结束,开始时间"+sdf3.format(d1)+",结束时间"+sdf3.format(d2)+",耗时"+(d2.getTime()-d1.getTime())/1000+"秒>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		media = null;
		programMsglistmap = null;
	}

	@Override
	public void progDownForGroup(Integer mediaGroupId, Integer[] pubIds) throws Exception {

		//针对在一个终端组上下刊多个节目的情况
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("mediaGroupId", mediaGroupId);
		param.put("pubIds", pubIds);
		//删除发布数据
		programPublishRecordDao.progDownForGroup(param);
		//清理时段设置表
		playTimeSetDao.progDownForGroupAfterDetele(param);

		//暂时处理  循环终端组id  获取每个终端组的节目
		List<ProgramMsg> programMsglist = programService.selectProgByGroupId(mediaGroupId);
		programMsglist = programMsglist==null?new ArrayList<ProgramMsg>():programMsglist;

		//查询终端组下的所有终端
		Map<String, Object> param22 = new HashMap<String, Object>();
		param22.put("mediaGroupIds", new Integer[]{mediaGroupId});
		List<Map<String, Object>> media = mediaAttributeDao.selectMediaByGroupId(param22);
		//发送下刊指令    因为是一个终端组  所以终端的节目单是一样的
		ProtocolModel protocolModel = new ProtocolModel();
		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d1 = new Date();
		for (Map<String, Object> map : media) {
			String machineCode = map.get("machineCode")==null?"":map.get("machineCode").toString();

			if(StringUtils.isNotBlank(machineCode)){
				//组装消息协议
				protocolModel.ServerToClient(MessageEvent.PROGRAMDOWN.event, programMsglist);
				protocolModel.setToClient(machineCode);
				JSONObject json = JSONObject.fromObject(protocolModel);
				log.debug("发布消息："+json);
				fBTopicSender.sendMsg(json.toString(), machineCode);
			}else{
				log.error("机器码为空，不发送指令");
			}
		}
		try {
			fBTopicSender.session.commit();
		} catch (Exception e) {
			log.error("提交一个组上下刊多个节目后的指令事务失败！", e);
		}
		Date d2 = new Date();
		log.info("发送一个组上下刊多个节目的指令"+media.size()+"个指令结束,开始时间"+sdf3.format(d1)+",结束时间"+sdf3.format(d2)+",耗时"+(d2.getTime()-d1.getTime())/1000+"秒>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		media = null;
		programMsglist = null;
	}

	@Override
	public List<Map<String, Object>> selectPageGroupByProg2(Integer programId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("programId", programId);
		return programPublishRecordDao.selectGroupByProg2(param);
	}

	@Override
	public void syncProg(Integer[] programIds) {

	}

	@Override
	public List<Map<String, Object>> selectPublishByMaterialId(Integer[] materialIds) {
		return programPublishRecordDao.selectPublishByMaterialId(materialIds);
	}

	@Override
	public void updateProgramOrderByKey(String publishIdAndOrder,String publishIdAndOrder2,Integer mediaGroupId,Integer direction) throws Exception{
		ProgramPublishRecordModel programPublishRecordModel = new ProgramPublishRecordModel();
		if(StringUtils.isNotBlank(publishIdAndOrder2)){//如果不是空值 说明是当页的上下移 而不是夸分页的上下移

			Integer publisherId2 = Integer.parseInt(publishIdAndOrder2.split("#")[0]);
			if(StringUtils.isNotBlank(publishIdAndOrder2.split("#")[1])){
				//交换排序
				Integer programOrder2 = Integer.parseInt(publishIdAndOrder2.split("#")[1]);
				Integer publisherId1 = Integer.parseInt(publishIdAndOrder.split("#")[0]);
				Integer programOrder1 = Integer.parseInt(publishIdAndOrder.split("#")[1]);
				programPublishRecordModel.setProgramOrder(programOrder2);
				programPublishRecordModel.setPublishId(publisherId1);
				programPublishRecordDao.updateProgramOrderByKey(programPublishRecordModel);

				programPublishRecordModel.setProgramOrder(programOrder1);
				programPublishRecordModel.setPublishId(publisherId2);
				programPublishRecordDao.updateProgramOrderByKey(programPublishRecordModel);
			}else{
				//排序为空时 不修改
				log.error("更新节目排序时id["+publisherId2+"]的排序值是空值");
			}
		}else{
			//夸分页上下移
			//根据组id  按排序查询组的前100的节目 一般一个组的节目不会超过100个
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("mediaGroupIds", new Integer[]{mediaGroupId});
			param.put("startRow", 0);
		    param.put("pageSize", 100);
		    List<Map<String,Object>> list = programPublishRecordDao.selectProgramByGroupId(param);
		    if(list.size() > 0){
		    	for (int i = 0; i < list.size(); i++) {
		    		String publishId = list.get(i).get("publishId").toString();
		    		String publishId1 = publishIdAndOrder.split("#")[0];
					if(publishId1.equals(publishId)){
						Integer programOrder1 = Integer.parseInt(publishIdAndOrder.split("#")[1]);
						if(direction.intValue() == 1){//向上移动
							if(i-1 < 0){
								throw new Exception("已经是排序最前的节目");
							}else{
								Integer publishId2 = Integer.parseInt(list.get(i-1).get("publishId").toString());
								Integer programOrder2 = list.get(i-1).get("programOrder")==null?null:Integer.parseInt(list.get(i-1).get("programOrder").toString());
								programPublishRecordModel.setProgramOrder(programOrder2);
								programPublishRecordModel.setPublishId(Integer.parseInt(publishId1));
								programPublishRecordDao.updateProgramOrderByKey(programPublishRecordModel);

								programPublishRecordModel.setProgramOrder(programOrder1);
								programPublishRecordModel.setPublishId(publishId2);
								programPublishRecordDao.updateProgramOrderByKey(programPublishRecordModel);
							}
						}
						if(direction.intValue() == 2){//向下移动
							if(i+1 > list.size()-1){
								throw new Exception("已经是排序最后的节目");
							}else{
								Integer publishId2 = Integer.parseInt(list.get(i+1).get("publishId").toString());
								Integer programOrder2 = list.get(i+1).get("programOrder")==null?null:Integer.parseInt(list.get(i+1).get("programOrder").toString());
								programPublishRecordModel.setProgramOrder(programOrder2);
								programPublishRecordModel.setPublishId(Integer.parseInt(publishId1));
								programPublishRecordDao.updateProgramOrderByKey(programPublishRecordModel);

								programPublishRecordModel.setProgramOrder(programOrder1);
								programPublishRecordModel.setPublishId(publishId2);
								programPublishRecordDao.updateProgramOrderByKey(programPublishRecordModel);
							}
						}
						break;
					}
				}
		    }

		}
	}

	@Override
	public List<Map<String, Object>> selectConflictTime(Map<String, Object> param) {
		return playTimeSetDao.selectConflictTime(param);
	}

	@Override
	public Map<String,Object> selectMaxPlayTime(Integer[] mediaGroupIds) {
		return programPublishRecordDao.selectMaxPlayTime(mediaGroupIds);
	}

	@Override
	public List<Map<String, Object>> checkFirstProgByMediaGroupId(Integer[] mediaGroupIds) {
		return programPublishRecordDao.checkFirstProgByMediaGroupId(mediaGroupIds);
	}


}
