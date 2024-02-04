package com.zycm.zybao.service.facade;

import com.zycm.zybao.common.enums.MessageEvent;
import com.zycm.zybao.common.utils.ArrayUtils;
import com.zycm.zybao.common.utils.FileUtil;
import com.zycm.zybao.common.utils.MD5;
import com.zycm.zybao.common.utils.ZipUtil;
import com.zycm.zybao.model.mqmodel.ProtocolModel;
import com.zycm.zybao.model.mqmodel.request.publish.ProgramMsg;
import com.zycm.zybao.service.interfaces.MediaGroupService;
import com.zycm.zybao.service.interfaces.ProgramExportService;
import com.zycm.zybao.service.interfaces.RedisHandleService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;



/**
* @ClassName: ProgramExportServiceFacade
* @Description: 制作好的zip做下载处理类
* @author sy
* @date 2018年7月13日
*
*/
@Slf4j
@Service("programExportServiceFacade")
public class ProgramExportServiceFacade {

	@Autowired(required = false)
	private ResourceLibServiceFacade resourceLibAdapter;
	@Autowired(required = false)
	private ProgramExportService programExportService;
	@Autowired(required = false)
	private RedisHandleService redisHandle;
	@Autowired(required = false)
	private MediaGroupService mediaGroupService;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	private String sysFileSep = System.getProperty("file.separator");
	private String materialRepositoryPath = PublishFtpServiceFacade.getLocalRepository();


	public String exportZIPByProgramIds(Integer[] programIds,List<String> progressInfo) throws Exception{
		if(programIds == null){
			throw new Exception("无效的多个节目id数据！");
		}

		String zippath = exportZIPByProgramIds(programIds,progressInfo, false, null, null);
		return zippath;
	}

	public List<String> exportZIPByMediaGroupIds(Integer mediaGroupId,List<String> progressInfo,HttpServletResponse response) throws Exception{
		List<String> zippaths = exportZIPByMediaGroupIds(new Integer[]{mediaGroupId},progressInfo);
		return zippaths;
	}




