package com.zycm.zybao.web.backend;

import com.zycm.zybao.service.interfaces.UserLogService;
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
import java.util.Map;




@Slf4j
@Controller
@RequestMapping("/userLog/*")
public class UserLogController {

	@Autowired(required = false)
	private UserLogService userLogService;

	@RequiresPermissions("userLog:selectPageByUid")
	@RequestMapping(value = "selectPageByUid")
	@ResponseBody
	public  Map<String,Object> selectPageByUid(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String page = request.getParameter("page");
		String pageSize = request.getParameter("pageSize");
		String uid = request.getParameter("uid");

		try{
			if(StringUtils.isNotBlank(page) && StringUtils.isNotBlank(pageSize) && StringUtils.isNotBlank(uid)){
				Map<String,Object> parammap = new HashMap<String,Object>();
				parammap.put("uid", Integer.parseInt(uid));

				returnmap = userLogService.selectPage(parammap, Integer.parseInt(page), Integer.parseInt(pageSize));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("分页查询指定用户日志信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "分页查询指定用户日志信息异常！");
		}

		return returnmap;
	}



}
