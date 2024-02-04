package com.zycm.zybao.web.backend;

import com.zycm.zybao.service.interfaces.MediaRunLogService;
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
import java.util.Map;




@Slf4j
@Controller
@RequestMapping("/runLog/*")
public class MediaRunLogController extends BaseController {

	@Autowired(required = false)
	private MediaRunLogService mediaRunLogService;

	@RequiresPermissions("runLog:selectPage")
	@RequestMapping(value = "selectPage")
	@ResponseBody
	public  Map<String,Object> selectPage(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String page = request.getParameter("page");
		String pageSize = request.getParameter("pageSize");
		String machineCode = request.getParameter("machineCode");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String logType = request.getParameter("logType");
		try{
			if(StringUtils.isNotBlank(page) && StringUtils.isNotBlank(pageSize) && StringUtils.isNotBlank(machineCode)){
				Map<String,Object> parammap = new HashMap<String,Object>();
				parammap.put("machineCode", machineCode);
				if(StringUtils.isNotBlank(startTime) && !isMatchDate(startTime)){
					returnmap.put("result", "error");
					returnmap.put("message", "参数startTime时间格式不正确！");
					return returnmap;
				}
				if(StringUtils.isNotBlank(endTime) && !isMatchDate(endTime)){
					returnmap.put("result", "error");
					returnmap.put("message", "参数endTime时间格式不正确！");
					return returnmap;
				}
				parammap.put("startTime",StringUtils.isNotBlank(startTime)?startTime:null );
				parammap.put("endTime",StringUtils.isNotBlank(endTime)?endTime:null );
				parammap.put("logType", StringUtils.isNotBlank(logType)?Integer.parseInt(logType):null);

				returnmap = mediaRunLogService.selectPage(parammap, Integer.parseInt(page), Integer.parseInt(pageSize));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("分页查询终端运行日志信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "分页查询终端运行日志信息异常！");
		}

		return returnmap;
	}

	@RequiresPermissions("runLog:pageStatisticsRunLog")
	@RequestMapping(value = "pageStatisticsRunLog")
	@ResponseBody
	public  Map<String,Object> pageStatisticsRunLog(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String page = request.getParameter("page");
		String pageSize = request.getParameter("pageSize");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String logType = request.getParameter("logType");
		String logNum = request.getParameter("logNum");
		String clientName = request.getParameter("clientName");

		try{

			if(StringUtils.isNotBlank(page) && StringUtils.isNotBlank(pageSize)){
				if(isMatchDate(startTime) && isMatchDate(endTime)){
					Map<String,Object> parammap = new HashMap<String,Object>();
					parammap.put("startTime", startTime);
					parammap.put("endTime", endTime);
					parammap.put("logType", StringUtils.isNotBlank(logType)?Integer.parseInt(logType):null);
					parammap.put("logNum", StringUtils.isNotBlank(logNum)?Integer.parseInt(logNum):null);
					parammap.put("clientName", StringUtils.isNotBlank(clientName)?clientName:null);
					if(getUGroupId(request) == this.SUPERUSERGROUPID){
						parammap.put("isAdmin", null);
					}else{
						parammap.put("isAdmin", false);
						parammap.put("uid", getUserId(request));
					}
					returnmap = mediaRunLogService.pageStatisticsRunLog(parammap,Integer.parseInt(page), Integer.parseInt(pageSize));
					returnmap.put("result", "success");
					returnmap.put("message", "成功");

				}else{
					returnmap.put("result", "error");
					returnmap.put("message", "必传参数不能为空或时间格式不正确");
				}
			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}

		}catch(Exception e){
			log.error("分页统计终端运行日志信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "分页统计终端运行日志信息异常！");
		}
		return returnmap;
	}

}
