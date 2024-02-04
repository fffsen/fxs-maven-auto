package com.zycm.zybao.web.backend;

import com.zycm.zybao.common.utils.ArrayUtils;
import com.zycm.zybao.model.vo.MediaAttributeVo;
import com.zycm.zybao.service.interfaces.MediaAttributeService;
import com.zycm.zybao.service.interfaces.UserService;
import com.zycm.zybao.web.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




@Slf4j
@Controller
@RequestMapping("/mediaAttribute/*")
public class MediaAttributeController extends BaseController {

	@Autowired(required = false)
	private MediaAttributeService mediaAttributeService;

	@Autowired(required = false)
	private UserService userService;

	@RequiresPermissions(value={"mediaAttribute:selectPage","mediaAttribute:selectPage_2"},logical=Logical.OR)
	@RequestMapping(value = "selectPage")
	@ResponseBody
	public  Map<String,Object> selectPage(HttpServletRequest request,
			HttpServletResponse response) {
		log.info("session的id："+request.getSession().getId());
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String page = request.getParameter("page");
		String pageSize = request.getParameter("pageSize");
		String mediaGroupId = cleanInvalidStr(request.getParameter("mediaGroupId"));
		String clientName = cleanInvalidStr(request.getParameter("clientName"));
		//String uid = cleanInvalidStr(request.getParameter("uid"));
		String clientNumber = cleanInvalidStr(request.getParameter("clientNumber"));
		String adStatus = cleanInvalidStr(request.getParameter("adStatus"));
		String netType = cleanInvalidStr(request.getParameter("netType"));
		String ftpId = cleanInvalidStr(request.getParameter("ftpId"));
		String iotType_p = cleanInvalidStr(request.getParameter("iotType"));
		String iccid_p = cleanInvalidStr(request.getParameter("iccid"));
		String imsi_p = cleanInvalidStr(request.getParameter("imsi"));
		String cardStatus_p = cleanInvalidStr(request.getParameter("cardStatus"));
		String connectAddr_p = cleanInvalidStr(request.getParameter("connectAddr"));
		String clientVersion_p = cleanInvalidStr(request.getParameter("clientVersion"));
		String dogVersion_p = cleanInvalidStr(request.getParameter("dogVersion"));
		String workStatus_p = cleanInvalidStr(request.getParameter("workStatus"));
		String videoOutMode_p = cleanInvalidStr(request.getParameter("videoOutMode"));
		String mediaIp_p = cleanInvalidStr(request.getParameter("mediaIp"));
		String diskFreeSpace_p = cleanInvalidStr(request.getParameter("diskFreeSpace"));
		String excludeExistMonitor_p = cleanInvalidStr(request.getParameter("excludeExistMonitor"));
		String recordScreenInfo_p = cleanInvalidStr(request.getParameter("recordScreenInfo"));

		try{
			if(StringUtils.isNotBlank(page) && StringUtils.isNotBlank(pageSize)){
				Map<String,Object> parammap = new HashMap<String,Object>();

				if(StringUtils.isNotBlank(mediaGroupId)){
					if("0".equals(mediaGroupId)){
						parammap.put("parentIds", null);
					}else{
						parammap.put("parentIds", new Integer[]{Integer.parseInt(mediaGroupId)});
					}

				}else{
					parammap.put("parentIds", null);
				}
				//查询用户配置的终端组管理
				if(getUGroupId(request) == this.SUPERUSERGROUPID){
					parammap.put("uid", null);
				}else{
					parammap.put("uid", getUserId(request));
				}

				parammap.put("clientName", clientName);
				parammap.put("clientNumber", clientNumber);
				parammap.put("adStatus", adStatus);
				parammap.put("netType", StringUtils.isNotBlank(netType)?Integer.parseInt(netType):null);
				parammap.put("ftpId", StringUtils.isNotBlank(ftpId)?Integer.parseInt(ftpId):null);
				parammap.put("iotType", StringUtils.isNotBlank(iotType_p)?Integer.parseInt(iotType_p):null);
				parammap.put("iccid", StringUtils.isNotBlank(iccid_p)?iccid_p:null);
				parammap.put("imsi", StringUtils.isNotBlank(imsi_p)?imsi_p:null);
				parammap.put("cardStatus", StringUtils.isNotBlank(cardStatus_p)?Integer.parseInt(cardStatus_p):null);
				parammap.put("connectAddr", StringUtils.isNotBlank(connectAddr_p)?connectAddr_p:null);
				parammap.put("clientVersion", StringUtils.isNotBlank(clientVersion_p)?clientVersion_p:null);
				parammap.put("dogVersion", StringUtils.isNotBlank(dogVersion_p)?dogVersion_p:null);
				parammap.put("workStatus", StringUtils.isNotBlank(workStatus_p)?Integer.parseInt(workStatus_p):null);
				parammap.put("videoOutMode", StringUtils.isNotBlank(videoOutMode_p)?Integer.parseInt(videoOutMode_p):null);
				parammap.put("mediaIp", StringUtils.isNotBlank(mediaIp_p)?mediaIp_p:null);
				//传入值以兆为单位  需要转化成字节
				parammap.put("diskFreeSpace", StringUtils.isNotBlank(diskFreeSpace_p)?Integer.parseInt(diskFreeSpace_p)*1024:null);
				parammap.put("excludeExistMonitor", StringUtils.isNotBlank(excludeExistMonitor_p)?1:null);
				parammap.put("recordScreenInfo", StringUtils.isNotBlank(recordScreenInfo_p)?recordScreenInfo_p:null);

				returnmap = mediaAttributeService.selectPage(parammap, Integer.parseInt(pageSize), Integer.parseInt(page));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("分页查询终端组的终端信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "分页查询终端组的终端信息异常！");
		}

		return returnmap;
	}

	@RequiresPermissions("mediaAttribute:updateIsDelete")
	@RequestMapping(value = "updateIsDelete")
	@ResponseBody
	public  Map<String,Object> updateIsDelete(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String mids = request.getParameter("mids");
		//String zycmToken = request.getParameter("zycmToken");
		try{
			if(StringUtils.isNotBlank(mids)){
				String[] midstr = mids.split(",");
				Integer[] midint = new Integer[midstr.length];
				for (int i = 0; i < midstr.length; i++) {
					midint[i] = Integer.parseInt(midstr[i]);
				}
				mediaAttributeService.updateIsDelete(midint);
				returnmap.put("result", "success");
				returnmap.put("message", "成功");
				returnmap.put("data", null);
			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
				returnmap.put("data", null);
			}
		}catch(Exception e){
			log.error("删除终端信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "删除终端信息异常！");
			returnmap.put("data", null);
		}

		return returnmap;
	}

	@RequiresPermissions("mediaAttribute:addMedia")
	@RequestMapping(value = "addMedia")
	@ResponseBody
	public  Map<String,Object> addMedia(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String mediaGroupId = request.getParameter("mediaGroupId");
		String numstr = request.getParameter("num");
		String startstr = request.getParameter("start");
		//String zycmToken = request.getParameter("zycmToken");

		try{
			if(StringUtils.isNotBlank(mediaGroupId) && StringUtils.isNotBlank(numstr)
					&& StringUtils.isNotBlank(startstr) ){
				Integer start = 1;
				try {
					start = Integer.parseInt(startstr);
					if(start > 10000000){
						returnmap.put("result", "error");
						returnmap.put("message", "起始编号不能超过10000000");
						returnmap.put("data", null);
						return returnmap;
					}
				} catch (Exception e) {
					returnmap.put("result", "error");
					returnmap.put("message", "起始编号必须是数字");
					returnmap.put("data", null);
					return returnmap;
				}
				Integer num = Integer.parseInt(numstr);
				if(num == 0){
					returnmap.put("result", "error");
					returnmap.put("message", "添加数量不能为0");
					returnmap.put("data", null);
					return returnmap;
				}else if(num > 1000){
					returnmap.put("result", "error");
					returnmap.put("message", "添加数量一次最多不超过1000台");
					returnmap.put("data", null);
					return returnmap;
				}

				mediaAttributeService.addMedia(Integer.parseInt(mediaGroupId), num, start);
				returnmap.put("result", "success");
				returnmap.put("message", "成功");
				returnmap.put("data", null);
			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
				returnmap.put("data", null);
			}
		}catch(Exception e){
			log.error("添加终端信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "添加终端信息异常！"+e.getMessage());
			returnmap.put("data", null);
		}

		return returnmap;
	}

	@RequiresPermissions("mediaAttribute:updateGroup")
	@RequestMapping(value = "updateGroup")
	@ResponseBody
	public  Map<String,Object> updateGroup(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String mediaGroupId = request.getParameter("mediaGroupId");
		String mids = request.getParameter("mids");
		//String zycmToken = request.getParameter("zycmToken");
		try{
			if(StringUtils.isNotBlank(mediaGroupId) && StringUtils.isNotBlank(mids)){
				String[] midstr = mids.split(",");
				Integer[] midint = new Integer[midstr.length];
				for (int i = 0; i < midstr.length; i++) {
					midint[i] = Integer.parseInt(midstr[i]);
				}

				mediaAttributeService.updateGroup(Integer.parseInt(mediaGroupId), midint);
				returnmap.put("result", "success");
				returnmap.put("message", "成功");
				returnmap.put("data", null);
			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
				returnmap.put("data", null);
			}
		}catch(Exception e){
			log.error("修改终端分组信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "修改终端分组信息异常！");
			returnmap.put("data", null);
		}

		return returnmap;
	}

	@RequiresPermissions("mediaAttribute:selectByPrimaryKey")
	@RequestMapping(value = "selectByPrimaryKey")
	@ResponseBody
	public  Map<String,Object> selectByPrimaryKey(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String mid = request.getParameter("mid");

		try{
			if(StringUtils.isNotBlank(mid)){
				Map<String,Object> media = mediaAttributeService.selectByPrimaryKey(Integer.parseInt(mid));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");
				returnmap.put("data", media);
			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
				returnmap.put("data", null);
			}
		}catch(Exception e){
			log.error("查询终端详细信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "查询终端详细信息异常！");
			returnmap.put("data", null);
		}

		return returnmap;
	}

	@RequiresPermissions("mediaAttribute:updateMedia")
	@RequestMapping(value = "updateMedia")
	@ResponseBody
	public  Map<String,Object> updateMedia(MediaAttributeVo mediaAttributeVo, HttpServletRequest request) {
		Map<String,Object> returnmap = new HashMap<String,Object>();

		try{
			if(null != mediaAttributeVo && null != mediaAttributeVo.getMid() && mediaAttributeVo.getMid() > 0){
				if(mediaAttributeVo.getLight().intValue() < 0 || mediaAttributeVo.getLight().intValue() > 100){
					returnmap.put("result", "error");
					returnmap.put("message", "亮度值的范围是0~100,不能超出范围");
					returnmap.put("data", null);
					return returnmap;
				}

				mediaAttributeService.updateMedia(mediaAttributeVo);
				returnmap.put("result", "success");
				returnmap.put("message", "成功");
				returnmap.put("data", null);
			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
				returnmap.put("data", null);
			}
		}catch(Exception e){
			log.error("修改终端详细信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "修改终端详细信息异常！");
			returnmap.put("data", null);
		}

		return returnmap;
	}

	@RequiresPermissions("mediaAttribute:mediaUpgrade")
	@RequestMapping(value = "mediaUpgrade")
	@ResponseBody
	public  Map<String,Object> mediaUpgrade(HttpServletRequest request) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String machineCodes = request.getParameter("machineCodes");
		String materialId = request.getParameter("materialId");
		//String zycmToken = request.getParameter("zycmToken");
		try{
			if(StringUtils.isNotBlank(machineCodes) && StringUtils.isNotBlank(materialId)){
				String[] machineCodesstr = machineCodes.split(",");

				mediaAttributeService.mediaUpgrade(machineCodesstr, Integer.parseInt(materialId));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");
				returnmap.put("data", null);
			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
				returnmap.put("data", null);
			}
		}catch(Exception e){
			log.error("升级终端信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "升级终端信息异常！"+e.getMessage());
			returnmap.put("data", null);
		}

		return returnmap;
	}

	@RequiresPermissions("mediaAttribute:screenshot")
	@RequestMapping(value = "screenshot")
	@ResponseBody
	public  Map<String,Object> screenshot(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String mids = request.getParameter("mids");
		//String zycmToken = request.getParameter("zycmToken");

		try{
			if(StringUtils.isNotBlank(mids)){
				String[] midsstr = mids.split(",");
				Integer[] midsint = new Integer[midsstr.length];
				for (int i = 0; i < midsstr.length; i++) {
					midsint[i] = Integer.parseInt(midsstr[i]);
				}
				List<Map<String,Object>> pathlist = mediaAttributeService.screenshot(midsint);
				Thread.sleep(3000);
				returnmap.put("result", "success");
				returnmap.put("message", "成功");
				returnmap.put("data", pathlist);
			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
				returnmap.put("data", null);
			}
		}catch(Exception e){
			log.error("远程截屏异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "远程截屏异常！");
			returnmap.put("data", null);
		}

		return returnmap;
	}

	@RequiresPermissions("mediaAttribute:getClientFile")
	@RequestMapping(value = "getClientFile")
	@ResponseBody
	public  Map<String,Object> getClientFile(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String machineCode = request.getParameter("machineCode");
		//String zycmToken = request.getParameter("zycmToken");
		try{
			if(StringUtils.isNotBlank(machineCode)){
				Map<String,Object> pathlist = mediaAttributeService.getClientFile(machineCode);

				returnmap.put("result", "success");
				returnmap.put("message", "成功");
				returnmap.put("data", pathlist.get("list"));
			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
				returnmap.put("data", null);
			}
		}catch(Exception e){
			log.error("获取终端文件异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "获取终端文件异常！");
			returnmap.put("data", null);
		}

		return returnmap;
	}

	@RequiresPermissions("mediaAttribute:deleteClientFile")
	@RequestMapping(value = "deleteClientFile")
	@ResponseBody
	public  Map<String,Object> deleteClientFile(HttpServletRequest request) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String machineCode_p = request.getParameter("machineCodes");
		//String fileName_p = request.getParameter("fileName");
		//String zycmToken = request.getParameter("zycmToken");
		try{
			if(StringUtils.isNotBlank(machineCode_p)){
				//String[] fileName = fileName_p.split(",");
				//mediaAttributeService.deleteClientFile(machineCode, fileName);
				String[] machineCodes = machineCode_p.split(",");
				mediaAttributeService.deleteExpireClientFile(machineCodes);

				returnmap.put("result", "success");
				returnmap.put("message", "成功");
				returnmap.put("data", null);
			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
				returnmap.put("data", null);
			}
		}catch(Exception e){
			log.error("删除终端文件异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "删除终端文件异常！");
			returnmap.put("data", null);
		}

		return returnmap;
	}

	@RequiresPermissions("mediaAttribute:deleteExpireClientFile")
	@RequestMapping(value = "deleteExpireClientFile")
	@ResponseBody
	public  Map<String,Object> deleteExpireClientFile(HttpServletRequest request) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String machineCodes_p = request.getParameter("machineCodes");

		try{
			if(StringUtils.isNotBlank(machineCodes_p)){
				String[] machineCodes = machineCodes_p.split(",");
				mediaAttributeService.deleteExpireClientFile(machineCodes);

				returnmap.put("result", "success");
				returnmap.put("message", "成功");
				returnmap.put("data", null);
			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
				returnmap.put("data", null);
			}
		}catch(Exception e){
			log.error("批量删除终端文件异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "批量删除终端文件异常！");
			returnmap.put("data", null);
		}

		return returnmap;
	}

	@RequiresPermissions("mediaAttribute:clientRestart")
	@RequestMapping(value = "clientRestart")
	@ResponseBody
	public  Map<String,Object> clientRestart(HttpServletRequest request) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String machineCodes_p = request.getParameter("machineCodes");
		//String zycmToken = request.getParameter("zycmToken");
		try{
			if(StringUtils.isNotBlank(machineCodes_p)){
				String[] machineCodes = machineCodes_p.split(",");
				mediaAttributeService.clientRestart(machineCodes);

				returnmap.put("result", "success");
				returnmap.put("message", "成功");
				returnmap.put("data", null);
			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
				returnmap.put("data", null);
			}
		}catch(Exception e){
			log.error("重启终端异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "重启终端异常！");
			returnmap.put("data", null);
		}

		return returnmap;
	}

	/**
	* @Title: selectMediaProgram
	* @Description: 获取终端档期数据     给选点系统调用
	* @param request
	* @param response
	* @return    参数
	* @author sy
	* @throws
	* @return Map<String,Object>    返回类型
	*
	*/
	@RequestMapping(value = "selectMediaProgram")
	@ResponseBody
	public  Map<String,Object> selectMediaProgram(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String mids = request.getParameter("mids");
		String clientName = request.getParameter("clientName");
		try{
			String[] mids_s = null;
			if(StringUtils.isNotBlank(mids)){
				mids_s = mids.split(",");
			}
			List<Map<String, Object>> media = mediaAttributeService.selectMediaProgram(
					(mids_s == null ? null : ArrayUtils.toInt(mids_s)),
					(StringUtils.isNotBlank(clientName) ? clientName : null));
			returnmap.put("result", "success");
			returnmap.put("message", "成功");
			returnmap.put("data", media);

		}catch(Exception e){
			log.error("查询终端档期信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "查询终端档期信息异常！");
			returnmap.put("data", null);
		}
		return returnmap;
	}

	@RequiresPermissions("mediaAttribute:selectPageByPrimaryKeys")
	@RequestMapping(value = "selectPageByPrimaryKeys",method=RequestMethod.POST)
	@ResponseBody
	public  Map<String,Object> selectPageByPrimaryKeys(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String page = request.getParameter("page");
		String pageSize = request.getParameter("pageSize");
		String mids_p = request.getParameter("mids");
		String clientName = request.getParameter("clientName");

		try{
			if(StringUtils.isNotBlank(page) && StringUtils.isNotBlank(pageSize) && StringUtils.isNotBlank(mids_p)){
				Map<String,Object> parammap = new HashMap<String,Object>();
				Integer[] mids = ArrayUtils.toInt(mids_p.split(","));
				parammap.put("clientName", StringUtils.isNotBlank(clientName)?clientName:null);
				parammap.put("mids", mids);

				returnmap = mediaAttributeService.selectPageByPrimaryKeys(parammap, Integer.parseInt(pageSize), Integer.parseInt(page));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("分页查询终端信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "分页查询终端信息异常！");
		}

		return returnmap;
	}

	@RequiresPermissions("mediaAttribute:discardMedia")
	@RequestMapping(value = "discardMedia")
	@ResponseBody
	public  Map<String,Object> discardMedia(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String machineCodes = request.getParameter("machineCodes");
		//String zycmToken = request.getParameter("zycmToken");
		try{
			if(StringUtils.isNotBlank(machineCodes)){
				String[] mcstr = machineCodes.split(",");
				for (String machineCode : mcstr) {
					if(StringUtils.isNotBlank(machineCode))
					mediaAttributeService.updateAdStatus(machineCode, -1);//拆机
				}
				returnmap.put("result", "success");
				returnmap.put("message", "成功");
				returnmap.put("data", null);
			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
				returnmap.put("data", null);
			}
		}catch(Exception e){
			log.error("终端拆机操作异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "终端拆机操作异常！");
			returnmap.put("data", null);
		}

		return returnmap;
	}

	@RequestMapping(value = "selectPageGroupProgNumZero")
	@ResponseBody
	public  Map<String,Object> selectPageGroupProgNumZero(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String page = request.getParameter("page");
		String pageSize = request.getParameter("pageSize");

		try{
			if(StringUtils.isNotBlank(page) && StringUtils.isNotBlank(pageSize)){
				Map<String,Object> parammap = new HashMap<String,Object>();

				//查询用户配置的终端组管理
				if(getUGroupId(request) == this.SUPERUSERGROUPID){
					parammap.put("uid", null);
					parammap.put("isAdmin", true);
				}else{
					parammap.put("uid", getUserId(request));
					parammap.put("isAdmin", false);
				}

				returnmap = mediaAttributeService.selectPageGroupProgNumZero(parammap, Integer.parseInt(pageSize), Integer.parseInt(page));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("分页查询终端组节目为0的信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "分页查询终端组节目为0的信息异常！");
		}

		return returnmap;
	}

	@RequestMapping(value = "selectPageMediaProgNumZero")
	@ResponseBody
	public  Map<String,Object> selectPageMediaProgNumZero(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String page = request.getParameter("page");
		String pageSize = request.getParameter("pageSize");

		try{
			if(StringUtils.isNotBlank(page) && StringUtils.isNotBlank(pageSize)){
				Map<String,Object> parammap = new HashMap<String,Object>();

				//查询用户配置的终端组管理
				if(getUGroupId(request) == this.SUPERUSERGROUPID){
					parammap.put("uid", null);
					parammap.put("isAdmin", true);
				}else{
					parammap.put("uid", getUserId(request));
					parammap.put("isAdmin", false);
				}

				returnmap = mediaAttributeService.selectPageMediaProgNumZero(parammap, Integer.parseInt(pageSize), Integer.parseInt(page));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("分页查询终端设备节目为0的信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "分页查询终端设备节目为0的信息异常！");
		}

		return returnmap;
	}

	@RequestMapping(value = "selectPageOfflineMedia")
	@ResponseBody
	public  Map<String,Object> selectPageOfflineMedia(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String page = request.getParameter("page");
		String pageSize = request.getParameter("pageSize");

		try{
			if(StringUtils.isNotBlank(page) && StringUtils.isNotBlank(pageSize)){
				Map<String,Object> parammap = new HashMap<String,Object>();

				//查询用户配置的终端组管理
				if(getUGroupId(request) == this.SUPERUSERGROUPID){
					parammap.put("uid", null);
				}else{
					parammap.put("uid", getUserId(request));
				}

				returnmap = mediaAttributeService.selectPageOfflineMedia(parammap, Integer.parseInt(pageSize), Integer.parseInt(page));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("分页查询离线终端离线时长信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "分页查询离线终端离线时长信息异常！");
		}

		return returnmap;
	}

	@RequiresPermissions("mediaAttribute:activeMedia")
	@RequestMapping(value = "activeMedia")
	@ResponseBody
	public  Map<String,Object> activeMedia(HttpServletRequest request) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String machineCode_P = request.getParameter("machineCodes");
		//String zycmToken = request.getParameter("zycmToken");
		try{
			if(StringUtils.isNotBlank(machineCode_P)){
				mediaAttributeService.activeMedia(machineCode_P.split(","), 1);
				returnmap.put("result", "success");
				returnmap.put("message", "成功");
			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("激活终端异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "激活终端异常！"+e.getMessage());
		}

		return returnmap;
	}

	@RequiresPermissions("mediaAttribute:disActiveMedia")
	@RequestMapping(value = "disActiveMedia")
	@ResponseBody
	public  Map<String,Object> disActiveMedia(HttpServletRequest request) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String machineCode_P = request.getParameter("machineCodes");
		//String zycmToken = request.getParameter("zycmToken");
		try{
			if(StringUtils.isNotBlank(machineCode_P)){
				mediaAttributeService.activeMedia(machineCode_P.split(","), 0);
				returnmap.put("result", "success");
				returnmap.put("message", "成功");
			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("禁用终端异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "禁用终端异常！"+e.getMessage());
		}

		return returnmap;
	}

	@RequiresPermissions("mediaAttribute:changeFtp")
	@RequestMapping(value = "changeFtp")
	@ResponseBody
	public  Map<String,Object> changeFtp(HttpServletRequest request) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String mids_p = request.getParameter("mids");
		String ftpId_p = request.getParameter("ftpId");
		//String zycmToken = request.getParameter("zycmToken");
		try{
			if(StringUtils.isNotBlank(mids_p) && StringUtils.isNotBlank(ftpId_p)){
				String[] machineCodes = mids_p.split(",");
				mediaAttributeService.changeFtp(ArrayUtils.toInt(machineCodes), Integer.parseInt(ftpId_p));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");
			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("批量切换ftp异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "批量切换ftp异常！");
		}

		return returnmap;
	}

	@RequiresPermissions("mediaAttribute:updateMedia")
	@RequestMapping(value = "updateIccidByManual")
	@ResponseBody
	public  Map<String,Object> updateIccidByManual(HttpServletRequest request) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String machineCode_p = request.getParameter("machineCode");
		String iccid_p = request.getParameter("iccid");
		String iccidUpdateType_p = request.getParameter("iccidUpdateType");
		try{
			if(StringUtils.isNotBlank(machineCode_p) && StringUtils.isNotBlank(iccidUpdateType_p)){
				if("1".equals(iccidUpdateType_p) && StringUtils.isBlank(iccid_p)){
					returnmap.put("result", "error");
					returnmap.put("message", "手动更新模式时物联卡号不能为空");
					return returnmap;
				}

				mediaAttributeService.updateIccidAndIccidUpdateType(machineCode_p, iccid_p, Integer.parseInt(iccidUpdateType_p));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");
			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("手动更新iccid异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "手动更新iccid异常！"+e.getMessage());
		}

		return returnmap;
	}

	@RequiresPermissions(value={"mediaAttribute:selectPage","mediaAttribute:selectPage_2"},logical=Logical.OR)
	@RequestMapping(value = "selectPageMediaByUids",method=RequestMethod.POST)
	@ResponseBody
	public  Map<String,Object> selectPageMediaByUids(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String page = request.getParameter("page");
		String pageSize = request.getParameter("pageSize");
		String uids_p = cleanInvalidStr(request.getParameter("uids"));
		String mediaGroupIds_p = cleanInvalidStr(request.getParameter("mediaGroupIds"));

		try{
			if(StringUtils.isNotBlank(page) && StringUtils.isNotBlank(pageSize)
					&& StringUtils.isNotBlank(uids_p) && StringUtils.isNotBlank(mediaGroupIds_p)){
				Map<String,Object> parammap = new HashMap<String,Object>();

				parammap.put("uids", ArrayUtils.toInt(uids_p.split(",")));
				parammap.put("mediaGroupIds", ArrayUtils.toInt(mediaGroupIds_p.split(",")));

				returnmap = mediaAttributeService.selectPageMediaByUids(parammap, Integer.parseInt(pageSize), Integer.parseInt(page));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("分页查询用户的终端信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "分页查询用户的终端信息异常！");
		}

		return returnmap;
	}

	/*@RequiresPermissions("mediaAttribute:syncClientTime")*/
	@RequestMapping(value = "syncClientTime")
	@ResponseBody
	public  Map<String,Object> syncClientTime(HttpServletRequest request) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String machineCodes_p = request.getParameter("machineCodes");
		try{
			if(StringUtils.isNotBlank(machineCodes_p)){
				String[] machineCodes = machineCodes_p.split(",");
				mediaAttributeService.syncClientTime(machineCodes);
				returnmap.put("result", "success");
				returnmap.put("message", "成功");
			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("发送同步时间指令异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "发送同步时间指令异常！"+e.getMessage());
		}
		return returnmap;
	}

	/*@RequiresPermissions("mediaAttribute:syncAllClientTime")*/
	@RequestMapping(value = "syncAllClientTime")
	@ResponseBody
	public  Map<String,Object> syncAllClientTime(HttpServletRequest request) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		try{
			mediaAttributeService.syncAllClientTime();
			returnmap.put("result", "success");
			returnmap.put("message", "成功");
		}catch(Exception e){
			log.error("发送同步所有终端时间指令异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "发送同步所有终端时间指令异常！"+e.getMessage());
		}
		return returnmap;
	}

	@RequestMapping(value = "selectMediaByGroupId")
	@ResponseBody
	public  Map<String,Object> selectMediaByGroupId(HttpServletRequest request) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String mediaGroupIds_p = request.getParameter("mediaGroupIds");
		try{
			if(StringUtils.isNotBlank(mediaGroupIds_p)){
				String[] mediaGroupIds = mediaGroupIds_p.split(",");
				List<Map<String,Object>> mediaList = mediaAttributeService.selectMediaByGroupId(ArrayUtils.toInt(mediaGroupIds));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");
				returnmap.put("data", mediaList);
			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
				returnmap.put("data", null);
			}
		}catch(Exception e){
			log.error("根据终端组id查询终端信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "根据终端组id查询终端信息异常！"+e.getMessage());
			returnmap.put("data", null);
		}
		return returnmap;
	}

	@RequestMapping(value = "selectGroupIdByMachineCodes")
	@ResponseBody
	public  Map<String,Object> selectGroupIdByMachineCodes(HttpServletRequest request) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String machineCodes_p = request.getParameter("machineCodes");
		try{
			if(StringUtils.isNotBlank(machineCodes_p)){
				String[] machineCodes = machineCodes_p.split(",");
				List<Map<String,Object>> mediaList = mediaAttributeService.selectGroupIdByMachineCodes(machineCodes);
				returnmap.put("result", "success");
				returnmap.put("message", "成功");
				returnmap.put("data", mediaList);
			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
				returnmap.put("data", null);
			}
		}catch(Exception e){
			log.error("根据机器码查询终端组信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "根据机器码查询终端组信息异常！"+e.getMessage());
			returnmap.put("data", null);
		}
		return returnmap;
	}
}
