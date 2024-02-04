package com.zycm.zybao.service.impl;

import com.zycm.zybao.common.utils.MD5;
import com.zycm.zybao.dao.*;
import com.zycm.zybao.model.entity.SysuserMediagroupModel;
import com.zycm.zybao.model.entity.UserModel;
import com.zycm.zybao.model.entity.UserRolesModel;
import com.zycm.zybao.model.vo.UserVo;
import com.zycm.zybao.service.interfaces.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired(required = false)
	private UserDao userDao;
	@Autowired(required = false)
	private UserRolesDao userRolesDao;
	@Autowired(required = false)
	private SysuserMediagroupDao sysuserMediagroupDao;
	@Autowired(required = false)
	private SysuserGroupMediagroupDao sysuserGroupMediagroupDao;

	@Override
	public Map<String, Object> selectPage(Map<String, Object> param, Integer page, Integer pageSize) {
		Map<String, Object> returndata = new HashMap<String, Object>();
		Integer totalCount = userDao.selectPageCount(param);
		if(null != totalCount && totalCount > 0){
			//总页数
	        Double totalPage = Math.ceil(totalCount/Double.parseDouble(pageSize+""));
	        int startRow = (page-1)*pageSize;
	        param.put("startRow", startRow);
	        param.put("pageSize", pageSize);
	        List<Map<String,Object>> list = userDao.selectPage(param);

	        returndata.put("dataList", list);
	        returndata.put("totalPage", totalPage);

		}else{
			returndata.put("dataList", new ArrayList());
	        returndata.put("totalPage", 0);
		}
		returndata.put("page", page);
		returndata.put("pageSize", pageSize);
		returndata.put("total", totalCount);

		return returndata;
	}

	@Override
	public void insert(UserVo userVo) {
		UserModel userModel = userVo.toModel();
		userModel.setUserStatus(0);
		//userModel.setUserType(0);
		userModel.setPassword(MD5.MD5AndMore(userModel.getPassword()));
		userModel.setRegTime(new Date());
		userDao.insert(userModel);
		//新加角色信息
		UserRolesModel userRolesModel = new UserRolesModel();
		userRolesModel.setUid(userModel.getUid());
		userRolesModel.setRoleId(userVo.getRoleId());
		userRolesDao.insert(userRolesModel);
		//处理用户配置的终端分组
		List<SysuserMediagroupModel> list = new ArrayList<SysuserMediagroupModel>();
		if(userVo.getIsAll() == 1){//可操作所有
			//查询该用户的用户组配置的所有终端组信息  直接复制数据给用户
			List<Map<String,Object>> userGroupList = sysuserGroupMediagroupDao.selectByUgroupId(userVo.getuGroupId());
			if(userGroupList.size() > 0 ){
				for (Map<String, Object> map : userGroupList) {
					SysuserMediagroupModel smm = new SysuserMediagroupModel();
					smm.setUid(userModel.getUid());
					smm.setMediaGroupId(Integer.parseInt(map.get("mediaGroupId").toString()));
					smm.setIsAll(0);
					list.add(smm);
				}
				sysuserMediagroupDao.batchInsert(list);
			}else{
				log.error("用户组【"+userVo.getuGroupId()+"】没有配置终端组数据!");
			}

		}else{
			String[] mediaGroupIds = userVo.getMediaGroupIds().split(",");
			if(mediaGroupIds.length > 0){
				for (int i = 0; i < mediaGroupIds.length; i++) {
					if(StringUtils.isNotBlank(mediaGroupIds[i])){
						SysuserMediagroupModel smm = new SysuserMediagroupModel();
						smm.setUid(userModel.getUid());
						smm.setMediaGroupId(Integer.parseInt(mediaGroupIds[i]));
						smm.setIsAll(0);
						list.add(smm);
					}
				}
				sysuserMediagroupDao.batchInsert(list);
			}else{
				log.error("新增用户时没有配置终端组数据!");
			}
		}

	}

	@Override
	public Map<String, Object> selectByPrimaryKey(Integer uid) {
		return userDao.selectByPrimaryKey(uid);
	}

	@Override
	public void updateByPrimaryKey(UserVo userVo) {
		UserModel userModel = userVo.toModel();
		userDao.updateByPrimaryKey(userModel);

		userRolesDao.deleteByUid(new Integer[]{userVo.getUid()});
		UserRolesModel userRolesModel = new UserRolesModel();
		userRolesModel.setUid(userModel.getUid());
		userRolesModel.setRoleId(userVo.getRoleId());
		userRolesDao.insert(userRolesModel);

		//处理用户配置的终端分组
		List<SysuserMediagroupModel> list = new ArrayList<SysuserMediagroupModel>();
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("uid", userModel.getUid());
		if(userVo.getIsAll() == 1){//可操作所有
			param.put("isSyncUserGroup", 1);
			userDao.updateSync(param);
			//先清理之前的数据
			sysuserMediagroupDao.deleteByUid(new Integer[]{userModel.getUid()});
			//查询该用户的用户组配置的所有终端组信息  直接复制数据给用户
			List<Map<String,Object>> userGroupList = sysuserGroupMediagroupDao.selectByUgroupId(userVo.getuGroupId());
			if(userGroupList.size() > 0 ){
				for (Map<String, Object> map : userGroupList) {
					SysuserMediagroupModel smm = new SysuserMediagroupModel();
					smm.setUid(userModel.getUid());
					smm.setMediaGroupId(Integer.parseInt(map.get("mediaGroupId").toString()));
					smm.setIsAll(0);
					list.add(smm);
				}
				sysuserMediagroupDao.batchInsert(list);
			}else{
				log.error("用户组【"+userVo.getuGroupId()+"】没有配置终端组数据!");
			}

		}else{
			param.put("isSyncUserGroup", 0);
			userDao.updateSync(param);

			String[] mediaGroupIds = userVo.getMediaGroupIds().split(",");
			if(mediaGroupIds.length > 0){
				for (int i = 0; i < mediaGroupIds.length; i++) {
					if(StringUtils.isNotBlank(mediaGroupIds[i])){
						SysuserMediagroupModel smm = new SysuserMediagroupModel();
						smm.setUid(userModel.getUid());
						smm.setMediaGroupId(Integer.parseInt(mediaGroupIds[i]));
						smm.setIsAll(0);
						list.add(smm);
					}
				}
				//先清理之前的数据
				sysuserMediagroupDao.deleteByUid(new Integer[]{userModel.getUid()});

				sysuserMediagroupDao.batchInsert(list);
			}else{
				log.error("新增用户时没有配置终端组数据!");
			}
		}

	}

	@Override
	public void updateByPrimaryKey2(UserVo userVo) {
		UserModel userModel = userVo.toModel();
		userModel.setUserName(null);
		userModel.setuGroupId(null);
		userModel.setIsSyncUserGroup(null);
		userDao.updateByPrimaryKey(userModel);
	}
	@Override
	public void updatePwd(Integer uid,String oldPassword,String newPassword) throws Exception {
		//验证旧密码
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("uid", uid);
		param.put("password", MD5.MD5AndMore(oldPassword));
		Map<String, Object> user = userDao.selectUidAndPwd(param);
		if(null != user && StringUtils.isNotBlank(user.get("uid").toString())){
			//修改密码
			param.put("password", MD5.MD5AndMore(newPassword));
			userDao.updatePwd(param);
		}else{
			log.error("输入的原始密码不正确！原始密码："+oldPassword);
			throw new Exception("输入的原始密码不正确！");
		}

	}

	@Override
	public void adminUpdatePwd(Integer uid,Integer operatorId,String newPassword) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("uid", uid);
		param.put("password", MD5.MD5AndMore(newPassword));
		userDao.updatePwd(param);

	}

	@Override
	public void updateStatus(Integer uid,Integer userStatus,Date date) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("uid", uid);
		param.put("userStatus", userStatus);
		param.put("lastTime", date);
		userDao.updateStatus(param);
	}

	@Override
	public void updateIsDelete(Integer[] uids) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("uids", uids);
		param.put("isDelete", 0);
		userDao.updateIsDelete(param);

		userRolesDao.deleteByUid(uids);

		//清理之前的数据
		sysuserMediagroupDao.deleteByUid(uids);
	}

	@Override
	public Map<String,Object> selectByUserName(Map<String,Object> param) {
		List<Map<String,Object>> users = userDao.selectByUserName(param);
		if(users.size() > 0){
			return users.get(0);
		}
		return null;
	}

	@Override
	public Map<String, Object> backLogin(String userName, String pwd) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userName", userName);
		param.put("password", MD5.MD5AndMore(pwd));
		return userDao.selectUserAndPwd(param);
	}

	@Override
	public List<Map<String, Object>> selectGroupByUid(Integer uid) {
		//延用之前的处理逻辑 如果是同步用户组的  就返回一个对象
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> userMap = userDao.selectByPrimaryKey(uid);
		if(userMap != null){
			String isSyncUserGroup = userMap.get("isSyncUserGroup").toString();
			if("1".equals(isSyncUserGroup)){
				 Map<String, Object> map = new HashMap<String, Object>();
				 map.put("uid", uid);
				 map.put("mediaGroupId", null);
				 map.put("isAll", isSyncUserGroup);
				 list.add(map);
			}else{
				list = sysuserMediagroupDao.selectByUid(uid);
			}
		}

		return list;
	}

	@Override
	public String selectUsersByUgroupId(Integer uid) {
		return userDao.selectUsersByUgroupId(uid);
	}

	@Override
	public Map<String, Object> selectByOpenId(String openId) {
		return userDao.selectByOpenId(openId);
	}

	@Override
	public List<Map<String, Object>> selectByCondition(Map<String, Object> param) {
		return userDao.selectByCondition(param);
	}

	@Override
	public void updateWarnNotice(Integer uid, Integer warnNotice) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("uid", uid);
		param.put("warnNotice", warnNotice);
		userDao.updateWarnNotice(param);
	}


}
