package com.zycm.zybao.amq.handler.receivehandler;

import com.zycm.zybao.amq.handler.BaseHandler;
import com.zycm.zybao.amq.topic.producer.FBTopicSender;
import com.zycm.zybao.common.constant.Constants;
import com.zycm.zybao.common.enums.MessageEvent;
import com.zycm.zybao.common.redis.RedisHandle;
import com.zycm.zybao.dao.FtpInfoDao;
import com.zycm.zybao.dao.MediaAttributeDao;
import com.zycm.zybao.dao.MediaGroupRelationDao;
import com.zycm.zybao.dao.MediaRunLogDao;
import com.zycm.zybao.model.entity.FtpInfoModel;
import com.zycm.zybao.model.entity.MediaAttributeModel;
import com.zycm.zybao.model.entity.MediaGroupRelationModel;
import com.zycm.zybao.model.mqmodel.ProtocolModel;
import com.zycm.zybao.model.mqmodel.request.clientconf.ClientConf;
import com.zycm.zybao.model.mqmodel.task.heartbeat.HeartbeatTask;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.activemq.command.ActiveMQBytesMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.jms.Message;
import java.util.*;

/**
* @ClassName: HeartbeatReceiveHandler
* @Description: 心跳接收 处理器
* @author sy
* @date 2017年11月10日 下午3:07:05
*
*/
@Slf4j
@Component("heartbeatReceiveHandler")
public class HeartbeatReceiveHandler implements BaseHandler{

	@Autowired(required = false)
	private MediaAttributeDao mediaAttributeDao;
	@Autowired(required = false)
	private MediaGroupRelationDao mediaGroupRelationDao;
	@Autowired(required = false)
	private MediaRunLogDao mediaRunLogDao;
	@Autowired(required = false)
	private RedisHandle redisHandle;
	@Autowired(required = false)
	private FtpInfoDao ftpInfoDao;
	@Autowired(required = false)
	private FBTopicSender fBTopicSender;

