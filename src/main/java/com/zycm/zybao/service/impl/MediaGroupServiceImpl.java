package com.zycm.zybao.service.impl;

import com.zycm.zybao.common.utils.ArrayUtils;
import com.zycm.zybao.dao.*;
import com.zycm.zybao.model.entity.MediaGroupModel;
import com.zycm.zybao.model.entity.ProgramPublishRecordModel;
import com.zycm.zybao.model.entity.SysuserGroupMediagroupModel;
import com.zycm.zybao.model.entity.SysuserMediagroupModel;
import com.zycm.zybao.service.interfaces.MediaGroupService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Service("mediaGroupService")
public class MediaGroupServiceImpl implements MediaGroupService {

	@Autowired(required = false)
	private MediaGroupDao mediaGroupDao;
	@Autowired(required = false)
	private ProgramPublishRecordDao programPublishRecordDao;
	@Autowired(required = false)
	private MediaGroupRelationDao mediaGroupRelationDao;
	@Autowired(required = false)
	private SysuserMediagroupDao sysuserMediagroupDao;
	@Autowired(required = false)
	private SysuserGroupMediagroupDao sysuserGroupMediagroupDao;
	@Autowired(required = false)
	private MediaAttributeDao mediaAttributeDao;
	/*
	 * 使用layui自带的tree组件使用的数据方法
	 */
	@Override
	public Map<String, Object> selectGroupTree(Integer uid,boolean spread) {
		Map<String, Object> returnmaptree = new HashMap<String, Object>();
		returnmaptree.put("data", 0);
		returnmaptree.put("label", "所有终端组");
		returnmaptree.put("name", "所有终端组");
		returnmaptree.put("parentId", null);
		//查询父id为0的第一级节点
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("parentId", 0);
		param.put("uid", uid);
		List<Map<String, Object>> rootNode = mediaGroupDao.selectByParentId(param);
		//查询所有子节点信息 排除父id为0
		List<Map<String, Object>> childNode = mediaGroupDao.selectChild(param);

		for (Map<String, Object> rootmap : rootNode) {
			structureTree(rootmap, childNode,spread);
		}
		returnmaptree.put("children", rootNode);
		return returnmaptree;
	}

	/*
	 * 使用xtree组件使用的数据方法
	 */
	@Override
	public Map<String, Object> selectGroupXTree(Integer uid) {
		Map<String, Object> returnmaptree = new HashMap<String, Object>();
		returnmaptree.put("value", 0);
		returnmaptree.put("title", "所有终端组");
		returnmaptree.put("parentId", null);
		//查询父id为0的第一级节点
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("parentId", 0);
		param.put("uid", uid);
		List<Map<String, Object>> rootNode = mediaGroupDao.selectByParentId(param);
		//查询所有子节点信息 排除父id为0
		List<Map<String, Object>> childNode = mediaGroupDao.selectChild(param);

		for (Map<String, Object> rootmap : rootNode) {
			structureXTree(rootmap, childNode);
		}
		returnmaptree.put("data", rootNode);
		return returnmaptree;
	}

	/*
	 * 使用xtree组件的数据接口  用户模块使用  给用户分配组时只会显示用户所在的用户组的配置的终端组数据
	 */
	@Override
	public Map<String, Object> selectGroupXTreeUser(Integer uGroupId) {
		Map<String, Object> returnmaptree = new HashMap<String, Object>();

		//查询父id为0的第一级节点
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("uGroupId", uGroupId);
		param.put("selectType", "parent");
		List<Map<String, Object>> rootNode = sysuserGroupMediagroupDao.selectMediaGroupByUGroupId(param);
		if(rootNode.size() > 0){
			returnmaptree.put("value", 0);
			returnmaptree.put("title", "所有终端组");
			returnmaptree.put("parentId", null);
			//查询所有子节点信息 排除父id为0
			param.put("selectType", "child");
			List<Map<String, Object>> childNode = sysuserGroupMediagroupDao.selectMediaGroupByUGroupId(param);

			for (Map<String, Object> rootmap : rootNode) {
				structureXTree(rootmap, childNode);
			}
		}
		returnmaptree.put("data", rootNode);
		return returnmaptree;
	}


