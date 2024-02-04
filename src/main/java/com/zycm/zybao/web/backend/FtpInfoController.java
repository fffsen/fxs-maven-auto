package com.zycm.zybao.web.backend;

import com.zycm.zybao.service.interfaces.FtpInfoService;
import com.zycm.zybao.web.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




@Slf4j
@Controller
@RequestMapping("/ftpInfo/*")
public class FtpInfoController extends BaseController {

	@Autowired(required = false)
	private FtpInfoService ftpInfoService;

	@RequiresPermissions(value={"ftpInfo:selectPage","mediaAttribute:changeFtp"},logical=Logical.OR)
	@RequestMapping(value = "selectPage")
	@ResponseBody
	public  Map<String,Object> selectPage(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String page = request.getParameter("page");
		String pageSize = request.getParameter("pageSize");
		String ipAddr = request.getParameter("ipAddr");
		String ftpType_P = request.getParameter("ftpType");

		try{
			if(StringUtils.isNotBlank(page) && StringUtils.isNotBlank(pageSize) ){
				Map<String,Object> parammap = new HashMap<String,Object>();
				parammap.put("ipAddr", ipAddr);
				parammap.put("ftpType", StringUtils.isNotBlank(ftpType_P)?Integer.parseInt(ftpType_P):null);
				if(getUGroupId(request) == this.SUPERUSERGROUPID){
					parammap.put("sameGroupUserId", null);
				}else{
					parammap.put("sameGroupUserId", getSameGroupUserId(request));
				}
				returnmap = ftpInfoService.selectPage(parammap, Integer.parseInt(page), Integer.parseInt(pageSize));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("分页查询ftp用户信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "分页查询ftp用户信息异常！");
		}

		return returnmap;
	}

	@RequiresPermissions("ftpInfo:addFtp")
	@RequestMapping(value = "addFtp")
	@ResponseBody
	public  Map<String,Object> addFtp(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String ipAddr = request.getParameter("ipAddr");
		String port = request.getParameter("port");
		String ftpUser = request.getParameter("ftpUser");
		String ftpPwd = request.getParameter("ftpPwd");
		String previewPath = request.getParameter("previewPath");
		String previewPort = request.getParameter("previewPort");
		String ftpType_P = request.getParameter("ftpType");
		String bucketName_P = request.getParameter("bucketName");
		String openHttp_P = request.getParameter("openHttp");

		try{
			if(StringUtils.isNotBlank(ipAddr) && StringUtils.isNotBlank(ftpUser)
					&& StringUtils.isNotBlank(port) && StringUtils.isNotBlank(ftpPwd)
					&& StringUtils.isNotBlank(previewPath) && StringUtils.isNotBlank(previewPort)
					&& StringUtils.isNotBlank(ftpType_P) && StringUtils.isNotBlank(openHttp_P)){

				if("2".equals(ftpType_P) && StringUtils.isBlank(bucketName_P)){
					returnmap.put("result", "error");
					returnmap.put("message", "oss模式时bucketName必传参数不能为空");
				}else{
					Map<String,Object> parammap = new HashMap<String,Object>();
					parammap.put("ipAddr", ipAddr);
					parammap.put("port", Integer.parseInt(port));
					parammap.put("ftpUser", ftpUser);
					parammap.put("ftpPwd", ftpPwd);
					parammap.put("isDefault", 0);
					parammap.put("creatorId", getUserId(request));
					parammap.put("previewPath", previewPath);
					parammap.put("previewPort", Integer.parseInt(previewPort));
					parammap.put("ftpType", Integer.parseInt(ftpType_P));
					parammap.put("bucketName", bucketName_P);
					parammap.put("openHttp", openHttp_P);

					ftpInfoService.insert(parammap);
					returnmap.put("result", "success");
					returnmap.put("message", "成功");
				}
			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("增加ftp用户信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "增加ftp用户信息异常！"+e.getMessage());
		}

		return returnmap;
	}

	@RequiresPermissions("ftpInfo:updateByPrimaryKey")
	@RequestMapping(value = "updateByPrimaryKey")
	@ResponseBody
	public  Map<String,Object> updateByPrimaryKey(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String ipAddr = request.getParameter("ipAddr");
		String port = request.getParameter("port");
		String ftpUser = request.getParameter("ftpUser");
		String ftpPwd = request.getParameter("ftpPwd");
		String ftpId = request.getParameter("ftpId");
		String previewPath = request.getParameter("previewPath");
		String previewPort = request.getParameter("previewPort");
		String ftpType_P = request.getParameter("ftpType");
		String bucketName_P = request.getParameter("bucketName");
		String openHttp_P = request.getParameter("openHttp");

		try{
			if(StringUtils.isNotBlank(ipAddr) && StringUtils.isNotBlank(ftpUser) && StringUtils.isNotBlank(ftpId)
					&& StringUtils.isNotBlank(ipAddr) && StringUtils.isNotBlank(ftpPwd)
					&& StringUtils.isNotBlank(previewPath) && StringUtils.isNotBlank(previewPort)
					&& StringUtils.isNotBlank(ftpType_P) && StringUtils.isNotBlank(openHttp_P)){
				if("2".equals(ftpType_P) && StringUtils.isBlank(bucketName_P)){
					returnmap.put("result", "error");
					returnmap.put("message", "oss模式时bucketName必传参数不能为空");
				}else{
					Map<String,Object> parammap = new HashMap<String,Object>();
					parammap.put("ipAddr", ipAddr);
					parammap.put("port", Integer.parseInt(port));
					parammap.put("ftpUser", ftpUser);
					parammap.put("ftpPwd", ftpPwd);
					parammap.put("ftpId", Integer.parseInt(ftpId));
					parammap.put("previewPath", previewPath);
					parammap.put("previewPort", Integer.parseInt(previewPort));
					parammap.put("ftpType", Integer.parseInt(ftpType_P));
					parammap.put("bucketName", bucketName_P);
					parammap.put("openHttp", openHttp_P);

					ftpInfoService.updateByPrimaryKey(parammap);
					returnmap.put("result", "success");
					returnmap.put("message", "成功");
				}
			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("修改ftp用户信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "修改ftp用户信息异常！"+e.getMessage());
		}

		return returnmap;
	}

	@RequiresPermissions("ftpInfo:deleteByPrimaryKey")
	@RequestMapping(value = "deleteByPrimaryKey")
	@ResponseBody
	public  Map<String,Object> deleteByPrimaryKey(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String ftpId = request.getParameter("ftpId");

		try{
			if(StringUtils.isNotBlank(ftpId)){
				ftpInfoService.deleteByPrimaryKey(Integer.parseInt(ftpId));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("删除ftp用户信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "删除ftp用户信息异常！"+e.getMessage());
		}

		return returnmap;
	}

	@RequestMapping(value = "selectByPrimaryKey")
	@ResponseBody
	public  Map<String,Object> selectByPrimaryKey(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String ftpId = request.getParameter("ftpId");

		try{
			if(StringUtils.isNotBlank(ftpId) ){
				Map<String,Object> returndata = ftpInfoService.selectByPrimaryKey(Integer.parseInt(ftpId));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");
				returnmap.put("data", returndata);

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
				returnmap.put("data", null);
			}
		}catch(Exception e){
			log.error("根据id查询ftp信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "根据id查询ftp信息异常！");
			returnmap.put("data", null);
		}

		return returnmap;
	}

	@RequiresPermissions("ftpInfo:updateDefaultById")
	@RequestMapping(value = "updateDefaultById")
	@ResponseBody
	public  Map<String,Object> updateDefaultById(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String ftpId = request.getParameter("ftpId");

		try{
			if(StringUtils.isNotBlank(ftpId) ){
				boolean b = false;
				if(getUGroupId(request) == this.SUPERUSERGROUPID){
					b = true;
				}else{
					throw new Exception("用户无权限");
				}
				ftpInfoService.updateDefaultById(Integer.parseInt(ftpId),getUserId(request),getSameGroupUserId(request),b);
				returnmap.put("result", "success");
				returnmap.put("message", "成功");

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("设置默认ftp异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "设置默认ftp异常！"+e.getMessage());
		}

		return returnmap;
	}

	@RequestMapping(value = "selectInfo")
	@ResponseBody
	public  Map<String,Object> selectInfo(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		try{
			List<Map<String,Object>> returndata = ftpInfoService.selectInfo(getUGroupId(request),this.SUPERUSERGROUPID,getSameGroupUserId(request));
			returnmap.put("result", "success");
			returnmap.put("message", "成功");
			returnmap.put("data", returndata);
		}catch(Exception e){
			log.error("查询ftp信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "查询ftp信息异常！");
			returnmap.put("data", null);
		}

		return returnmap;
	}

}
