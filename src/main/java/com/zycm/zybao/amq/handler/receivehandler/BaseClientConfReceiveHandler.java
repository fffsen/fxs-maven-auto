package com.zycm.zybao.amq.handler.receivehandler;

import com.zycm.zybao.amq.handler.BaseHandler;
import com.zycm.zybao.amq.topic.producer.FBTopicSender;
import com.zycm.zybao.common.config.FtpConfig;
import com.zycm.zybao.common.enums.MessageEvent;
import com.zycm.zybao.dao.MediaAttributeDao;
import com.zycm.zybao.model.mqmodel.ProtocolModel;
import com.zycm.zybao.model.mqmodel.request.clientconf.ClientConf;
import com.zycm.zybao.model.mqmodel.respose.clientconf.BaseClientConfRespose;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.jms.Message;
import java.util.Map;

/**
* @ClassName: BaseClientConfReceiveHandler
* @Description: 接收终端基本配置信息  处理类
* @author sy
* @date 2017年11月10日 上午11:53:44
*
*/
@Slf4j
@Component("baseClientConfReceiveHandler")
public class BaseClientConfReceiveHandler implements BaseHandler{

	@Autowired(required = false)
	private MediaAttributeDao mediaAttributeDao;
	@Autowired(required = false)
	private FBTopicSender fBTopicSender;

	public static final String IMG_SERVER_PATH = FtpConfig.getImgServerPath();

	@Override
	public void processor(Object message, Message msg) {
		BaseClientConfRespose baseClientConfRespose = null;
		try {
			baseClientConfRespose = (BaseClientConfRespose) JSONObject.toBean(JSONObject.fromObject(message), BaseClientConfRespose.class);
		} catch (Exception e) {
			log.error("【"+message+"】转换成baseClientConfRespose json对象异常！");
			return;
		}

		if(null != baseClientConfRespose){
			if(StringUtils.isNotBlank(baseClientConfRespose.getMachineCode())){
				Map<String,Object> baseMediaAttribute = mediaAttributeDao.selectAllConfByMachineCode(baseClientConfRespose.getMachineCode());
				if(null != baseMediaAttribute){
					if(baseMediaAttribute.get("ftpId") != null){
						ClientConf clientConf = new ClientConf();
						clientConf.setFtpId(Integer.parseInt(baseMediaAttribute.get("ftpId").toString()));
						clientConf.setFtpIp(baseMediaAttribute.get("ftpIp").toString());
						clientConf.setFtpPort(Integer.parseInt(baseMediaAttribute.get("ftpPort").toString()));
						clientConf.setFtpUser(baseMediaAttribute.get("ftpUser").toString());
						clientConf.setFtpPwd(baseMediaAttribute.get("ftpPwd").toString());
						clientConf.setLight(Integer.parseInt(baseMediaAttribute.get("light").toString()));
						clientConf.setVoice(Integer.parseInt(baseMediaAttribute.get("voice").toString()));
						clientConf.setFtpType(Integer.parseInt(baseMediaAttribute.get("ftpType").toString()));
						clientConf.setBucketName(baseMediaAttribute.get("bucketName")!=null?baseMediaAttribute.get("bucketName").toString():"");
						clientConf.setOpenHttp(Integer.parseInt(baseMediaAttribute.get("openHttp").toString()));
						String previewPathPrefix = baseMediaAttribute.get("previewPath").toString();
						String httpUrlPrefix = (previewPathPrefix.contains("http")?previewPathPrefix:"http://"+previewPathPrefix)+":"+baseMediaAttribute.get("previewPort").toString();
						clientConf.setHttpUrlPrefix(httpUrlPrefix);
						clientConf.setOssUploadUrlPrefix(IMG_SERVER_PATH);

						//FBTopicSender sender= new FBTopicSender();
						ProtocolModel protocolModel = new ProtocolModel();
						protocolModel.ServerToClient(MessageEvent.BASECLIENTCONF.event, clientConf);
						protocolModel.setToClient(baseClientConfRespose.getMachineCode());
						JSONObject json = JSONObject.fromObject(protocolModel);
						log.debug("发送给终端基本配置信息："+json);
						fBTopicSender.sendSingleMsg(json.toString(), baseClientConfRespose.getMachineCode());
					}else{
						log.error("机器码【"+baseClientConfRespose.getMachineCode()+"】查询基本配置时ftpId的结果为空！");
					}
				}else{
					log.error("机器码【"+baseClientConfRespose.getMachineCode()+"】在查询基本配置的结果为空！");
				}

			}else{
				log.error("接收验证终端配置信息中的机器码为空！");
			}
		}else{
			log.error("接收验证终端配置信息为空！");
		}
	}

}
