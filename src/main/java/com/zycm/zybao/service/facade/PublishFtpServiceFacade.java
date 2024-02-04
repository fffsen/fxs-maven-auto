package com.zycm.zybao.service.facade;

import com.zycm.zybao.common.config.FtpConfig;
import com.zycm.zybao.common.config.SysConfig;
import com.zycm.zybao.common.utils.*;
import com.zycm.zybao.model.entity.ProgramMaterialModel;
import com.zycm.zybao.model.upload.ProgressModel;
import com.zycm.zybao.model.vo.UploadVo;
import com.zycm.zybao.service.interfaces.ProgramMaterialService;
import com.zycm.zybao.service.interfaces.ProgramService;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.util.*;


@Slf4j
@Service
public class PublishFtpServiceFacade {

	public static final String PROGRESS_KEY = "upload_ps";

	@Autowired(required = false)
	private ProgramMaterialService programMaterialService;
	@Autowired(required = false)
	private ProgramService programService;

	private final static String AUDIT_SWITCH = SysConfig.materialAudit;
	private final static String IMG_SERVER_PATH = FtpConfig.imgServerPath;

	@Autowired(required = false)
	private ResourceLibServiceFacade resourceLibAdapter;

	public synchronized List<String> uploadFiles2(CommonsMultipartFile[] files, UploadVo uploadVo, Map<String,Map<String, ProgressModel>> process_map, List<Integer> re_materialIds){
		String remoteFolderPath = uploadVo.getRelativePath();
		List<String> error = new ArrayList<String>();
		Map<String,ProgressModel> process_item = null;
		if(uploadVo.isShowProgress()){
			//实例进度条信息类
			process_item = new HashMap<String,ProgressModel>();
			process_map.put(PROGRESS_KEY+"_"+uploadVo.getCreaterId(), process_item);
		}

		//建立ftp连接
		if(files.length > 0){
			try {
				resourceLibAdapter.connect();
			} catch (Exception e) {
				error.add("连接资源库失败");
				return error;
			}
		}else{
			error.add("上传的文件为空");
			return error;
		}

		//地址处理
		//获取ffmpeg目录
		String proPath = this.getClass().getClassLoader().getResource("").getPath().replace("file:/","");//转码工具地址
		String os = System.getProperty("os.name");

		if(!os.toLowerCase().startsWith("win")){
			proPath = proPath.substring(proPath.indexOf("/") == 0?1:0, proPath.indexOf("zycm-advert-publish"));
			proPath = FileUtil.sysFileSep+proPath;
		} else {
			//proPath = proPath.substring(proPath.indexOf("/") == 0?1:0, proPath.indexOf("zycm-advert-publish")+19);
			proPath = proPath.replace("/", FileUtil.sysFileSep);
		}
		log.info("转换工具路径："+proPath);

		//遍历处理文件
		List<ProgramMaterialModel> materialList = new ArrayList<ProgramMaterialModel>();
		Encoder encoder = new Encoder();
		for (int i = 0; i < files.length; i++) {
			try {
				if(files[i].getSize() > 0){
					//给文件做验证及编码转换  再上传ftp或oss
					//获取的是临时文件信息
					DiskFileItem fi = (DiskFileItem)files[i].getFileItem();
					String localFilePaths = fi.getStoreLocation().getPath();
					String listfilenames = files[i].getOriginalFilename();
					Integer type = FileTypeUtil.getFileType(listfilenames);
					MultimediaInfo mediaInfo = null;
					BufferedImage bfimage = null;
					//图片文件验证
					if(type == 1){//图片
						try {
							String picDecoder = VideoConvertUtil.getPicDecoder(fi.getStoreLocation());
							if(StringUtils.isNotBlank(picDecoder)
									&& !"MJPEG".equalsIgnoreCase(picDecoder)
									&& !"JPEG".equalsIgnoreCase(picDecoder)
									//&& !"jpeg2000".equalsIgnoreCase(picDecoder)
									&& !"MJPG".equalsIgnoreCase(picDecoder)
									&& !"JPG".equalsIgnoreCase(picDecoder)
									&& !"PNG".equalsIgnoreCase(picDecoder)){
								//图片编码格式不是 mjpeg png 就需要转码
								log.error(listfilenames+"图片编码["+picDecoder+"]不是支持的类型请转换成jpg图片!");
								error.add(listfilenames+"图片编码["+picDecoder+"]不是支持的类型请转换成jpg图片!");
								continue;
							}
						} catch (Exception e) {
							log.error(listfilenames+"图片编码格式不正确!",e);
							error.add(listfilenames+"图片编码格式不正确!");
							continue;
						}
					}

					//视频文件验证
					if(type == 2){
						mediaInfo = encoder.getInfo(fi.getStoreLocation());
						String videoDecoder = mediaInfo.getVideo().getDecoder();
						if(!"h264".equalsIgnoreCase(videoDecoder) || !"mp4".equalsIgnoreCase(listfilenames.substring(listfilenames.lastIndexOf(".")+1))){
							//视频类型做转码处理 都转成mp4
							String convertToPath = proPath+FileUtil.sysFileSep+"ffpeg"+FileUtil.sysFileSep+"convert"+FileUtil.sysFileSep+listfilenames.substring(0, listfilenames.lastIndexOf("."))+".mp4";
							String ffmpegexe = proPath+FileUtil.sysFileSep+"ffpeg"+FileUtil.sysFileSep+"ffmpeg.exe";
							//boolean isconvert = false;
							if(!os.toLowerCase().startsWith("win")){
								ffmpegexe = "ffmpeg";
							}

							try {
								if(!"h264".equalsIgnoreCase(mediaInfo.getVideo().getDecoder())){
									//需要视频转码成H264
									VideoConvertUtil.videoToMp4(ffmpegexe, localFilePaths, convertToPath,false);
								}else{
									if(!"mp4".equalsIgnoreCase(listfilenames.substring(listfilenames.lastIndexOf(".")+1))){
										//不是mp4文件  需无损转码成mp4文件
										VideoConvertUtil.videoToMp4(ffmpegexe, localFilePaths, convertToPath,true);
									}
								}
							} catch (Exception e) {
								log.error(listfilenames+"转换成H264视频编码的mp4失败!", e);
								error.add(listfilenames+"转换成H264视频编码的mp4失败!");
								continue;
							}
							listfilenames = listfilenames.substring(0, listfilenames.lastIndexOf("."))+".mp4";
							localFilePaths = convertToPath;
						}
					}

					//文本文件验证
					if(type == 3){
						if(fi.getStoreLocation().length() > 1024*1024){
							log.error(listfilenames+" 文本大小超过1M!");
							error.add(listfilenames+" 文本大小超过1M!");
							continue;
						}
						//判断文件编码
						try {
							String txtfilecode = EncodingDetect.getJavaEncode(fi.getStoreLocation());
							if(!"UTF-8".equalsIgnoreCase(txtfilecode)){
								//转换txt编码
								String convertToPath1 = proPath+FileUtil.sysFileSep+"ffpeg"+FileUtil.sysFileSep+"convert"+FileUtil.sysFileSep+listfilenames.substring(0, listfilenames.lastIndexOf("."))+".txt";
								FileUtil.convertTxtAsUTF8(fi.getStoreLocation(), txtfilecode, convertToPath1);

								listfilenames = listfilenames.substring(0, listfilenames.lastIndexOf("."))+".txt";
								localFilePaths = convertToPath1;
							}else{
								if(listfilenames.contains(".TXT")){
									//处理后缀大写问题 此问题仅出现在oss中 如果大写后缀会出现下载为0kb的文件现象
									listfilenames = listfilenames.substring(0, listfilenames.lastIndexOf("."))+".txt";
								}
							}
						} catch (Exception e) {
							log.error(listfilenames+"转换成utf8的txt文件失败!", e);
							error.add(listfilenames+"转换成utf8的txt文件失败!");
							continue;
						}
					}

					if(type == 99){
						log.error(listfilenames+"不支持的类型不做上传!");
						error.add(listfilenames+"不支持的类型不做上传!");
						continue;
					}

					if(type == 6){
						if(listfilenames.split("-").length != 2){
							error.add("["+listfilenames+"]上传的安装包不符合文件命名规则:XXX-版本号.apk,'-'符号只能有一个！");
							continue;
						}
					}

					boolean isexists = resourceLibAdapter.doesObjectExist(remoteFolderPath+listfilenames);

					List<Map<String,Object>> mList = programMaterialService.checkMaterialName(new String[]{listfilenames}, null, null);
					//判断上传的文件在ftp或数据库中是否已存在
					if(isexists || mList.size() > 0){
						log.error(">>>ftp路径：["+remoteFolderPath+listfilenames+"]上传的文件已存在");
						error.add("["+remoteFolderPath+listfilenames+"]上传的文件已存在");
					}else{
						boolean b = false;
						if(uploadVo.isShowProgress()){
							//开始上传文件前  设置数据
							ProgressModel progressModel = new ProgressModel();
							progressModel.setContentLength(fi.getStoreLocation().length()/1024);
							progressModel.setBytesRead(0);
							progressModel.setPercent("0");
							progressModel.setRemoteFilename(listfilenames);

							process_item.put(listfilenames, progressModel);//用户一次上传多个文件的处理

							b = uploadOneWithProgress(localFilePaths, listfilenames, remoteFolderPath,progressModel);
						}else{
							b = uploadOne(localFilePaths, listfilenames, remoteFolderPath);
						}

						if(b){
							//上传成功后校验md5值
							String mastermd5 = resourceLibAdapter.getMd5(remoteFolderPath+listfilenames);
							File localf = new File(localFilePaths);
							String localmd5 = Md5Util.getFileMD5(localf);
							if(localmd5.equals(mastermd5)){
								 ProgramMaterialModel pmm = new ProgramMaterialModel();
				       			 pmm.setMaterialName(listfilenames);
				       			 pmm.setCreatorId(uploadVo.getCreaterId());
				       			 pmm.setMaterialPath(remoteFolderPath);
				       			 pmm.setType(type);
				       			 pmm.setIsPrivate(0);

				       			 pmm.setSize(new BigDecimal(localf.length()/1024.00));//kb
				       			 if(type == 1){//图片
				       				 bfimage = ImageIO.read(fi.getStoreLocation());
				       				 pmm.setHeight(new BigDecimal(bfimage.getHeight()));
				           			 pmm.setWidth(new BigDecimal(bfimage.getWidth()));

				           			 //开始内容审核
					       			 if("1".equals(AUDIT_SWITCH)){
					       				String ispass = OSSManageUtil.picAiAudit(IMG_SERVER_PATH+"/"+remoteFolderPath+listfilenames);
					       				pmm.setAiAuditStatus(ispass);
					       			 }
				       			 }else if(type == 2){//视频
				       				 pmm.setHeight(new BigDecimal(mediaInfo.getVideo().getSize().getHeight()));
				           			 pmm.setWidth(new BigDecimal(mediaInfo.getVideo().getSize().getWidth()));
				           			 long ls = mediaInfo.getDuration();
				           			 pmm.setTimeLenth((int)ls/1000);

				           			 //开始内容审核
					       			 if("1".equals(AUDIT_SWITCH)){
					       				String taskid = OSSManageUtil.videoContentSecurity(IMG_SERVER_PATH+"/"+remoteFolderPath+listfilenames);
					       				pmm.setAiAuditStatus(taskid);//视频则保存任务id  单个的查询检查结果
					       			 }
				       			 }else if(type == 3){//文本
				       				 //开始内容审核
					       			 if("1".equals(AUDIT_SWITCH)){
					       				BufferedReader bis = null;
					       				StringBuffer buf=new StringBuffer();
					       				try {
						       				//获取远程文件的编码  避免读取到乱码
						       				bis = new BufferedReader(new FileReader(localf));
						       				String temp;
						       				while ((temp = bis.readLine()) != null) {
						       				   buf.append(temp);
						       				   if(buf.length()>=10000){
						       				    break;
						       				   }
						       				}
										} catch (Exception e) {
											log.error("文本智能审核读取文本异常", e);
										} finally {
											if(bis != null)
											bis.close();
										}
					       				if(StringUtils.isNotBlank(buf.toString())){
					       					String ispass = OSSManageUtil.txtAiAudit(buf.toString());
						       				pmm.setAiAuditStatus(ispass);
					       				}else{
					       					log.warn("文本智能审核读取文本内容为空");
					       				}
					       			 }
				       			 }else if(type == 6){//安装包
			       					 pmm.setApkVersion("V"+listfilenames.split("-")[listfilenames.split("-").length-1].replace(".apk", "").replace(".APK", ""));
				       			 }else {

				       			 }
				       			 pmm.setAuditStatus(0);
				       			 pmm.setUploadTime(new Date());
				       			 pmm.setIsDelete(0);
				       			 pmm.setCheckCode(mastermd5);
				       			 materialList.add(pmm);

							}else{
								//如果不相同需要先删除远程的文件  重新上传
								log.error("文件["+files[i].getOriginalFilename()+"]在ftp服务上md5值["+mastermd5+"]服务上md5值["+localmd5+"]两者不一致");
								resourceLibAdapter.deleteFile(remoteFolderPath+listfilenames);
								error.add("["+remoteFolderPath+listfilenames+"]上传的文件有数据丢失请重新上传");
							}
						}else{
							error.add("["+remoteFolderPath+listfilenames+"]上传的文件失败");
						}
					}
				}else{
					log.error("["+files[i].getOriginalFilename()+"]上传的文件大小为0不做上传");
					error.add("["+files[i].getOriginalFilename()+"]上传的文件大小为0不做上传");
				}
			} catch (Exception e) {
				log.error("["+files[i].getOriginalFilename()+"]上传时出现异常", e);
				error.add("["+files[i].getOriginalFilename()+"]上传时出现异常");
			}


		}
		//添加上传信息
		if(materialList.size() > 0){
			List<ProgramMaterialModel> ll = programMaterialService.batchInsert(materialList);
			//返回新增的素材id
			if(materialList.size() > 0){
				for (ProgramMaterialModel programMaterialModel2 : ll) {
					re_materialIds.add(programMaterialModel2.getMaterialId());
				}
			}

		}

		if(files.length > 0){
			try {
				resourceLibAdapter.close();
			} catch (Exception e) {
				log.error("资源库连接关闭异常", e);
			}
		}
		return error;
	}

