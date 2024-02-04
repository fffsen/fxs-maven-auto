package com.zycm.zybao.web.backend;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import com.google.gson.Gson;
import com.zycm.zybao.common.config.AliyunConfig;
import com.zycm.zybao.web.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/aliyun/*")
public class AliyunController extends BaseController {


	@RequestMapping(value = "getsts2",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String,Object> getsts2(HttpServletRequest request1,
			HttpServletResponse response1) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
        //发起请求，并得到响应。
        try {
			String jsonstr = getsts(request1,response1);
            returnmap.put("result", "success");
			returnmap.put("message", "成功");
			returnmap.put("data", jsonstr);
            //System.out.println(new Gson().toJson(a_response));
        } catch (Exception e) {
        	returnmap.put("result", "error");
			returnmap.put("message", "获取授权码异常！");
			returnmap.put("data", null);
        }
        return returnmap;
	}

	@RequestMapping(value = "getsts",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getsts(HttpServletRequest request1,HttpServletResponse response1) {
		//构建一个阿里云客户端，用于发起请求。
        //构建阿里云客户端时需要设置AccessKey ID和AccessKey Secret。
        DefaultProfile profile = DefaultProfile.getProfile(AliyunConfig.regionId, AliyunConfig.accessKeyId, AliyunConfig.secret);

        IAcsClient client = new DefaultAcsClient(profile);

        //构造请求，设置参数。关于参数含义和设置方法，请参见API参考。
        AssumeRoleRequest a_request = new AssumeRoleRequest();
        //request.setRegionId("cn-hangzhou");
        a_request.setRoleArn(AliyunConfig.roleArn);
        a_request.setRoleSessionName(AliyunConfig.roleSessionName);
        AssumeRoleResponse a_response = null;
		try {
			a_response = client.getAcsResponse(a_request);

		} catch (ServerException e) {
			// TODO Auto-generated catch block
			log.error("服务异常！", e);
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			log.error("终端异常！"+e.getErrMsg(), e);
		}
		 return new Gson().toJson(a_response);

	}
}
