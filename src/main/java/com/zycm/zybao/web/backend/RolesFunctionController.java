package com.zycm.zybao.web.backend;

import com.zycm.zybao.service.interfaces.RolesFunctionService;
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
@RequestMapping("/rolesFunction/*")
public class RolesFunctionController {

	@Autowired(required = false)
	private RolesFunctionService rolesFunctionService;

	@RequestMapping(value = "selectByRole")
	@ResponseBody
	public  Map<String,Object> selectByRole(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String roleId = request.getParameter("roleId");

		try{
			if(StringUtils.isNotBlank(roleId)){
				List<Map<String,Object>> rolesFunctionmap = rolesFunctionService.selectByRole(Integer.parseInt(roleId));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");
				returnmap.put("data", rolesFunctionmap);
			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
				returnmap.put("data", null);
			}
		}catch(Exception e){
			log.error("分页查询ftp用户信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "分页查询ftp用户信息异常！");
			returnmap.put("data", null);
		}

		return returnmap;
	}

	@RequestMapping(value = "savePrivilege")
	@ResponseBody
	public  Map<String,Object> savePrivilege(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String roleId = request.getParameter("roleId");
		String functionIdsstr = request.getParameter("functionIds");

		try{
			if(StringUtils.isNotBlank(roleId) && StringUtils.isNotBlank(functionIdsstr)){
				String[] functionIdsarray = functionIdsstr.split(",");
				Integer[] functionIds = new Integer[functionIdsarray.length];
				for (int i = 0; i < functionIdsarray.length; i++) {
					functionIds[i] = Integer.parseInt(functionIdsarray[i]);
				}
				rolesFunctionService.savePrivilege(Integer.parseInt(roleId), functionIds);
				returnmap.put("result", "success");
				returnmap.put("message", "成功");

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("保存角色权限信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "保存角色权限信息异常！");
		}

		return returnmap;
	}


}
