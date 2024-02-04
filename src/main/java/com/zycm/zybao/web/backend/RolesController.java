package com.zycm.zybao.web.backend;

import com.zycm.zybao.service.interfaces.RolesService;
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
import java.util.List;
import java.util.Map;




@Slf4j
@Controller
@RequestMapping("/roles/*")
public class RolesController extends BaseController {

	@Autowired(required = false)
	private RolesService rolesService;

	@RequestMapping(value = "selectPage")
	@ResponseBody
	public  Map<String,Object> selectPage(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String page = request.getParameter("page");
		String pageSize = request.getParameter("pageSize");
		String roleName = request.getParameter("roleName");

		try{
			if(StringUtils.isNotBlank(page) && StringUtils.isNotBlank(pageSize) ){
				if(getUGroupId(request) != this.SUPERUSERGROUPID){
					returnmap.put("result", "error");
					returnmap.put("message", "仅限平台管理员使用！");
					return returnmap;
				}
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("roleName", StringUtils.isNotBlank(roleName)?roleName:null);
				if(getUGroupId(request) != this.SUPERUSERGROUPID){
					param.put("uGroupId", getUGroupId(request));
				}else{
					param.put("uGroupId", null);
				}
				returnmap = rolesService.selectPage(Integer.parseInt(page), Integer.parseInt(pageSize),param);
				returnmap.put("result", "success");
				returnmap.put("message", "成功");

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("分页查询角色信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "分页查询角色信息异常！");
		}

		return returnmap;
	}

	@RequestMapping(value = "selectByPrimaryKey")
	@ResponseBody
	public  Map<String,Object> selectByPrimaryKey(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String roleId = request.getParameter("roleId");

		try{
			if(StringUtils.isNotBlank(roleId)){

				if(getUGroupId(request) != this.SUPERUSERGROUPID){
					returnmap.put("result", "error");
					returnmap.put("message", "仅限平台管理员使用！");
					return returnmap;
				}
				Map<String,Object> roles = rolesService.selectByPrimaryKey(Integer.parseInt(roleId));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");
				returnmap.put("data", roles);

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
				returnmap.put("data", null);
			}
		}catch(Exception e){
			log.error("查询角色信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "查询角色信息异常！");
			returnmap.put("data", null);
		}

		return returnmap;
	}

	@RequestMapping(value = "insert")
	@ResponseBody
	public  Map<String,Object> insert(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String roleName = request.getParameter("roleName");
		String roleIntroduce = request.getParameter("roleIntroduce");

		try{
			if(StringUtils.isNotBlank(roleName) && StringUtils.isNotBlank(roleIntroduce)){

				if(getUGroupId(request) != this.SUPERUSERGROUPID){
					returnmap.put("result", "error");
					returnmap.put("message", "仅限平台管理员使用！");
					return returnmap;
				}
				rolesService.insert(roleName, roleIntroduce);
				returnmap.put("result", "success");
				returnmap.put("message", "成功");

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("新增角色信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "新增角色信息异常！");
		}

		return returnmap;
	}

	@RequestMapping(value = "updateByPrimaryKey")
	@ResponseBody
	public  Map<String,Object> updateByPrimaryKey(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String roleName = request.getParameter("roleName");
		String roleIntroduce = request.getParameter("roleIntroduce");
		String roleId = request.getParameter("roleId");

		try{
			if(StringUtils.isNotBlank(roleId) && StringUtils.isNotBlank(roleName) && StringUtils.isNotBlank(roleIntroduce)){

				if(getUGroupId(request) != this.SUPERUSERGROUPID){
					returnmap.put("result", "error");
					returnmap.put("message", "仅限平台管理员使用！");
					return returnmap;
				}
				rolesService.updateByPrimaryKey(Integer.parseInt(roleId), roleName, roleIntroduce);
				returnmap.put("result", "success");
				returnmap.put("message", "成功");

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("修改角色信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "修改角色信息异常！");
		}

		return returnmap;
	}

	@RequestMapping(value = "deleteByPrimaryKey")
	@ResponseBody
	public  Map<String,Object> deleteByPrimaryKey(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String roleId = request.getParameter("roleId");

		try{
			if(StringUtils.isNotBlank(roleId) ){

				if(getUGroupId(request) != this.SUPERUSERGROUPID){
					returnmap.put("result", "error");
					returnmap.put("message", "仅限平台管理员使用！");
					return returnmap;
				}
				rolesService.deleteByPrimaryKey(Integer.parseInt(roleId));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("删除角色信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "删除角色信息异常！"+e.getMessage());
		}

		return returnmap;
	}

	@RequestMapping(value = "selectByName")
	@ResponseBody
	public  Map<String,Object> selectByName(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String roleName = request.getParameter("roleName");
		String roleId = request.getParameter("roleId");
		try{
			if(StringUtils.isNotBlank(roleName)){
				if(getUGroupId(request) != this.SUPERUSERGROUPID){
					returnmap.put("result", "error");
					returnmap.put("message", "仅限平台管理员使用！");
					return returnmap;
				}
				Map<String,Object> param = new HashMap<String,Object>();
				param.put("roleId", StringUtils.isNotBlank(roleId)?Integer.parseInt(roleId):null);
				param.put("roleName", roleName);
				param.put("roleToProject", "advert-publish");
				List<Map<String,Object>> roles = rolesService.selectByName(param);
				returnmap.put("result", "success");
				returnmap.put("message", "成功");
				returnmap.put("data", roles);

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
				returnmap.put("data", null);
			}
		}catch(Exception e){
			log.error("根据角色名称查询信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "根据角色名称查询信息异常！");
			returnmap.put("data", null);
		}

		return returnmap;
	}

	@RequestMapping(value = "selectRoleInfo")
	@ResponseBody
	public  Map<String,Object> selectRoleInfo(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();

		try{
			Map<String, Object> param = new HashMap<String, Object>();
			if(getUGroupId(request) != this.SUPERUSERGROUPID){
				param.put("uGroupId", getUGroupId(request));
			}else{
				param.put("uGroupId", null);
			}

			List<Map<String,Object>> roles = rolesService.selectRoleInfo(param);
			returnmap.put("result", "success");
			returnmap.put("message", "成功");
			returnmap.put("data", roles);
		}catch(Exception e){
			log.error("查询角色信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "查询角色信息异常！");
			returnmap.put("data", null);
		}

		return returnmap;
	}

}
