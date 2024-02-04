package com.zycm.zybao.web.backend;

import com.zycm.zybao.common.utils.ArrayUtils;
import com.zycm.zybao.service.interfaces.SysUserGroupService;
import com.zycm.zybao.web.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
@RequestMapping("/userGroup/*")
public class SysUserGroupController extends BaseController {

	@Autowired(required = false)
	private SysUserGroupService sysUserGroupService;

	@RequiresPermissions("userGroup:selectPage")
	@RequestMapping(value = "selectPage")
	@ResponseBody
	public  Map<String,Object> selectPage(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String page = request.getParameter("page");
		String pageSize = request.getParameter("pageSize");
		String userGroupName = request.getParameter("userGroupName");

		try{
			if(StringUtils.isNotBlank(page) && StringUtils.isNotBlank(pageSize) ){
				Map<String,Object> parammap = new HashMap<String,Object>();
				parammap.put("userGroupName", StringUtils.isNotBlank(userGroupName)?userGroupName:null);
				if(getUGroupId(request) != this.SUPERUSERGROUPID){
					parammap.put("uGroupId", getUGroupId(request));
				}

				returnmap = sysUserGroupService.selectPage(parammap, Integer.parseInt(page), Integer.parseInt(pageSize));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("分页查询用户组信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "分页查询用户组信息异常！");
		}

		return returnmap;
	}

	@RequestMapping(value = "selectList")
	@ResponseBody
	public  Map<String,Object> selectList(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String userGroupName = request.getParameter("userGroupName");

		try{
			Map<String,Object> parammap = new HashMap<String,Object>();
			parammap.put("userGroupName", StringUtils.isNotBlank(userGroupName)?userGroupName:null);
			if(getUGroupId(request) != this.SUPERUSERGROUPID){
				parammap.put("uGroupId", getUGroupId(request));
			}
			List<Map<String,Object>> list = sysUserGroupService.selectList(parammap);
			returnmap.put("result", "success");
			returnmap.put("message", "成功");
			returnmap.put("data", list);
		}catch(Exception e){
			log.error("查询用户组信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "查询用户组信息异常！");
			returnmap.put("data", null);
		}

		return returnmap;
	}

	@RequiresPermissions("userGroup:addUserGroup")
	@RequestMapping(value = "addUserGroup")
	@ResponseBody
	public  Map<String,Object> addUserGroup(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String userGroupName = request.getParameter("userGroupName");

		try{
			if(StringUtils.isNotBlank(userGroupName) ){
				sysUserGroupService.insert(userGroupName);
				returnmap.put("result", "success");
				returnmap.put("message", "成功");

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("增加用户组信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "增加用户组信息异常！"+e.getMessage());
		}

		return returnmap;
	}

	@RequiresPermissions("userGroup:deleteByPrimaryKey")
	@RequestMapping(value = "deleteByPrimaryKey")
	@ResponseBody
	public  Map<String,Object> deleteByPrimaryKey(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String uGroupId = request.getParameter("uGroupId");

		try{
			if(StringUtils.isNotBlank(uGroupId)){
				sysUserGroupService.deleteByPrimaryKey(Integer.parseInt(uGroupId));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("删除用户组信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "删除用户组信息异常！"+e.getMessage());
		}

		return returnmap;
	}


	@RequiresPermissions("userGroup:updateGroupName")
	@RequestMapping(value = "updateGroupName")
	@ResponseBody
	public  Map<String,Object> updateGroupName(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String uGroupId = request.getParameter("uGroupId");
		String userGroupName = request.getParameter("userGroupName");

		try{
			if(StringUtils.isNotBlank(uGroupId) && StringUtils.isNotBlank(userGroupName)){
				sysUserGroupService.updateGroupName(Integer.parseInt(uGroupId), userGroupName);
				returnmap.put("result", "success");
				returnmap.put("message", "成功");

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("修改用户组名称异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "修改用户组名称异常！"+e.getMessage());
		}

		return returnmap;
	}

	@RequestMapping(value = "validName")
	@ResponseBody
	public  Map<String,Object> validName(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String userGroupName = request.getParameter("userGroupName");
		String uGroupId = request.getParameter("uGroupId");
		try{
			if(StringUtils.isNotBlank(userGroupName)){
				Map<String,Object> parammap = new HashMap<String,Object>();
				parammap.put("userGroupName", userGroupName);
				parammap.put("uGroupId", StringUtils.isNotBlank(uGroupId)?Integer.parseInt(uGroupId):null);

				List<Map<String,Object>> list = sysUserGroupService.validName(parammap);
				returnmap.put("result", "success");
				returnmap.put("message", "成功");
				returnmap.put("data", list);
			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
				returnmap.put("data", null);
			}

		}catch(Exception e){
			log.error("验证用户组名称重复异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "验证用户组名称重复异常！");
			returnmap.put("data", null);
		}

		return returnmap;
	}

	@RequiresPermissions("userGroup:confMediaGroupToUserGroup")
	@RequestMapping(value = "confMediaGroupToUserGroup")
	@ResponseBody
	public  Map<String,Object> confMediaGroupToUserGroup(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String uGroupId = request.getParameter("uGroupId");
		String groudIds_p = request.getParameter("groudIds");
		try{
			if(StringUtils.isNotBlank(uGroupId) && StringUtils.isNotBlank(groudIds_p)){
				Integer[] groudIds = ArrayUtils.toInt(groudIds_p.split(","));
				sysUserGroupService.confMediaGroupToUserGroup(Integer.parseInt(uGroupId), groudIds);
				returnmap.put("result", "success");
				returnmap.put("message", "成功");

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("用户组配置终端组信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "用户组配置终端组信息异常！"+e.getMessage());
		}

		return returnmap;
	}

	@RequestMapping(value = "getGroupIdByUGroupId")
	@ResponseBody
	public  Map<String,Object> getGroupIdByUGroupId(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String uGroupId = request.getParameter("uGroupId");
		try{
			if(StringUtils.isNotBlank(uGroupId)){
				List<Map<String,Object>> list = sysUserGroupService.getGroupIdByUGroupId(Integer.parseInt(uGroupId));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");
				returnmap.put("data", list);
			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
				returnmap.put("data", null);
			}

		}catch(Exception e){
			log.error("获取用户组的终端组信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "获取用户组的终端组信息异常！");
			returnmap.put("data", null);
		}

		return returnmap;
	}
}
