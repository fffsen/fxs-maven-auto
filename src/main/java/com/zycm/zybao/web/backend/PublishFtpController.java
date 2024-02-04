package com.zycm.zybao.web.backend;

import com.zycm.zybao.common.utils.ArrayUtils;
import com.zycm.zybao.model.upload.ProgressModel;
import com.zycm.zybao.model.vo.UploadVo;
import com.zycm.zybao.service.facade.PublishFtpServiceFacade;
import com.zycm.zybao.web.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.Map.Entry;


@Slf4j
@Controller
@RequestMapping("/ftp/*")
public class PublishFtpController extends BaseController {

	//存储进度数据
	private Map<String,Map<String, ProgressModel>> process_map = new HashMap<String,Map<String,ProgressModel>>();
	@Autowired(required = false)
	private PublishFtpServiceFacade publishFtpServiceFacade;

	//angular版使用
/*	@RequestMapping(value = "uploadMaterial")
	@ResponseBody
	public  Map<String,Object> uploadMaterial(@RequestParam("files") CommonsMultipartFile[] files,HttpServletRequest request,
			HttpServletResponse response) {

		Map<String,Object> map = new HashMap<String,Object>();
		String relativePath = request.getParameter("relativePath");
    	//网站或直播源用的字段
    	String materialType = request.getParameter("materialType");
		try{
			if(StringUtils.isNoneBlank(relativePath)){
				//处理相对路径
				if("/".equals(relativePath) || StringUtils.isBlank(relativePath)){
					relativePath = "/material/";
				}else{
					relativePath = "/material"+relativePath;
				}
				//处理网站或直播源信息
				if("4".equals(materialType) || "5".equals(materialType)){
         			map.put("result", "error");
              		map.put("message", "该接口不处理网址、直播源的添加");
              		map.put("data", null);
              		return map;
             	}
				UploadVo uploadVo = new UploadVo();
				uploadVo.setRelativePath(relativePath);
				uploadVo.setCreaterId(getUserId(request));
				uploadVo.setShowProgress(false);

				List<String> errormsg = publishFtpServiceFacade.uploadFiles2(files,uploadVo,process_map,null);
				if(errormsg.size() > 0){
					map.put("result", "fail");
					map.put("message", "上传素材有异常");
					map.put("data", errormsg);
				}else{
					map.put("result", "success");
					map.put("message", "成功");
					map.put("data", null);
				}
			}else{
				map.put("result", "error");
				map.put("message", "上传的文件不能为空");
				map.put("data", null);
			}
		}catch(Exception e){
			log.error("上传文件异常！", e);
			map.put("result", "error");
			map.put("message", "上传文件异常！"+e.getMessage());
			map.put("data", null);
		}

		return map;
	}*/

