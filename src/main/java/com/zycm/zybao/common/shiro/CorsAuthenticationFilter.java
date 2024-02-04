package com.zycm.zybao.common.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

public class CorsAuthenticationFilter extends AuthenticationFilter {

    /**FormAuthenticationFilter
     * 直接过滤可以访问的请求类型
     */


	@Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        //无条件放行OPTIONS
        if (httpRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
        	return true;
        }
        httpResponse.setHeader("Access-Control-Allow-Origin", httpRequest.getHeader("Origin"));
    	httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
    	httpResponse.setHeader("Access-Control-Allow-Methods", "POST,PUT,GET,OPTIONS,DELETE");
    	httpResponse.setHeader("Access-Control-Max-Age", "3600");
    	httpResponse.setHeader("Access-Control-Allow-Headers", "Accept,Origin,Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With,zycmToken");
    	httpResponse.setStatus(800);
    	return getSubject(request, response).isAuthenticated();
    	//return super.onAccessDenied(request, httpResponse,null);
    }

	@Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (request instanceof HttpServletRequest) {
            if (((HttpServletRequest) request).getMethod().toUpperCase().equals("OPTIONS")) {
                return true;
            }
        }
        return super.isAccessAllowed(request, response, mappedValue);
    }

}