	private boolean uploadOneWithProgress(String localFilePaths,String listfilenames, String remoteFolderPath,ProgressModel progressModel){
		try {
			resourceLibAdapter.resumeUploadProgress(localFilePaths, remoteFolderPath+listfilenames,progressModel);
			log.info("文件【"+localFilePaths+"】已上传至【"+remoteFolderPath+listfilenames+"】");
			return true;
		} catch (Exception e) {
			log.error("文件【"+localFilePaths+"】上传至【"+remoteFolderPath+listfilenames+"】出现异常",e);
			return false;
		}
	}

	private boolean uploadOne(String localFilePaths,String listfilenames, String remoteFolderPath){
		try {
			resourceLibAdapter.resumeUpload(localFilePaths, remoteFolderPath+listfilenames);
			log.info("文件【"+localFilePaths+"】已上传至【"+remoteFolderPath+listfilenames+"】");
			return true;
		} catch (Exception e) {
			log.error("文件【"+localFilePaths+"】上传至【"+remoteFolderPath+listfilenames+"】出现异常",e);
			return false;
		}
	}

	public Integer addNetAndLiveAddr(HttpSession session,String fileName,Integer creatorId,String relativePath,String materialType,String sourceUrl) throws Exception{
		List<ProgramMaterialModel> materialList = new ArrayList<ProgramMaterialModel>();
		//上传网站、直播脚本
		resourceLibAdapter.connect();
		resourceLibAdapter.uploadResourceFile(sourceUrl, relativePath+fileName);
		String mastermd5 = resourceLibAdapter.getMd5(relativePath+fileName);
		resourceLibAdapter.close();
 		//网站、直播源
 		 ProgramMaterialModel pmm = new ProgramMaterialModel();
		 pmm.setMaterialName(fileName);
		 pmm.setCreatorId(creatorId);
		 pmm.setMaterialPath(relativePath);
		 pmm.setType(Integer.parseInt(materialType));
		 pmm.setSize(new BigDecimal((sourceUrl.getBytes().length/1024.00)).setScale(4, BigDecimal.ROUND_HALF_DOWN));
		 pmm.setIsPrivate(0);
		 pmm.setUploadTime(new Date());
		 pmm.setSourceUrl(sourceUrl);
		 pmm.setAuditStatus(0);
		 pmm.setIsDelete(0);
		 pmm.setCheckCode(mastermd5);
		 materialList.add(pmm);
		 List<ProgramMaterialModel> ll = programMaterialService.batchInsert(materialList);

		 return ll.get(0).getMaterialId();
	}

