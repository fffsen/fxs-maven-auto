package com.zycm.zybao.service.impl;

import com.zycm.zybao.dao.*;
import com.zycm.zybao.model.entity.MediaGroupModel;
import com.zycm.zybao.model.entity.SysUserGroupModel;
import com.zycm.zybao.model.entity.SysuserGroupMediagroupModel;
import com.zycm.zybao.model.entity.SysuserMediagroupModel;
import com.zycm.zybao.service.interfaces.SysUserGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;


@Slf4j
@Service("sysUserGroupService")
public class SysUserGroupServiceImpl implements SysUserGroupService {

	@Autowired(required = false)
	private UserDao userDao;

	@Autowired(required = false)
	private SysUserGroupDao sysUserGroupDao;

	@Autowired(required = false)
	private MediaGroupDao mediaGroupDao;

	@Autowired(required = false)
	private SysuserGroupMediagroupDao sysuserGroupMediagroupDao;
	@Autowired(required = false)
	private SysuserMediagroupDao sysuserMediagroupDao;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

	@Override
	public Map<String, Object> selectPage(Map<String, Object> param, Integer page, Integer pageSize) {
		Map<String, Object> returndata = new HashMap<String, Object>();
		Integer totalCount = sysUserGroupDao.selectPageCount(param);
		if(null != totalCount && totalCount > 0){
			//总页数
	        Double totalPage = Math.ceil(totalCount/Double.parseDouble(pageSize+""));
	        int startRow = (page-1)*pageSize;
	        param.put("startRow", startRow);
	        param.put("pageSize", pageSize);
	        List<Map<String,Object>> list = sysUserGroupDao.selectPage(param);

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
	public List<Map<String, Object>> selectList(Map<String, Object> param) {
		return sysUserGroupDao.selectList(param);
	}

	@Override
	public void deleteByPrimaryKey(Integer uGroupId) throws Exception{
		Map<String,Object> parammap = new HashMap<String,Object>();
		parammap.put("uGroupId", uGroupId);
		Integer count = userDao.selectPageCount(parammap);
		if(count > 0){
			throw new Exception("用户组中存在用户，请先核查所删除的用户！");
		}else{
			sysUserGroupDao.deleteByPrimaryKey(uGroupId);
			//清理 用户组的终端组信息
			sysuserGroupMediagroupDao.deleteByUgroupId(new Integer[]{uGroupId});
		}
	}

	@Override
	public void insert(String userGroupName) throws Exception{
		//验证名称重复
		Map<String,Object> parammap = new HashMap<String,Object>();
		parammap.put("userGroupName", userGroupName);
		parammap.put("uGroupId", null);
		List<Map<String,Object>> gnamelist = sysUserGroupDao.validName(parammap);
		if(gnamelist.size() > 0){
			throw new Exception("用户组名称已存在！");
		}
		Date date = new Date();
		//添加商户用户组
		SysUserGroupModel sysUserGroupModel = new SysUserGroupModel();
		sysUserGroupModel.setUserGroupName(userGroupName);
		sysUserGroupModel.setCreateTime(date);
		sysUserGroupDao.insert(sysUserGroupModel);

		//验证组名重复 重复则名称加上创建时间
		String newUserGroupName = userGroupName;
		parammap.put("mediaGroupName", newUserGroupName+"的终端组");
		List<Map<String, Object>> mediaGoupList = mediaGroupDao.selectByGroupName(parammap);
		if(mediaGoupList.size() > 0){//如果已存在则加入创建时间
			newUserGroupName = newUserGroupName+"-"+sdf.format(date);
		}
		//添加商户的固定专属默认组与未分组
		MediaGroupModel mediaGroupModel = new MediaGroupModel();
		mediaGroupModel.setMediaGroupName(newUserGroupName+"的终端组");
		mediaGroupModel.setParentId(0);
		mediaGroupModel.setIsFixed(1);
		mediaGroupDao.insert(mediaGroupModel);
		MediaGroupModel mediaGroupModel2 = new MediaGroupModel();
		mediaGroupModel2.setMediaGroupName(newUserGroupName+"的未分组");
		mediaGroupModel2.setParentId(mediaGroupModel.getMediaGroupId());
		mediaGroupModel2.setIsFixed(1);
		mediaGroupDao.insert(mediaGroupModel2);
		//添加默认组到用户组上
		List<SysuserGroupMediagroupModel> list = new ArrayList<SysuserGroupMediagroupModel>();
		for (int i = 0; i < 2; i++) {
			SysuserGroupMediagroupModel sgm = new SysuserGroupMediagroupModel();
			sgm.setuGroupId(sysUserGroupModel.getuGroupId());
			if(i == 0){
				sgm.setMediaGroupId(mediaGroupModel.getMediaGroupId());
			}else{
				sgm.setMediaGroupId(mediaGroupModel2.getMediaGroupId());
			}
			list.add(sgm);
		}
		sysuserGroupMediagroupDao.batchInsert(list);
	}

	@Override
	public void updateGroupName(Integer uGroupId, String userGroupName) throws Exception{
		//验证名称重复
		Map<String,Object> parammap = new HashMap<String,Object>();
		parammap.put("userGroupName", userGroupName);
		parammap.put("uGroupId", uGroupId);
		List<Map<String,Object>> gnamelist = sysUserGroupDao.validName(parammap);
		if(gnamelist.size() > 0){
			throw new Exception("用户组名称已存在！");
		}

		SysUserGroupModel sysUserGroupModel = new SysUserGroupModel();
		sysUserGroupModel.setuGroupId(uGroupId);
		sysUserGroupModel.setUserGroupName(userGroupName);
		sysUserGroupDao.updateGroupName(sysUserGroupModel);

		//修改对应固定组的名称
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("uGroupId", uGroupId);
		param.put("isFixed", 1);
		List<Map<String, Object>> list = sysuserGroupMediagroupDao.selectUserGroupOfFixed(param);
		if(list.size() == 2){
			for (int i = 0; i < 2; i++) {
				String mediaGroupId = list.get(i).get("mediaGroupId").toString();
				String name = list.get(i).get("mediaGroupName").toString();
				if(name.contains("的终端组")){
					if(name.contains("-")){
						name = userGroupName+"-"+name.split("-")[1];
					}else{
						name = userGroupName+"的终端组";
					}
				}
				if(name.contains("的未分组")){
					if(name.contains("-")){
						name = userGroupName+"-"+name.split("-")[1];
					}else{
						name = userGroupName+"的未分组";
					}

				}
				MediaGroupModel mediaGroupModel = new MediaGroupModel();
				mediaGroupModel.setMediaGroupName(name);
				mediaGroupModel.setMediaGroupId(Integer.parseInt(mediaGroupId));
				mediaGroupDao.updateGroupName(mediaGroupModel);
			}
		}
	}

	@Override
	public List<Map<String, Object>> validName(Map<String, Object> param) {
		return sysUserGroupDao.validName(param);
	}

	@Override
	public void confMediaGroupToUserGroup(Integer uGroupId, Integer[] groudIds) throws Exception {
		List<SysuserGroupMediagroupModel> sgmgmList = new ArrayList<SysuserGroupMediagroupModel>();
		List<Integer> cancelMediaGroup = new ArrayList<Integer>();

		//验证选择的终端组是否有2个以上的固定未分组存在 一个用户组也就是一个商户  只允许存在一个固定的未分组
		Map<String, Object> param2 = new HashMap<String, Object>();
		param2.put("groupIds", groudIds);
		List<Map<String,Object>> vaildList = sysuserGroupMediagroupDao.vaildNoGroup(param2);
		if(vaildList.size() != 1){
			if(vaildList.size() == 0){
				throw new Exception("用户组必须配置一个固定的未分组！");
			}
			if(vaildList.size() > 1){
				String gname = "";
				for (Map<String, Object> map : vaildList) {
					gname += ","+map.get("mediaGroupName").toString();
				}
				throw new Exception("用户组不能配置多个固定的未分组！请从["+gname.substring(1)+"]中只勾选一个");
			}
		}


		//先判断该用户组是否之前已经配置过
		List<Map<String,Object>> userGroupOfMediaGroup = sysuserGroupMediagroupDao.selectByUgroupId(uGroupId);
		if(userGroupOfMediaGroup.size() > 0 ){
			//筛选出此次修改配置  已去掉的终端组项  以方便做后面的处理  处理非同步用户的数据删除
			for (Map<String, Object> mediaGroupMap : userGroupOfMediaGroup){
				Integer oldMediaGroupId = Integer.parseInt(mediaGroupMap.get("mediaGroupId").toString());
				for (int i = 0; i < groudIds.length; i++) {
					if(oldMediaGroupId.intValue() == groudIds[i].intValue()){//如果已存在 说明此终端组未取消  继续下一个对比
						break;
					}
					if(i == (groudIds.length - 1)){//最后一次对比依然不相等
						cancelMediaGroup.add(oldMediaGroupId);
					}
				}
			}
			//已有就先删除 再添加
			sysuserGroupMediagroupDao.deleteByUgroupId(new Integer[]{uGroupId});
		}

		for (int i = 0; i < groudIds.length; i++) {
			SysuserGroupMediagroupModel sgmgm = new SysuserGroupMediagroupModel();
			sgmgm.setMediaGroupId(groudIds[i]);
			sgmgm.setuGroupId(uGroupId);
			/*sgmgm.setIsAll(0);*/
			sgmgmList.add(sgmgm);
		}

		if(sgmgmList.size() > 0){
			sysuserGroupMediagroupDao.batchInsert(sgmgmList);

			//同步用户组下的需同步的用户  给予复制配置的终端组信息
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("uGroupId", uGroupId);
			param.put("isSyncUserGroup", 1);
			List<Map<String,Object>> ulist = userDao.selectSyncUser(param);
			if(ulist.size() > 0 ){
				for (Map<String, Object> map2 : ulist) {
					Integer userId = Integer.parseInt(map2.get("uid").toString());
					//先删除用户之前配置的终端组
					sysuserMediagroupDao.deleteByUid(new Integer[]{userId});
					//再把需同步的用户组上新配置的终端组数据 做复制添加 达到用户与用户组的终端组数据同步的目的
					List<SysuserMediagroupModel> smlist = new ArrayList<SysuserMediagroupModel>();
					for (Integer groudId_p : groudIds) {
						SysuserMediagroupModel sm = new SysuserMediagroupModel();
						sm.setMediaGroupId(groudId_p);
						sm.setUid(userId);
						sm.setIsAll(0);
						smlist.add(sm);
					}
					sysuserMediagroupDao.batchInsert(smlist);
				}
			}

			//没有选择同步的用户 需筛选出此次配置 减少了哪些终端组  需要删除用户组下的用户有这些已删除的终端组数据
			if(cancelMediaGroup.size() > 0 ){
				param.put("mediaGroupIds", cancelMediaGroup);
				sysuserMediagroupDao.deleteMediaGroupByUserGroup(param);
			}
		}

	}

	@Override
	public List<Map<String, Object>> getGroupIdByUGroupId(Integer uGroupId) {
		return sysuserGroupMediagroupDao.selectByUgroupId(uGroupId);
	}





}
