package com.zycm.zybao.web.backend;

import com.zycm.zybao.service.interfaces.wx.WxPushService;
import com.zycm.zybao.web.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;




@Slf4j
@Controller
@RequestMapping("/wx/*")
public class WeiXinController extends BaseController {


	@Autowired(required = false)
	private WxPushService wxPushService;

	/*@RequiresPermissions("workDate:selectPage")*/
	@RequestMapping(value = "checktk",produces = {"text/plain;charset=utf-8","text/html;charset=utf-8"})
	@ResponseBody
	public  String checktk(HttpServletRequest request,
			HttpServletResponse response) {
		// 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
        String signature = request.getParameter("signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");

		try{
			if(StringUtils.isNotBlank(signature) && StringUtils.isNotBlank(timestamp)
					&& StringUtils.isNotBlank(nonce) && StringUtils.isNotBlank(echostr)){

				boolean chkResult = wxPushService.chkSignature(timestamp, nonce, signature);
				if(chkResult){
					return echostr;
				}else{
					return "验证失败";
				}
			}else{
				return "必传参数不能为空";
			}
		}catch(Exception e){
			log.error("验证微信api签名异常！", e);
			return "验证微信api签名异常！";
		}

	}


}