	/** 构造树结构  单个父节点  自循环查子节点
	* @Title: structureTree
	* @Description: TODO
	* @author sy
	* @param @param rootmap
	* @param @param childNode
	* @param @return
	* @return Map<String,Object>
	* @throws
	*/
	private Map<String, Object> structureTree(Map<String, Object> rootmap,List<Map<String, Object>> childNode,boolean spread){
		List<Map<String, Object>> childs = new ArrayList<Map<String, Object>>();
		//父节点id
		String mediaGroupId = rootmap.get("data").toString();
		for (Map<String, Object> childmap : childNode) {
			//获取子节点的父节点id
			String parentId = childmap.get("parentId").toString();
			//匹配到父节点的所有子节点
			if(mediaGroupId.equals(parentId)){
				childs.add(childmap);
			}
		}
		if(spread){
			rootmap.put("spread", true);
		}

		rootmap.put("children", childs);
		if(childs.size() == 0){
			rootmap.put("lastChildren", 1);
			return rootmap;
		}else{
			rootmap.put("lastChildren", 0);
		}
		//再回调 遍历下一级的子节点
		for (Map<String, Object> map : childs) {
			structureTree(map, childNode,spread);
		}

		return rootmap;
	}

	private Map<String, Object> structureXTree(Map<String, Object> rootmap,List<Map<String, Object>> childNode){
		List<Map<String, Object>> childs = new ArrayList<Map<String, Object>>();
		//父节点id
		String mediaGroupId = rootmap.get("value").toString();
		for (Map<String, Object> childmap : childNode) {
			//获取子节点的父节点id
			String parentId = childmap.get("parentId").toString();
			//匹配到父节点的所有子节点
			if(mediaGroupId.equals(parentId)){
				childs.add(childmap);
			}
		}
		rootmap.put("data", childs);
		if(childs.size() == 0){
			rootmap.put("lastChildren", 1);
			return rootmap;
		}else{
			rootmap.put("lastChildren", 0);
		}
		//再回调 遍历下一级的子节点
		for (Map<String, Object> map : childs) {
			structureXTree(map, childNode);
		}

		return rootmap;
	}

	@Override
	public void insert(String mediaGroupName, Integer parentId,Integer uid,Integer uGroupId) throws Exception {
		//先验证是不是在未分组性质的组下加的子分组
		Map<String,Object> defaultGroup = mediaGroupDao.selectDefaultGroup(parentId);
		if(defaultGroup != null){
			throw new Exception("默认的未分组下不能添加子分组！");
		}

		//验证名称重复
		Map<String,Object> param1 = new HashMap<String,Object>();
		param1.put("mediaGroupName", mediaGroupName);
		List<Map<String,Object>> mglist = mediaGroupDao.selectByGroupName(param1);
		if(mglist.size() > 0){
			throw new Exception("终端组名称重复！");
		}

		MediaGroupModel mediaGroupModel = new MediaGroupModel();
		mediaGroupModel.setMediaGroupName(mediaGroupName);
		mediaGroupModel.setParentId(parentId);
		mediaGroupModel.setIsFixed(0);
		mediaGroupDao.insert(mediaGroupModel);

		//如果新加的分组是在一个末节点下  那么需要对父节点的节目 led节目与终端做迁移
		//查询parentId的终端组是否有节目及终端 有就迁移到新建的子分组中
		List<ProgramPublishRecordModel> progList = programPublishRecordDao.selectGroupProg(parentId);
		if(progList.size() > 0 && mediaGroupModel.getMediaGroupId() != null){//切换节目
			param1.put("oldMediaGroupId", parentId);
			param1.put("newMediaGroupId", mediaGroupModel.getMediaGroupId());
			programPublishRecordDao.updateGroupProgByGroupId(param1);
		}
		//查询parentId的终端组是否有终端 有就迁移到新建的子分组中
		param1.put("mediaGroupIds", new Integer[]{parentId});
		List<Map<String, Object>> mediaList = mediaAttributeDao.selectMediaByGroupId(param1);
		if(mediaList.size() > 0 && mediaGroupModel.getMediaGroupId() != null){
			param1.put("oldMediaGroupId", parentId);
			param1.put("newMediaGroupId", mediaGroupModel.getMediaGroupId());
			mediaGroupRelationDao.updateGroupMediaByGroupId(param1);
		}
		//处理用户所管理的终端组逻辑
		if(parentId.intValue() != 0){//第一级的是超级管理员控制的不需要处理
			//所有用户组中配置的终端组 包含了新增组的父节点的 都要做自动勾选的数据处理
			List<Map<String,Object>> userGroupList = sysuserGroupMediagroupDao.selectByMediaGroupId(parentId);
			if(userGroupList.size() > 0){
				List<SysuserGroupMediagroupModel> list = new ArrayList<SysuserGroupMediagroupModel>();
				for (Map<String, Object> map : userGroupList) {
					SysuserGroupMediagroupModel sgmm = new SysuserGroupMediagroupModel();
					sgmm.setMediaGroupId(mediaGroupModel.getMediaGroupId());
					sgmm.setuGroupId(Integer.parseInt(map.get("uGroupId").toString()));
					list.add(sgmm);
				}
				sysuserGroupMediagroupDao.batchInsert(list);
			}

			//所有用户(自动同步的不同步的不需要自动增加新组)中配置的终端组 包含了新增组的父节点的 都要做自动勾选的数据处理
			List<Map<String,Object>> userList = sysuserMediagroupDao.selectByMediaGroupId(parentId);
			if(userList.size() > 0){
				List<SysuserMediagroupModel> smlist = new ArrayList<SysuserMediagroupModel>();
				for (Map<String, Object> map : userList) {
					SysuserMediagroupModel sm = new SysuserMediagroupModel();
					sm.setMediaGroupId(mediaGroupModel.getMediaGroupId());
					sm.setUid(Integer.parseInt(map.get("uid").toString()));
					sm.setIsAll(0);
					smlist.add(sm);
				}
				sysuserMediagroupDao.batchInsert(smlist);
			}
		}

	}

