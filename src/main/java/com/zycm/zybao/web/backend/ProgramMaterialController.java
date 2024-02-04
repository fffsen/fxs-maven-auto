package com.zycm.zybao.web.backend;

import com.zycm.zybao.common.utils.StringUtils;
import com.zycm.zybao.model.entity.ProgramMaterialModel;
import com.zycm.zybao.service.facade.PublishFtpServiceFacade;
import com.zycm.zybao.service.interfaces.ProgramMaterialService;
import com.zycm.zybao.service.interfaces.ProgramPublishRecordService;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/material/*")
public class ProgramMaterialController extends BaseController {

	@Autowired(required = false)
	private ProgramMaterialService programMaterialService;
	@Autowired(required = false)
	private PublishFtpServiceFacade publishFtpServiceFacade;
	@Autowired(required = false)
	private ProgramPublishRecordService programPublishRecordService;

	@RequiresPermissions(value={"material:selectPage","material:selectPage_2"},logical=Logical.OR)
	@RequestMapping(value = "selectPage")
	@ResponseBody
	public  Map<String,Object> selectPage(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String page = request.getParameter("page");
		String pageSize = request.getParameter("pageSize");
		String relativePath = request.getParameter("relativePath");
		String materialName = request.getParameter("materialName");
		String type = request.getParameter("type");
		String auditStatus = request.getParameter("auditStatus");

		try{
			if(StringUtils.isNotBlank(page) && StringUtils.isNotBlank(pageSize)){
				Map<String,Object> parammap = new HashMap<String,Object>();
				//处理相对路径
				if("/".equals(relativePath) || StringUtils.isBlank(relativePath)){
					relativePath = "/material/";
				}else{
					relativePath = "/material"+relativePath;
				}
				parammap.put("materialPath", relativePath);
				parammap.put("materialName", StringUtils.isNotBlank(materialName)?materialName:null);
				if(getUGroupId(request) == this.SUPERUSERGROUPID){
					parammap.put("sameGroupUserId", null);
				}else{
					parammap.put("sameGroupUserId", getSameGroupUserId(request));
				}

				parammap.put("type", type);
				parammap.put("isPrivate", 1);
				parammap.put("auditStatus", StringUtils.isNotBlank(auditStatus)?Integer.parseInt(auditStatus):null);
				returnmap = programMaterialService.selectPage(parammap, Integer.parseInt(pageSize), Integer.parseInt(page));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("分页查询素材信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "分页查询素材信息异常！"+e.getMessage());
		}

		return returnmap;
	}

	@RequestMapping(value = "selectByPrimaryKey")
	@ResponseBody
	public  Map<String,Object> selectByPrimaryKey(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> map = new HashMap<String,Object>();
		String materialId = request.getParameter("materialId");
		try{
			if(StringUtils.isNotBlank(materialId)){
				Map<String,Object> returnmap = programMaterialService.selectByPrimaryKey(Integer.parseInt(materialId));
				map.put("result", "success");
				map.put("message", "成功");
				map.put("data", returnmap);
			}else{
				map.put("result", "error");
				map.put("message", "必传参数不能为空");
				map.put("data", null);
			}
		}catch(Exception e){
			log.error("根据id查询素材信息异常！", e);
			map.put("result", "error");
			map.put("message", "根据id查询素材信息异常！");
			map.put("data", null);
		}

		return map;
	}

	@RequestMapping(value = "selectByMaterialName")
	@ResponseBody
	public  Map<String,Object> selectByMaterialName(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> map = new HashMap<String,Object>();
		String materialName = request.getParameter("materialName");
		try{
			if(StringUtils.isNotBlank(materialName)){
				String[] names = materialName.split(",");
				List<Map<String,Object>> list = programMaterialService.selectByMaterialName(names);

				map.put("result", "success");
				map.put("message", "成功");
				map.put("data", list);
			}else{
				map.put("result", "error");
				map.put("message", "必传参数不能为空");
				map.put("data", null);
			}
		}catch(Exception e){
			log.error("根据素材名称查询信息异常！", e);
			map.put("result", "error");
			map.put("message", "根据素材名称查询信息异常！");
			map.put("data",null);
		}

		return map;
	}

	@RequestMapping(value = "checkMaterialName")
	@ResponseBody
	public  Map<String,Object> checkMaterialName(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> map = new HashMap<String,Object>();
		String materialNames = request.getParameter("materialNames");
		String type = StringUtils.cleanInvalidStr(request.getParameter("type"));
		String materialIds = StringUtils.cleanInvalidStr(request.getParameter("materialIds"));
		try{
			if(StringUtils.isNotBlank(materialNames)){
				String[] names = materialNames.split(",");
				Integer type_p = null;
				if(StringUtils.isNotBlank(type)){
					type_p = Integer.parseInt(type);
				}
				Integer[] materialIds_p = null;
				if(StringUtils.isNotBlank(materialIds)){
					String[] s = materialIds.split(",");
					materialIds_p = new Integer[s.length];
					for (int i = 0; i < materialIds_p.length; i++) {
						if(StringUtils.isNotBlank(s[i]))
						materialIds_p[i] = Integer.parseInt(s[i]);
					}
				}
				List<Map<String,Object>> list = programMaterialService.checkMaterialName(names, type_p, materialIds_p);

				map.put("result", "success");
				map.put("message", "成功");
				map.put("data", list);
			}else{
				map.put("result", "error");
				map.put("message", "必传参数不能为空");
				map.put("data", null);
			}
		}catch(Exception e){
			log.error("验证素材名称异常！", e);
			map.put("result", "error");
			map.put("message", "验证素材名称异常！");
			map.put("data",null);
		}

		return map;
	}

	@RequiresPermissions("material:updateMaterial")
	@RequestMapping(value = "updateMaterial")
	@ResponseBody
	public  Map<String,Object> updateMaterial(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> map = new HashMap<String,Object>();
		String materialId = request.getParameter("materialId");
		String materialName = request.getParameter("materialName");
		String sourceUrl = request.getParameter("sourceUrl");
		String auditStatus = request.getParameter("auditStatus");
		String auditRemark = request.getParameter("auditRemark");

		try{
			if(StringUtils.isNotBlank(materialId) && StringUtils.isNotBlank(materialName)){

				ProgramMaterialModel programMaterialModel = new ProgramMaterialModel();
				programMaterialModel.setMaterialId(Integer.parseInt(materialId));
				programMaterialModel.setMaterialName(materialName);
				programMaterialModel.setSourceUrl(sourceUrl);
				if(StringUtils.isNotBlank(sourceUrl)){
					programMaterialModel.setSize(new BigDecimal((sourceUrl.getBytes().length/1024.00)).setScale(4, BigDecimal.ROUND_HALF_DOWN));
				}
				if(StringUtils.isNotBlank(auditStatus)){
					programMaterialModel.setAuditStatus(Integer.parseInt(auditStatus));
					programMaterialModel.setAuditorId(getUserId(request));
					programMaterialModel.setAuditRemark(auditRemark);
				}

				programMaterialService.updateMaterial(programMaterialModel);
				map.put("result", "success");
				map.put("message", "成功");
			}else{
				map.put("result", "error");
				map.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("修改素材名称、有效时间、私有字段信息异常！", e);
			map.put("result", "error");
			map.put("message", e.getMessage());
		}

		return map;
	}

	@RequiresPermissions("material:updateAuditStatus")
	@RequestMapping(value = "updateAuditStatus")
	@ResponseBody
	public  Map<String,Object> updateAuditStatus(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> map = new HashMap<String,Object>();
		String materialId = request.getParameter("materialId");
		String auditStatus = request.getParameter("auditStatus");
		String auditorId = request.getParameter("auditorId");
		String auditRemark = request.getParameter("auditRemark");
		try{
			if(StringUtils.isNotBlank(materialId) && StringUtils.isNotBlank(auditStatus)
					&& StringUtils.isNotBlank(auditorId) ){
				ProgramMaterialModel programMaterialModel = new ProgramMaterialModel();
				programMaterialModel.setMaterialId(Integer.parseInt(materialId));
				programMaterialModel.setAuditorId(Integer.parseInt(auditorId));
				programMaterialModel.setAuditRemark(auditRemark);
				programMaterialModel.setAuditStatus(Integer.parseInt(auditStatus));
				programMaterialService.updateAuditStatus(programMaterialModel);
				map.put("result", "success");
				map.put("message", "成功");
			}else{
				map.put("result", "error");
				map.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("审核素材信息异常！", e);
			map.put("result", "error");
			map.put("message", "审核素材信息异常！");
		}

		return map;
	}

	@RequiresPermissions("material:updateIsDelete")
	@RequestMapping(value = "updateIsDelete")
	@ResponseBody
	public  Map<String,Object> updateIsDelete(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> map = new HashMap<String,Object>();
		String materialIds = request.getParameter("materialIds");
		List<String> error = new ArrayList<String>();
		if(StringUtils.isNotBlank(materialIds) ){
			String[] materialId = materialIds.split(",");
			Integer[] materialIdint = new Integer[materialId.length];
			for (int i = 0; i < materialId.length; i++) {
				if(StringUtils.isNotBlank(materialId[i])){
					materialIdint[i] = Integer.parseInt(materialId[i]);
				}
			}
			//验证素材是不是被发布
			List<Map<String,Object>> prolist = programPublishRecordService.selectPublishByMaterialId(materialIdint);
			if(prolist.size() > 0){
				for (int i = 0; i < prolist.size(); i++) {
					String materialName = prolist.get(i).get("materialName").toString();
					String publishIds = prolist.get(i).get("publishIds")==null?"":prolist.get(i).get("publishIds").toString();
					if(StringUtils.isNotBlank(publishIds)){
						error.add("素材【"+materialName+"】已发布到终端,请下刊再删除");
					}
				}
				if(error.size() > 0){
					map.put("result", "fail");
					map.put("message", "删除失败");
					map.put("data", error);
					return map;
				}
			}
			//验证素材是不是已经加入节目中
			List<Map<String,Object>> hasprog = programMaterialService.checkMaterialInProg(materialIdint);
			if(hasprog.size() > 0){
				String names = "";
				for (Map<String, Object> map2 : hasprog) {
					names += ","+map2.get("materialName").toString();
				}
				error.add("删除失败"+names+"已加入节目中,请先删除节目！");
				map.put("result", "fail");
				map.put("message", "删除失败");
				map.put("data", error);
				return map;
			}

			for (int i = 0; i < materialId.length; i++) {
				try {
					String errormsg = publishFtpServiceFacade.deleteFile(Integer.parseInt(materialId[i]));
					if(StringUtils.isNotBlank(errormsg)){
						error.add(errormsg);
					}
				} catch (Exception e) {
					error.add("删除【"+materialId[i]+"】异常");
				}

			}
			if(error.size() == 0){
				map.put("result", "success");
				map.put("message", "成功");
			}else{
				map.put("result", "fail");
				map.put("message", "其中"+error.size()+"个删除失败");
				map.put("data", error);
			}

		}else{
			map.put("result", "error");
			map.put("message", "必传参数不能为空");
		}


		return map;
	}

	@RequestMapping(value = "selectApk")
	@ResponseBody
	public  Map<String,Object> selectApk(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String page = request.getParameter("page");
		String pageSize = request.getParameter("pageSize");
		String auditStatus = request.getParameter("auditStatus");

		try{
			if(StringUtils.isNotBlank(page) && StringUtils.isNotBlank(pageSize)){
				Map<String,Object> parammap = new HashMap<String,Object>();

				parammap.put("creatorId", getUserId(request));
				parammap.put("auditStatus", StringUtils.isNotBlank(auditStatus)?Integer.parseInt(auditStatus):null);
				returnmap = programMaterialService.selectApk(parammap, Integer.parseInt(pageSize), Integer.parseInt(page));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("分页查询安装包信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "分页查询安装包信息异常！");
		}

		return returnmap;
	}

	@RequestMapping(value = "showTxt")
	@ResponseBody
	public  Map<String,Object> showTxt(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String path = request.getParameter("path");

		try{
			if(StringUtils.isNotBlank(path)){
				String remotxt = programMaterialService.readRemoteTxt(path);
				returnmap.put("result", "success");
				returnmap.put("message", "成功");
				returnmap.put("data", remotxt);
			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
				returnmap.put("data", null);
			}
		}catch(Exception e){
			log.error("获取远程txt文件内容信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "获取远程txt文件内容信息异常！");
			returnmap.put("data", null);
		}
		return returnmap;
	}

	@RequestMapping(value = "syncMaterialMD5")
	@ResponseBody
	public  Map<String,Object> syncMaterialMD5(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();

		try{
			String res = programMaterialService.syncMaterialMD5();
			returnmap.put("result", "success");
			returnmap.put("message", "成功");
			returnmap.put("data", res);
		}catch(Exception e){
			log.error("同步素材的md5值异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "同步素材的md5值异常！");
		}
		return returnmap;
	}

}
