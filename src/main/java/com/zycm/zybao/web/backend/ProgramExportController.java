package com.zycm.zybao.web.backend;

import com.zycm.zybao.common.utils.ArrayUtils;
import com.zycm.zybao.service.facade.ProgramExportServiceFacade;
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
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Controller
@RequestMapping("/export/*")
public class ProgramExportController extends BaseController {

	@Autowired(required = false)
	private ProgramExportServiceFacade programExportServiceFacade;
	/*@Autowired(required = false)
	*//*private UserExportListService userExportListService;*/

	private Map<String,List<String>> progressInfoList = new HashMap<String,List<String>>();
	private Map<String,Boolean> isFinish = new HashMap<String,Boolean>();
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");


	@RequestMapping("/exportZIPByProgramIds")
	@ResponseBody
    public Map<String,Object> exportZIPByProgramIds(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		String programIdstr = request.getParameter("programIds");
		String uuid = request.getParameter("uuid");
		try {
			if(StringUtils.isNoneBlank(programIdstr) && StringUtils.isNoneBlank(uuid)){
				List<String> progressInfo = new ArrayList<String>();
				progressInfoList.put(uuid, progressInfo);
				isFinish.put(uuid, false);
				Integer[] programIds = ArrayUtils.toInt(programIdstr.split(","));
				String zippath = programExportServiceFacade.exportZIPByProgramIds(programIds,progressInfo);
				isFinish.put(uuid, true);
				if(StringUtils.isNotBlank(zippath)){

			    	//response.setContentType("text/html;charset=utf-8");
			    	//String filename = zippath.substring(zippath.lastIndexOf(File.separator)+1, zippath.length());
			    	String filename = "Programs.zip";
			        response.setContentType("application/x-msdownload;");
			        response.setHeader("Content-disposition", "attachment; filename="
			                 + new String(filename.getBytes("utf-8"), "ISO8859-1"));
			        //response.setHeader("Content-Length", String.valueOf(Double.parseDouble(map.get("size").toString())*1024));
			        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(zippath));
			        BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
			        byte[] buff = new byte[2048];
			        int bytesRead;
			        while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
			            bos.write(buff, 0, bytesRead);
			        }

			        if (bis != null)
			            bis.close();
			        if (bos != null)
			            bos.close();

			    }
				//清理zip文件
				new File(zippath).delete();
				/*map.put("result", "error");
				map.put("message", "导出节目包异常");
				map.put("data", null);*/
				//logger.error("exportZIPByMediaGroupId导出节目包异常!");
				return null;
			}else{
				map.put("result", "error");
				map.put("message", "必传参数不能为空");
				map.put("data", null);
				log.info("exportZIPByProgramIds导出节目包异常！必传参数不能为空");
				return map;
			}
		} catch (Exception e) {
			map.put("result", "error");
			map.put("message", "导出节目包异常，"+e.getMessage());
			map.put("data", null);
			log.error("exportZIPByProgramIds导出节目包异常!",e);
			isFinish.put(uuid, true);
			return map;
		}

    }

	@RequiresPermissions("export:exportZIPByMediaGroupId")
	@RequestMapping("/exportZIPByMediaGroupId")
	@ResponseBody
    public Map<String,Object> exportZIPByMediaGroupId(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		String mediaGroupId = request.getParameter("mediaGroupId");
		String uuid = request.getParameter("uuid");
		//String zycmToken = request.getParameter("zycmToken");
		try {
			if(StringUtils.isNoneBlank(mediaGroupId) && StringUtils.isNoneBlank(uuid)){
				List<String> progressInfo = new ArrayList<String>();
				progressInfoList.put(uuid, progressInfo);
				isFinish.put(uuid, false);
				List<String> zippaths = programExportServiceFacade.exportZIPByMediaGroupIds(Integer.parseInt(mediaGroupId), progressInfo, response);
				isFinish.put(uuid, true);
			    if(zippaths.size() > 0){
			    	for (int i = 0; i < zippaths.size(); i++) {
			    		//response.setContentType("text/html;charset=utf-8");
			    		String zippath = zippaths.get(i);
			    		if(StringUtils.isNotBlank(zippath)){
			    			//String filename = zippath.substring(zippath.lastIndexOf(File.pathSeparator), zippath.length());
			    			String filename = "Programs.zip";
			    			response.setContentType("application/x-msdownload;");
					        response.setHeader("Content-disposition", "attachment; filename="
					                 + new String(filename.getBytes("utf-8"), "ISO8859-1"));
					        //response.setHeader("Content-Length", String.valueOf(Double.parseDouble(map.get("size").toString())*1024));
					        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(zippath));
					        BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
					        byte[] buff = new byte[2048];
					        int bytesRead;
					        while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
					            bos.write(buff, 0, bytesRead);
					        }

					        if (bis != null)
					            bis.close();
					        if (bos != null)
					            bos.close();
			    		}
					}
			    }
				/*map.put("result", "error");
				map.put("message", "导出节目包成功");
				map.put("data", null);*/
				log.info("exportZIPByMediaGroupId导出节目包成功!");
				return null;
			}else{
				map.put("result", "error");
				map.put("message", "必传参数不能为空");
				map.put("data", null);
				log.info("exportZIPByMediaGroupId导出节目包异常！必传参数不能为空");
				return map;
			}
		} catch (Exception e) {
			map.put("result", "error");
			map.put("message", "导出节目包异常，"+e.getMessage());
			map.put("data", null);
			log.error("exportZIPByMediaGroupId导出节目包异常!",e);
			isFinish.put(uuid, true);
			return map;
		}

    }
