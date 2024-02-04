 package com.zycm.zybao.amq.handler.receivehandler;

 import com.zycm.zybao.amq.handler.BaseHandler;
 import com.zycm.zybao.common.enums.MessageEvent;
 import com.zycm.zybao.common.utils.IotUtil;
 import com.zycm.zybao.dao.IotMediaAttributeDao;
 import com.zycm.zybao.dao.MediaAttributeDao;
 import com.zycm.zybao.dao.MediaRunLogDao;
 import com.zycm.zybao.model.entity.IotMediaAttributeModel;
 import com.zycm.zybao.model.entity.MediaRunLogModel;
 import com.zycm.zybao.model.mqmodel.respose.clientconf.ClientConfRespose;
 import com.zycm.zybao.service.interfaces.MediaRunLogService;
 import lombok.extern.slf4j.Slf4j;
 import net.sf.json.JSONObject;
 import org.apache.commons.lang3.StringUtils;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Component;

 import javax.jms.Message;
 import java.util.ArrayList;
 import java.util.Date;
 import java.util.List;
 import java.util.Map;

/**
* @ClassName: ClientFileResposeHandler
* @Description: 接收终端所有配置  处理类
* @author sy
* @date 2017年11月10日 上午11:53:44
*
*/
@Slf4j
@Component("allClientConfReceiveHandler")
public class AllClientConfReceiveHandler implements BaseHandler{

	@Autowired(required = false)
	private MediaAttributeDao mediaAttributeDao;
	@Autowired(required = false)
	private IotMediaAttributeDao iotMediaAttributeDao;
	@Autowired(required = false)
	private MediaRunLogService mediaRunLogService;