	public String exportZIPByProgramIds(Integer[] programIds,List<String> progressInfo,boolean isPublish,Integer mediaGroupId,String zipName) {

		String zipfilename = sdf.format(new Date())+UUID.randomUUID().toString().replace("-", "").toLowerCase();
		String zipPath = System.getProperty("user.home")+sysFileSep+"materialRepository"+sysFileSep+"zip"+sysFileSep+zipfilename+sysFileSep+"Programs";
		//判断服务器本地素材仓库是否存在
		File repositoryFile = new File(materialRepositoryPath);
		if(!repositoryFile.exists()){
			repositoryFile.mkdirs();
		}

		//查询播放脚本
		List<ProgramMsg> proJsonList = null;
		if(isPublish){
			//如果是发布的节目   就生成发布后的脚本   带发布时的播放时间设置信息
			proJsonList = programExportService.selectProgByGroupId(mediaGroupId);
			progressInfo.add("[消息]根据终端组id"+mediaGroupId+"准备播放脚本成功");
		}else{
			//如果是做未发布的处理   就生成永久播放的脚本
			proJsonList = programExportService.selectProgByProgramIds(programIds);
			progressInfo.add("[消息]根据节目id["+JSONArray.fromObject(programIds).toString()+"]准备播放脚本成功");
			zipName = zipfilename;
		}
		String cmdjson="";
		String newGroupMD5="";
		if(proJsonList.size() > 0){
			ProtocolModel protocolModel = new ProtocolModel();
			protocolModel.ServerToClient(MessageEvent.PUBLISH.event, proJsonList);
			protocolModel.setToClient("");
			cmdjson = JSONObject.fromObject(protocolModel).toString();
			newGroupMD5 = MD5.MD5(cmdjson);
			if(isPublish){
				//redis md5脚本校验
				Object oldGroupMD5 = redisHandle.get("group_"+mediaGroupId);
				if(oldGroupMD5 != null){
					//有值需校验

					if(oldGroupMD5.equals(newGroupMD5)){
						//如果值相等说明终端组的节目没有变动
						String pf = System.getProperty("user.home")+sysFileSep+"materialRepository"+sysFileSep+"zip"+sysFileSep+zipName+".zip";
						if(new File(pf).exists()){
							progressInfo.add("[消息]"+zipfilename+"已生成过脚本无变动直接使用!");
							return pf;
						}
					}
				}
			}
		}else{
			progressInfo.add("[错误]"+zipfilename+"查询不到播放脚本!");
			return null;
		}

		//查询出需要下载多少个素材
		List<Map<String,Object>> materials = programExportService.selectByProgramIds(programIds);
		if(materials.size() > 0){
			//创建本次导出的目录
			repositoryFile = new File(zipPath);
			if(!repositoryFile.exists()){
				repositoryFile.mkdirs();
			}

			//开始扫描素材库及需要下载的素材
			progressInfo.add("[消息]开始下载素材到服务器的素材仓库.........");
			File materialFile = null;
			// 2K的数据缓冲
            byte[] bs = new byte[2048];

			for (Map<String, Object> map : materials) {
				String materialName = map.get("materialName").toString();
				String materialPath = map.get("materialPath").toString();
				materialFile = new File(materialRepositoryPath+materialName);
				if(!materialFile.exists()){
					//不存在的素材需从ftp下载
					progressInfo.add("[消息]服务器素材仓库开始下载["+materialPath+materialName+"]...");

					InputStream in = null;
					FileOutputStream out = null;
					// 读取到的数据长度
		            int len;
					try {
						resourceLibAdapter.connect();
						in = resourceLibAdapter.downloadStream(materialPath+materialName);
						out = new FileOutputStream(materialRepositoryPath+materialName);

						// 开始读取
			            while ((len = in.read(bs)) != -1) {
			            	out.write(bs, 0, len);
			            }
			            progressInfo.add("[消息]服务器素材仓库下载完成["+materialPath+materialName+"]...");
					} catch (Exception e) {
						progressInfo.add("[错误]服务器素材仓库开始下载["+materialPath+materialName+"]出现异常!"+e.getMessage());
						log.error("服务器素材仓库开始下载["+materialPath+materialName+"]出现异常!", e);
						return null;
					} finally {
			            // 完毕，关闭所有链接
			            try {
			            	if(null != out)
			                out.close();
			            	if(null != in)
			                in.close();
			            	if(null != resourceLibAdapter)
		            		resourceLibAdapter.close();
			            } catch (Exception e) {
			            	log.error("关闭文件流对象出现异常!", e);
			            }
			        }
				}else{
					progressInfo.add("[消息]服务器素材仓库已存在["+materialPath+materialName+"]...");
				}
				//把素材复制到 本次导出的目录中
				try {
					FileUtil.copyFile(materialRepositoryPath+materialName, zipPath+sysFileSep+materialName);
				} catch (Exception e) {
					log.error("[错误]"+materialRepositoryPath+materialName+"复制到"+zipPath+materialName+"失败！", e);
					return null;
				}
			}
			progressInfo.add("[消息]结束下载素材到服务器的素材仓库.........");
		}else{
			progressInfo.add("[错误]"+JSONArray.fromObject(programIds).toString()+"没有查询到素材信息!");
			log.error(JSONArray.fromObject(programIds).toString()+"没有查询到素材信息!");
			return null;
		}

		//生成播放脚本
		if(proJsonList.size() > 0){
			try {
				FileOutputStream outFile = new FileOutputStream(zipPath+sysFileSep+"ProgramTasks.json");
				outFile.write(cmdjson.getBytes());
				outFile.close();
				progressInfo.add("[消息]"+zipfilename+"工作文件的播放脚本生成成功!");
			} catch (Exception e) {
				progressInfo.add("[错误]"+zipfilename+"工作文件的播放脚本生成失败!");
				log.error("生成节目播放脚本异常！", e);
				return null;
			}

			//压缩成zip
			try {
				//判断是否存在zip
				String p = System.getProperty("user.home")+sysFileSep+"materialRepository"+sysFileSep+"zip"+sysFileSep+zipName+".zip";
				File pp = new File(p);
				if(pp.exists()){
					pp.delete();
				}
				//增加一层文件夹
				ZipUtil.zip(zipPath.replace("Programs", ""), System.getProperty("user.home")+sysFileSep+"materialRepository"+sysFileSep+"zip", zipName+".zip");
				progressInfo.add("[消息]"+zipName+"节目包生成完成!");
				if(isPublish){
					redisHandle.set("group_"+mediaGroupId, newGroupMD5);
				}
				//清理临时存文件的目录
				FileUtil.deleteDirectory(repositoryFile);
				repositoryFile.getParentFile().delete();
				return System.getProperty("user.home")+sysFileSep+"materialRepository"+sysFileSep+"zip"+sysFileSep+zipName+".zip";
			} catch (Exception e) {
				progressInfo.add("[错误]"+zipName+"节目包生成失败!");
				log.error("节目包生成失败！", e);

			}
		}else{
			progressInfo.add("[错误]"+zipfilename+"查询不到播放脚本!");
		}
		//清理临时存文件的目录
		FileUtil.deleteDirectory(repositoryFile);
		repositoryFile.getParentFile().delete();
		return null;
	}


	public List<String> exportZIPByMediaGroupIds(Integer[] mediaGroupIds,List<String> progressInfo) {
		//根据终端组id查出所有节目id
		Map<String,Object> mediaGroup = null;
		List<String> returnZipPath = new ArrayList<String>();
		List<Map<String,Object>> list = null;

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("startRow", 0);
	    param.put("pageSize", 100);

		for (Integer mediaGroupId : mediaGroupIds) {
			mediaGroup = programExportService.selectByPrimaryKey(mediaGroupId);
			if(mediaGroup != null){
				String mediaGroupName = mediaGroup.get("mediaGroupName").toString();
				param.put("mediaGroupIds", new Integer[]{mediaGroupId});
				list = programExportService.selectProgramByGroupId(param);
				Integer[] programIds = null;
				if(list.size() > 0){
					programIds = new Integer[list.size()];
					for (int i = 0; i < list.size(); i++) {
						programIds[i] = Integer.parseInt(list.get(i).get("programId").toString());
					}
					progressInfo.add("[消息]开始制作["+mediaGroupName+"]分组的节目包!>>>>>>>>>>>>>>>>>>>>");
					String zippath = exportZIPByProgramIds(programIds,progressInfo,true, mediaGroupId, mediaGroupName);
					if(StringUtils.isNotBlank(zippath))
					returnZipPath.add(zippath);
					progressInfo.add("[消息]结束制作["+mediaGroupName+"]分组的节目包!<<<<<<<<<<<<<<<<<<<");
				}else{
					progressInfo.add("[错误]["+mediaGroupName+"]没有查询到节目！");
				}
			}
		}
		return returnZipPath;
	}

}
