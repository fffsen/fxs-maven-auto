package com.zycm.zybao.web.backend;

import com.zycm.zybao.common.utils.MD5;
import com.zycm.zybao.model.vo.UserVo;
import com.zycm.zybao.service.interfaces.UserLogService;
import com.zycm.zybao.service.interfaces.UserService;
import com.zycm.zybao.web.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




@Slf4j
@Controller
@RequestMapping("/sysUser/*")
public class UserController extends BaseController {

	@Autowired(required = false)
	private UserService userService;
	@Autowired(required = false)
	private UserLogService userLogService;

	@RequiresPermissions("sysUser:selectPage")
	@RequestMapping(value = "selectPage")
	@ResponseBody
	public  Map<String,Object> selectPage(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String page = request.getParameter("page");
		String pageSize = request.getParameter("pageSize");
		String userName = request.getParameter("userName");
		String uGroupId = cleanInvalidStr(request.getParameter("uGroupId"));
		try{
			if(StringUtils.isNotBlank(page) && StringUtils.isNotBlank(pageSize) ){
				Map<String,Object> parammap = new HashMap<String,Object>();
				parammap.put("userName", userName);
				if(getUGroupId(request) == this.SUPERUSERGROUPID){
					parammap.put("uGroupId", StringUtils.isNotBlank(uGroupId)?Integer.parseInt(uGroupId):null);
				}else{
					parammap.put("uGroupId", getUGroupId(request));
				}

				returnmap = userService.selectPage(parammap, Integer.parseInt(page), Integer.parseInt(pageSize));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("分页查询系统用户信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "分页查询系统用户信息异常！");
		}

		return returnmap;
	}

	@RequiresPermissions("sysUser:addUser")
	@RequestMapping(value = "addUser")
	@ResponseBody
	public  Map<String,Object> addUser(UserVo userVo, HttpServletRequest request) {
		Map<String,Object> returnmap = new HashMap<String,Object>();

		try{
			String error = userVo.isNotEmpty();
			if(null != userVo && StringUtils.isBlank(error) ){
				if(userVo.getRoleId() == 1){
					returnmap.put("result", "error");
					returnmap.put("message", "数据错误");
					return returnmap;
				}
				userService.insert(userVo);
				returnmap.put("result", "success");
				returnmap.put("message", "成功");

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", StringUtils.isBlank(error)?"必传参数不能为空":error);
			}
		}catch(Exception e){
			log.error("增加系统用户信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "增加系统用户信息异常！");
		}

		return returnmap;
	}

	@RequestMapping(value = "selectByPrimaryKey")
	@ResponseBody
	public  Map<String,Object> selectByPrimaryKey(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String uid = request.getParameter("uid");

		try{
			if(StringUtils.isNotBlank(uid) ){

				Map<String,Object> user = userService.selectByPrimaryKey(Integer.parseInt(uid));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");
				returnmap.put("data", user);
			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
				returnmap.put("data", null);
			}
		}catch(Exception e){
			log.error("根据用户id查询系统用户信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "根据用户id查询系统用户信息异常！");
			returnmap.put("data", null);
		}

		return returnmap;
	}
	@RequestMapping(value = "selectByUserName")
	@ResponseBody
	public  Map<String,Object> selectByUserName(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String userName = request.getParameter("userName");
		String uid = request.getParameter("uid");
		try{
			if(StringUtils.isNotBlank(userName) ){
				Map<String,Object> param = new HashMap<String,Object>();
				param.put("userName", userName);
				param.put("uid", uid);
				Map<String,Object> user = userService.selectByUserName(param);
				returnmap.put("result", "success");
				returnmap.put("message", "成功");
				returnmap.put("data", user);
			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
				returnmap.put("data", null);
			}
		}catch(Exception e){
			log.error("根据用户名查询系统用户信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "根据用户名查询系统用户信息异常！");
			returnmap.put("data", null);
		}

		return returnmap;
	}

	/**
	* @Title: updateByPrimaryKey
	* @Description: 修改系统用户信息
	* @param userVo
	* @param request
	* @return    参数
	* @author sy
	* @throws
	* @return Map<String,Object>    返回类型
	*
	*/
	@RequiresPermissions("sysUser:updateByPrimaryKey")
	@RequestMapping(value = "updateByPrimaryKey")
	@ResponseBody
	public  Map<String,Object> updateByPrimaryKey(UserVo userVo,HttpServletRequest request) {
		Map<String,Object> returnmap = new HashMap<String,Object>();

		try{
			String error = userVo.isNotEmpty2();
			if(null != userVo && StringUtils.isBlank(error) && userVo.getUid() > 0 ){
				if(userVo.getRoleId() == 1){
					returnmap.put("result", "error");
					returnmap.put("message", "数据错误");
					return returnmap;
				}
				userService.updateByPrimaryKey(userVo);
				returnmap.put("result", "success");
				returnmap.put("message", "成功");

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", StringUtils.isBlank(error)?"必传参数不能为空":error);
			}
		}catch(Exception e){
			log.error("根据用户id修改用户信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "根据用户id修改用户信息异常！");
		}

		return returnmap;
	}
	/**
	* @Title: updateByPrimaryKey2
	* @Description: 修改当前登录用户的信息
	* @param userVo
	* @param request
	* @return    参数
	* @author sy
	* @throws
	* @return Map<String,Object>    返回类型
	*
	*/
	@RequestMapping(value = "updateByPrimaryKey2")
	@ResponseBody
	public  Map<String,Object> updateByPrimaryKey2(UserVo userVo,HttpServletRequest request) {
		Map<String,Object> returnmap = new HashMap<String,Object>();

		try{
			userVo.setUid(getUserId(request));
			String error = userVo.isNotEmpty3();
			if(null != userVo && StringUtils.isBlank(error)){

				userService.updateByPrimaryKey2(userVo);
				returnmap.put("result", "success");
				returnmap.put("message", "成功");

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", StringUtils.isBlank(error)?"必传参数不能为空":error);
			}
		}catch(Exception e){
			log.error("根据用户id修改用户信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "根据用户id修改用户信息异常！");
		}

		return returnmap;
	}

	@RequestMapping(value = "updatePwd")
	@ResponseBody
	public  Map<String,Object> updatePwd(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String oldPassword = request.getParameter("oldPassword");
		String newPassword = request.getParameter("newPassword");

		try{
			if(StringUtils.isNotBlank(newPassword) && StringUtils.isNotBlank(oldPassword)){
				userService.updatePwd(getUserId(request), oldPassword,newPassword);
				returnmap.put("result", "success");
				returnmap.put("message", "成功");
				returnmap.put("data", null);

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
				returnmap.put("data", null);
			}
		}catch(Exception e){
			log.error("修改用户密码异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "修改用户密码异常！"+e.getMessage());
			returnmap.put("data", null);
		}

		return returnmap;
	}

	@RequiresPermissions("sysUser:adminUpdatePwd")
	@RequestMapping(value = "adminUpdatePwd")
	@ResponseBody
	public  Map<String,Object> adminUpdatePwd(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String uid = request.getParameter("uid");
		String newPassword = request.getParameter("newPassword");

		try{
			if(StringUtils.isNotBlank(newPassword) && StringUtils.isNotBlank(uid)){
				userService.adminUpdatePwd(Integer.parseInt(uid), getUserId(request),newPassword);
				returnmap.put("result", "success");
				returnmap.put("message", "成功");
				returnmap.put("data", null);

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
				returnmap.put("data", null);
			}
		}catch(Exception e){
			log.error("重置用户密码异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "重置用户密码异常！"+e.getMessage());
			returnmap.put("data", null);
		}

		return returnmap;
	}

	@RequiresPermissions("sysUser:updateIsDelete")
	@RequestMapping(value = "updateIsDelete")
	@ResponseBody
	public  Map<String,Object> updateIsDelete(HttpServletRequest request) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String uids = request.getParameter("uids");

		try{
			if(StringUtils.isNotBlank(uids) ){
				String[] uidstr = uids.split(",");
				Integer[] uidint = new Integer[uidstr.length];
				Integer usid = getUserId(request);
				for (int i = 0; i < uidstr.length; i++) {
					String uid = uidstr[i];
					if(StringUtils.isNotBlank(uid)){
						uidint[i] = Integer.parseInt(uid);
						if(uidint[i] == usid.intValue()){
							returnmap.put("result", "error");
							returnmap.put("message", "不能删除当前登录用户！");
							return returnmap;
						}
					}

				}
				userService.updateIsDelete(uidint);
				returnmap.put("result", "success");
				returnmap.put("message", "成功");

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("删除系统用户异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "删除系统用户异常！");
		}

		return returnmap;
	}

	/** 后台登录接口
	* @Title: backLogin
	* @Description: TODO
	* @author sy
	* @param @param request
	* @param @param response
	* @param @return
	* @return Map<String,Object>
	* @throws
	*/
	@RequestMapping(value = "/backLogin")
	public @ResponseBody Map<String,Object> backLogin(HttpServletRequest request,
			HttpServletResponse response){
		Map<String,Object> map = new HashMap<String, Object>();
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");

		try{

			Map<String,Object>  usersModel = null;
			if(StringUtils.isNotBlank(userName)&&StringUtils.isNotBlank(password)){
				usersModel =userService.backLogin(userName, password);
				if(usersModel != null){
					Integer uid= Integer.parseInt(usersModel.get("uid").toString());

					//登录失败限次
					int fnum = userLogService.selectLoginFailByUid(uid);
					if(fnum >=5){
						map.put("result", "error");
						map.put("message", "该账户已5次登录失败账户已锁！");
						map.put("data", null);
					}else{
						userService.updateStatus(uid, 1,new Date());
						userLogService.insert(uid, 1, "用户登录成功");
						map.put("result", "success");
						map.put("message", "登录成功");
						SecurityUtils.getSecurityManager().logout(SecurityUtils.getSubject());
						// 登录后存放进shiro token
						UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
						SecurityUtils.getSubject().login(token);
						//token为一位随机数加sessionid
						String zycmToken = MD5.MD5(uid+"::"+userName+"::"+SecurityUtils.getSubject().getSession().getId().toString(),1989);
						usersModel.put("zycmToken", zycmToken);
						map.put("data", usersModel);

						loginMap.put(zycmToken, usersModel);
					}
				}else{
					Map<String,Object> param = new HashMap<String,Object>();
					param.put("userName", userName);
					Map<String,Object> us = userService.selectByUserName(param);
					if(us != null){
						userLogService.insert(Integer.parseInt(us.get("uid").toString()), 4, request.getRemoteHost()+" 使用了账户:"+userName+",密码:"+password+"尝试登录！");
						//登录失败限次
						int fnum = userLogService.selectLoginFailByUid(Integer.parseInt(us.get("uid").toString()));
						if(fnum <5){
							map.put("result", "error");
							map.put("message", "该账户还剩"+(5 - fnum)+"次登录机会！");
							map.put("data", null);
						}else{
							map.put("result", "error");
							map.put("message", "该账户已5次登录失败账户已锁！");
							map.put("data", null);
						}
					}else{
						userLogService.insert(-99, 4, request.getRemoteHost()+" 使用了账户:"+userName+",密码:"+password+"尝试登录！");
						map.put("result", "error");
						map.put("message", "登录用户的账户或密码错误");
						map.put("data", null);
					}
				}
			}else{
				map.put("result", "error");
				map.put("message", "登录用户的账户或密码为空");
				map.put("data", null);
			}

		}catch(Exception e){
			log.error("登录异常:", e);
			map.put("result", "error");
			map.put("message", "登录异常");
			map.put("data", null);
		}
		return map;
	}

	/** 后台登出接口
	* @Title: backLogin
	* @Description: TODO
	* @author sy
	* @param @param request
	* @param @param response
	* @param @return
	* @return Map<String,Object>
	* @throws
	*/
	@RequestMapping(value = "/backLoginOut")
	public @ResponseBody Map<String,Object> backLoginOut(HttpServletRequest request,
			HttpServletResponse response){
		Map<String,Object> map = new HashMap<String, Object>();
		try{
			Map<String,Object>  usersModel = null;
			userService.updateStatus(getUserId(request), 0,null);
			userLogService.insert(getUserId(request), 1, "用户退出登录");
			SecurityUtils.getSecurityManager().logout(SecurityUtils.getSubject());
			map.put("result", "success");
			map.put("message", "退出登录成功");
			map.put("data", usersModel);

		}catch(Exception e){
			log.error("退出登录异常:", e);
			map.put("result", "error");
			map.put("message", "退出登录异常");
			map.put("data", null);
		}
		return map;
	}

	@RequestMapping(value = "selectGroupByUid")
	@ResponseBody
	public  Map<String,Object> selectGroupByUid(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String uid = request.getParameter("uid");

		try{
			if(StringUtils.isNotBlank(uid) ){

				List<Map<String,Object>> list = userService.selectGroupByUid(Integer.parseInt(uid));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");
				returnmap.put("data", list);
			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
				returnmap.put("data", null);
			}
		}catch(Exception e){
			log.error("根据用户id查询系统用户配置的终端组信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "根据用户id查询系统用户配置的终端组信息异常！");
			returnmap.put("data", null);
		}

		return returnmap;
	}

	@RequestMapping(value = "updateWarnNotice")
	@ResponseBody
	public  Map<String,Object> updateWarnNotice(HttpServletRequest request,HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String warnNotice = request.getParameter("warnNotice");
		try{
			if(StringUtils.isNotBlank(warnNotice)){
				userService.updateWarnNotice(getUserId(request), Integer.parseInt(warnNotice));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("更新用户预警通知信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "更新用户预警通知信息异常！");
		}

		return returnmap;
	}
}
