package com.zycm.zybao.web.backend;

import com.zycm.zybao.common.utils.ArrayUtils;
import com.zycm.zybao.service.interfaces.SysUserNotifyService;
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
@RequestMapping("/userNotify/*")
public class SysUserNotifyController extends BaseController {

	@Autowired(required = false)
	private SysUserNotifyService sysUserNotifyService;

	@RequiresPermissions("userNotify:selectPage")
	@RequestMapping(value = "selectPage")
	@ResponseBody
	public  Map<String,Object> selectPage(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String page = request.getParameter("page");
		String pageSize = request.getParameter("pageSize");
		String readStatus = cleanInvalidStr(request.getParameter("readStatus"));
		String notifyType = cleanInvalidStr(request.getParameter("type"));
		String message = cleanInvalidStr(request.getParameter("message"));

		try{
			if(StringUtils.isNotBlank(page) && StringUtils.isNotBlank(pageSize)){

				Map<String,Object> parammap = new HashMap<String,Object>();
				parammap.put("receiverId", getUserId(request));
				parammap.put("readStatus", StringUtils.isNotBlank(readStatus)?Integer.parseInt(readStatus):null);
				parammap.put("notifyType", StringUtils.isNotBlank(notifyType)?Integer.parseInt(notifyType):null);
				parammap.put("info", StringUtils.isNotBlank(message)?message:null);

				returnmap = sysUserNotifyService.selectPage(parammap,Integer.parseInt(page),Integer.parseInt(pageSize));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("分页查询预警通知信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "分页查询预警通知信息异常！");
		}

		return returnmap;
	}

	@RequestMapping(value = "selectCount")
	@ResponseBody
	public  Map<String,Object> selectCount(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		try{
			Map<String, Object> countmap = sysUserNotifyService.selectCount(getUserId(request));
			returnmap.put("result", "success");
			returnmap.put("message", "成功");
			returnmap.put("data", countmap);
		}catch(Exception e){
			log.error("查询用户未读的预警通知信息总数异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "查询用户未读的预警通知信息总数异常！");
			returnmap.put("data", null);
		}
		return returnmap;
	}

	@RequiresPermissions("userNotify:updateReadStatus")
	@RequestMapping(value = "updateReadStatus")
	@ResponseBody
	public  Map<String,Object> updateReadStatus(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String notifyIds_p = request.getParameter("notifyIds");

		try{
			if(StringUtils.isNotBlank(notifyIds_p)){
				Integer[] notifyIds = ArrayUtils.toInt(notifyIds_p.split(","));
				sysUserNotifyService.updateReadStatusByKeys(getUserId(request), notifyIds);
			}else{
				sysUserNotifyService.updateReadStatusByKeys(getUserId(request), null);
			}
			returnmap.put("result", "success");
			returnmap.put("message", "成功");
		}catch(Exception e){
			log.error("修改读取状态信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "修改读取状态信息异常！"+e.getMessage());
		}

		return returnmap;
	}

	@RequiresPermissions("userNotify:deleteByKeys")
	@RequestMapping(value = "deleteByKeys")
	@ResponseBody
	public  Map<String,Object> deleteByKeys(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String notifyIds_p = request.getParameter("notifyIds");

		try{
			if(StringUtils.isNotBlank(notifyIds_p)){
				Integer[] notifyIds = ArrayUtils.toInt(notifyIds_p.split(","));
				sysUserNotifyService.deleteByKeys(getUserId(request), notifyIds);
			}else{
				sysUserNotifyService.deleteByKeys(getUserId(request), null);
			}
			returnmap.put("result", "success");
			returnmap.put("message", "成功");
		}catch(Exception e){
			log.error("删除预警通知信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "删除预警通知信息异常！"+e.getMessage());
		}

		return returnmap;
	}

}