/*
	@RequiresPermissions("export:exportZIPByMids")
	@RequestMapping("/exportZIPByMids")
	@ResponseBody
    public Map<String,Object> exportZIPByMids(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		String mids_p = request.getParameter("mids");
		String uuid = request.getParameter("uuid");
		try {
			if(StringUtils.isNotBlank(uuid)){
				Integer[] mids = null;
				if(StringUtils.isBlank(mids_p)){
					//没有指定 默认全部导出
					List<Integer> allmids = userExportListService.selectUserExportByUid(getUserId(request));
					if(allmids.size() > 0){
						mids = new Integer[allmids.size()];
						allmids.toArray(mids);
					}else{
						map.put("result", "error");
						map.put("message", "用户的导出列表为空！");
						map.put("data", null);
						log.error("用户的导出列表为空！!");
						return map;
					}
				}else{
					String[] mids_s = mids_p.split(",");
					mids = ArrayUtils.toInt(mids_s);
				}

				List<String> progressInfo = new ArrayList<String>();

				progressInfoList.put(uuid, progressInfo);
				isFinish.put(uuid, false);
				String zippath = programExportServiceFacade.exportZIPByMids(mids, progressInfo);

			    if(StringUtils.isNotBlank(zippath)){
	    			String filename = "(请解压后放u盘)"+getUserId(request)+"_"+sdf.format(new Date())+"_"+zippath.substring(zippath.lastIndexOf(System.getProperty("file.separator"))+1);
	    			//String filename = "Programs.zip";
	    			response.setContentType("application/x-msdownload;");
			        response.setHeader("Content-disposition", "attachment; filename="
			                 + new String(filename.getBytes("utf-8"), "ISO8859-1"));
			        //response.setHeader("Content-Length", String.valueOf(Double.parseDouble(map.get("size").toString())*1024));
			        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(zippath));
			        BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
			        byte[] buff = new byte[2048];
			        int bytesRead;
			        while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
			            bos.write(buff, 0, bytesRead);
			        }

			        if (bis != null)
			            bis.close();
			        if (bos != null)
			            bos.close();

			    }
			    isFinish.put(uuid, true);
				*//*map.put("result", "error");
				map.put("message", "导出节目包成功");
				map.put("data", null);*//*
				log.info("exportZIPByMids导出节目包成功!");
				return null;
			}else{
				map.put("result", "error");
				map.put("message", "必传参数不能为空");
				map.put("data", null);
				log.info("exportZIPByMids导出节目包异常！必传参数不能为空");
				return map;
			}
		} catch (Exception e) {
			map.put("result", "error");
			map.put("message", "导出节目包异常，"+e.getMessage());
			map.put("data", null);
			log.error("exportZIPByMids导出节目包异常!",e);
			isFinish.put(uuid, true);
			return map;
		}

    }*/

	@RequiresPermissions("export:getExportInfo")
	@RequestMapping("/getExportInfo")
	@ResponseBody
    public Map<String,Object> getExportInfo(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		String uuid = request.getParameter("uuid");
		try {
			if(StringUtils.isNoneBlank(uuid)){
				map.put("result", "success");
				map.put("message", "成功");
				Map<String,Object> refreshInfo = new HashMap<String,Object>();
				refreshInfo.put("isFinish", isFinish.get(uuid));
				refreshInfo.put("info", progressInfoList.get(uuid));
				map.put("data", refreshInfo);
				if(isFinish.get(uuid)){//导出完成 清理数据
					progressInfoList.remove(uuid);
				}
			}else{
				map.put("result", "error");
				map.put("message", "必传参数不能为空");
				map.put("data", null);
			}
		} catch (Exception e) {
			map.put("result", "error");
			map.put("message", "获取导出节目包处理信息异常，"+e.getMessage());
			map.put("data", null);
			log.error("获取导出节目包处理信息异常!",e);
		}
		return map;
    }

}
