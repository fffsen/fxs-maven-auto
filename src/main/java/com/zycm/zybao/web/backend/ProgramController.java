package com.zycm.zybao.web.backend;

import com.alibaba.fastjson.JSONObject;
import com.zycm.zybao.common.utils.ArrayUtils;
import com.zycm.zybao.common.utils.StringUtils;
import com.zycm.zybao.model.vo.ProgramLayoutVo;
import com.zycm.zybao.model.vo.ProgramMaterialRelationVo;
import com.zycm.zybao.model.vo.ProgramVo;
import com.zycm.zybao.service.interfaces.ProgramService;
import com.zycm.zybao.web.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
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
@RequestMapping("/program/*")
public class ProgramController extends BaseController {

	@Autowired(required = false)
	private ProgramService programService;

	@RequiresPermissions(value={"program:selectPage","program:selectPage_2"},logical=Logical.OR)
	@RequestMapping(value = "selectPage")
	@ResponseBody
	public  Map<String,Object> selectPage(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String page = request.getParameter("page");
		String pageSize = request.getParameter("pageSize");
		String programName = request.getParameter("programName");
		String materialId = request.getParameter("materialId");
		String auditStatus = request.getParameter("auditStatus");
		try{
			if(StringUtils.isNotBlank(page) && StringUtils.isNotBlank(pageSize)){
				Map<String,Object> parammap = new HashMap<String,Object>();
				if(getUGroupId(request) == this.SUPERUSERGROUPID){
					parammap.put("sameGroupUserId", null);
				}else{
					parammap.put("sameGroupUserId", getSameGroupUserId(request));
				}
				parammap.put("programName", programName);
				parammap.put("materialId", materialId);
				parammap.put("auditStatus", StringUtils.isNotBlank(auditStatus)?Integer.parseInt(auditStatus):null);
				returnmap = programService.selectPage(parammap, Integer.parseInt(pageSize), Integer.parseInt(page));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("分页查询节目信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "分页查询节目信息异常！");
		}

		return returnmap;
	}

	@RequiresPermissions(value={"program:selectPage","program:selectPage_2"},logical=Logical.OR)
	@RequestMapping(value = "selectPageProgTimeMode")
	@ResponseBody
	public  Map<String,Object> selectPageProgTimeMode(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String page = request.getParameter("page");
		String pageSize = request.getParameter("pageSize");
		String programName = request.getParameter("programName");
		String auditStatus = request.getParameter("auditStatus");
		String searchProgType = request.getParameter("searchProgType");
		try{
			if(StringUtils.isNotBlank(page) && StringUtils.isNotBlank(pageSize)){
				Map<String,Object> parammap = new HashMap<String,Object>();
				if(getUGroupId(request) == this.SUPERUSERGROUPID){
					parammap.put("sameGroupUserId", null);
				}else{
					parammap.put("sameGroupUserId", getSameGroupUserId(request));
				}
				parammap.put("programName", programName);
				if(StringUtils.isNotBlank(searchProgType)){
					if("1".equals(searchProgType)){//过期
						parammap.put("searchProgType", Integer.parseInt(searchProgType));
					}else if("2".equals(searchProgType)){//优先
						parammap.put("playLevel", 2);
					}
				}

				parammap.put("auditStatus", StringUtils.isNotBlank(auditStatus)?Integer.parseInt(auditStatus):null);
				returnmap = programService.selectPageProgTimeMode(parammap, Integer.parseInt(pageSize), Integer.parseInt(page));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("分页查询节目信息及发布模式异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "分页查询节目信息及发布模式异常！");
		}

		return returnmap;
	}

	@RequestMapping(value = "selectPageNoOrders")
	@ResponseBody
	public  Map<String,Object> selectPageNoOrders(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String page = request.getParameter("page");
		String pageSize = request.getParameter("pageSize");
		String programName = request.getParameter("programName");
		String materialId = request.getParameter("materialId");
		String auditStatus = request.getParameter("auditStatus");
		try{
			if(StringUtils.isNotBlank(page) && StringUtils.isNotBlank(pageSize) /*&& StringUtils.isNotBlank(uid)*/){
				Map<String,Object> parammap = new HashMap<String,Object>();

				parammap.put("programName", programName);
				parammap.put("materialId", materialId);
				parammap.put("auditStatus", StringUtils.isNotBlank(auditStatus)?Integer.parseInt(auditStatus):null);
				/*parammap.put("uid", Integer.parseInt(uid));*/
				returnmap = programService.selectPageNoOrders(parammap, Integer.parseInt(pageSize), Integer.parseInt(page));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("分页查询未配置订单的节目信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "分页查询未配置订单的节目信息异常！");
		}

		return returnmap;
	}

	@RequestMapping(value = "selectByProgramId")
	@ResponseBody
	public  Map<String,Object> selectByProgramId(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String programId = request.getParameter("programId");
		try{
			if(StringUtils.isNotBlank(programId)){
				Map<String,Object> programmap = programService.selectByProgramId(Integer.parseInt(programId));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");
				returnmap.put("data", programmap);
			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
				returnmap.put("data", null);
			}
		}catch(Exception e){
			log.error("根据节目id查询详细信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "根据节目id查询详细信息异常！");
			returnmap.put("data", null);
		}

		return returnmap;
	}

	@RequestMapping(value = "checkProgName")
	@ResponseBody
	public  Map<String,Object> checkProgName(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String programName = request.getParameter("programName");
		String programId = StringUtils.cleanInvalidStr(request.getParameter("programId"));
		try{
			if(StringUtils.isNotBlank(programName)){
				Integer programId_p = null;
				if(StringUtils.isNotBlank(programId)){
					programId_p = Integer.parseInt(programId);
				}
				List<Map<String,Object>> programmap = programService.checkProgName(programName, programId_p);
				returnmap.put("result", "success");
				returnmap.put("message", "成功");
				returnmap.put("data", programmap);
			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
				returnmap.put("data", null);
			}
		}catch(Exception e){
			log.error("根据节目名称查询节目信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "根据节目名称查询节目信息异常！");
			returnmap.put("data", null);
		}

		return returnmap;
	}

	@RequiresPermissions(value={"program:saveProgram","program:saveProgram_2"},logical=Logical.OR)
	@RequestMapping(value = "/saveProgram")
	@ResponseBody
	public Map<String,Object> saveProgram(HttpServletRequest request,HttpServletResponse response) {
		Map<String,Object> map = new HashMap<String, Object>();
		String program = request.getParameter("program");
		String layout = request.getParameter("layout");
		String material = request.getParameter("material");

		try{
			if(StringUtils.isNotBlank(program) && StringUtils.isNotBlank(layout) && StringUtils.isNotBlank(material)){
				ProgramVo pv = JSONObject.parseObject(program, ProgramVo.class);
				if(StringUtils.isBlank(pv.getProgramName()) || pv.getScreenType() == null){
					map.put("result", "error");
					map.put("message", "节目名或屏幕类型不能为空");
					map.put("data", null);
					return map;
				}
				pv.setCreatorId(getUserId(request));

				List<ProgramLayoutVo> pl = JSONObject.parseArray(layout, ProgramLayoutVo.class);
				List<ProgramMaterialRelationVo> pr = JSONObject.parseArray(material, ProgramMaterialRelationVo.class);
				Integer programId = programService.saveProgram(pv, pl, pr);
				map.put("result", "success");
				map.put("message", "成功");
				Map<String,Object> rmap = new HashMap<String, Object>();
				rmap.put("programId", programId);
				map.put("data", rmap);
			}else{
				map.put("result", "error");
				map.put("message", "传的3个参数不能为空");
				map.put("data", null);
			}
		}catch(Exception e){
			log.error("保存节目信息异常！", e);
			map.put("result", "error");
			map.put("message", "保存节目信息异常！"+e.getMessage());
			map.put("data", null);
		}


		return map;
	}

	@RequiresPermissions("program:updateProgram")
	@RequestMapping(value = "/updateProgram")
	@ResponseBody
	public Map<String,Object> updateProgram(HttpServletRequest request,HttpServletResponse response) {
		Map<String,Object> map = new HashMap<String, Object>();
		String program = request.getParameter("program");
		String layout = request.getParameter("layout");
		String material = request.getParameter("material");

		try{
			if(StringUtils.isNotBlank(program) && StringUtils.isNotBlank(layout) && StringUtils.isNotBlank(material)){
				ProgramVo pv = JSONObject.parseObject(program, ProgramVo.class);
				if(StringUtils.isBlank(pv.getProgramName()) || pv.getScreenType() == null || pv.getProgramId() == null){
					map.put("result", "error");
					map.put("message", "节目id、节目名、屏幕类型不能为空");
					map.put("data", null);
					return map;
				}
				pv.setCreatorId(getUserId(request));

				List<ProgramLayoutVo> pl = JSONObject.parseArray(layout, ProgramLayoutVo.class);
				List<ProgramMaterialRelationVo> pr = JSONObject.parseArray(material, ProgramMaterialRelationVo.class);
				programService.updateProgram(pv, pl, pr);
				map.put("result", "success");
				map.put("message", "成功");
				map.put("data", null);
			}else{
				map.put("result", "error");
				map.put("message", "传的参数不能为空");
				map.put("data", null);
			}
		}catch(Exception e){
			log.error("修改节目信息异常！", e);
			map.put("result", "error");
			map.put("message", "修改节目信息异常！"+e.getMessage());
			map.put("data", null);
		}


		return map;
	}

	@RequiresPermissions("program:deleteProgram")
	@RequestMapping(value = "deleteProgram")
	@ResponseBody
	public  Map<String,Object> deleteProgram(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String programId = request.getParameter("programId");
		try{
			if(StringUtils.isNotBlank(programId)){
				programService.deleteProgram(Integer.parseInt(programId));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");
				returnmap.put("data", null);
			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
				returnmap.put("data", null);
			}
		}catch(Exception e){
			log.error("删除节目信息异常！"+e.getMessage(), e);
			returnmap.put("result", "error");
			returnmap.put("message", "删除节目信息异常！"+e.getMessage());
			returnmap.put("data", null);
		}

		return returnmap;
	}

	@RequiresPermissions("program:updateName")
	@RequestMapping(value = "updateName")
	@ResponseBody
	public  Map<String,Object> updateName(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String programId = request.getParameter("programId");
		String programName = request.getParameter("programName");

		try{
			if(StringUtils.isNotBlank(programId) && StringUtils.isNotBlank(programName)){
				programService.updateName(Integer.parseInt(programId), programName);
				returnmap.put("result", "success");
				returnmap.put("message", "成功");
				returnmap.put("data", null);
			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
				returnmap.put("data", null);
			}
		}catch(Exception e){
			log.error("修改节目名异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "修改节目名异常！");
			returnmap.put("data", null);
		}

		return returnmap;
	}

	@RequiresPermissions("program:updateAudit")
	@RequestMapping(value = "updateAudit")
	@ResponseBody
	public  Map<String,Object> updateAudit(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String programId = request.getParameter("programId");
		String auditStatus = request.getParameter("auditStatus");
		String auditRemark = request.getParameter("auditRemark");
		try{
			if(StringUtils.isNotBlank(programId) && StringUtils.isNotBlank(auditStatus)){
				programService.updateAudit(Integer.parseInt(programId), Integer.parseInt(auditStatus), auditRemark);
				returnmap.put("result", "success");
				returnmap.put("message", "成功");
				returnmap.put("data", null);
			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
				returnmap.put("data", null);
			}
		}catch(Exception e){
			log.error("审核节目异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "审核节目异常！"+e.getMessage());
			returnmap.put("data", null);
		}

		return returnmap;
	}

	/*修改节目的基本信息及审核节目信息*/
	@RequestMapping(value = "updateProgramInfo")
	@ResponseBody
	public  Map<String,Object> updateProgramInfo(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String programId = request.getParameter("programId");
		String programName = request.getParameter("programName");
		String auditStatus = request.getParameter("auditStatus");
		String auditRemark = request.getParameter("auditRemark");

		try{
			if(StringUtils.isNotBlank(programId) && StringUtils.isNotBlank(programName) && StringUtils.isNotBlank(auditStatus)){
				programService.updateProgramInfo(Integer.parseInt(programId), programName,Integer.parseInt(auditStatus),auditRemark);
				returnmap.put("result", "success");
				returnmap.put("message", "成功");
				returnmap.put("data", null);
			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
				returnmap.put("data", null);
			}
		}catch(Exception e){
			log.error("修改节目基本信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "修改节目基本信息异常！"+e.getMessage());
			returnmap.put("data", null);
		}

		return returnmap;
	}

	@RequestMapping(value = "selectPageProgramAndLayout")
	@ResponseBody
	public  Map<String,Object> selectPageProgramAndLayout(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String page = request.getParameter("page");
		String pageSize = request.getParameter("pageSize");
		String programName = request.getParameter("programName");
		String screenType = request.getParameter("screenType");
		String auditStatus = request.getParameter("auditStatus");
		try{
			if(StringUtils.isNotBlank(page) && StringUtils.isNotBlank(pageSize)){
				Map<String,Object> parammap = new HashMap<String,Object>();
				if(getUGroupId(request) == this.SUPERUSERGROUPID){
					parammap.put("sameGroupUserId", null);
				}else{
					parammap.put("sameGroupUserId", getSameGroupUserId(request));
				}
				parammap.put("programName", programName);
				parammap.put("screenType", screenType);
				parammap.put("auditStatus", StringUtils.isNotBlank(auditStatus)?Integer.parseInt(auditStatus):null);
				returnmap = programService.selectPageProgramAndLayout(parammap, Integer.parseInt(pageSize), Integer.parseInt(page));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("分页查询节目与布局信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "分页查询节目与布局信息异常！");
		}

		return returnmap;
	}

	@RequiresPermissions("program:oneKeyMakeProgram")
	@RequestMapping(value = "oneKeyMakeProgram")
	@ResponseBody
	public  Map<String,Object> oneKeyMakeProgram(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String materialIds_s = request.getParameter("materialIds");
		String programName = request.getParameter("programName");
		String screenType = request.getParameter("screenType");
		String p_width = request.getParameter("width");
		String p_height = request.getParameter("height");
		String playTime = request.getParameter("playTime");
		try{
			Integer[] materialIds = null;
			if(StringUtils.isNotBlank(materialIds_s)){
				materialIds = ArrayUtils.toInt(materialIds_s.split(","));
				if(materialIds == null){
					returnmap.put("result", "error");
					returnmap.put("message", "materialIds参数不能为空");
					returnmap.put("data", null);
					return returnmap;
				}
			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "materialIds参数不能为空");
				returnmap.put("data", null);
				return returnmap;
			}

			if(StringUtils.isNotBlank(programName) && StringUtils.isNotBlank(screenType)
					&& StringUtils.isNotBlank(p_width) && StringUtils.isNotBlank(p_height) ){
				programService.oneKeyMakeProgram(materialIds, programName, Integer.parseInt(screenType),
						p_width, p_height, null, getUserId(request));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");
				returnmap.put("data", null);
			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
				returnmap.put("data", null);
			}
		}catch(Exception e){
			log.error("一键节目制作异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "一键节目制作异常！");
			returnmap.put("data", null);
		}

		return returnmap;
	}
}
