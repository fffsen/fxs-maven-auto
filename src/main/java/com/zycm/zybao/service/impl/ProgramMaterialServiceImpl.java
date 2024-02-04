package com.zycm.zybao.service.impl;

import com.zycm.zybao.common.config.FtpConfig;
import com.zycm.zybao.common.utils.*;
import com.zycm.zybao.dao.ProgramMaterialDao;
import com.zycm.zybao.dao.ProgramMaterialRelationDao;
import com.zycm.zybao.dao.ProgramPublishRecordDao;
import com.zycm.zybao.model.entity.ProgramMaterialModel;
import com.zycm.zybao.service.facade.ResourceLibServiceFacade;
import com.zycm.zybao.service.interfaces.ProgramMaterialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Slf4j
@Service("programMaterialService")
public class ProgramMaterialServiceImpl implements ProgramMaterialService {

	@Autowired(required = false)
	private ResourceLibServiceFacade resourceLibAdapter;
	@Autowired(required = false)
	private ProgramMaterialDao programMaterialDao;
	@Autowired(required = false)
	private ProgramMaterialRelationDao programMaterialRelationDao;
	@Autowired(required = false)
	private ProgramPublishRecordDao programPublishRecordDao;

	private static final String img_prefix = FtpConfig.imgServerPath;
	@Override
	public Map<String, Object> selectPage(Map<String, Object> map,Integer pageSize,Integer page) {
		Map<String, Object> returndata = new HashMap<String, Object>();
		if(map.get("materialName") != null){
			//如果是名称模糊匹配  就要做夸文件夹查询处理
			Map<String,Object> folderId = programMaterialDao.selectFolderByMaterial(map);
			if(folderId != null && folderId.get("materialIds") != null && StringUtils.isNotBlank(folderId.get("materialIds").toString())){
				String[] materialIds_str = folderId.get("materialIds").toString().split(",");
				map.put("folderId", ArrayUtils.toInt(materialIds_str));
			}else{
				map.put("folderId", null);
			}
		}

		String totalCount = programMaterialDao.selectPageCount(map);
		if(!"0".equals(totalCount)){
			//总页数
	        Double totalPage = Math.ceil(Integer.parseInt(totalCount)/Double.parseDouble(pageSize+""));
	        int startRow = (page-1)*pageSize;
	        map.put("startRow", startRow);
	        map.put("pageSize", pageSize);
	        List<Map<String,Object>> list = programMaterialDao.selectPage(map);
	        for(Map<String,Object> map2 : list){
	        	//图片地址信息处理
	        	String materialName = map2.get("materialName").toString();
	    		String materialPath = map2.get("materialPath").toString();
	    		String type = map2.get("type").toString();
	    		if(!"0".equals(type) && !"4".equals(type) && !"5".equals(type)){
	    			map2.put("previewPath",img_prefix + materialPath+materialName);
	    		}else{
	    			map2.put("previewPath","");
	    		}
	    		//大小转换处理
	    		String size_kb = map2.get("size") == null?"":map2.get("size").toString();
	    		if(StringUtils.isNotBlank(size_kb)){
	    			map2.put("size",NumUtil.sizeToStr(new BigDecimal(size_kb)));
	    		}else{
	    			map2.put("size","");
	    		}

	        }

	        returndata.put("dataList", list);
	        returndata.put("totalPage", totalPage);

		}else{
			returndata.put("dataList", new ArrayList());
	        returndata.put("totalPage", 0);
		}
		returndata.put("page", page);
		returndata.put("pageSize", pageSize);
		returndata.put("total", Integer.parseInt(totalCount));

		return returndata;
	}

	@Override
	public Map<String, Object> selectByPrimaryKey(Integer materialId) {
		Map<String, Object> map = programMaterialDao.selectByPrimaryKey(materialId);
		String materialName = map.get("materialName").toString();
		String materialPath = map.get("materialPath").toString();
		map.put("previewPath", img_prefix+materialPath+materialName);

		String type = map.get("type").toString();
		String aiAuditStatus = map.get("aiAuditStatus") != null?map.get("aiAuditStatus").toString():"";
		String auditStatus = map.get("auditStatus").toString();
		//只有未审核的视频才需要查询智能审核的结果
		if("0".equals(auditStatus) && "2".equals(type) && StringUtils.isNotBlank(aiAuditStatus)){//视频智能审核结果需要单独查询
			try {
				String ispass = OSSManageUtil.videoAiAudit(aiAuditStatus);
				if(!"myexception".equals(ispass)){
					ProgramMaterialModel programMaterialModel = new ProgramMaterialModel();
					programMaterialModel.setMaterialId(materialId);
					programMaterialModel.setAiAuditStatus(ispass);
					programMaterialDao.updateAiAudit(programMaterialModel);
					map.put("aiAuditStatus", ispass);
				}else{
					map.put("aiAuditStatus", "checking");
				}
			} catch (Exception e) {
				log.error("获取视频的质检结果异常", e);
				map.put("aiAuditStatus", "checking");
			}
		}
		return map;
	}

