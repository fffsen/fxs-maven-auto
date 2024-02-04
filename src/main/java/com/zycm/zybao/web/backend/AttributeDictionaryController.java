package com.zycm.zybao.web.backend;

import com.zycm.zybao.service.interfaces.AttributeDictionaryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




@Slf4j
@Controller
@RequestMapping("/dictionary/*")
public class AttributeDictionaryController {

	@Autowired(required = false)
	private AttributeDictionaryService attributeDictionaryService;

	@RequestMapping(value = "selectDepartment")
	@ResponseBody
	public  Map<String,Object> selectDepartment(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();


		try{
			List<Map<String,Object>> departments = attributeDictionaryService.selectDepartment(null);
			returnmap.put("result", "success");
			returnmap.put("message", "成功");
			returnmap.put("data", departments);
		}catch(Exception e){
			log.error("查询部门信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "查询部门信息异常！");
			returnmap.put("data", null);
		}

		return returnmap;
	}

/*	@RequestMapping(value = "loadSyncPlayGroup")
	@ResponseBody
	public  Map<String,Object> loadSyncPlayGroup(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String masterMachineCode = request.getParameter("masterMachineCode");
		String slaveMachineCode = request.getParameter("slaveMachineCode");
		String type = request.getParameter("type");
		try{
			if(StringUtils.isNotBlank(masterMachineCode) && StringUtils.isNotBlank(slaveMachineCode) && StringUtils.isNotBlank(type)){
				attributeDictionaryService.loadSyncPlayGroup(masterMachineCode,slaveMachineCode,type);
				returnmap.put("result", "success");
				returnmap.put("message", "成功");
				returnmap.put("data", null);

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
				returnmap.put("data", null);
			}
		}catch(Exception e){
			log.error("载入同步播放组信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "载入同步播放组信息异常！");
			returnmap.put("data", null);
		}

		return returnmap;
	}*/

}