	@RequiresPermissions("ftp:uploadMaterial3")
	@RequestMapping(value = "uploadMaterial3")
	@ResponseBody
	public  Map<String,Object> uploadMaterial3(@RequestParam("file") CommonsMultipartFile[] file, HttpServletRequest request,
											   HttpServletResponse response) {

		Map<String,Object> map = new HashMap<String,Object>();
		String relativePath = request.getParameter("relativePath");
		String showProgress = request.getParameter("showProgress");
		String uuid = request.getParameter("uuid");
		try{
			if(StringUtils.isNoneBlank(relativePath)){
				//处理相对路径
				if("/".equals(relativePath) || StringUtils.isBlank(relativePath)){
					relativePath = "/material/";
				}else{
					relativePath = "/material"+relativePath;
				}
				List<Integer> re_materialIds = new ArrayList<Integer>();
				UploadVo uploadVo = new UploadVo();
				uploadVo.setRelativePath(relativePath);
				uploadVo.setCreaterId(getUserId(request));
				uploadVo.setShowProgress("1".equals(showProgress)?true:false);
				uploadVo.setUuid(uuid);

				//开始上传前  清理掉用户半小时之前的上传进度信息
				Map<String, ProgressModel> user_upload_file_map = process_map.get(PublishFtpServiceFacade.PROGRESS_KEY+"_"+uploadVo.getCreaterId());
				if(user_upload_file_map != null && user_upload_file_map.keySet().size() > 0){
					log.debug("统计上传用户 "+uploadVo.getCreaterId()+" 的上传进度信息"+user_upload_file_map.keySet().size()+"条");
					long nowTime = System.currentTimeMillis();
					Set<Entry<String, ProgressModel>> user_upload_file_set = user_upload_file_map.entrySet();
					Iterator<Entry<String, ProgressModel>> iterator = user_upload_file_set.iterator();
					while(iterator.hasNext()){
						Entry<String, ProgressModel> entry = iterator.next();
					    String key_user_upload_file=entry.getKey();
					    if(nowTime - user_upload_file_map.get(key_user_upload_file).getStartReatTime() > 5*60*1000){
							//特别注意：不能使用map.remove(name)  否则会报同样的错误
					    	//如果文件上传记录超过半小时  直接清理掉
					    	iterator.remove();
						}
					}
					log.debug("清理后用户 "+uploadVo.getCreaterId()+" 的上传进度信息"+process_map.get(PublishFtpServiceFacade.PROGRESS_KEY+"_"+uploadVo.getCreaterId()).keySet().size()+"条");
				}

				List<String> errormsg = publishFtpServiceFacade.uploadFiles2(file,uploadVo,process_map,re_materialIds);
				if(errormsg.size() > 0){
					map.put("result", "fail");
					map.put("message", "上传素材有异常");
					map.put("data", errormsg);
				}else{
					map.put("result", "success");
					map.put("message", "成功");
					map.put("data", re_materialIds);
				}
			}else{
				map.put("result", "error");
				map.put("message", "上传的文件不能为空");
				map.put("data", null);
			}
		}catch(Exception e){
			log.error("上传文件异常！", e);
			map.put("result", "error");
			map.put("message", "上传文件异常！"+e.getMessage());
			map.put("data", null);
		}

		return map;
	}

	/**
	* @Title: uploadMaterial2
	* @Description: 添加网址、直播源   angular版使用
	* @author sy
	* @param @param request
	* @param @param response
	* @param @return
	* @return Map<String,Object>
	* @throws
	*/
	@RequestMapping(value = "uploadMaterial2")
	@ResponseBody
	public  Map<String,Object> uploadMaterial2(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> map = new HashMap<String,Object>();
		String relativePath = request.getParameter("relativePath");
    	//网站或直播源用的字段
    	String materialType = request.getParameter("materialType");
    	String fileName = request.getParameter("fileName");
    	String sourceUrl = request.getParameter("sourceUrl");
		try{
			if(StringUtils.isNoneBlank(relativePath) && StringUtils.isNoneBlank(materialType)
					&& StringUtils.isNoneBlank(fileName) && StringUtils.isNoneBlank(sourceUrl)){
				//处理相对路径
				if("/".equals(relativePath) || StringUtils.isBlank(relativePath)){
					relativePath = "/material/";
				}else{
					relativePath = "/material"+relativePath;
				}
				//处理网站或直播源信息
				if("4".equals(materialType) || "5".equals(materialType)){
					fileName = "4".equals(materialType)?fileName+".web":fileName+".live";
					Integer materialId = publishFtpServiceFacade.addNetAndLiveAddr(request.getSession(),fileName, getUserId(request), relativePath, materialType, sourceUrl);
         			map.put("result", "success");
              		map.put("message", "成功");
              		map.put("data", materialId);
              		return map;
             	}else{
             		map.put("result", "error");
    				map.put("message", "该接口只处理网站或直播源信息");
    				map.put("data", null);
             	}

			}else{
				map.put("result", "error");
				map.put("message", "参数不能为空");
				map.put("data", null);
			}
		}catch(Exception e){
			log.error("保存网站或直播源信息异常！", e);
			map.put("result", "error");
			map.put("message", "保存网站或直播源信息异常！"+e.getMessage());
			map.put("data", null);
		}

		return map;
	}

