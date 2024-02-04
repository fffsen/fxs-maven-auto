package com.zycm.zybao.service.impl;

import com.zycm.zybao.amq.handler.receivehandler.BaseClientConfReceiveHandler;
import com.zycm.zybao.amq.handler.receivehandler.ClientFileReceiveHandler;
import com.zycm.zybao.amq.topic.producer.FBTopicSender;
import com.zycm.zybao.common.config.FtpConfig;
import com.zycm.zybao.common.config.RedisConfig;
import com.zycm.zybao.common.config.SysConfig;
import com.zycm.zybao.common.enums.MessageEvent;
import com.zycm.zybao.common.redis.RedisHandle;
import com.zycm.zybao.common.utils.IdGenerateUtil;
import com.zycm.zybao.common.utils.IotUtil;
import com.zycm.zybao.common.utils.NumUtil;
import com.zycm.zybao.dao.*;
import com.zycm.zybao.model.entity.IotMediaAttributeModel;
import com.zycm.zybao.model.entity.MediaAttributeModel;
import com.zycm.zybao.model.entity.MediaGroupRelationModel;
import com.zycm.zybao.model.mqmodel.ProtocolModel;
import com.zycm.zybao.model.mqmodel.request.clientconf.ClientConf;
import com.zycm.zybao.model.mqmodel.request.clientfile.ClientFile;
import com.zycm.zybao.model.mqmodel.request.deletefiie.DeleteExpireFile;
import com.zycm.zybao.model.mqmodel.request.deletefiie.DeleteFile;
import com.zycm.zybao.model.mqmodel.request.light.Light;
import com.zycm.zybao.model.mqmodel.request.screenshot.Screenshot;
import com.zycm.zybao.model.mqmodel.request.syncdate.SyncDate;
import com.zycm.zybao.model.mqmodel.request.upgrade.Upgrade;
import com.zycm.zybao.model.mqmodel.respose.clientfile.ClientFileList;
import com.zycm.zybao.model.vo.MediaAttributeVo;
import com.zycm.zybao.service.interfaces.MediaAttributeService;
import com.zycm.zybao.service.interfaces.ProgramPublishRecordService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;


@Slf4j
@Service("mediaAttributeService")
public class MediaAttributeServiceImpl implements MediaAttributeService {

	@Autowired(required = false)
	private MediaAttributeDao mediaAttributeDao;
	@Autowired(required = false)
	private MediaGroupRelationDao mediaGroupRelationDao;
	@Autowired(required = false)
	private ProgramMaterialDao programMaterialDao;
	@Autowired(required = false)
	private ProgramPublishRecordService programPublishRecordService;
	@Autowired(required = false)
	private RedisHandle redisHandle;
	@Autowired(required = false)
	private FBTopicSender fBTopicSender;
	@Autowired(required = false)
	private FtpInfoDao ftpInfoDao;
	@Autowired(required = false)
	private IotMediaAttributeDao iotMediaAttributeDao;


	private String redis_prefix = RedisConfig.redisPrefix;

	@Override
	public void updateIsDelete(Integer[] mids) {
		mediaAttributeDao.deleteByMids(mids);
		//删除与终端组的关系数据
		mediaGroupRelationDao.deleteByMid(mids);
	}

	@Override
	public void addMedia(Integer media_group_id, Integer num, Integer start) throws Exception{
		String[] clientNumberArray = generateNo(start, num, 8);
		List<Map<String,Object>> clientList = mediaAttributeDao.selectByClientNumber(clientNumberArray);
		if(clientList.size() > 0){
			Map<String,Object> media = mediaAttributeDao.selectMaxNumber("ZYCM");
			if(null != media){
				String max = media.get("clientNumber")==null?"ZYCM00000000":media.get("clientNumber").toString();
				throw new Exception("已有"+clientList.size()+"个终端编号已被使用，重复的编号中最大编号为"+clientList.get(0).get("clientNumber").toString()+",最大编号为"+max);
			}else{
				throw new Exception("已有"+clientList.size()+"个终端编号已被使用，重复的编号中最大编号为"+clientList.get(0).get("clientNumber").toString());
			}
		}
		List<MediaAttributeModel> mediaList = new ArrayList<MediaAttributeModel>();
		Date date = new Date();
		for (int i = 0; i < num; i++) {
			String machineCode = UUID.randomUUID().toString().replaceAll("-", "");
			MediaAttributeModel ma = new MediaAttributeModel();
			ma.setMachineCode(machineCode);
			ma.setClientNumber(clientNumberArray[i]);
			ma.setClientName(clientNumberArray[i]);
			ma.setCreateTime(date);
			ma.setAdDelete(1);
			ma.setAdStatus(0);
			mediaList.add(ma);
		}
		mediaAttributeDao.batchInsert(mediaList);
		//新建终端组与终端的关系
		List<MediaGroupRelationModel> mediaGroupRelationList = new ArrayList<MediaGroupRelationModel>();
		for (MediaAttributeModel mediaAttributeModel : mediaList) {
			MediaGroupRelationModel mediaGroupRelation = new MediaGroupRelationModel();
			mediaGroupRelation.setMediaGroupId(media_group_id);
			mediaGroupRelation.setMid(mediaAttributeModel.getMid());
			mediaGroupRelationList.add(mediaGroupRelation);
		}
		mediaGroupRelationDao.batchInsert(mediaGroupRelationList);
	}

