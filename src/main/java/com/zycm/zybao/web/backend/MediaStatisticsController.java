package com.zycm.zybao.web.backend;

import com.zycm.zybao.service.interfaces.MediaRunLogService;
import com.zycm.zybao.service.interfaces.StatisticsService;
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
@RequestMapping("/mediaStatistics/*")
public class MediaStatisticsController extends BaseController {

	@Autowired(required = false)
	private StatisticsService statisticsService;
	@Autowired(required = false)
	private MediaRunLogService mediaRunLogService;

	@RequestMapping(value = "selectMediaStatistics")
	@ResponseBody
	public  Map<String,Object> selectMediaStatistics(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();

		try{
			Map<String,Object> parammap = new HashMap<String,Object>();

			//查询用户配置的终端组管理
			if(getUGroupId(request) == this.SUPERUSERGROUPID){
				parammap.put("uid", null);
				parammap.put("isAdmin", true);
			}else{
				parammap.put("uid", getUserId(request));
				parammap.put("isAdmin", false);
			}

			Map<String,Object> returnmap2 = statisticsService.selectMediaStatistics(parammap);
			returnmap.put("result", "success");
			returnmap.put("message", "成功");
			returnmap.put("data", returnmap2);
		}catch(Exception e){
			log.error("统计终端设备的总数、在线、离线、异常、拆机、无节目终端数信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "统计终端设备的总数、在线、离线、异常、拆机、无节目终端数信息异常！");
			returnmap.put("data", null);
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