	@RequiresPermissions("ftp:downloadMaterial")
	@RequestMapping("/downloadMaterial")
	@ResponseBody
    public Map<String,Object> downloadMaterial(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		String materialId = request.getParameter("materialId");
		try {
			if(StringUtils.isNoneBlank(materialId)){
				publishFtpServiceFacade.downLoad(Integer.parseInt(materialId), response);
				return null;
			}else{
				map.put("result", "error");
				map.put("message", "必传参数不能为空");
				map.put("data", null);
				log.info("下载文件异常！必传参数不能为空");
				return map;
			}
		} catch (Exception e) {
			map.put("result", "error");
			map.put("message", "下载文件异常，"+e.getMessage());
			map.put("data", null);
			log.error("下载文件异常!",e);
			return map;
		}



    }

	@RequiresPermissions("ftp:createFolder")
	@RequestMapping(value = "createFolder")
	@ResponseBody
	public  Map<String,Object> createFolder(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> map = new HashMap<String,Object>();
		String relativePath = request.getParameter("relativePath");
    	String folder = request.getParameter("folder") != null?request.getParameter("folder").trim():"";
    	String materialIds_p = request.getParameter("materialIds");
		try{
			if(StringUtils.isNoneBlank(relativePath) && StringUtils.isNoneBlank(folder)){
				//处理相对路径
				if("/".equals(relativePath) || StringUtils.isBlank(relativePath)){
					relativePath = "/material/";
				}else{
					relativePath = "/material"+relativePath;
				}
				Integer creatorId = getUserId(request);

				Integer[] materialIds = null;
				if(StringUtils.isNotBlank(materialIds_p)){//素材id不为空  则把素材存入文件夹
					materialIds = ArrayUtils.toInt(materialIds_p.split(","));
				}

				if(publishFtpServiceFacade.existsFolder(relativePath, folder, creatorId.toString())){
					map.put("message", "["+relativePath+folder+"/]文件已存在");
					map.put("result", "error");
				}else{
					publishFtpServiceFacade.createFolder(relativePath, folder, creatorId,materialIds);
					map.put("message", "成功");
					map.put("result", "success");
				}

				map.put("data", null);
			}else{
				map.put("result", "error");
				map.put("message", "必传参数不能为空");
				map.put("data", null);
			}
		}catch(Exception e){
			log.error("创建文件夹异常！", e);
			map.put("result", "error");
			map.put("message", "创建文件夹异常！"+e.getMessage());
			map.put("data", null);
		}

		return map;
	}
	 /**
     * process 获取进度
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/process")
    @ResponseBody
    public Map<String,Object> process(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	List<ProgressModel> list_process = new ArrayList<ProgressModel>();
    	Map<String,Object> map = new HashMap<String,Object>();
    	Integer creatorId = getUserId(request);
    	if(creatorId != null){
    		map.put("result", "success");
    		map.put("message", "成功");
    		Map<String,ProgressModel> pm = process_map.get(PublishFtpServiceFacade.PROGRESS_KEY+"_"+creatorId);
    		if(null != pm){
    			for (String key : pm.keySet()) {
        			list_process.add(pm.get(key));
    			}
    		}
    		map.put("data",list_process);
    	}else{
    		map.put("result", "error");
			map.put("message", "必传参数不能为空");
			map.put("data", null);
    	}

        return map;
    }

    /**
     * processClean 清理进度条数据
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/processClean")
    @ResponseBody
    public Map<String,Object> processClean(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	List<ProgressModel> list_process = new ArrayList<ProgressModel>();
    	Map<String,Object> map = new HashMap<String,Object>();
    	Integer creatorId = getUserId(request);
    	if(creatorId != null){
    		process_map.put(PublishFtpServiceFacade.PROGRESS_KEY+"_"+creatorId,new HashMap<String,ProgressModel>());
    		map.put("result", "success");
    		map.put("message", "清理"+creatorId+"进度条数据成功");
    		map.put("data",list_process);
    	}else{
    		map.put("result", "error");
			map.put("message", "清理"+creatorId+"进度条数据失败");
			map.put("data", null);
    	}

        return map;
    }
}
