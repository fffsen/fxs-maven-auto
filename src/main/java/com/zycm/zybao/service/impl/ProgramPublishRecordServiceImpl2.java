package com.zycm.zybao.service.impl;

import com.zycm.zybao.amq.topic.producer.FBTopicSender;
import com.zycm.zybao.common.config.SysConfig;
import com.zycm.zybao.common.enums.MessageEvent;
import com.zycm.zybao.common.redis.RedisHandle;
import com.zycm.zybao.dao.MediaAttributeDao;
import com.zycm.zybao.dao.PlayTimeSetDao;
import com.zycm.zybao.dao.ProgramDao;
import com.zycm.zybao.dao.ProgramPublishRecordDao;
import com.zycm.zybao.model.mqmodel.ProtocolModel;
import com.zycm.zybao.model.mqmodel.request.publish.ProgramMsg;
import com.zycm.zybao.service.interfaces.ProgramPublishRecordService2;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service("programPublishRecordService2")
public class ProgramPublishRecordServiceImpl2 implements ProgramPublishRecordService2 {

	public static final String SYS_PUBLISH_MODEL = SysConfig.sysPublishModel;

	@Autowired(required = false)
	private ProgramPublishRecordDao programPublishRecordDao;
	@Autowired(required = false)
	private MediaAttributeDao mediaAttributeDao;
	@Autowired(required = false)
	private PlayTimeSetDao playTimeSetDao;
	@Autowired(required = false)
	private RedisHandle redisHandle;
	@Autowired(required = false)
	private FBTopicSender fBTopicSender;
	@Autowired(required = false)
	private ProgramDao programDao;


	@Override
	public void publishProgByGroup(Integer[] mediaGroupIds,Integer pTaskId) {
		//给每个终端组下的所有媒体机发送  最新的节目单
		//暂时处理  循环终端组id  获取每个终端组的节目
		Map<String,List<ProgramMsg>> programMsglistmap = new HashMap<String,List<ProgramMsg>>();
		List<ProgramMsg> programMsglist = new ArrayList<ProgramMsg>();
		for (int i = 0; i < mediaGroupIds.length; i++) {
			//先判断是不是同步组
			/*Map<String,Object> masterMedia = syncGroupDetailsDao.selectMasterByGroupId(mediaGroupIds[i]);
			if(masterMedia != null){//发布节目时同步组第一次是不发布节目单指令的  只标记变动行为等定时任务来触发 因为就算这里发送节目单也是老的节目单
				Object programMsgObj = redisHandle.get(Constants.REDIS_SYNC_MASTER_PROGRAM_PREFIX+mediaGroupIds[i]);
				if(programMsgObj != null){
					ProgramMsg programMsg= (ProgramMsg)JSONObject.toBean(JSONObject.fromObject(programMsgObj), ProgramMsg.class);
					programMsglist.add(programMsg);
				}else{//第一次不发送节目单指令 等定时任务触发
					log.info("同步组【"+mediaGroupIds[i]+"】节目单还未生成");
				}
			}else{
				programMsglist = programDao.selectProgByGroupId(mediaGroupIds[i]);
			}*/
			programMsglist = programDao.selectProgByGroupId(mediaGroupIds[i]);
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
			List<ProgramMsg> programMsglist = programDao.selectProgByGroupId(mediaGroupIds[i]);
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
	public List<Map<String, Object>> selectPageGroupByProg2(Integer programId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("programId", programId);
		return programPublishRecordDao.selectGroupByProg2(param);
	}



}
