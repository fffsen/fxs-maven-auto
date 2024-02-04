package com.zycm.zybao.service.impl;

import com.zycm.zybao.amq.handler.receivehandler.BaseClientConfReceiveHandler;
import com.zycm.zybao.amq.topic.producer.FBTopicSender;
import com.zycm.zybao.common.enums.MessageEvent;
import com.zycm.zybao.common.utils.ArrayUtils;
import com.zycm.zybao.dao.FtpInfoDao;
import com.zycm.zybao.dao.MediaAttributeDao;
import com.zycm.zybao.dao.UserDao;
import com.zycm.zybao.model.mqmodel.ProtocolModel;
import com.zycm.zybao.model.mqmodel.request.clientconf.ClientConf;
import com.zycm.zybao.service.interfaces.FtpInfoService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;


@Slf4j
@Service("ftpInfoService")
public class FtpInfoServiceImpl implements FtpInfoService {

	@Autowired(required = false)
	private FtpInfoDao ftpInfoDao;
	@Autowired(required = false)
	private UserDao userDao;
	@Autowired(required = false)
	private MediaAttributeDao mediaAttributeDao;
	@Autowired(required = false)
	private FBTopicSender fBTopicSender;

	@Override
	public Map<String, Object> selectPage(Map<String, Object> param, Integer page, Integer pageSize) {
		Map<String, Object> returndata = new HashMap<String, Object>();
		Integer totalCount = ftpInfoDao.selectPageCount(param);
		if(null != totalCount && totalCount > 0){
			//总页数
	        Double totalPage = Math.ceil(totalCount/Double.parseDouble(pageSize+""));
	        int startRow = (page-1)*pageSize;
	        param.put("startRow", startRow);
	        param.put("pageSize", pageSize);
	        List<Map<String,Object>> list = ftpInfoDao.selectPage(param);

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
	public void insert(Map<String, Object> param) throws Exception{
		//验证重复
		List<Map<String,Object>> list = ftpInfoDao.validRepeat(param);
		if(list.size() > 0){
			throw new Exception("ftp的数据内容已存在！");
		}
		ftpInfoDao.insert(param);
	}

	@Override
	public void updateByPrimaryKey(Map<String, Object> param) throws Exception {
		//ftp被修改后 需判断ip 端口  用户 密码是否被修改了  如果被修改了需同步数据到媒体机
		Map<String,Object> ftp = ftpInfoDao.selectByPrimaryKey(Integer.parseInt(param.get("ftpId").toString()));
		boolean b = false;
		if(!param.get("ipAddr").toString().equals(ftp.get("ipAddr").toString())
				|| !param.get("port").toString().equals(ftp.get("port").toString())
				|| !param.get("ftpUser").toString().equals(ftp.get("ftpUser").toString())
				|| !param.get("ftpPwd").toString().equals(ftp.get("ftpPwd").toString())
				|| !param.get("ftpType").toString().equals(ftp.get("ftpType").toString())){
			//判断修改后的内容是否已存在
			List<Map<String,Object>> ftpl = ftpInfoDao.validRepeat(param);
			if(ftpl.size() > 0){
				throw new Exception("ftp的数据内容已存在！");
			}else{
				b = true;
			}
		}

		ftpInfoDao.updateByPrimaryKey(param);
		if(b){
			//同步修改的ftp信息  发送最新配置给终端
			try {
				List<Map<String,Object>> baseMediaAttribute = mediaAttributeDao.selectByFtpId(Integer.parseInt(param.get("ftpId").toString()));
				if(baseMediaAttribute.size() > 0){
					//FBTopicSender sender= new FBTopicSender();
					ProtocolModel protocolModel = new ProtocolModel();
					SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date d1 = new Date();
					for (Map<String, Object> map : baseMediaAttribute) {
						String machineCode = map.get("machineCode")==null?"":map.get("machineCode").toString();
						Integer light = map.get("light")==null?50:Integer.parseInt(map.get("light").toString());
						Integer voice = map.get("voice")==null?20:Integer.parseInt(map.get("voice").toString());
						if(StringUtils.isNotBlank(machineCode)){
							//组装消息协议
							ClientConf clientConf = new ClientConf();
							clientConf.setFtpId(Integer.parseInt(param.get("ftpId").toString()));
							clientConf.setFtpIp(param.get("ipAddr").toString());
							clientConf.setFtpPort(Integer.parseInt(param.get("port").toString()));
							clientConf.setFtpUser(param.get("ftpUser").toString());
							clientConf.setFtpPwd(param.get("ftpPwd").toString());
							clientConf.setLight(light);
							clientConf.setVoice(voice);
							clientConf.setBucketName(param.get("bucketName")!=null?param.get("bucketName").toString():"");
							clientConf.setFtpType(Integer.parseInt(param.get("ftpType").toString()));
							clientConf.setOpenHttp(Integer.parseInt(param.get("openHttp").toString()));
							String previewPathPrefix = param.get("previewPath").toString();
							String httpUrlPrefix = (previewPathPrefix.contains("http")?previewPathPrefix:"http://"+previewPathPrefix)+":"+param.get("previewPort").toString();
							clientConf.setHttpUrlPrefix(httpUrlPrefix);
							clientConf.setOssUploadUrlPrefix(BaseClientConfReceiveHandler.IMG_SERVER_PATH);

							protocolModel.ServerToClient(MessageEvent.BASECLIENTCONF.event, clientConf);
							protocolModel.setToClient(machineCode);
							JSONObject json = JSONObject.fromObject(protocolModel);
							log.debug("发送给终端ftp配置信息："+json);
							fBTopicSender.sendMsg(json.toString(), machineCode);
						}else{
							log.error("机器码为空，不发送指令");
						}
					}
					fBTopicSender.session.commit();

					Date d2 = new Date();
					log.info("发送最新的终端ftp的指令"+baseMediaAttribute.size()+"个指令结束,开始时间"+sdf3.format(d1)+",结束时间"+sdf3.format(d2)+",耗时"+(d2.getTime()-d1.getTime())/1000+"秒>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				}
			} catch (Exception e) {
				log.error("发送最新终端ftp配置信息失败！", e);
			}

		}
	}

	@Override
	public void deleteByPrimaryKey(Integer ftpId) throws Exception{
		//检查ftp是否已被使用
		List<Map<String,Object>> list = mediaAttributeDao.selectByFtpId(ftpId);
		if(list.size() > 0){
			throw new Exception("ftp已配置到终端不能删除只能修改！");
		}

		//查询删除id的详细
		Map<String,Object> ftpmap = ftpInfoDao.selectByPrimaryKey(ftpId);
		if(null != ftpmap){
			String isDefault = ftpmap.get("isDefault").toString();
			//判断ftp是不是默认ftp  如果是则不能删除
			if("1".equals(isDefault)){
				throw new Exception("默认ftp不能被删除，可以改变默认状态再删除");
			}else{
				ftpInfoDao.deleteByPrimaryKey(ftpId);
			}
		}

	}

	@Override
	public Map<String, Object> selectByPrimaryKey(Integer ftpId) {
		return ftpInfoDao.selectByPrimaryKey(ftpId);
	}

	@Override
	public void updateDefaultById(Integer ftpId,Integer userId,Integer[] sameGroupUserId,boolean b) throws Exception{
		Map<String,Object> param = new HashMap<String,Object>();
		if(b){//如果是超级管理员  就要查选择的ftp创建者用户组的默认ftp情况
			//全修改成不默认的
			param.put("isDefault", 0);
			ftpInfoDao.updateDefaultById(param);
			//修改指定的为默认ftp
			param.put("isDefault", 1);
			param.put("ftpId", ftpId);
			ftpInfoDao.updateDefaultById(param);
		}
	}

	@Override
	public List<Map<String, Object>> selectInfo(Integer uGroupId,Integer sId,Integer[] sameGroupUserId) {
		//查询管理员的默认ftp与用户自己的用户组添加的ftp信息
		Map<String,Object> parammap = new HashMap<String,Object>();
		if(uGroupId.intValue() == sId.intValue()){
			parammap.put("sameGroupUserId", null);
		}else{
			//附加管理员的信息
			String sameGroupUserId_p = userDao.selectAdmin(sId);
			Integer[] sids = ArrayUtils.toInt(sameGroupUserId_p.split(","));
			int length1 = sameGroupUserId.length;
			int length2 = sids.length;
			sameGroupUserId = Arrays.copyOf(sameGroupUserId, sameGroupUserId.length + sids.length);
			System.arraycopy(sids, 0, sameGroupUserId, length1, length2);
			parammap.put("sameGroupUserId", sameGroupUserId);
		}
		return ftpInfoDao.selectInfo(parammap);
	}

}
