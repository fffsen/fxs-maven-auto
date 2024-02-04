package com.zycm.zybao.web.backend;

import com.zycm.zybao.service.interfaces.MediaPlayLogService;
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
@RequestMapping("/playLog/*")
public class MediaPlayLogController extends BaseController {

	@Autowired(required = false)
	private MediaPlayLogService mediaPlayLogService;

	@RequiresPermissions("playLog:selectPage")
	@RequestMapping(value = "selectPage")
	@ResponseBody
	public  Map<String,Object> selectPage(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String page = request.getParameter("page");
		String pageSize = request.getParameter("pageSize");
		String machineCode = request.getParameter("machineCode");

		try{
			if(StringUtils.isNotBlank(page) && StringUtils.isNotBlank(pageSize) && StringUtils.isNotBlank(machineCode)){
				Map<String,Object> parammap = new HashMap<String,Object>();
				parammap.put("machineCode", machineCode);

				returnmap = mediaPlayLogService.selectPage(parammap, Integer.parseInt(page), Integer.parseInt(pageSize));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("分页查询终端播放日志信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "分页查询终端播放日志信息异常！");
		}

		return returnmap;
	}

	@RequiresPermissions("playLog:pageStatisticsPlayTime")
	@RequestMapping(value = "pageStatisticsPlayTime")
	@ResponseBody
	public  Map<String,Object> pageStatisticsPlayTime(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String page = request.getParameter("page");
		String pageSize = request.getParameter("pageSize");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String materialName = request.getParameter("materialName");
		String noPlay_p = request.getParameter("noPlay");

		try{
			if(StringUtils.isNotBlank(page) && StringUtils.isNotBlank(pageSize)
					&& isMatchDate(startDate) && isMatchDate(endDate)){
				Map<String,Object> parammap = new HashMap<String,Object>();
				parammap.put("startDate", startDate);
				parammap.put("endDate", endDate);
				parammap.put("materialName", StringUtils.isNotBlank(materialName)?materialName:null);
				parammap.put("noPlay", StringUtils.isNotBlank(noPlay_p)?0:null);
				if(getUGroupId(request) == this.SUPERUSERGROUPID){
					parammap.put("isAdmin", null);
				}else{
					parammap.put("isAdmin", false);
					parammap.put("uid", getUserId(request));
				}
				returnmap = mediaPlayLogService.pageStatisticsPlayTime(parammap, Integer.parseInt(page), Integer.parseInt(pageSize));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("分页统计素材播放次数信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "分页统计素材播放次数信息异常！");
		}

		return returnmap;
	}

	@RequiresPermissions("playLog:selectPagePlayTimeDetail")
	@RequestMapping(value = "selectPagePlayTimeDetail")
	@ResponseBody
	public  Map<String,Object> selectPagePlayTimeDetail(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String page = request.getParameter("page");
		String pageSize = request.getParameter("pageSize");
		String materialId = request.getParameter("materialId");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String noPlay_p = request.getParameter("noPlay");

		try{
			if(StringUtils.isNotBlank(page) && StringUtils.isNotBlank(pageSize)
					&& StringUtils.isNotBlank(materialId) && isMatchDate(startDate) && isMatchDate(endDate)){
				Map<String,Object> parammap = new HashMap<String,Object>();
				parammap.put("materialId", Integer.parseInt(materialId));
				parammap.put("startDate", startDate);
				parammap.put("endDate", endDate);
				parammap.put("noPlay", StringUtils.isNotBlank(noPlay_p)?0:null);
				if(getUGroupId(request) == this.SUPERUSERGROUPID){
					parammap.put("isAdmin", null);
				}else{
					parammap.put("isAdmin", false);
					parammap.put("uid", getUserId(request));
				}
				returnmap = mediaPlayLogService.selectPagePlayTimeDetail(parammap, Integer.parseInt(page), Integer.parseInt(pageSize));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空或时间格式不正确yyyy-MM-dd");
			}
		}catch(Exception e){
			log.error("分页查看统计详细信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "分页查看统计详细信息异常！");
		}

		return returnmap;
	}

}