	public void createFolder(String remoteFolder,String folder,String creatorId) throws Exception{
		resourceLibAdapter.connect();
		resourceLibAdapter.createDir(remoteFolder+folder.trim()+"/");
		resourceLibAdapter.close();

		List<ProgramMaterialModel> materialList = new ArrayList<ProgramMaterialModel>();
 		//网站、直播源
 		 ProgramMaterialModel pmm = new ProgramMaterialModel();
		 pmm.setMaterialName(folder.trim());
		 pmm.setCreatorId(Integer.parseInt(creatorId));
		 pmm.setMaterialPath(remoteFolder);
		 pmm.setType(0);
		 pmm.setIsPrivate(0);
		 pmm.setUploadTime(new Date());
		 pmm.setAuditStatus(1);
		 pmm.setIsDelete(0);
		 materialList.add(pmm);
		 programMaterialService.batchInsert(materialList);

	}

	//上传完文件后  直接把素材放入新建的文件夹中
	public void createFolder(String remoteFolder,String folder,Integer creatorId,Integer[] materialIds) throws Exception{
		resourceLibAdapter.connect();
		resourceLibAdapter.createDir(remoteFolder+folder.trim()+"/");

		List<ProgramMaterialModel> materialList = new ArrayList<ProgramMaterialModel>();
		if(materialIds == null || materialIds.length == 0){//只要新建文件夹
	 		//网站、直播源
	 		 ProgramMaterialModel pmm = new ProgramMaterialModel();
			 pmm.setMaterialName(folder.trim());
			 pmm.setCreatorId(creatorId);
			 pmm.setMaterialPath(remoteFolder);
			 pmm.setType(0);
			 pmm.setIsPrivate(0);
			 pmm.setUploadTime(new Date());
			 pmm.setAuditStatus(1);
			 pmm.setIsDelete(0);
			 materialList.add(pmm);
			 programMaterialService.batchInsert(materialList);
		}else{//不为空  则把素材放入文件夹中

	 		 //网站、直播源
	 		 ProgramMaterialModel pmm = new ProgramMaterialModel();
			 pmm.setMaterialName(folder.trim());
			 pmm.setCreatorId(creatorId);
			 pmm.setMaterialPath(remoteFolder);
			 pmm.setType(0);
			 pmm.setIsPrivate(0);
			 pmm.setUploadTime(new Date());
			 pmm.setAuditStatus(1);
			 pmm.setIsDelete(0);
			 materialList.add(pmm);
			 programMaterialService.batchInsert(materialList);
			 //转移ftp上指定素材的目录
			 for (Integer materialId : materialIds) {
				try {
					Map<String,Object> material = programMaterialService.selectByPrimaryKey(materialId);
					String materialName = material.get("materialName").toString();
					String materialPath = material.get("materialPath").toString();
					if(StringUtils.isNoneBlank(materialName,materialPath)){
						//移动文件到指定目录
						resourceLibAdapter.moveFile(materialPath+materialName, remoteFolder+folder.trim()+"/"+materialName);
						//修改素材的路径
						programMaterialService.updateMaterialPath(materialId, remoteFolder+folder.trim()+"/");
					}
				} catch (Exception e) {
					log.error("文件目录更换失败，素材id"+materialId+"更换后的目录"+remoteFolder+folder.trim()+"/", e);
					throw new Exception("文件目录更换失败，素材id"+materialId+"更换后的目录"+remoteFolder+folder.trim()+"/");
				}

			 }
		}
		resourceLibAdapter.close();
	}

