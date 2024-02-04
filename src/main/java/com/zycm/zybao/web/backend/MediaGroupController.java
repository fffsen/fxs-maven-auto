package com.zycm.zybao.web.backend;

import com.zycm.zybao.common.utils.ArrayUtils;
import com.zycm.zybao.service.interfaces.MediaGroupService;
import com.zycm.zybao.service.interfaces.UserService;
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
import java.util.List;
import java.util.Map;




@Slf4j
@Controller
@RequestMapping("/mediaGroup/*")
public class MediaGroupController extends BaseController {

	@Autowired(required = false)
	private MediaGroupService mediaGroupService;

	@RequestMapping(value = "selectGroupTree")
	@ResponseBody
	public  Map<String,Object> selectGroupTree(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String isAll = request.getParameter("isAll");
		String spread = request.getParameter("spread");
		try{
			boolean sp = false;
			if(StringUtils.isNotBlank(spread) && "1".equals(spread)){
				sp = true;
			}
			Map<String,Object> map;
			if(getUGroupId(request) == this.SUPERUSERGROUPID){
				map = mediaGroupService.selectGroupTree(null,sp);
			}else{
				map = mediaGroupService.selectGroupTree(getUserId(request),sp);
			}
			returnmap.put("result", "success");
			returnmap.put("message", "成功");
			returnmap.put("data", map);
		}catch(Exception e){
			log.error("查询并构造终端组树信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "查询并构造终端组树信息异常！");
			returnmap.put("data", null);
		}

		return returnmap;
	}

	@RequestMapping(value = "selectGroupXTree")
	@ResponseBody
	public  Map<String,Object> selectGroupXTree(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		//String uid = request.getParameter("uid");
		//String isAll = request.getParameter("isAll");
		try{
			/*if(StringUtils.isNotBlank(uid)){*/
				Map<String,Object> map;
				if(getUGroupId(request) == this.SUPERUSERGROUPID){
					map = mediaGroupService.selectGroupXTree(null);
				}else{
					map = mediaGroupService.selectGroupXTree(getUserId(request));
				}
				/*if("1".equals(isAll)){
					map = mediaGroupService.selectGroupXTree(null);
				}else{
					//查询用户配置的终端组管理
					List<Map<String,Object>> glist = userService.selectGroupByUid(Integer.parseInt(uid));
					if(glist.size() == 1 && "1".equals(glist.get(0).get("isAll").toString())){
						map = mediaGroupService.selectGroupXTree(null);
					}else{
						map = mediaGroupService.selectGroupXTree(Integer.parseInt(uid));
					}
				}*/
				returnmap.put("result", "success");
				returnmap.put("message", "成功");
				returnmap.put("data", map);
			/*}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
				returnmap.put("data", null);
			}*/
		}catch(Exception e){
			log.error("查询并构造终端组树信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "查询并构造终端组树信息异常！");
			returnmap.put("data", null);
		}

		return returnmap;
	}

	@RequestMapping(value = "selectGroupXTreeUser")
	@ResponseBody
	public  Map<String,Object> selectGroupXTreeUser(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String uGroupId = request.getParameter("uGroupId");
		//String isAll = request.getParameter("isAll");//是否
		try{
			if(StringUtils.isNotBlank(uGroupId)){
				Map<String,Object> map = mediaGroupService.selectGroupXTreeUser(Integer.parseInt(uGroupId));//这个是查用户组配置的终端组信息

				returnmap.put("result", "success");
				returnmap.put("message", "成功");
				returnmap.put("data", map);
			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
				returnmap.put("data", null);
			}
		}catch(Exception e){
			log.error("根据用户查询用户组的终端组树信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "根据用户查询用户组的终端组树信息异常！");
			returnmap.put("data", null);
		}

		return returnmap;
	}

	@RequestMapping(value = "selectByGroupName")
	@ResponseBody
	public  Map<String,Object> selectByGroupName(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String mediaGroupName = request.getParameter("mediaGroupName");
		String mediaGroupId = request.getParameter("mediaGroupId");
		try{
			if(StringUtils.isNotBlank(mediaGroupName)){
				Map<String,Object> param = new HashMap<String,Object>();
				param.put("mediaGroupName", mediaGroupName);
				param.put("mediaGroupId", StringUtils.isNotBlank(mediaGroupId)?Integer.parseInt(mediaGroupId):null);
				List<Map<String,Object>> map = mediaGroupService.selectByGroupName(param);
				returnmap.put("result", "success");
				returnmap.put("message", "成功");
				returnmap.put("data", map);
			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
				returnmap.put("data", null);
			}
		}catch(Exception e){
			log.error("根据终端组名称查询信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "根据终端组名称查询信息异常！");
			returnmap.put("data", null);
		}

		return returnmap;
	}

	@RequiresPermissions("mediaGroup:addGroup")
	@RequestMapping(value = "addGroup")
	@ResponseBody
	public  Map<String,Object> addGroup(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String mediaGroupName = request.getParameter("mediaGroupName");
		String parentId = request.getParameter("parentId");
		//String uid = request.getParameter("uid");
		try{
			if(StringUtils.isNotBlank(mediaGroupName) && StringUtils.isNotBlank(parentId)){
				mediaGroupService.insert(mediaGroupName,Integer.parseInt(parentId),getUserId(request),getUGroupId(request));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");
				returnmap.put("data", null);
			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
				returnmap.put("data", null);
			}
		}catch(Exception e){
			log.error("添加终端组信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "添加终端组信息异常！"+e.getMessage());
			returnmap.put("data", null);
		}

		return returnmap;
	}

	@RequestMapping(value = "selectMediaByGroupIdAndChild")
	@ResponseBody
	public  Map<String,Object> selectMediaByGroupIdAndChild(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String mediaGroupId = request.getParameter("mediaGroupId");
		try{
			if(StringUtils.isNotBlank(mediaGroupId)){
				List<Map<String,Object>> map = mediaGroupService.selectMediaByGroupIdAndChild(Integer.parseInt(mediaGroupId));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");
				returnmap.put("data", map);
			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
				returnmap.put("data", null);
			}
		}catch(Exception e){
			log.error("根据终端组id查询其终端及其所有子节点终端信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "根据终端组id查询其终端及其所有子节点终端信息异常！");
			returnmap.put("data", null);
		}

		return returnmap;
	}

	@RequestMapping(value = "selectChildByGroupId")
	@ResponseBody
	public  Map<String,Object> selectChildByGroupId(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String mediaGroupId = request.getParameter("mediaGroupId");
		try{
			if(StringUtils.isNotBlank(mediaGroupId)){
				List<Integer> map = mediaGroupService.selectChildByGroupId(Integer.parseInt(mediaGroupId));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");
				returnmap.put("data", map);
			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
				returnmap.put("data", null);
			}
		}catch(Exception e){
			log.error("根据终端组id查询其所有子节点信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "根据终端组id查询其所有子节点信息异常！");
			returnmap.put("data", null);
		}

		return returnmap;
	}

	@RequiresPermissions("mediaGroup:deleteGroup")
	@RequestMapping(value = "deleteGroup")
	@ResponseBody
	public  Map<String,Object> deleteGroup(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String mediaGroupId = request.getParameter("mediaGroupId");
		try{
			if(StringUtils.isNotBlank(mediaGroupId)){
				if("0".equals(mediaGroupId) || "1".equals(mediaGroupId)){
					returnmap.put("result", "error");
					returnmap.put("message", "不能删除默认组和未分组");
					returnmap.put("data", null);
				}else{
					mediaGroupService.deleteByPrimaryKey(Integer.parseInt(mediaGroupId));
					returnmap.put("result", "success");
					returnmap.put("message", "成功");
					returnmap.put("data", null);
				}

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
				returnmap.put("data", null);
			}
		}catch(Exception e){
			log.error("删除终端组信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "删除终端组信息异常！"+e.getMessage());
			returnmap.put("data", null);
		}

		return returnmap;
	}

	@RequiresPermissions("mediaGroup:updateGroupName")
	@RequestMapping(value = "updateGroupName")
	@ResponseBody
	public  Map<String,Object> updateGroupName(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String mediaGroupId = request.getParameter("mediaGroupId");
		String mediaGroupName = request.getParameter("mediaGroupName");
		try{
			if(StringUtils.isNotBlank(mediaGroupId) && StringUtils.isNotBlank(mediaGroupName)){
				if("0".equals(mediaGroupId) || "1".equals(mediaGroupId)){
					returnmap.put("result", "error");
					returnmap.put("message", "不能修改默认组和未分组的名称");
					returnmap.put("data", null);
				}else{
					mediaGroupService.updateGroupName(mediaGroupName, Integer.parseInt(mediaGroupId));
					returnmap.put("result", "success");
					returnmap.put("message", "成功");
					returnmap.put("data", null);
				}
			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
				returnmap.put("data", null);
			}
		}catch(Exception e){
			log.error("修改终端组名称异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "修改终端组名称异常！"+e.getMessage());
			returnmap.put("data", null);
		}

		return returnmap;
	}


	@RequestMapping(value = "selectGroupIdByMids")
	@ResponseBody
	public  Map<String,Object> selectGroupIdByMids(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String mids_p = request.getParameter("mids");
		try{
			if(StringUtils.isNotBlank(mids_p)){
				Integer[] mids = ArrayUtils.toInt(mids_p.split(","));
				List<Map<String,Object>> map = mediaGroupService.selectGroupIdByMids(mids);
				returnmap.put("result", "success");
				returnmap.put("message", "成功");
				returnmap.put("data", map);
			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
				returnmap.put("data", null);
			}
		}catch(Exception e){
			log.error("根据多个终端id查询组id信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "根据多个终端id查询组id信息异常！");
			returnmap.put("data", null);
		}

		return returnmap;
	}

	@RequestMapping(value = "selectPageLastGroup")
	@ResponseBody
	public  Map<String,Object> selectPageLastGroup(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String page = request.getParameter("page");
		String pageSize = request.getParameter("pageSize");
		String mediaGroupName = request.getParameter("mediaGroupName");
		try{
			if(StringUtils.isNotBlank(page) && StringUtils.isNotBlank(pageSize) ){

				Map<String, Object> param = new HashMap<String, Object>();
				param.put("mediaGroupName", StringUtils.isNotBlank(mediaGroupName)?mediaGroupName:null);
				returnmap = mediaGroupService.selectPageLastGroup(Integer.parseInt(page), Integer.parseInt(pageSize),param);
				returnmap.put("result", "success");
				returnmap.put("message", "成功");

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("分页查询排除了已使用的同步终端组后的最末终端信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "分页查询排除了已使用的同步终端组后的最末终端信息异常！");
		}

		return returnmap;
	}
}