	@Override
	public void deleteByPrimaryKey(Integer mediaGroupId) throws Exception{
		//判断是不是固定组  商户的固定组只有在没有所属的用户组时才能被删除 存在所属的用户组时是不能被删除的  避免管理员的误操作导致商户的媒体机丢失
		Map<String,Object> mg = mediaGroupDao.selectByPrimaryKey(mediaGroupId);
		if("1".equals(mg.get("isFixed").toString())){
			//如果是固定组  还需判断是否已没有所属的用户组信息
			List<Map<String,Object>> list = sysuserGroupMediagroupDao.selectByMediaGroupId(mediaGroupId);
			if(list.size() > 0){
				throw new Exception("改固定终端组还存在所属的用户组不能删除！");
			}
		}

		//查询删除终端组下的所有子节点
		String mediaGroupIdstr = mediaGroupDao.selectAllChild(mediaGroupId);
		if(StringUtils.isNotBlank(mediaGroupIdstr)){
			//判断子节点下的终端媒体机
			Integer[] mediaGroupIds = ArrayUtils.toInt(mediaGroupIdstr.split(","));
			List<Map<String, Object>> medialist = mediaGroupRelationDao.selectMidByGroupId(mediaGroupIds);
			if(medialist.size() > 0){
				Integer[] mids = new Integer[medialist.size()];
				for (int i = 0; i < mids.length; i++) {
					mids[i] = Integer.parseInt(medialist.get(i).get("mid").toString());
				}
				//判断删除的终端组是不是只有一个所属的用户组  如果只有一个那么删除的终端组的终端机都迁移到用户组的未分组下  如果有多个就迁移到超级管理员的未分组下
				List<Map<String, Object>> userGrouplist = sysuserGroupMediagroupDao.selectByMediaGroupId(mediaGroupId);
				if(userGrouplist.size() == 1){
					Integer ugId = Integer.parseInt(userGrouplist.get(0).get("uGroupId").toString());
					List<Map<String, Object>> noGrouplist = sysuserGroupMediagroupDao.selectNoGroupByUgroupId(ugId);
					if(noGrouplist.size() == 1){//更新终端到用户组的未分组中
						Integer mgId2 = Integer.parseInt(noGrouplist.get(0).get("mediaGroupId").toString());
						mediaGroupRelationDao.updateGroup(mgId2, mids);
					}else{
						//把媒体机更新为 未分组下的媒体机
						mediaGroupRelationDao.updateGroup(1, mids);
					}
				}else{
					//把媒体机更新为 未分组下的媒体机
					mediaGroupRelationDao.updateGroup(1, mids);
				}
			}
			//删除组
			mediaGroupDao.deleteByPrimaryKeys(mediaGroupIds);
			//删除原组的节目数据
			programPublishRecordDao.deleteByGroupIds(mediaGroupIds);
			//删除终端组关联的用户组信息
			sysuserGroupMediagroupDao.deleteByMediaGroupIds(mediaGroupIds);
			//删除终端组关联的用户信息
			sysuserMediagroupDao.deleteByMediaGroupIds(mediaGroupIds);
		}else{
			log.error("根据终端组id "+mediaGroupId+"没有查到子节点数据及本身id！");
		}

	}