	@Override
	public List<ProgramMaterialModel> batchInsert(List<ProgramMaterialModel> list) {
		programMaterialDao.batchInsert(list);
		return list;
	}

	@Override
	public void updateMaterial(ProgramMaterialModel programMaterialModel) throws Exception{
		Map<String,Object> programMaterialmap = programMaterialDao.selectByPrimaryKey(programMaterialModel.getMaterialId());
		if(programMaterialmap != null){
			String type = programMaterialmap.get("type").toString();
			String oldmaterialName = programMaterialmap.get("materialName").toString();
			String materialPath = programMaterialmap.get("materialPath").toString();
			Integer auditStatus_old = Integer.parseInt(programMaterialmap.get("auditStatus").toString());
			String newmaterialName = programMaterialModel.getMaterialName();
			String suffix_old = "",suffix_new = "";

			//验证名称是否已存在
			List<Map<String,Object>> mmlist = programMaterialDao.checkMaterialName(new String[]{programMaterialModel.getMaterialName()}, Integer.parseInt(type), new Integer[]{programMaterialModel.getMaterialId()});
			if(mmlist.size() > 0){
				log.error("素材名称已存在素材库中！");
				throw new Exception("素材名称已存在素材库中！");
			}
			//验证素材发布后 不能修改基础属性
			if(!"0".equals(type) && !"6".equals(type)){
				List<Map<String, Object>> programPublishRecordList = programPublishRecordDao.selectPublishByMaterialId(new Integer[]{programMaterialModel.getMaterialId()});
				if(programPublishRecordList.size() > 0 && programPublishRecordList.get(0).get("publishIds") != null
						&& StringUtils.isNotBlank(programPublishRecordList.get(0).get("publishIds").toString())){
					log.error("素材已发布不能修改基本信息！");
					throw new Exception("素材已发布不能修改基本信息！");
				}
			}

			if("1".equals(type) || "2".equals(type) || "3".equals(type) || "6".equals(type)){
				if(newmaterialName.lastIndexOf(".") == -1){
					log.error("文件名无有效的对应文件类型后缀！");
					throw new Exception("文件名无有效的对应文件类型后缀！");
				}
				//修改上传的文件
				suffix_old = oldmaterialName.substring(oldmaterialName.lastIndexOf("."));
				suffix_new = newmaterialName.substring(newmaterialName.lastIndexOf("."));
				//判断新名称的文件类型后缀是否与之前的相同  不能改成其他类型的文件只能是同类型的文件改名称
				if(suffix_old.equals(suffix_new)){
					//修改信息
					if(auditStatus_old.intValue() == 1){//已审核通过的不要再审核
						programMaterialModel.setAuditStatus(null);
					}
					programMaterialModel.setMaterialName(newmaterialName);
					programMaterialDao.updateMaterial2(programMaterialModel);

					if(!oldmaterialName.equals(newmaterialName)){//新旧名称不相同修改素材名称
						//ftp上修改  直接修改文件名
						try {
							resourceLibAdapter.connect();
							resourceLibAdapter.moveFile(materialPath+oldmaterialName, materialPath+newmaterialName);
							resourceLibAdapter.close();
						} catch (Exception e) {
							log.error("重命名文件失败", e);
							throw new Exception("重命名文件失败");
						}
					}

				}else{
					log.error("新素材名称的后缀类型必须与之前的相同，重命名不支持素材类型的转换！");
					throw new Exception("新素材名称的后缀类型["+suffix_new+"]必须与之前的["+suffix_old+"]相同，重命名不支持素材类型的转换！");
				}

			}else if("0".equals(type)){//修改文件夹
				String ex ="重命名文件夹失败";
				try {
					if(!oldmaterialName.equals(newmaterialName)){
						resourceLibAdapter.connect();
						//判断文件夹下是否存在文件
						if(!resourceLibAdapter.dirExistFile(materialPath+oldmaterialName+"/") && !oldmaterialName.equals(newmaterialName)){
							programMaterialModel.setAuditStatus(null);
							programMaterialDao.updateMaterial2(programMaterialModel);
							resourceLibAdapter.moveDir(materialPath+oldmaterialName, materialPath+newmaterialName);
						}else{
							if(resourceLibAdapter.dirExistFile(materialPath+oldmaterialName+"/")){
								log.error("["+materialPath+oldmaterialName+"/"+"]文件下存在其他文件，无法修改文件名");
								ex ="["+materialPath+oldmaterialName+"/"+"]文件下存在其他文件，无法修改文件名";
								throw new Exception(ex);
							}

						}
						resourceLibAdapter.close();
					}else{
						programMaterialDao.updateMaterial(programMaterialModel);
					}

				} catch (Exception e) {
					log.error("重命名文件夹失败", e);
					throw new Exception(ex);
				}


			}else  if("4".equals(type) || "5".equals(type)){//网址、直播源
				if(newmaterialName.lastIndexOf(".") == -1){
					log.error("文件名无有效的对应文件类型后缀！");
					throw new Exception("文件名无有效的对应文件类型后缀！");
				}
				if(auditStatus_old.intValue() == 1){//已审核通过的不要再审核 内容也不能更改 只能改素材名称
					programMaterialModel.setAuditStatus(null);
					programMaterialModel.setSourceUrl(null);
				}else{
					if(StringUtils.isBlank(programMaterialModel.getSourceUrl())){
						log.error("修改网址、直播源时sourceUrl参数不能为空！");
						throw new Exception("修改网址、直播源时sourceUrl参数不能为空！");
					}
				}

				suffix_old = oldmaterialName.substring(oldmaterialName.lastIndexOf("."));
				suffix_new = newmaterialName.substring(newmaterialName.lastIndexOf("."));
				if(suffix_old.equals(suffix_new)){
					programMaterialModel.setMaterialName(newmaterialName);
					programMaterialDao.updateMaterial2(programMaterialModel);
					if(!oldmaterialName.equals(newmaterialName)){//名称有变动才进入修改流程
						//ftp上修改  直接修改文件名
						try {
							resourceLibAdapter.connect();
							resourceLibAdapter.moveFile(materialPath+oldmaterialName, materialPath+newmaterialName);
							resourceLibAdapter.close();
						} catch (Exception e) {
							log.error("重命名网址、直播源失败", e);
							throw new Exception("重命名网址、直播源失败");
						}
					}
				}else{
					log.error("新素材名称的后缀类型必须与之前的相同，重命名不支持素材类型的转换！");
					throw new Exception("新素材名称的后缀类型["+suffix_new+"]必须与之前的["+suffix_old+"]相同，重命名不支持素材类型的转换！");
				}

			}else{
				if(newmaterialName.lastIndexOf(".") == -1){
					log.error("文件名无有效的对应文件类型后缀！");
					throw new Exception("文件名无有效的对应文件类型后缀！");
				}
				if(auditStatus_old.intValue() == 1){//已审核通过的不要再审核 内容也不能更改 只能改素材名称
					programMaterialModel.setAuditStatus(null);
				}
				suffix_old = oldmaterialName.substring(oldmaterialName.lastIndexOf("."));
				suffix_new = newmaterialName.substring(newmaterialName.lastIndexOf("."));

				if(suffix_old.equals(suffix_new)){
					programMaterialModel.setMaterialName(newmaterialName);

					//ftp上修改  直接修改文件名
					try {
						programMaterialDao.updateMaterial2(programMaterialModel);
						if(!oldmaterialName.equals(newmaterialName)){
							resourceLibAdapter.connect();
							resourceLibAdapter.moveFile(materialPath+oldmaterialName, materialPath+newmaterialName);
							resourceLibAdapter.close();
						}
					} catch (Exception e) {
						log.error("重命名其他后期扩展类型素材失败", e);
						throw new Exception("重命名其他后期扩展类型素材失败");
					}
				}else{
					log.error("新素材名称的后缀类型必须与之前的相同，重命名不支持素材类型的转换！");
					throw new Exception("新素材名称的后缀类型["+suffix_new+"]必须与之前的["+suffix_old+"]相同，重命名不支持素材类型的转换！");
				}

			}
		}else{
			log.error("根据素材id查询不到数据！");
			throw new Exception("根据素材id查询不到数据！");
		}
	}