	public boolean existsFolder(String remoteFolder,String folder,String creatorId) throws Exception{
		resourceLibAdapter.connect();
		String ftppath = remoteFolder+folder+"/";
		if(resourceLibAdapter.doesObjectExist(ftppath)){
			resourceLibAdapter.close();
			return true;
		}else{
			resourceLibAdapter.close();
			return false;
		}
	}

	public String deleteFile(Integer materialId) throws Exception{
		String errormsg = "";
		Map<String, Object> map = programMaterialService.selectByPrimaryKey(materialId);
		if(map != null){
			//删除素材
			resourceLibAdapter.connect();
			String target = "";
			try {
				String type = map.get("type").toString();
				if("1".equals(type) || "2".equals(type) || "3".equals(type) || "4".equals(type) || "5".equals(type) || "6".equals(type)){
					target = map.get("materialPath").toString()+map.get("materialName").toString();
					try {
						resourceLibAdapter.deleteFile(target);
					} catch (Exception e) {
						log.error("删除素材【"+target+"】异常", e);
					}

					//清理本地素材仓库的素材  避免删除一个素材后又上传一个同名素材而导致导出的节目包素材不一致的情况
					try {
						File localRepositoryFile = new File(this.getLocalRepository()+map.get("materialName").toString());
						if(localRepositoryFile.exists()){
							localRepositoryFile.delete();
						}
					} catch (Exception e) {
						log.error("清理服务器的本地素材仓库异常！", e);
					}
					//逻辑删除素材信息
					programMaterialService.updateIsDelete(materialId);
					//删除节目中的素材数据
					List<Map<String,Object>> prolist = programService.selectByMaterialId(materialId);

					programService.deleteByMaterialId(materialId);
					//删除后   处理节目时间的计算
					if(prolist.size() > 0)
					programService.deleteMaterialIdReduceTime(prolist);

				}else if("0".equals(type)){
					target = map.get("materialPath").toString()+"/"+map.get("materialName").toString()+"/";
					target = target.replaceAll("//", "/");
					if(!resourceLibAdapter.dirExistFile(target)){//判断目录下是否存在文件
						resourceLibAdapter.deleteDir(target);
					}else{
						errormsg = "不能删除【"+target+"】，只能删除空的文件夹";
						log.error("不能删除【"+target+"】，只能删除空的文件夹");
						resourceLibAdapter.close();
						return errormsg;
					}
					//逻辑删除素材信息
					programMaterialService.updateIsDelete(materialId);

				}else{
					target = map.get("materialName").toString();
					log.error("不支持的素材类型："+target);
				}


			} catch (Exception e) {
				log.error("删除【"+target+"】失败");
				errormsg = "删除【"+target+"】失败";
				//throw new Exception("删除【"+target+"】失败");
			}
			resourceLibAdapter.close();

		}else{
			errormsg = "素材【"+materialId+"】查不到数据";
		}
		return errormsg;
	}