	@Override
	public void processor(Object msg,Message message) {
		HeartbeatTask heartbeatTask = null;
		try {
			heartbeatTask = (HeartbeatTask) JSONObject.toBean(JSONObject.fromObject(msg), HeartbeatTask.class);
		} catch (Exception e) {
			log.error("【"+msg+"】转换成HeartbeatTask json对象异常！");
			return;
		}

		if(null != heartbeatTask){
			if(StringUtils.isNotBlank(heartbeatTask.getMachineCode())){
				//有心跳数据 首先解除有异常状态的终端
				/*if(redisHandle.hasSetValue(ERRORSTATUS_REDIS_PREFIX, heartbeatTask.getMachineCode())){
					redisHandle.removeSetValue(ERRORSTATUS_REDIS_PREFIX, heartbeatTask.getMachineCode());
					Map<String,Object> param = new HashMap<String,Object>();
					param.put("adStatus", 1);
					param.put("machineCode", new String[]{heartbeatTask.getMachineCode()});
					param.put("useStatus", null);
					mediaAttributeDao.updateAdStatusByConf(param);
				}*/

				Date date = new Date();
				//判断redis有没有这个终端机
				Object redis_heart = redisHandle.get(Constants.ONLINE_REDIS_PREFIX+heartbeatTask.getMachineCode());
				if(redis_heart != null){
					//判断redis中终端是不是刚创建状态  刚创建状态是没有终端的具体信息json串的
					if(!Constants.ESTABLISH_ONLINE.equals(redis_heart.toString())){
						HeartbeatTask heartbeatTask2 = null;
						try {
							heartbeatTask2 = (HeartbeatTask) JSONObject.toBean(JSONObject.fromObject(redis_heart), HeartbeatTask.class);
							//更新redis
							if(heartbeatTask2 != null
									&& (!heartbeatTask2.getIp().equals(heartbeatTask.getIp())
											|| !heartbeatTask2.getClientNumber().equals(heartbeatTask.getClientNumber())
											|| !heartbeatTask2.getPlayProgramIds().equals(heartbeatTask.getPlayProgramIds()))){
								//只要ip与编号有变化  就要更新标记让定时任务把变化的数据更新到数据库
								heartbeatTask.setIsUpdateToDB(1);
							}else{
								heartbeatTask.setIsUpdateToDB(heartbeatTask2.getIsUpdateToDB()==null?1:heartbeatTask2.getIsUpdateToDB());
							}
						} catch (Exception e) {
							log.error("心跳读取【"+Constants.ONLINE_REDIS_PREFIX+heartbeatTask.getMachineCode()+"】的redis数据转换成HeartbeatTask对象出现异常！",e);
						}
					}
					heartbeatTask.setReceiveTime(date);
					redisHandle.set(Constants.ONLINE_REDIS_PREFIX+heartbeatTask.getMachineCode(),JSONObject.fromObject(heartbeatTask).toString(),Constants.ONLINE_REDIS_EXPIRETIME);
					//redisHandle.remove(OFFLINE_REDIS_PREFIX+heartbeatTask.getMachineCode());
				}else{
					Integer playProgramNum = StringUtils.isNotBlank(heartbeatTask.getPlayProgramIds())?heartbeatTask.getPlayProgramIds().split(",").length:0;

					//新加终端机
					//根据机器码查询数据库
					Map<String,Object> condition = new HashMap<String,Object>();
					condition.put("machineCode", heartbeatTask.getMachineCode());
					List<Map<String,Object>> list = mediaAttributeDao.selectByCondition(condition);
					if(list.size() == 0){
						//查询默认ftp
						FtpInfoModel ftpInfoModel = ftpInfoDao.selectDefaultFpt();

						//不存在对应机器码的终端信息  新增一个终端
						List<MediaAttributeModel> malist = new ArrayList<MediaAttributeModel>();
						MediaAttributeModel mediaAttributeModel = new MediaAttributeModel();
						mediaAttributeModel.setMachineCode(heartbeatTask.getMachineCode());
						mediaAttributeModel.setClientNumber(heartbeatTask.getClientNumber());
						mediaAttributeModel.setClientName(heartbeatTask.getClientNumber());
						mediaAttributeModel.setMediaIp(heartbeatTask.getIp());
						mediaAttributeModel.setLight(50);
						mediaAttributeModel.setVoice(20);
						mediaAttributeModel.setCreateTime(date);
						mediaAttributeModel.setAdStatus(1);
						mediaAttributeModel.setAdDelete(1);
						mediaAttributeModel.setPlayFileNum(playProgramNum);
						mediaAttributeModel.setPlayProgramNum(0);//节目数在定时任务中更新
						if(ftpInfoModel != null){
							mediaAttributeModel.setFtpId(ftpInfoModel.getFtpId());
						}
						malist.add(mediaAttributeModel);
						mediaAttributeDao.batchInsert(malist);
						//加入未激活列表
						redisHandle.addMap(Constants.DISTRUST_MEDIA_REDIS_PREFIX, heartbeatTask.getMachineCode(), 1);

						//新加到未分组中
						//新建终端组与终端的关系
						List<MediaGroupRelationModel> mediaGroupRelationList = new ArrayList<MediaGroupRelationModel>();
						MediaGroupRelationModel mediaGroupRelation = new MediaGroupRelationModel();
						mediaGroupRelation.setMediaGroupId(1);//未分组id为1
						mediaGroupRelation.setMid(mediaAttributeModel.getMid());
						mediaGroupRelationList.add(mediaGroupRelation);
						mediaGroupRelationDao.batchInsert(mediaGroupRelationList);

						//信息增加成功后
						if(ftpInfoModel != null){
							ClientConf clientConf = new ClientConf();
							clientConf.setFtpId(ftpInfoModel.getFtpId());
							clientConf.setFtpIp(ftpInfoModel.getIpAddr());
							clientConf.setFtpPort(ftpInfoModel.getPort());
							clientConf.setFtpUser(ftpInfoModel.getFtpUser());
							clientConf.setFtpPwd(ftpInfoModel.getFtpPwd());
							clientConf.setLight(50);
							clientConf.setVoice(20);
							clientConf.setBucketName(ftpInfoModel.getBucketName()!=null?ftpInfoModel.getBucketName():"");
							clientConf.setFtpType(ftpInfoModel.getFtpType());
							clientConf.setOpenHttp(ftpInfoModel.getOpenHttp());
							String previewPathPrefix = ftpInfoModel.getPreviewPath();
							String httpUrlPrefix = (previewPathPrefix.contains("http")?previewPathPrefix:"http://"+previewPathPrefix)+":"+ftpInfoModel.getPreviewPort();
							clientConf.setHttpUrlPrefix(httpUrlPrefix);
							clientConf.setOssUploadUrlPrefix(BaseClientConfReceiveHandler.IMG_SERVER_PATH);

							//FBTopicSender sender= new FBTopicSender();
							ProtocolModel protocolModel = new ProtocolModel();
							protocolModel.ServerToClient(MessageEvent.BASECLIENTCONF.event, clientConf);
							protocolModel.setToClient(heartbeatTask.getMachineCode());
							JSONObject json = JSONObject.fromObject(protocolModel);
							//System.out.println("新机注册发送给终端基本配置信息："+json);
							fBTopicSender.sendSingleMsg(json.toString(), heartbeatTask.getMachineCode());
						}
					}else{
						//已存在 修改redis对应的信息

						String d_ip = list.get(0).get("mediaIp")!=null?list.get(0).get("mediaIp").toString():"";
						String d_clientNumber = list.get(0).get("clientNumber")!=null?list.get(0).get("clientNumber").toString():"";
						Integer d_playProgramNum =list.get(0).get("playProgramNum")!=null?Integer.parseInt(list.get(0).get("playProgramNum").toString()):0;
						//重连上来的要判断ip、编码有没有改变
						if(!d_ip.equals(heartbeatTask.getIp()) || !d_clientNumber.equals(heartbeatTask.getClientNumber()) || d_playProgramNum.intValue() != playProgramNum){
							//只要ip与编号有变化  就要更新标记让定时任务把变化的数据更新到数据库
							heartbeatTask.setIsUpdateToDB(1);
						}else{
							heartbeatTask.setIsUpdateToDB(0);
						}
					}
					//加入redis
					heartbeatTask.setReceiveTime(date);
					redisHandle.set(Constants.ONLINE_REDIS_PREFIX+heartbeatTask.getMachineCode(),JSONObject.fromObject(heartbeatTask).toString(),Constants.ONLINE_REDIS_EXPIRETIME);
					//redisHandle.remove(OFFLINE_REDIS_PREFIX+heartbeatTask.getMachineCode());
				}

				//用于补漏数据 避免ConsumerStatusListener监听在线终端漏掉的终端数据 或修正机器码对应的producerId变更了的情况
				String producerId = ((ActiveMQBytesMessage)message).getProducerId().toString();
				//处理 上线日志
				/*if(1 == online_status.get(producerId)){
					//记录登录日志
					List<MediaRunLogModel> mrlist = new ArrayList<MediaRunLogModel>();

					MediaRunLogModel mediaRunLogModel = new MediaRunLogModel();
					mediaRunLogModel.setMachineCode(heartbeatTask.getMachineCode());
					mediaRunLogModel.setLogInfo(SERVICE_PREFIX+"终端上线");
					mediaRunLogModel.setLogLevel(1);
					mediaRunLogModel.setLogTime(date);
					mediaRunLogModel.setCreateTime(date);
					mrlist.add(mediaRunLogModel);
					mediaRunLogDao.batchInsert(mrlist);

					online_status.put(producerId, 0);
				}*/
				//保存终端机的信息  用于转化终端id(这个终端id是amq自动生成的如‘node2-openstack-46069-1528681711648-3:2847’)与机器码之间的关系
				clientIdKey.put(producerId, heartbeatTask.getMachineCode());
				//machineCodeKey.put(heartbeatTask.getMachineCode(), producerId);
			}else{
				log.error("心跳信息中机器码为空！");
			}
		}else{
			log.error("心跳信息【"+heartbeatTask+"】为空！");
		}

	}

}