	@Override
	public void updateMaterialPath(Integer materialId,String materialPath) {
		ProgramMaterialModel programMaterialModel = new ProgramMaterialModel();
		programMaterialModel.setMaterialId(materialId);
		programMaterialModel.setMaterialPath(materialPath);
		programMaterialDao.updateMaterialPath(programMaterialModel);
	}

	@Override
	public void updateAuditStatus(ProgramMaterialModel programMaterialModel) {
		programMaterialDao.updateAuditStatus(programMaterialModel);
	}

	@Override
	public void updateIsDelete(Integer materialId) {
		ProgramMaterialModel programMaterialModel = new ProgramMaterialModel();
		programMaterialModel.setMaterialId(materialId);
		programMaterialModel.setIsDelete(1);
		programMaterialDao.updateIsDelete(programMaterialModel);
	}

	@Override
	public Map<String, Object> selectApk(Map<String, Object> map, Integer pageSize, Integer page) {
		Map<String, Object> returndata = new HashMap<String, Object>();
		String totalCount = programMaterialDao.selectApkCount(map);
		if(!"0".equals(totalCount)){
			//总页数
	        Double totalPage = Math.ceil(Integer.parseInt(totalCount)/Double.parseDouble(pageSize+""));
	        int startRow = (page-1)*pageSize;
	        map.put("startRow", startRow);
	        map.put("pageSize", pageSize);
	        List<Map<String,Object>> list = programMaterialDao.selectApk(map);
	        for(Map<String,Object> map2 : list){
	        	String materialName = map2.get("materialName").toString();
	    		String materialPath = map2.get("materialPath").toString();
	    		map2.put("previewPath",img_prefix+materialPath+materialName);
	    		//大小转换处理
	    		String size_kb = map2.get("size") == null?"":map2.get("size").toString();
	    		if(StringUtils.isNotBlank(size_kb)){
	    			map2.put("size",NumUtil.sizeToStr(new BigDecimal(size_kb)));
	    		}else{
	    			map2.put("size","");
	    		}
	        }

	        returndata.put("dataList", list);
	        returndata.put("totalPage", totalPage);

		}else{
			returndata.put("dataList", new ArrayList());
	        returndata.put("totalPage", 0);
		}
		returndata.put("page", page);
		returndata.put("pageSize", pageSize);
		returndata.put("total", Integer.parseInt(totalCount));

		return returndata;
	}