	private String[] generateNo(Integer start,Integer num,Integer length){
		String[] no = new String[num];

		for (int i = 0; i < num.intValue(); i++) {
			//补0
			Integer startvalue =  start + i;
			Integer addzero = length.intValue() - startvalue.toString().length();
			String addzerostr ="";
			for (int j = 0; j < addzero; j++) {
				addzerostr += "0";
			}
			no[i] = "ZYCM"+addzerostr+startvalue;
		}
		return no;
	}

	@Override
	public void updateGroup(Integer mediaGroupId, Integer[] mids) {
		mediaGroupRelationDao.updateGroup(mediaGroupId, mids);

		//调整分组后   发布最新的节目单给新加进来的终端
		programPublishRecordService.publishProgByGroup(new Integer[]{mediaGroupId});
	}

	@Override
	public Map<String, Object> selectPage(Map<String, Object> map,Integer pageSize,Integer page) {
		Map<String, Object> returndata = new HashMap<String, Object>();
		String totalCount = mediaAttributeDao.selectPageCount(map);
		if(!"0".equals(totalCount)){
			//总页数
	        Double totalPage = Math.ceil(Integer.parseInt(totalCount)/Double.parseDouble(pageSize+""));
	        int startRow = (page-1)*pageSize;
	        map.put("startRow", startRow);
	        map.put("pageSize", pageSize);
	        List<Map<String,Object>> list = mediaAttributeDao.selectPage(map);
	        Integer[] groupIds = new Integer[list.size()];
	        for (int i = 0; i < list.size(); i++) {
	    		//大小转换处理
	    		String diskFreeSpace_kb = list.get(i).get("diskFreeSpace") == null?"":list.get(i).get("diskFreeSpace").toString();
	        	String mediaGroupId = list.get(i).get("mediaGroupId") == null?"":list.get(i).get("mediaGroupId").toString();
	    		if(StringUtils.isNotBlank(diskFreeSpace_kb)){
	    			list.get(i).put("diskFreeSpace",NumUtil.sizeToStr(new BigDecimal(diskFreeSpace_kb)));
	    		}else{
	    			list.get(i).put("diskFreeSpace","");
	    		}
	    		if(StringUtils.isNotBlank(mediaGroupId)){
	    			groupIds[i] = Integer.parseInt(mediaGroupId);
	    		}
			}
	        if(groupIds.length > 0){
	        	 List<Map<String,Object>> groupProgNumlist = mediaAttributeDao.selectGroupProgNum(groupIds);
	 	        if(groupProgNumlist.size() > 0){
	 	        	 for (int i = 0; i < list.size(); i++) {
	 	        		 String mediaGroupId = list.get(i).get("mediaGroupId") == null?"":list.get(i).get("mediaGroupId").toString();
	 	        		 if(StringUtils.isNotBlank(mediaGroupId)){
	 	        			for (int j = 0; j < groupProgNumlist.size(); j++) {
	 	 	 					if(mediaGroupId.equals(groupProgNumlist.get(j).get("mediaGroupId").toString())){
	 	 	 						list.get(i).put("programNum",Integer.parseInt(groupProgNumlist.get(j).get("programNum").toString()));
	 	 	 					}
	 	 	 				}
	 	 	    		 }else{
	 	 	    			list.get(i).put("programNum",0);
	 	 	    		 }
	 	 			}
	 	        }else{
	 	        	 for (int i = 0; i < list.size(); i++) {
	 	        		 list.get(i).put("programNum",0);
	 	 			}
	 	        }
	        }else{
	        	 for (int i = 0; i < list.size(); i++) {
 	        		 list.get(i).put("programNum",0);
 	 			}
	        }


	        returndata.put("dataList", list);
	        returndata.put("totalPage", totalPage);

		}else{
			returndata.put("dataList", new ArrayList());
	        returndata.put("totalPage", 0);
		}
		//统计
		List<Map<String, Object>> onlineStatistics = mediaAttributeDao.getOnlineStatistics(map);
		int tt = 0;
		if(onlineStatistics != null && onlineStatistics.size() > 0){
			returndata.put("onClient", "0");
			returndata.put("offClient", "0");
			returndata.put("exceptionClient", "0");
			returndata.put("invalidClient", "0");
			for (Map<String, Object> map2 : onlineStatistics) {
				if(map2.get("adStatus") == null || "".equals(map2.get("adStatus").toString())){
					tt += Integer.parseInt(map2.get("num").toString());
				}else{
					int  num = map2.get("num")!=null?Integer.parseInt(map2.get("num").toString()):0;
					if("-2".equals(map2.get("adStatus").toString())){
						returndata.put("exceptionClient", num);
					}else if("-1".equals(map2.get("adStatus").toString())){
						returndata.put("invalidClient", num);
					}else if("0".equals(map2.get("adStatus").toString())){
						returndata.put("offClient", num);
					}else if("1".equals(map2.get("adStatus").toString())){
						returndata.put("onClient", num);
					}
					tt += num;
				}
			}
			returndata.put("totalClient", tt);
		}else{
			returndata.put("totalClient", 0);
			returndata.put("onClient", 0);
			returndata.put("offClient", 0);
			returndata.put("exceptionClient", 0);
			returndata.put("invalidClient", 0);
		}
		returndata.put("page", page);
		returndata.put("pageSize", pageSize);
		returndata.put("total", Integer.parseInt(totalCount));

		return returndata;

	}

