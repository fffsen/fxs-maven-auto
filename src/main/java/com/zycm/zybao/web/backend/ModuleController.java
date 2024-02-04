package com.zycm.zybao.web.backend;

import com.zycm.zybao.model.entity.ModuleFunctionModel;
import com.zycm.zybao.model.entity.ModuleModel;
import com.zycm.zybao.service.interfaces.ModuleService;
import com.zycm.zybao.service.interfaces.UserService;
import com.zycm.zybao.web.BaseController;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/module/*")
public class ModuleController extends BaseController {

	@Autowired(required = false)
	private ModuleService moduleService;

	@Autowired(required = false)
	private UserService userService;

	@RequestMapping(value = "selectPublishModule")
	@ResponseBody
	public  Map<String,Object> selectPublishModule(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();

		try{
			List<ModuleModel> modulemap = moduleService.selectPublishModule();
			returnmap.put("result", "success");
			returnmap.put("message", "成功");
			returnmap.put("data", modulemap);
		}catch(Exception e){
			log.error("查询发布系统的所有权限信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "查询发布系统的所有权限信息异常！");
			returnmap.put("data", null);
		}

		return returnmap;
	}

	@RequestMapping(value = "selectUserPermission")
	@ResponseBody
	public  Map<String,Object> selectUserPermission(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();

		try{
			Map<String, Object> usermap = userService.selectByPrimaryKey(getUserId(request));
			if(usermap != null){
				Integer roleId = Integer.parseInt(usermap.get("roleId").toString());
				List<ModuleFunctionModel> modulemap = moduleService.selectByRoleIds(new Integer[]{roleId});
				returnmap.put("result", "success");
				returnmap.put("message", "成功");
				returnmap.put("data", modulemap);
			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "当前用户无角色信息,请核实用户信息");
				returnmap.put("data", null);
			}
		}catch(Exception e){
			log.error("查询当前用户权限信息异常,请重新登录", e);
			returnmap.put("result", "error");
			returnmap.put("message", "查询当前用户权限信息异常,请重新登录");
			returnmap.put("data", null);
		}

		return returnmap;
	}

}