	@Override
	public List<Map<String, Object>> selectByMaterialName(String[] materialName) {
		return programMaterialDao.selectByMaterialName(materialName);
	}

	@Override
	public List<Map<String, Object>> checkMaterialName(String[] materialNames, Integer type, Integer[] materialIds) {
		return programMaterialDao.checkMaterialName(materialNames, type, materialIds);
	}

	@Override
	public String readRemoteTxt(String path) throws Exception{
		URL url = new URL(path);
		HttpURLConnection httpUrl = (HttpURLConnection) url.openConnection();// 连接指定的资源
		httpUrl.connect();// 获取网络输入流
		//获取远程文件的编码  避免读取到乱码
		String fileEncode= EncodingDetect.getJavaEncode(url);
		BufferedReader bis = new BufferedReader( new InputStreamReader(httpUrl.getInputStream(),fileEncode));
		StringBuffer buf=new StringBuffer();
		String temp;
		while ((temp = bis.readLine()) != null) {
		   buf.append(temp);
		   if(buf.length()>=1000){
		    break;
		   }
		}
		bis.close();
		return buf.toString();
	}

	@Override
	public List<Map<String, Object>> checkMaterialInProg(Integer[] materialIds) {
		return programMaterialRelationDao.checkMaterialInProg(materialIds);
	}

	@Override
	public String syncMaterialMD5() {
		ProgramMaterialModel programMaterialModel = new ProgramMaterialModel();
		List<Map<String,Object>> nullMD5List = programMaterialDao.selectNullMD5();
		String ids="";
		if(nullMD5List.size() > 0){
			for (Map<String, Object> map : nullMD5List) {
				try {
					String remoteFolderPath = map.get("materialPath").toString();
					String filename = map.get("materialName").toString();
					Integer materialId = Integer.parseInt(map.get("materialId").toString());
					String mastermd5 = UrlRequestUtil.getMasterMaterialMd5(remoteFolderPath+filename);
					if(StringUtils.isNotBlank(mastermd5)){
						programMaterialModel.setMaterialId(materialId);
						programMaterialModel.setCheckCode(mastermd5);
						programMaterialDao.updateMD5(programMaterialModel);
						ids += ","+materialId;
					}
				} catch (Exception e) {
					log.error("更新素材md5值异常", e);
				}
			}
		}
		String result_s = "查询"+nullMD5List.size()+"个更新【"+ids+"】共"+(StringUtils.isNotBlank(ids)?(ids.substring(1).split(",").length):0)+"个素材的md5值";
		return result_s;
	}

}