	@Override
	public void processor(Object message, Message msg) {
		ClientConfRespose clientConfRespose = null;
		try {
	 		clientConfRespose = (ClientConfRespose) JSONObject.toBean(JSONObject.fromObject(message), ClientConfRespose.class);
		} catch (Exception e) {
			log.error("【"+message+"】转换成clientConfRespose json对象异常！");
			return;
		}

		if(null != clientConfRespose){
			if(StringUtils.isNotBlank(clientConfRespose.getMachineCode())){
				clientConfRespose.setDogVersion(StringUtils.isNotBlank(clientConfRespose.getDogVersion())?clientConfRespose.getDogVersion().replace("看门狗在线:",""):"");
				mediaAttributeDao.updateAllConfByMachineCode(clientConfRespose);

				//处理联网卡
				Map<String,Object> iotMap = iotMediaAttributeDao.selectByMachineCode(clientConfRespose.getMachineCode());
				if(StringUtils.isNotBlank(clientConfRespose.getIccid())){
					//记录换卡日志
		    		Date date = new Date();
					List<MediaRunLogModel> mrlist = new ArrayList<MediaRunLogModel>();

					if(iotMap != null){
						//过滤无效的iccid  "00000000000000000000"
						String icc = clientConfRespose.getIccid().replace("0", "");
						//String icc_a = icc.replace("0", "");
						if(StringUtils.isBlank(icc) && "0".equals(iotMap.get("iccidUpdateType").toString())){//只反馈自动获取iccid时的异常 手动的不反馈
							String changeinfo="联网卡iccid获取失败:"+clientConfRespose.getIccid();
							/*MediaRunLogModel mediaRunLogModel = new MediaRunLogModel();
							mediaRunLogModel.setMachineCode(clientConfRespose.getMachineCode());
							mediaRunLogModel.setLogInfo(changeinfo);
							mediaRunLogModel.setLogLevel(3);//1普通 2 错误异常 3警告
							mediaRunLogModel.setLogTime(date);
							mediaRunLogModel.setCreateTime(date);
							mrlist.add(mediaRunLogModel);
							mediaRunLogDao.batchInsert(mrlist);*/
							mediaRunLogService.insert(clientConfRespose.getMachineCode(), changeinfo, 3, date);
							return;
						}

						//如果存在则判断终端是不是要做iccid数据修改
						String old_iccid = iotMap.get("iccid").toString();
						if(!old_iccid.equals(clientConfRespose.getIccid()) && "0".equals(iotMap.get("iccidUpdateType").toString())){//卡有变动 并且iccid更新类型是自动模式

							//查询新iccid是否已存在
							Map<String,Object> newIccidMap = iotMediaAttributeDao.selectByIccid(clientConfRespose.getIccid());
							if(newIccidMap != null){//新iccid已存在  说明换的卡是从其他点位转移过来的 需要记录
								//变更终端
								MediaRunLogModel mediaRunLogModel = new MediaRunLogModel();
								mediaRunLogModel.setMachineCode(clientConfRespose.getMachineCode());
								String changeinfo="联网卡变动:["+old_iccid+"]更换成"+newIccidMap.get("clientName").toString()+"("+newIccidMap.get("machineCode").toString()+")的["+clientConfRespose.getIccid()+"]";
								mediaRunLogModel.setLogInfo(changeinfo);
								mediaRunLogModel.setLogLevel(3);//1普通 2 错误异常 3警告
								mediaRunLogModel.setLogTime(date);
								mediaRunLogModel.setCreateTime(date);
								//被变更终端
								MediaRunLogModel mediaRunLogModel2 = new MediaRunLogModel();
								mediaRunLogModel2.setMachineCode(newIccidMap.get("machineCode").toString());
								String changeinfo2="联网卡变动:["+clientConfRespose.getIccid()+"]被另一台终端["+clientConfRespose.getMachineCode()+"]使用";
								mediaRunLogModel2.setLogInfo(changeinfo2);
								mediaRunLogModel2.setLogLevel(3);//1普通 2 错误异常 3警告
								mediaRunLogModel2.setLogTime(date);
								mediaRunLogModel2.setCreateTime(date);

								mrlist.add(mediaRunLogModel);
								mrlist.add(mediaRunLogModel2);
								mediaRunLogService.batchInsert(mrlist);
								//删除重复的记录
								iotMediaAttributeDao.deleteByIccid(clientConfRespose.getIccid());

							}else{
								MediaRunLogModel mediaRunLogModel = new MediaRunLogModel();
								mediaRunLogModel.setMachineCode(clientConfRespose.getMachineCode());
								String changeinfo="联网卡变动:["+old_iccid+"]更换成["+clientConfRespose.getIccid()+"]";
								mediaRunLogModel.setLogInfo(changeinfo);
								mediaRunLogModel.setLogLevel(3);//1普通 2 错误异常 3警告
								mediaRunLogModel.setLogTime(date);
								mediaRunLogModel.setCreateTime(date);
								mrlist.add(mediaRunLogModel);
								mediaRunLogService.batchInsert(mrlist);
							}

							//删除本身的老记录
							iotMediaAttributeDao.deleteByMachineCode(clientConfRespose.getMachineCode());
							//新增记录
							IotMediaAttributeModel iotMediaAttributeModel = new IotMediaAttributeModel();
							iotMediaAttributeModel.setMachineCode(clientConfRespose.getMachineCode());
							iotMediaAttributeModel.setIccid(clientConfRespose.getIccid());
							try {
								iotMediaAttributeModel.setIotType(IotUtil.getIotType(clientConfRespose.getIccid()));
							} catch (Exception e) {
								log.error(e.getMessage(), e);
							}
							//iotMediaAttributeModel.setCardType("1");
							iotMediaAttributeModel.setCardStatus(0);
							iotMediaAttributeModel.setCreateTime(new Date());
							iotMediaAttributeModel.setIsMain(1);
							//iotMediaAttributeModel.setIsDelete(1);
							iotMediaAttributeModel.setIccidUpdateType(0);//自动
							try {
								iotMediaAttributeDao.insert(iotMediaAttributeModel);
							} catch (Exception e) {//减少物联卡号重复带来的冗余日志
								log.error(iotMediaAttributeModel.getMachineCode()+"更新物联卡["+iotMediaAttributeModel.getIccid()+"]信息失败！");
							}


							/*Map<String,Object> param = new HashMap<String, Object>();
							param.put("iccid", clientConfRespose.getIccid());
							param.put("machineCode", clientConfRespose.getMachineCode());
							iotMediaAttributeDao.updateIccid(param);*/
						}else{
							//卡无变动 或iccid更新类型是手动模式 不需要做处理
						}
					}else{
						//过滤无效的iccid  "00000000000000000000"
						String icc = clientConfRespose.getIccid().replace("0", "");
						//String icc_a = icc.replace("0", "");
						if(StringUtils.isBlank(icc)){
							String changeinfo="联网卡iccid获取失败:"+clientConfRespose.getIccid();
							/*MediaRunLogModel mediaRunLogModel = new MediaRunLogModel();
							mediaRunLogModel.setMachineCode(clientConfRespose.getMachineCode());
							mediaRunLogModel.setLogInfo(changeinfo);
							mediaRunLogModel.setLogLevel(3);//1普通 2 错误异常 3警告
							mediaRunLogModel.setLogTime(date);
							mediaRunLogModel.setCreateTime(date);
							mrlist.add(mediaRunLogModel);
							mediaRunLogDao.batchInsert(mrlist);*/
							mediaRunLogService.insert(clientConfRespose.getMachineCode(), changeinfo, 3, date);
							return;
						}
						//不存在数据  就要新加iccid数据
						IotMediaAttributeModel iotMediaAttributeModel = new IotMediaAttributeModel();
						iotMediaAttributeModel.setMachineCode(clientConfRespose.getMachineCode());
						iotMediaAttributeModel.setIccid(clientConfRespose.getIccid());
						try {
							iotMediaAttributeModel.setIotType(IotUtil.getIotType(clientConfRespose.getIccid()));
						} catch (Exception e) {
							log.error(e.getMessage(), e);
						}
						//iotMediaAttributeModel.setCardType("1");
						iotMediaAttributeModel.setCardStatus(0);
						iotMediaAttributeModel.setCreateTime(new Date());
						iotMediaAttributeModel.setIsMain(1);
						//iotMediaAttributeModel.setIsDelete(1);
						iotMediaAttributeModel.setIccidUpdateType(0);//自动
						iotMediaAttributeDao.insert(iotMediaAttributeModel);
					}
				}else{
					if(iotMap != null){
						//终端反馈的iccid为空 而数据库中有联网卡配置  需要删掉之前的配置
						String old_iccid1 = iotMap.get("iccid").toString();
						String changeinfo="联网卡变动:终端"+clientConfRespose.getMachineCode()+"的["+old_iccid1+"]联网卡已被取下";
						//记录换卡日志
			    		Date date = new Date();
			    		/*List<MediaRunLogModel> mrlist = new ArrayList<MediaRunLogModel>();
						MediaRunLogModel mediaRunLogModel = new MediaRunLogModel();
						mediaRunLogModel.setMachineCode(clientConfRespose.getMachineCode());

						//logger.warn(changeinfo);
						mediaRunLogModel.setLogInfo(changeinfo);
						mediaRunLogModel.setLogLevel(1);//1普通 2 错误异常 3警告
						mediaRunLogModel.setLogTime(date);
						mediaRunLogModel.setCreateTime(date);
						mrlist.add(mediaRunLogModel);
						mediaRunLogDao.batchInsert(mrlist);*/
						mediaRunLogService.insert(clientConfRespose.getMachineCode(), changeinfo, 1, date);
						//删除重复的记录
						iotMediaAttributeDao.deleteByMachineCode(clientConfRespose.getMachineCode());

					}
				}

			}else{
				log.error("接收的终端所有配置信息中机器码为空！");
			}
		}else{
			log.error("接收的终端所有配置信息为空！");
		}
	}

}