	@Override
	public Map<String, Object> selectPageMediaProgNumZero(Map<String, Object> param, Integer pageSize,Integer page) {
		Map<String, Object> returndata = new HashMap<String, Object>();
		Integer totalCount = mediaAttributeDao.selectPageMediaProgNumZeroCount(param);
		if(null != totalCount && totalCount > 0){
			//总页数
	        Double totalPage = Math.ceil(totalCount/Double.parseDouble(pageSize+""));
	        int startRow = (page-1)*pageSize;
	        param.put("startRow", startRow);
	        param.put("pageSize", pageSize);
	        List<Map<String,Object>> list = mediaAttributeDao.selectPageMediaProgNumZero(param);

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
	public Map<String, Object> selectByPrimaryKey(Integer mid) {
		List<Map<String, Object>> list = mediaAttributeDao.selectByPrimaryKeys(new Integer[]{mid});
		if(list.size() > 0){
			Map<String, Object> map2 =  list.get(0);
			//大小转换处理
    		String diskFreeSpace_kb = map2.get("diskFreeSpace") == null?"":map2.get("diskFreeSpace").toString();
			String diskSpace_kb = map2.get("diskSpace") == null?"":map2.get("diskSpace").toString();
			Integer mediaGroupId = map2.get("mediaGroupId") == null?null:Integer.parseInt(map2.get("mediaGroupId").toString());
    		if(StringUtils.isNotBlank(diskFreeSpace_kb)){
    			map2.put("diskFreeSpace",NumUtil.sizeToStr(new BigDecimal(diskFreeSpace_kb)));
    		}else{
    			map2.put("diskFreeSpace","");
    		}
    		if(StringUtils.isNotBlank(diskSpace_kb)){
    			map2.put("diskSpace",NumUtil.sizeToStr(new BigDecimal(diskSpace_kb)));
    		}else{
    			map2.put("diskSpace","");
    		}

    		if(null != mediaGroupId){
    			List<Map<String,Object>> groupProgNumlist = mediaAttributeDao.selectGroupProgNum(new Integer[]{mediaGroupId});
     	        if(groupProgNumlist.size() > 0){
     	        	if(mediaGroupId.toString().equals(groupProgNumlist.get(0).get("mediaGroupId").toString())){
 						map2.put("programNum",Integer.parseInt(groupProgNumlist.get(0).get("programNum").toString()));
     	        	}
     	        }else{
     	        	map2.put("programNum",0);
     	        }
    		}else{
    			map2.put("programNum",0);
    		}

    		return map2;
		}else{
			return null;
		}

	}

	@Override
	public void updateMedia(MediaAttributeVo mediaAttributeVo) {
		List<Map<String, Object>> oldMediaList = mediaAttributeDao.selectByPrimaryKeys(new Integer[]{mediaAttributeVo.getMid()});
		//修改信息
		mediaAttributeDao.updateMedia(mediaAttributeVo.toModel());
		//发送最新配置给终端
		try {
			Map<String,Object> baseMediaAttribute = mediaAttributeDao.selectAllConfByMachineCode(mediaAttributeVo.getMachineCode());
			if(null != baseMediaAttribute){
				ClientConf clientConf = new ClientConf();
				clientConf.setFtpId(Integer.parseInt(baseMediaAttribute.get("ftpId").toString()));
				clientConf.setFtpIp(baseMediaAttribute.get("ftpIp").toString());
				clientConf.setFtpPort(Integer.parseInt(baseMediaAttribute.get("ftpPort").toString()));
				clientConf.setFtpUser(baseMediaAttribute.get("ftpUser").toString());
				clientConf.setFtpPwd(baseMediaAttribute.get("ftpPwd").toString());
				clientConf.setLight(Integer.parseInt(baseMediaAttribute.get("light").toString()));
				clientConf.setVoice(Integer.parseInt(baseMediaAttribute.get("voice").toString()));
				clientConf.setBucketName(baseMediaAttribute.get("bucketName")!=null?baseMediaAttribute.get("bucketName").toString():"");
				clientConf.setFtpType(Integer.parseInt(baseMediaAttribute.get("ftpType").toString()));
				clientConf.setOpenHttp(Integer.parseInt(baseMediaAttribute.get("openHttp").toString()));
				String previewPathPrefix = baseMediaAttribute.get("previewPath").toString();
				String httpUrlPrefix = (previewPathPrefix.contains("http")?previewPathPrefix:"http://"+previewPathPrefix)+":"+baseMediaAttribute.get("previewPort").toString();
				clientConf.setHttpUrlPrefix(httpUrlPrefix);
				clientConf.setOssUploadUrlPrefix(BaseClientConfReceiveHandler.IMG_SERVER_PATH);

				ProtocolModel protocolModel = new ProtocolModel();
				protocolModel.ServerToClient(MessageEvent.BASECLIENTCONF.event, clientConf);
				protocolModel.setToClient(mediaAttributeVo.getMachineCode());
				JSONObject json = JSONObject.fromObject(protocolModel);
				log.debug("发送给终端基本配置信息："+json);
				fBTopicSender.sendSingleMsg(json.toString(), mediaAttributeVo.getMachineCode());
			}else{
				log.error("机器码【"+mediaAttributeVo.getMachineCode()+"】在查询基本配置的结果为空！");
			}

			//判断是否需修改亮度
			if(oldMediaList.size() == 1){
				Integer old_light = Integer.parseInt(oldMediaList.get(0).get("light").toString());
				Integer new_light = Integer.parseInt(baseMediaAttribute.get("light").toString());
				if(old_light.intValue() != new_light.intValue()){//调整亮度
					Light light = new Light();
					light.setMachineCode(mediaAttributeVo.getMachineCode());
					light.setLightRatio(mediaAttributeVo.getLight());

					ProtocolModel protocolModel = new ProtocolModel();
					protocolModel.ServerToClient(MessageEvent.LIGHTSET.event, light);
					protocolModel.setToClient(mediaAttributeVo.getMachineCode());
					JSONObject json = JSONObject.fromObject(protocolModel);
					log.debug("发送亮度变更指令："+json);
					fBTopicSender.sendSingleMsg(json.toString(), mediaAttributeVo.getMachineCode());
				}
			}
		} catch (Exception e) {
			log.error("发送最新终端基本配置信息失败！", e);
		}


	}

	@Override
	public void mediaUpgrade(String[] machineCodes, Integer materialId) throws Exception{
		Map<String, Object> material = programMaterialDao.selectByPrimaryKey(materialId);
		if(null != material){
			Upgrade upgrade = new Upgrade();
			upgrade.setMaterialId(materialId);
			upgrade.setMaterialName(material.get("materialName").toString());
			String materialPath = material.get("materialPath").toString();
			if(StringUtils.isNotBlank(materialPath)){
				if("oss".equals(SysConfig.sysFtpModel)){
					materialPath = materialPath.substring(1);
				}
			}
			upgrade.setMaterialPath(materialPath);

			upgrade.setSize(new BigDecimal(material.get("size").toString()));
			upgrade.setType(Integer.parseInt(material.get("type").toString()));
			upgrade.setAppVersion(material.get("apkVersion").toString());
			if(StringUtils.isNotBlank(upgrade.getMaterialName()) && upgrade.getMaterialName().contains("PsClient")){
				upgrade.setIsSave(1);
			}else{
				upgrade.setIsSave(0);
			}
			ProtocolModel protocolModel = new ProtocolModel();
			protocolModel.ServerToClient(MessageEvent.UPGRADE.event, upgrade);
			JSONObject json = JSONObject.fromObject(protocolModel);


			for (int i = 0; i < machineCodes.length; i++) {
				log.info("更新指令："+json.toString());
				fBTopicSender.sendMsg(json.toString(), machineCodes[i]);
			}
			try {
				fBTopicSender.session.commit();
			} catch (Exception e) {
				log.error("提交升级终端指令事务失败！", e);
			}
		}else{
			throw new Exception("根据素材id【"+materialId+"】查询不到安装包信息");
		}


	}

	@Override
	public List<Map<String, Object>> screenshot(Integer[] mids) {
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		List<Map<String,Object>> mediaList = mediaAttributeDao.selectByPrimaryKeys(mids);
		if(mediaList.size() > 0){
			long date_id = IdGenerateUtil.GenerateByTime();
			String ssExpression = FtpConfig.imgServerScreenshotExpression;
			ProtocolModel protocolModel = new ProtocolModel();
			Screenshot screenshot = new Screenshot();
			if("oss".equals(SysConfig.sysFtpModel)){
				screenshot.setFtpPath("screenshot/");
			}else{
				screenshot.setFtpPath("/screenshot/");
			}
			for (int i = 0; i < mediaList.size(); i++) {
				//发送指令
				String machineCode = mediaList.get(i).get("machineCode")==null?"":mediaList.get(i).get("machineCode").toString();
				String previewPath = mediaList.get(i).get("previewPath")==null?"":mediaList.get(i).get("previewPath").toString();
				String previewPort = mediaList.get(i).get("previewPort")==null?"":mediaList.get(i).get("previewPort").toString();
				String pic_name = mids[i]+"_"+date_id+".jpg";
				if(StringUtils.isNotBlank(machineCode)){
					//组装消息协议
					screenshot.setShotName(pic_name);
					protocolModel.ServerToClient(MessageEvent.SCREENSHOT.event, screenshot);
					protocolModel.setToClient(machineCode);
					JSONObject json = JSONObject.fromObject(protocolModel);
					log.debug("发布消息："+json);
					fBTopicSender.sendSingleMsg(json.toString(), machineCode);

				}else{
					log.error("机器码为空，不发送指令");
				}

				//封装数据
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("mid", mediaList.get(i).get("mid").toString());
				map.put("clientName", mediaList.get(i).get("clientName").toString());
				if("oss".equals(SysConfig.sysFtpModel)){
					map.put("path", previewPath+"/screenshot/"+machineCode+"/"+pic_name);
				}else{
					map.put("path", ssExpression.replace("*", previewPath)+":"+previewPort+"/screenshot/"+machineCode+"/"+pic_name);
				}
				returnList.add(map);
			}
		}else{
			log.error("多个终端id查询的结果为空");
		}

		return returnList;
	}

	@Override
	public Map<String,Object> getClientFile(String machineCode) {
		//根据机器码发送请求指令
		ProtocolModel protocolModel = new ProtocolModel();
		ClientFile clientFile = new ClientFile();
		clientFile.setMachineCode(machineCode);
		protocolModel.ServerToClient(MessageEvent.CLIENTFILE.event, clientFile);
		protocolModel.setToClient(machineCode);
		JSONObject json = JSONObject.fromObject(protocolModel);
		log.debug("发布消息："+json);
		fBTopicSender.sendSingleMsg(json.toString(), machineCode);

		//获取接收端的信息  获取对应的终端文件信息
		List<ClientFileList> list = new ArrayList<ClientFileList>();
		try {
			Thread.sleep(1000);
			boolean isreturninfo = ClientFileReceiveHandler.clientFileConcurrentHashMap.containsKey(machineCode);
			if(isreturninfo){
				list = ClientFileReceiveHandler.clientFileConcurrentHashMap.get(machineCode);
				//消费消息后 清理消息
				ClientFileReceiveHandler.clientFileConcurrentHashMap.remove(machineCode);
			}else{
				Thread.sleep(4000);
				boolean isreturninfo2 = ClientFileReceiveHandler.clientFileConcurrentHashMap.containsKey(machineCode);
				if(isreturninfo2){
					list = ClientFileReceiveHandler.clientFileConcurrentHashMap.get(machineCode);
					//消费消息后 清理消息
					ClientFileReceiveHandler.clientFileConcurrentHashMap.remove(machineCode);
				}
			}
		} catch (InterruptedException e) {
			log.error("获取终端文件的接收信息异常", e);
		}

		Map<String,Object> returnmap = new HashMap<String,Object>();
		returnmap.put("list", list);

		return returnmap;
	}

	@Override
	public void deleteClientFile(String machineCode, String[] fileName) {
		//根据机器码发送请求指令
		ProtocolModel protocolModel = new ProtocolModel();
		List<DeleteFile> deleteFileList = new ArrayList<DeleteFile>();
		for (int i = 0; i < fileName.length; i++) {
			DeleteFile deleteFile = new DeleteFile();
			deleteFile.setMaterialName(fileName[i]);
			deleteFileList.add(deleteFile);
		}

		protocolModel.ServerToClient(MessageEvent.DELETEFILE.event, deleteFileList);
		protocolModel.setToClient(machineCode);
		JSONObject json = JSONObject.fromObject(protocolModel);
		log.debug("发布消息："+json);
		fBTopicSender.sendSingleMsg(json.toString(), machineCode);
	}

	@Override
	public void deleteExpireClientFile(String[] machineCodes) {
		//根据机器码发送请求指令
		ProtocolModel protocolModel = new ProtocolModel();

		for (String machineCode : machineCodes) {
			if(StringUtils.isNotBlank(machineCode)){
				DeleteExpireFile deleteExpireFile = new DeleteExpireFile();
				deleteExpireFile.setMachineCode(machineCode);
				deleteExpireFile.setMode(0);

				protocolModel.ServerToClient(MessageEvent.DELETEEXPIREFILE.event, deleteExpireFile);
				protocolModel.setToClient(machineCode);
				JSONObject json = JSONObject.fromObject(protocolModel);
				log.debug("删除全部过期文件指令："+json);
				fBTopicSender.sendMsg(json.toString(), machineCode);
			}else{
				log.error("机器码为空，不发送指令");
			}
		}
		try {
			fBTopicSender.session.commit();
		} catch (Exception e) {
			log.error("提交发布指令事务失败！", e);
		}

	}

	@Override
	public void clientRestart(String[] machineCode) {
		//根据机器码发送请求指令
		ProtocolModel protocolModel = new ProtocolModel();
		protocolModel.ServerToClient(MessageEvent.RESTART.event, null);

		for (int i = 0; i < machineCode.length; i++) {
			protocolModel.setToClient(machineCode[i]);
			JSONObject json = JSONObject.fromObject(protocolModel);
			log.debug("发布消息："+json);
			fBTopicSender.sendMsg(json.toString(), machineCode[i]);
		}
		try {
			fBTopicSender.session.commit();
		} catch (Exception e) {
			log.error("提交重启指令事务失败！", e);
		}
	}

	@Override
	public void updateAdStatus(String machineCode,Integer adStatus) {
		MediaAttributeModel mediaAttributeModel = new MediaAttributeModel();
		mediaAttributeModel.setAdStatus(adStatus);
		mediaAttributeModel.setMachineCode(machineCode);
		mediaAttributeDao.updateAdStatus(mediaAttributeModel);
	}

	@Override
	public void batchInsert(List<MediaAttributeModel> list) {
		mediaAttributeDao.batchInsert(list);
	}

	@Override
	public List<Map<String, Object>> selectMediaProgram(Integer[] mids, String clientName) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("mids", mids);
		param.put("clientName", clientName);
		return mediaAttributeDao.selectMediaProgram(param);
	}

	@Override
	public Map<String, Object> selectPageByPrimaryKeys(Map<String, Object> map, Integer pageSize, Integer page) {
		Map<String, Object> returndata = new HashMap<String, Object>();
		Integer totalCount = mediaAttributeDao.selectPageByPrimaryKeysCount(map);
		if(null != totalCount && totalCount > 0){
			//总页数
	        Double totalPage = Math.ceil(totalCount/Double.parseDouble(pageSize+""));
	        int startRow = (page-1)*pageSize;
	        map.put("startRow", startRow);
	        map.put("pageSize", pageSize);
	        List<Map<String,Object>> list = mediaAttributeDao.selectPageByPrimaryKeys(map);

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
	public Map<String, Object> selectPageGroupProgNumZero(Map<String, Object> map,Integer pageSize,Integer page) {
		Map<String, Object> returndata = new HashMap<String, Object>();
		Integer totalCount = mediaAttributeDao.selectPageGroupProgNumZeroCount(map);
		if(totalCount != 0){
			//总页数
	        Double totalPage = Math.ceil(totalCount/Double.parseDouble(pageSize+""));
	        int startRow = (page-1)*pageSize;
	        map.put("startRow", startRow);
	        map.put("pageSize", pageSize);
	        List<Map<String,Object>> list = mediaAttributeDao.selectPageGroupProgNumZero(map);

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
	public Map<String, Object> selectPageOfflineMedia(Map<String, Object> map,Integer pageSize,Integer page) {
		Map<String, Object> returndata = new HashMap<String, Object>();
		Integer totalCount = mediaAttributeDao.selectPageOfflineMediaCount(map);
		if(totalCount != 0){
			//总页数
	        Double totalPage = Math.ceil(totalCount/Double.parseDouble(pageSize+""));
	        int startRow = (page-1)*pageSize;
	        map.put("startRow", startRow);
	        map.put("pageSize", pageSize);
	        List<Map<String,Object>> list = mediaAttributeDao.selectPageOfflineMedia(map);

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
	public void activeMedia(String[] machineCodes, Integer status) throws Exception{
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("machineCode", machineCodes);
		param.put("workStatus", status);

		mediaAttributeDao.updateWorkStatus(param);
		if(status.intValue() == 0){// 不激活=禁用
			for (String mc : machineCodes) {
				if(StringUtils.isNotBlank(mc))
				redisHandle.addMap(redis_prefix+"distrust_media", mc, 1);
			}
		}else if(status.intValue() == 1){//激活
			for (String mc : machineCodes) {
				if(StringUtils.isNotBlank(mc))
				redisHandle.removeMapField(redis_prefix+"distrust_media", mc);
			}
		}

	}

	@Override
	public void changeFtp(Integer[] mids,Integer ftpId){
		//修改ftp
		Map<String,Object> param1 = new HashMap<String,Object>();
		param1.put("ftpId", ftpId);
		param1.put("mids", mids);
		mediaAttributeDao.updateFtp(param1);
		//同步修改的ftp信息  发送最新配置给终端
		try {
			List<Map<String,Object>> baseMediaAttribute = mediaAttributeDao.selectByPrimaryKeys(mids);
			if(baseMediaAttribute.size() > 0){
				//FBTopicSender sender= new FBTopicSender();
				ProtocolModel protocolModel = new ProtocolModel();
				SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date d1 = new Date();
				Map<String,Object> param = ftpInfoDao.selectByPrimaryKey(ftpId);

				for (Map<String,Object> map : baseMediaAttribute) {
					String machineCode = map.get("machineCode")==null?"":map.get("machineCode").toString();
					Integer light = map.get("light")==null?50:Integer.parseInt(map.get("light").toString());
					Integer voice = map.get("voice")==null?20:Integer.parseInt(map.get("voice").toString());
					if(StringUtils.isNotBlank(machineCode)){
						//组装消息协议
						ClientConf clientConf = new ClientConf();
						clientConf.setFtpId(Integer.parseInt(param.get("ftpId").toString()));
						clientConf.setFtpIp(param.get("ipAddr").toString());
						clientConf.setFtpPort(Integer.parseInt(param.get("port").toString()));
						clientConf.setFtpUser(param.get("ftpUser").toString());
						clientConf.setFtpPwd(param.get("ftpPwd").toString());
						clientConf.setLight(light);
						clientConf.setVoice(voice);
						clientConf.setBucketName(param.get("bucketName")!=null?param.get("bucketName").toString():"");
						clientConf.setFtpType(Integer.parseInt(param.get("ftpType").toString()));
						clientConf.setOpenHttp(Integer.parseInt(param.get("openHttp").toString()));
						String previewPathPrefix = param.get("previewPath").toString();
						String httpUrlPrefix = (previewPathPrefix.contains("http")?previewPathPrefix:"http://"+previewPathPrefix)+":"+param.get("previewPort").toString();
						clientConf.setHttpUrlPrefix(httpUrlPrefix);
						clientConf.setOssUploadUrlPrefix(BaseClientConfReceiveHandler.IMG_SERVER_PATH);

						protocolModel.ServerToClient(MessageEvent.BASECLIENTCONF.event, clientConf);
						protocolModel.setToClient(machineCode);
						JSONObject json = JSONObject.fromObject(protocolModel);
						log.debug("发送给终端ftp配置信息："+json);
						fBTopicSender.sendMsg(json.toString(), machineCode);
					}else{
						log.error("机器码为空，不发送指令");
					}
				}
				fBTopicSender.session.commit();

				Date d2 = new Date();
				log.info("发送最新的终端ftp的指令"+baseMediaAttribute.size()+"个指令结束,开始时间"+sdf3.format(d1)+",结束时间"+sdf3.format(d2)+",耗时"+(d2.getTime()-d1.getTime())/1000+"秒>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			}
		} catch (Exception e) {
			log.error("发送最新终端ftp配置信息失败！", e);
		}
	}

	@Override
	public void updateIccidAndIccidUpdateType(String machineCode,String iccid,Integer iccidUpdateType) throws Exception{
		if(iccidUpdateType.intValue() == 0){//自动  直接删除旧记录  下次终端反馈会自动记录
			iotMediaAttributeDao.deleteByMachineCode(machineCode);
		}else if(iccidUpdateType.intValue() == 1){//手动
			//判断iccid是否已存在
			Map<String,Object> iotm = iotMediaAttributeDao.selectByIccid(iccid);
			if(iotm != null && !machineCode.equals(iotm.get("machineCode").toString())){
				throw new Exception("iccid["+iccid+"]已存在!");
			}
			//删除旧记录
			iotMediaAttributeDao.deleteByMachineCode(machineCode);
			//新增新纪录
			IotMediaAttributeModel iotMediaAttributeModel = new IotMediaAttributeModel();
			iotMediaAttributeModel.setMachineCode(machineCode);
			iotMediaAttributeModel.setIccid(iccid);
			try {
				iotMediaAttributeModel.setIotType(IotUtil.getIotType(iccid));
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
			iotMediaAttributeModel.setCardStatus(0);
			iotMediaAttributeModel.setCreateTime(new Date());
			iotMediaAttributeModel.setIsMain(1);
			iotMediaAttributeModel.setIccidUpdateType(iccidUpdateType);//0自动 1手动
			iotMediaAttributeDao.insert(iotMediaAttributeModel);
		}else{
			log.error("更新iccid的更新类型未知！");
		}

	}

	@Override
	public List<Map<String, Object>> selectNoticeUserByMachineCodes(Map<String,Object> param) {
		return mediaAttributeDao.selectNoticeUserByMachineCodes(param);
	}

	@Override
	public List<Map<String, Object>> selectGroupIdByMachineCodes(String[] machineCodes) {
		return mediaAttributeDao.selectGroupIdByMachineCodes(machineCodes);
	}

	@Override
	public Map<String, Object> selectPageMediaByUids(Map<String, Object> param, Integer pageSize, Integer page) {
		Map<String, Object> returndata = new HashMap<String, Object>();
		Integer totalCount = mediaAttributeDao.selectPageMediaByUidsCount(param);
		if(null != totalCount && totalCount > 0){
			//总页数
	        Double totalPage = Math.ceil(totalCount/Double.parseDouble(pageSize+""));
	        int startRow = (page-1)*pageSize;
	        param.put("startRow", startRow);
	        param.put("pageSize", pageSize);
	        List<Map<String,Object>> list = mediaAttributeDao.selectPageMediaByUids(param);

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

	/**
	* @Title: syncClientTime
	* @Description: 同步终端时间
	* @param machineCodes
	* @throws Exception    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	@Override
	public void syncClientTime(String[] machineCodes) throws Exception{
		if(machineCodes.length > 0){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd.HHmmss");
			SyncDate sd = new SyncDate(sdf.format(new Date()));
			//FBTopicSender sender= new FBTopicSender();
			ProtocolModel protocolModel = new ProtocolModel();
			protocolModel.ServerToClient(MessageEvent.SYNCDATE.event, sd);
			for (int i = 0; i < machineCodes.length; i++) {
				protocolModel.setToClient(machineCodes[i]);
				JSONObject json = JSONObject.fromObject(protocolModel);
				log.debug("向终端发送同步时间："+json);
				//fBTopicSender.sendSingleMsg(json.toString(), machineCodes[i]);
				fBTopicSender.sendMsg(json.toString(), machineCodes[i]);
			}
			fBTopicSender.session.commit();
		}else{
			log.warn("同步时间时机器码为空不需要发送指令");
		}
	}

	/**
	* @Title: syncAllClientTime
	* @Description: 同步所有终端时间
	* @throws Exception    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	@Override
	public void syncAllClientTime() throws Exception{
		//查询所有在线终端 发送同步时间指令
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("adStatus", 1);
		List<Map<String,Object>> mapList = mediaAttributeDao.selectByCondition(param);
		if(mapList.size() > 0){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd.HHmmss");
			SyncDate sd = new SyncDate(sdf.format(new Date()));
			//FBTopicSender sender= new FBTopicSender();
			ProtocolModel protocolModel = new ProtocolModel();
			protocolModel.ServerToClient(MessageEvent.SYNCDATE.event, sd);
			for (int i = 0; i < mapList.size(); i++) {
				String machineCode = mapList.get(i).get("machineCode").toString();
				protocolModel.setToClient(machineCode);
				JSONObject json = JSONObject.fromObject(protocolModel);
				log.debug("向终端发送同步时间："+json);
				//fBTopicSender.sendSingleMsg(json.toString(), machineCodes[i]);
				fBTopicSender.sendMsg(json.toString(), machineCode);
			}
			fBTopicSender.session.commit();
		}
	}

	@Override
	public List<Map<String, Object>> selectMediaByGroupId(Integer[] mediaGroupIds) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("mediaGroupIds", mediaGroupIds);
		return mediaAttributeDao.selectMediaByGroupId(param);
	}

	@Override
	public Map<String, Object> selectClientInfo(Map<String, Object> param, Integer pageSize, Integer page) {
		Map<String, Object> returndata = new HashMap<String, Object>();
		Integer totalCount = iotMediaAttributeDao.selectClientInfoCount(param);
		if(null != totalCount && totalCount > 0){
			//总页数
	        Double totalPage = Math.ceil(totalCount/Double.parseDouble(pageSize+""));
	        int startRow = (page-1)*pageSize;
	        param.put("startRow", startRow);
	        param.put("pageSize", pageSize);
	        List<Map<String,Object>> list = iotMediaAttributeDao.selectClientInfo(param);

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
	public Map<String, Object> selectIotInfo(Map<String, Object> param, Integer pageSize, Integer page) {
		Map<String, Object> returndata = new HashMap<String, Object>();
		Integer totalCount = iotMediaAttributeDao.selectIotInfoCount(param);
		if(null != totalCount && totalCount > 0){
			//总页数
	        Double totalPage = Math.ceil(totalCount/Double.parseDouble(pageSize+""));
	        int startRow = (page-1)*pageSize;
	        param.put("startRow", startRow);
	        param.put("pageSize", pageSize);
	        List<Map<String,Object>> list = iotMediaAttributeDao.selectIotInfo(param);

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
	public List<Map<String,Object>> selectByCondition(Map<String, Object> param){
		return mediaAttributeDao.selectByCondition(param);
	}
}