	@Override
	public List<Map<String, Object>> selectMediaByGroupIdAndChild(Integer mediaGroupId) {
		String mediaGroupIdstr = mediaGroupDao.selectAllChild(mediaGroupId);
		if(StringUtils.isNotBlank(mediaGroupIdstr)){
			Integer[] mediaGroupIds = ArrayUtils.toInt(mediaGroupIdstr.split(","));
			List<Map<String, Object>> remap = mediaGroupRelationDao.selectMidByGroupId(mediaGroupIds);
			if(remap.size() > 0){
				return remap;
			}else{
				return new ArrayList<Map<String, Object>>();
			}
		}
		return null;
	}

	@Override
	public void updateGroupName(String mediaGroupName, Integer mediaGroupId) throws Exception{
		//固定组不能修改名称
		Map<String,Object> mg = mediaGroupDao.selectByPrimaryKey(mediaGroupId);
		if(mediaGroupId == 0 || mediaGroupId == 1 || "1".equals(mg.get("isFixed").toString())){
			throw new Exception("用户组的固定组不能修改名称！");
		}

		//验证名称重复
		Map<String,Object> param1 = new HashMap<String,Object>();
		param1.put("mediaGroupName", mediaGroupName);
		param1.put("mediaGroupId", mediaGroupId);
		List<Map<String,Object>> mglist = mediaGroupDao.selectByGroupName(param1);
		if(mglist.size() > 0){
			throw new Exception("终端组名称已存在！");
		}

		MediaGroupModel mediaGroupModel = new MediaGroupModel();
		mediaGroupModel.setMediaGroupName(mediaGroupName);
		mediaGroupModel.setMediaGroupId(mediaGroupId);
		mediaGroupDao.updateGroupName(mediaGroupModel);
	}

	@Override
	public List<Map<String, Object>> selectByGroupName(Map<String,Object> param) {
		return mediaGroupDao.selectByGroupName(param);
	}

	@Override
	public List<Integer> selectChildByGroupId(Integer mediaGroupId) {
		List<Integer> cids = new ArrayList<Integer>();
		String mediaGroupIdstr = mediaGroupDao.selectAllChild(mediaGroupId);
		if(StringUtils.isNotBlank(mediaGroupIdstr)){
			String[] sid = mediaGroupIdstr.split(",");
			for (int i = 0; i < sid.length; i++) {
				if(StringUtils.isNotBlank(sid[i])){
					cids.add(Integer.parseInt(sid[i]));
				}
			}
		}
		return cids;
	}

	@Override
	public List<Map<String, Object>> selectGroupIdByMids(Integer[] mids) {
		return mediaGroupRelationDao.selectGroupIdByMids(mids);
	}

	@Override
	public Map<String, Object> selectPageLastGroup(Integer page, Integer pageSize, Map<String, Object> param) {
		Map<String, Object> returndata = new HashMap<String, Object>();
		Integer totalCount = mediaGroupDao.selectPageLastGroupCount(param);
		if(null != totalCount && totalCount > 0){
			//总页数
	        Double totalPage = Math.ceil(totalCount/Double.parseDouble(pageSize+""));
	        int startRow = (page-1)*pageSize;
	        param.put("startRow", startRow);
	        param.put("pageSize", pageSize);
	        List<Map<String,Object>> list = mediaGroupDao.selectPageLastGroup(param);

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



}