	public void downLoad(Integer materialId,HttpServletResponse response) throws Exception{
		Map<String, Object> map = programMaterialService.selectByPrimaryKey(materialId);
		if(map != null){
			resourceLibAdapter.connect();
			String materialPath = map.get("materialPath").toString();
			String materialName = map.get("materialName").toString();
			String type = map.get("type").toString();
			if("1".equals(type) || "2".equals(type) || "3".equals(type) || "6".equals(type)){
				String remotePath = materialPath+materialName;
				//验证文件是否存在ftp
				if(resourceLibAdapter.doesObjectExist(remotePath)){
					InputStream in = resourceLibAdapter.downloadStream(remotePath);
			        BufferedInputStream bis = null;
			        BufferedOutputStream bos = null;

			        try {
		                 response.setContentType("application/x-msdownload;");
		                 response.setHeader("Content-disposition", "attachment; filename="
		                         + new String(materialName.getBytes("utf-8"), "ISO8859-1"));
		                 response.setHeader("Content-Length", String.valueOf(Double.parseDouble(map.get("size").toString())*1024));
		                 bis = new BufferedInputStream(in);
		                 bos = new BufferedOutputStream(response.getOutputStream());
		                 byte[] buff = new byte[2048];
		                 int bytesRead;
		                 while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
		                     bos.write(buff, 0, bytesRead);
		                 }
		                 log.info("素材【"+materialName+"】下载成功");
		             } catch (Exception e) {
		            	 log.error("素材【"+materialName+"】下载失败");
		                 throw new Exception("素材【"+materialName+"】下载失败", e);
		             } finally {
		                 if (bis != null)
		                     bis.close();
		                 if (bos != null)
		                     bos.close();
		             }
				}else{
					throw new Exception("素材【"+materialName+"】已不存在ftp服务器上");
				}
			}else{
				log.error("网站、文件、直播源无需下载处理"+materialName);
				throw new Exception("素材【"+materialName+"】属于网站、直播源、文件夹不支持下载可直接详情查看");
			}
		}else{
			log.error("根据id【"+materialId+"】没有查到要下载的素材");
		}


	}


	public synchronized List<String> uploadEnclosure(CommonsMultipartFile[] files, String remoteFolderPath){
		List<String> error = new ArrayList<String>();

		//建立ftp连接
		if(files.length > 0){
			try {
				resourceLibAdapter.connect();
			} catch (Exception e) {
				error.add("连接资源库失败");
				return error;
			}
		}else{
			error.add("上传的文件为空");
			return error;
		}
		/*String proPath = this.getClass().getClassLoader().getResource("").getPath().replace("file:/","");//转码工具地址
		proPath = proPath.substring(proPath.indexOf("/") == 0?1:0, proPath.indexOf("zycm-advert-publish"));
		String sysFileSep = System.getProperty("file.separator");
		String os = System.getProperty("os.name");
		proPath = proPath.replace("/", sysFileSep);
		if(!os.toLowerCase().startsWith("win")){
			proPath = sysFileSep+proPath;
		}
		log.info("转换工具路径："+proPath);*/
		String proPath = this.getClass().getClassLoader().getResource("").getPath().replace("file:/","");//转码工具地址
		String os = System.getProperty("os.name");
		//proPath = proPath.replace("/", FileUtil.sysFileSep);
		if(!os.toLowerCase().startsWith("win")){
			proPath = proPath.substring(proPath.indexOf("/") == 0?1:0, proPath.indexOf("zycm-advert-publish"));
			proPath = FileUtil.sysFileSep+proPath;
		} else {
			proPath = proPath.replace("/", FileUtil.sysFileSep);
			//proPath = proPath.substring(proPath.indexOf("/") == 0?1:0, proPath.indexOf("zycm-advert-publish")+19);
		}
		log.info("转换工具路径："+proPath);

		List<ProgramMaterialModel> materialList = new ArrayList<ProgramMaterialModel>();
		Encoder encoder = new Encoder();
		for (int i = 0; i < files.length; i++) {
			try {
				if(files[i].getSize() > 0){
					//获取的是临时文件信息
					DiskFileItem fi = (DiskFileItem)files[i].getFileItem();
					String localFilePaths = fi.getStoreLocation().getPath();
					String listfilenames = files[i].getOriginalFilename();
					Integer type = FileTypeUtil.getFileType(listfilenames);
					MultimediaInfo mediaInfo = null;
					BufferedImage bfimage = null;

					boolean isexists = resourceLibAdapter.doesObjectExist(remoteFolderPath+listfilenames);
					if(isexists){
						log.error(">>>ftp路径：["+remoteFolderPath+listfilenames+"]上传的文件已存在");
						error.add("["+remoteFolderPath+listfilenames+"]上传的文件已存在");
					}else{
						//开始上传文件前  设置数据

						boolean b = uploadOne(localFilePaths, listfilenames, remoteFolderPath);
						if(b){

						}else{
							error.add("["+remoteFolderPath+listfilenames+"]上传的文件失败");
						}
					}
				}else{
					log.error("["+files[i].getOriginalFilename()+"]上传的文件大小为0不做上传");
					error.add("["+files[i].getOriginalFilename()+"]上传的文件大小为0不做上传");
				}
			} catch (Exception e) {
				log.error("["+files[i].getOriginalFilename()+"]上传时出现异常", e);
				error.add("["+files[i].getOriginalFilename()+"]上传时出现异常");
			}


		}
		//添加上传信息
		if(materialList.size() > 0){
			programMaterialService.batchInsert(materialList);
		}

		if(files.length > 0){
			try {
				resourceLibAdapter.close();
			} catch (Exception e) {
				log.error("资源库连接关闭异常", e);
			}
		}
		return error;
	}

	public static String getLocalRepository(){
		String sysFileSep = System.getProperty("file.separator");
		String materialPath = System.getProperty("user.home")+sysFileSep+"materialRepository"+sysFileSep+"material"+sysFileSep;
		return materialPath;
	}

}
