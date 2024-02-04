package com.zycm.zybao.web.backend;

import com.zycm.zybao.common.utils.ArrayUtils;
import com.zycm.zybao.model.vo.ProgramPublishVo;
import com.zycm.zybao.service.interfaces.ProgramPublishRecordService;
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
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




@Slf4j
@Controller
@RequestMapping("/publishRecord/*")
public class ProgramPublishRecordController extends BaseController {

	@Autowired(required = false)
	private ProgramPublishRecordService programPublishRecordService;

	@RequiresPermissions("publishRecord:selectPage")
	@RequestMapping(value = "selectPage")
	@ResponseBody
	public  Map<String,Object> selectPage(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String page = request.getParameter("page");
		String pageSize = request.getParameter("pageSize");
		String mediaGroupId = request.getParameter("mediaGroupId");

		try{
			if(StringUtils.isNotBlank(page) && StringUtils.isNotBlank(pageSize) && StringUtils.isNotBlank(mediaGroupId)){

				returnmap = programPublishRecordService.selectPage(Integer.parseInt(mediaGroupId), Integer.parseInt(pageSize), Integer.parseInt(page));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("分页查询终端组的节目信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "分页查询终端组的节目信息异常！");
		}

		return returnmap;
	}

	@RequestMapping(value = "selectPageGroupByProg")
	@ResponseBody
	public  Map<String,Object> selectPageGroupByProg(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String page = request.getParameter("page");
		String pageSize = request.getParameter("pageSize");
		String programId = request.getParameter("programId");
		String groupName = request.getParameter("groupName");

		try{
			if(StringUtils.isNotBlank(page) && StringUtils.isNotBlank(pageSize) && StringUtils.isNotBlank(programId)){
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("programId", Integer.parseInt(programId));
				param.put("groupName", StringUtils.isNotBlank(groupName)?groupName:null);
				returnmap = programPublishRecordService.selectPageGroupByProg(param,
						Integer.parseInt(pageSize), Integer.parseInt(page));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("根据节目id分页查询发布到的终端组信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "根据节目id分页查询发布到的终端组信息异常！");
		}

		return returnmap;
	}

	@RequiresPermissions("publishRecord:publishProg")
	@RequestMapping(value = "publishProg")
	@ResponseBody
	public  Map<String,Object> publishProg(ProgramPublishVo programPublishVo, HttpServletRequest request) {
		Map<String,Object> returnmap = new HashMap<String,Object>();

		try{
			if(null == programPublishVo){
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
				return returnmap;
			}
			String error = programPublishVo.isNotNull();
			if(StringUtils.isBlank(error)){
				programPublishVo.setPublisherId(getUserId(request));
				programPublishRecordService.publishProg(programPublishVo);
				returnmap.put("result", "success");
				returnmap.put("message", "成功");

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", error);
			}
		}catch(Exception e){
			log.error("发布节目操作异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "发布节目操作异常！");
		}

		return returnmap;
	}

	@RequiresPermissions("publishRecord:progDownForProg")
	@RequestMapping(value = "progDownForProg")
	@ResponseBody
	public  Map<String,Object> progDownForProg(HttpServletRequest request) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String programId =  request.getParameter("programId");
		String mediaGroupIds =  request.getParameter("mediaGroupIds");
		try{

			if(StringUtils.isNotBlank(programId) && StringUtils.isNotBlank(mediaGroupIds)){
				String[] mediaGroupIdstr = mediaGroupIds.split(",");
				Integer[] mediaGroupIdsint = new Integer[mediaGroupIdstr.length];
				for (int i = 0; i < mediaGroupIdstr.length; i++) {
					mediaGroupIdsint[i] = Integer.parseInt(mediaGroupIdstr[i]);
				}
				programPublishRecordService.progDownForProg(Integer.parseInt(programId), mediaGroupIdsint);
				returnmap.put("result", "success");
				returnmap.put("message", "成功");

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("多个终端下刊一个节目异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "多个终端下刊一个节目异常！"+e.getMessage());
		}

		return returnmap;
	}

	@RequiresPermissions("publishRecord:progDownForGroup")
	@RequestMapping(value = "progDownForGroup")
	@ResponseBody
	public  Map<String,Object> progDownForGroup(HttpServletRequest request) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String mediaGroupId =  request.getParameter("mediaGroupId");
		String pubIds =  request.getParameter("pubIds");

		try{

			if(StringUtils.isNotBlank(mediaGroupId) && StringUtils.isNotBlank(pubIds)){
				programPublishRecordService.progDownForGroup(Integer.parseInt(mediaGroupId), ArrayUtils.toInt(pubIds.split(",")));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("多个节目在一个终端组上下刊异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "多个节目在一个终端组上下刊异常！"+e.getMessage());
		}

		return returnmap;
	}

	@RequiresPermissions("publishRecord:updateProgramOrderByKey")
	@RequestMapping(value = "updateProgramOrderByKey")
	@ResponseBody
	public  Map<String,Object> updateProgramOrderByKey(HttpServletRequest request) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String publishIdAndOrder =  request.getParameter("publishIdAndOrder");
		String publishIdAndOrder2 =  request.getParameter("publishIdAndOrder2");
		String mediaGroupId =  request.getParameter("mediaGroupId");
		String direction =  request.getParameter("direction");
		try{

			if((StringUtils.isNotBlank(publishIdAndOrder) && StringUtils.isNotBlank(publishIdAndOrder2))
					|| (StringUtils.isNotBlank(publishIdAndOrder) && StringUtils.isNotBlank(mediaGroupId)
							&& StringUtils.isNotBlank(direction))){
				programPublishRecordService.updateProgramOrderByKey(publishIdAndOrder, publishIdAndOrder2,
						StringUtils.isNotBlank(mediaGroupId)?Integer.parseInt(mediaGroupId):0, StringUtils.isNotBlank(direction)?Integer.parseInt(direction):0);
				returnmap.put("result", "success");
				returnmap.put("message", "成功");

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("更新节目排序异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "更新节目排序异常！"+e.getMessage());
		}

		return returnmap;
	}

	@RequiresPermissions("publishRecord:publishProgByGroup")
	@RequestMapping(value = "publishProgByGroup")
	@ResponseBody
	public  Map<String,Object> publishProgByGroup(HttpServletRequest request) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String mediaGroupIds =  request.getParameter("mediaGroupIds");
		try{
			if(StringUtils.isNotBlank(mediaGroupIds)){
				String[] mediaGroupIds_s = mediaGroupIds.split(",");
				programPublishRecordService.publishProgByGroup(ArrayUtils.toInt(mediaGroupIds_s));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("同步终端组节目操作异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "同步终端组节目操作异常！");
		}

		return returnmap;
	}

	@RequestMapping(value = "selectConflictTime")
	@ResponseBody
	public  Map<String,Object> selectConflictTime(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String mediaGroupIds = request.getParameter("mediaGroupIds");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");

		try{
			if(StringUtils.isAnyBlank(mediaGroupIds,startDate,endDate,startTime,endTime)){
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
				returnmap.put("data", null);
			}else{
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
				Map<String,Object> param = new HashMap<String,Object>();
				String[] mediaGroupIds_s = mediaGroupIds.split(",");
				param.put("mediaGroupIds", ArrayUtils.toInt(mediaGroupIds_s));
				param.put("startDate", sdf1.format(sdf1.parse(startDate)));
				param.put("endDate", sdf1.format(sdf1.parse(endDate)));
				param.put("startTime", sdf2.format(sdf2.parse(startTime)));
				param.put("endTime", sdf2.format(sdf2.parse(endTime)));

				List<Map<String,Object>> list = programPublishRecordService.selectConflictTime(param);
				returnmap.put("result", "success");
				returnmap.put("message", "成功");
				returnmap.put("data", list);
			}
		}catch(Exception e){
			log.error("查询冲突的时间段信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "查询冲突的时间段信息异常！");
			returnmap.put("data", null);
		}

		return returnmap;
	}

	@RequestMapping(value = "selectMaxPlayTime")
	@ResponseBody
	public  Map<String,Object> selectMaxPlayTime(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String mediaGroupIds = request.getParameter("mediaGroupIds");

		try{
			if(StringUtils.isNotBlank(mediaGroupIds)){
				String[] mediaGroupIds_s = mediaGroupIds.split(",");
				Map<String,Object> maxPlayTimeMap = programPublishRecordService.selectMaxPlayTime(ArrayUtils.toInt(mediaGroupIds_s));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");
				returnmap.put("data", maxPlayTimeMap);
			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
				returnmap.put("data", null);

			}
		}catch(Exception e){
			log.error("查询终端组中最长播放总时长异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "查询终端组中最长播放总时长异常！");
			returnmap.put("data", null);
		}

		return returnmap;
	}

	@RequestMapping(value = "checkFirstProgByMediaGroupId")
	@ResponseBody
	public  Map<String,Object> checkFirstProgByMediaGroupId(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String mediaGroupIds = request.getParameter("mediaGroupIds");

		try{
			if(StringUtils.isNotBlank(mediaGroupIds)){
				String[] mediaGroupIds_s = mediaGroupIds.split(",");
				List<Map<String,Object>> firstProgMap = programPublishRecordService.checkFirstProgByMediaGroupId(ArrayUtils.toInt(mediaGroupIds_s));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");
				returnmap.put("data", firstProgMap);
			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
				returnmap.put("data", null);

			}
		}catch(Exception e){
			log.error("查询终端组中的优先节目异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "查询终端组中的优先节目异常！");
			returnmap.put("data", null);
		}

		return returnmap;
	}
}
