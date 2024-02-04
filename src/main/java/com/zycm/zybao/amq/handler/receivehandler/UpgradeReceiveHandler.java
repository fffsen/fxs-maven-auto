package com.zycm.zybao.amq.handler.receivehandler;

import com.zycm.zybao.amq.handler.BaseHandler;
import com.zycm.zybao.amq.topic.producer.FBTopicSender;
import com.zycm.zybao.common.enums.MessageEvent;
import com.zycm.zybao.dao.ProgramMaterialDao;
import com.zycm.zybao.model.mqmodel.ProtocolModel;
import com.zycm.zybao.model.mqmodel.request.upgrade.Upgrade;
import com.zycm.zybao.model.mqmodel.respose.upgrade.UpgradeRespose;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
* @ClassName: UpgradeReceiveHandler
* @Description: 校验升级  处理类
* @author sy
* @date 2017年11月10日 上午11:53:44
*
*/
@Slf4j
@Component("upgradeReceiveHandler")
public class UpgradeReceiveHandler implements BaseHandler{

	@Autowired(required = false)
	private ProgramMaterialDao programMaterialDao;
	@Autowired(required = false)
	private FBTopicSender fBTopicSender;

	@Override
	public void processor(Object message, Message msg) {
		UpgradeRespose upgradeRespose = null;
		try {
			upgradeRespose = (UpgradeRespose) JSONObject.toBean(JSONObject.fromObject(message), UpgradeRespose.class);
		} catch (Exception e) {
			log.error("【"+message+"】转换成UpgradeRespose json对象异常！");
			return;
		}
		if(null != upgradeRespose && StringUtils.isNotBlank(upgradeRespose.getMachineCode())){
			List<Map<String,Object>> materials = programMaterialDao.selectByApkVersion(upgradeRespose.getAppVersion());
			if(materials.size() > 0){
				Map<String,Object> material = materials.get(0);
				Upgrade upgrade = new Upgrade();
				upgrade.setMaterialId(Integer.parseInt(material.get("materialId").toString()));
				upgrade.setMaterialName(material.get("materialName").toString());
				upgrade.setMaterialPath(material.get("materialPath").toString());
				upgrade.setSize(new BigDecimal(material.get("size").toString()));
				upgrade.setType(Integer.parseInt(material.get("type").toString()));
				upgrade.setAppVersion(material.get("apkVersion").toString());
				upgrade.setIsSave(0);
				ProtocolModel protocolModel = new ProtocolModel();
				protocolModel.ServerToClient(MessageEvent.UPGRADE.event, upgrade);
				JSONObject json = JSONObject.fromObject(protocolModel);
				//FBTopicSender sender= new FBTopicSender();
				log.debug("向终端发送同步app版本："+json);
				fBTopicSender.sendSingleMsg(json.toString(), upgradeRespose.getMachineCode());
			}else{
				log.error("终端同步app版本未查到版本信息！");
			}
		}else{
			log.error("终端同步app版本接收到的消息为空！");
		}
	}

}
