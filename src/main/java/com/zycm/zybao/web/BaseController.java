package com.zycm.zybao.web;

import com.alibaba.druid.sql.visitor.functions.If;
import com.zycm.zybao.common.utils.ArrayUtils;
import com.zycm.zybao.model.vo.FtpLoginVo;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;






@Controller
public abstract class BaseController {

	/*全局变量登录信息缓存*/
	public static Map<String, FtpLoginVo> login_map = new HashMap<String,FtpLoginVo>();
	public static Map<String, Map<String,Object>> loginMap = new HashMap<String, Map<String,Object>>();

	public final static int SUPERUSERGROUPID=-1;//超级用户组id

	public Session shiroSession;


	/**
	 * @Title: cleanOverdueCode
	 * @Description: 清除过期的验证码
	 * @return void
	 * @throws
	 */
	@SuppressWarnings({ "rawtypes" })
	public void cleanOverdueCode(){
		int overdueTime = 120;      //登录记录过期时间  | 分钟

		 /* 避免  ConcurrentModificationException 异常的方法*/

		Iterator iterator = login_map.keySet().iterator();
	    while (iterator.hasNext()) {
	    	String key = (String) iterator.next();
	    	FtpLoginVo ftp = login_map.get(key);
	    	// OVER_DUETIME 分钟超时
            if(System.currentTimeMillis()-ftp.getTime() > 60000*overdueTime){
            	iterator.remove();      //这行代码是关键。
            	login_map.remove(key);		//移除失效记录
            }
	    }
	}

	public String cleanInvalidStr(String str){
		if(null != str ){
			return str.replace("undefined","").replace("null", "");
		}else{
			return str;
		}
	}

	public static boolean isMatchDate(String orginal){
		if (StringUtils.isBlank(orginal)) {
            return false;
        }
		Pattern pattern = Pattern.compile("\\d{4}[-]\\d{1,2}[-]\\d{1,2}(\\s\\d{2}:\\d{2}(:\\d{2})?)?");
		Matcher isNum = pattern.matcher(orginal);
		return isNum.matches();
	}

	private Integer getUserId(){
		shiroSession = SecurityUtils.getSubject().getSession();
		if(shiroSession != null && shiroSession.getAttribute("userId") != null){
			return Integer.parseInt(shiroSession.getAttribute("userId").toString());
		}
		return null;
	}
	private Integer getUserIdByToken(HttpServletRequest request){
		String zycmToken = request.getHeader("zycmToken");
		if (StringUtils.isBlank(zycmToken)) {
			return null;
		}
		Map<String, Object> userMap = loginMap.get(zycmToken);
		if (MapUtils.isEmpty(userMap)) {
			return null;
		}
		Object userId = userMap.get("uid");
		if (userId == null) {
			return null;
		}
		return (Integer) userId;
	}

	private Integer getUGroupIdByToken(HttpServletRequest request){
		String zycmToken = request.getHeader("zycmToken");
		if (StringUtils.isBlank(zycmToken)) {
			return null;
		}
		Map<String, Object> userMap = loginMap.get(zycmToken);
		if (MapUtils.isEmpty(userMap)) {
			return null;
		}
		Object uGroupId = userMap.get("uGroupId");
		if (uGroupId == null) {
			return null;
		}
		return (Integer) uGroupId;
	}

	private Integer getUGroupId(){
		shiroSession = SecurityUtils.getSubject().getSession();
		if(shiroSession != null && shiroSession.getAttribute("uGroupId") != null){
			return Integer.parseInt(shiroSession.getAttribute("uGroupId").toString());
		}
		return null;
	}

	private Integer[] getSameGroupUserId(){
		shiroSession = SecurityUtils.getSubject().getSession();
		if(shiroSession != null && shiroSession.getAttribute("sameGroupUserId") != null){
			String s = shiroSession.getAttribute("sameGroupUserId").toString();
			if(StringUtils.isNotBlank(s)){
				return ArrayUtils.toInt(s.split(","));
			}
		}

		return null;
	}

	public Integer getUserId(HttpServletRequest request) throws Exception{
		Integer userid = getUserId();
		if(userid == null){
			throw new Exception("身份令牌失效请重新登录！");
		}

		return userid;
	}

	public Integer getUGroupId(HttpServletRequest request) throws Exception{

		Integer ugid = getUGroupId();
		if(ugid == null){
			throw new Exception("身份令牌失效请重新登录！");
		}

		return ugid;
	}

	public Integer[] getSameGroupUserId(HttpServletRequest request) throws Exception{
		Integer userid = getUserId();
		if(userid == null){
			throw new Exception("身份令牌失效请重新登录！");
		}
		return getSameGroupUserId();
	}

}
