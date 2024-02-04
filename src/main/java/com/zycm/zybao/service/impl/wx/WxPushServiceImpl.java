package com.zycm.zybao.service.impl.wx;

import com.zycm.zybao.common.config.WxConfig;
import com.zycm.zybao.dao.UserDao;
import com.zycm.zybao.service.interfaces.wx.WxPushService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage.WxMpTemplateMessageBuilder;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service("wxPushService")
public class WxPushServiceImpl implements WxPushService {


	@Autowired(required = false)
	private UserDao userDao;
    /**
     * 微信公众号API的Service
     */
    private final WxMpService wxMpService;

    private WxMpTemplateMessageBuilder templateMessageb = WxMpTemplateMessage.builder();

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 构造注入
     */
    WxPushServiceImpl() {
    	WxMpService ws = new WxMpServiceImpl();
    	WxMpDefaultConfigImpl wc = new WxMpDefaultConfigImpl();
    	wc.setAppId(WxConfig.appId);
    	wc.setSecret(WxConfig.appSecret);
    	wc.setToken(WxConfig.token);
    	//wc.setAesKey(aesKey);

    	//ws.addConfigStorage("gh_c37b438576d4", wc);

    	Map<String, WxMpConfigStorage> configStorages = new HashMap<String, WxMpConfigStorage>();
    	configStorages.put(WxConfig.mpId , wc);
    	ws.setMultiConfigStorages(configStorages);

        this.wxMpService = ws;
        //BaseWxMpServiceImpl
    }


    /**
     * 发送微信模板信息
     *
     * @param openId 接受者openId
     * @return 是否推送成功
     */
    public Boolean sendWxMsg(String openId,List<WxMpTemplateData> wxMpTemplateDataList) {
        // 发送模板消息接口
    	//WxMpTemplateMessageBuilder templateMessageb = WxMpTemplateMessage.builder();
    	templateMessageb.toUser(openId);
    	templateMessageb.templateId(WxConfig.templateId);
    	if(StringUtils.isNotBlank(WxConfig.url)){
    		templateMessageb.url(WxConfig.url +"?oid="+openId);
    	}

    	WxMpTemplateMessage templateMessage = templateMessageb.build();
    	/*WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                // 接收者openid
                .toUser(openId)
                // 模板id
                .templateId("GurSXAYYdKcAiJQJQiJUpaGrMNhXF09do2nrS8IOiiY")
                // 模板跳转链接
                .url("http://www.baidu.com")
                .build();*/
        // 添加模板数据
    	templateMessage.setData(wxMpTemplateDataList);
        /*templateMessage.addData(new WxMpTemplateData("first", "您好", "#FF00FF"))
                .addData(new WxMpTemplateData("keyword1", "这是个测试", "#A9A9A9"))
                .addData(new WxMpTemplateData("keyword2", "这又是个测试", "#FF00FF"))
                .addData(new WxMpTemplateData("keyword3", "这又是个测试", "#FF00FF"))
                .addData(new WxMpTemplateData("remark", "这还是个测试", "#000000"));*/
        String msgId = null;
        try {
            // 发送模板消息
            msgId = wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
        } catch (WxErrorException e) {
        	log.error("推送微信模板信息异常", e);
        }
        log.warn("·==++--·推送微信模板信息："+(msgId != null ? "成功" : "失败")+"·--++==·");
        return msgId != null;
    }


	@Override
	public boolean sendWarnMsg(String uId,String msg) {
		if(StringUtils.isNotBlank(uId) && StringUtils.isNotBlank(msg)){
			Map<String,Object> usmap = userDao.selectByPrimaryKey(Integer.parseInt(uId));
			if(usmap != null && usmap.get("openId") != null){
				//根据openid查询用户及所属终端的离线信息
				List<WxMpTemplateData> wxMpTemplateDataList = new ArrayList<WxMpTemplateData>();
				String uname = usmap.get("realName").toString();
				String opend = usmap.get("openId").toString();
				wxMpTemplateDataList.add(new WxMpTemplateData("first", "终端在线状态实时提醒您", "#A9A9A9"));
				wxMpTemplateDataList.add(new WxMpTemplateData("keyword1", uname, "#000000"));
				wxMpTemplateDataList.add(new WxMpTemplateData("keyword2", "刚刚有"+msg.split(",").length+"个终端离线", "#000000"));
				wxMpTemplateDataList.add(new WxMpTemplateData("keyword3", sdf.format(new Date()), "#000000"));
				wxMpTemplateDataList.add(new WxMpTemplateData("remark", msg+" 离线", "#000000"));

				return sendWxMsg(opend,wxMpTemplateDataList);
			}else{
				log.warn(uId+"查询不到用户或没有openId");
				return false;
			}

		}else{
			log.error("uid或消息内容为空");
			return false;
		}

	}


	@Override
	public boolean chkSignature(String timestamp, String nonce, String signature) {
		return wxMpService.checkSignature(timestamp, nonce, signature);
	}
}
