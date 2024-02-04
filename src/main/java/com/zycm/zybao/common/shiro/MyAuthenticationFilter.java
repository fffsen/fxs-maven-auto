package com.zycm.zybao.common.shiro;

import com.zycm.zybao.model.entity.ModuleFunctionModel;
import com.zycm.zybao.service.interfaces.ModuleService;
import com.zycm.zybao.service.interfaces.UserLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.PathMatchingFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @ClassName: MyAuthenticationFilter
* @Description: 自定义 登录处理器  暂时没使用
* @author sy
* @date 2019年3月4日
*
*/
@Slf4j
public class MyAuthenticationFilter extends PathMatchingFilter{


	private ModuleService moduleService;

	private UserLogService userLogService;

	private String loginUrl;

	private List<ModuleFunctionModel> permissions;

	private Map<String,String> permissionNames = new HashMap<String,String>();

	public  boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception  {
		String s = getPathWithinApplication(request);

		if(permissions == null){
			log.info("######初始化所有的权限点信息！");
			permissions = moduleService.selectByRoleIds(new Integer[]{1});
	        for (ModuleFunctionModel permission : permissions) {
	            permissionNames.put(permission.getFunctionUrl(), permission.getFunctionName());
	        }
		}


		if(!loginUrl.equals(s) && !s.contains("/front/")){
			Subject subject = SecurityUtils.getSubject();
			if(!subject.isAuthenticated()){//未认证
				userLogService.insert(-99, 2, "未知用户访问："+s+",IP:"+subject.getSession().getHost());
			}else{
				String permiss = s.substring(1).replace("/", ":");
				if(subject.isPermitted(permiss)){//有权限
					userLogService.insert(Integer.parseInt(subject.getSession().getAttribute("userId").toString()), 1, permissionNames.get(s));
				}else{//无权限
					if(permissionNames.containsKey(s)){
						userLogService.insert(Integer.parseInt(subject.getSession().getAttribute("userId").toString()), 3, permissionNames.get(s));
					}else{
						userLogService.insert(Integer.parseInt(subject.getSession().getAttribute("userId").toString()), 3, subject.getPrincipal()+" 访问："+s+",IP:"+subject.getSession().getHost());
					}
				}
			}
		}

		return super.onPreHandle(request, response, mappedValue);
	}

	public void setModuleService(ModuleService moduleService) {
		this.moduleService = moduleService;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}


	public void setUserLogService(UserLogService userLogService) {
		this.userLogService = userLogService;
	}


}
