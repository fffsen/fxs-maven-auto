package com.zycm.zybao.web.backend;

import com.zycm.zybao.service.interfaces.MediaAttributeService;
import com.zycm.zybao.service.interfaces.ProgramPublishRecordService;
import com.zycm.zybao.service.interfaces.ProgramService;
import com.zycm.zybao.service.interfaces.UserService;
import com.zycm.zybao.service.interfaces.rpc.ToThreeService;
import com.zycm.zybao.web.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;





/**
* @ClassName: ToThreeController
* @Description: 提供给第三方的web服务
* @author sy
* @date 2019年1月23日
*
*/
@Slf4j
@Controller
@RequestMapping("/webservice/*")
public class ToThreeController extends BaseController {

	@Autowired(required = false)
	private MediaAttributeService mediaAttributeService;

	@Autowired(required = false)
	private UserService userService;

	@Autowired(required = false)
	private ProgramPublishRecordService programPublishRecordService;

	@Autowired(required = false)
	private ProgramService programService;

	@Autowired(required = false)
	private ToThreeService toThreeService;

	@RequestMapping(value = "selectPageMedia")
	@ResponseBody
	public  Map<String,Object> selectPageMedia(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String page = request.getParameter("page");
		String pageSize = request.getParameter("pageSize");
		String mediaGroupId = cleanInvalidStr(request.getParameter("mediaGroupId"));
		String clientName = cleanInvalidStr(request.getParameter("clientName"));
		String uid = cleanInvalidStr(request.getParameter("uid"));
		String clientNumber = cleanInvalidStr(request.getParameter("clientNumber"));
		String adStatus = cleanInvalidStr(request.getParameter("adStatus"));

		try{
			if(StringUtils.isNotBlank(page) && StringUtils.isNotBlank(pageSize)){
				Map<String,Object> parammap = new HashMap<String,Object>();

				if(StringUtils.isNotBlank(mediaGroupId)){
					if("0".equals(mediaGroupId)){
						parammap.put("parentIds", null);
					}else{
						parammap.put("parentIds", new Integer[]{Integer.parseInt(mediaGroupId)});
					}

				}else{
					parammap.put("parentIds", null);
				}
				//查询用户配置的终端组管理
				if(StringUtils.isNotBlank(uid)){
					parammap.put("uid", Integer.parseInt(uid));
				}else{
					parammap.put("uid", null);
				}

				parammap.put("clientName", clientName);
				parammap.put("clientNumber", clientNumber);
				parammap.put("adStatus", adStatus);

				returnmap = mediaAttributeService.selectPage(parammap, Integer.parseInt(pageSize), Integer.parseInt(page));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("分页查询终端组的终端信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "分页查询终端组的终端信息异常！");
		}

		return returnmap;
	}

	@RequestMapping(value = "selectPageProgram")
	@ResponseBody
	public  Map<String,Object> selectPageProgram(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String page = request.getParameter("page");
		String pageSize = request.getParameter("pageSize");
		String mediaGroupId = request.getParameter("mediaGroupId");

		try{
			if(StringUtils.isNotBlank(page) && StringUtils.isNotBlank(pageSize) && StringUtils.isNotBlank(mediaGroupId)){

				returnmap = programPublishRecordService.selectPage(Integer.parseInt(mediaGroupId), Integer.parseInt(pageSize), Integer.parseInt(page));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");

			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
			}
		}catch(Exception e){
			log.error("分页查询终端组的终端信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "分页查询终端组的终端信息异常！");
		}

		return returnmap;
	}

	@RequestMapping(value = "selectMediaByKey")
	@ResponseBody
	public  Map<String,Object> selectMediaByKey(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> returnmap = new HashMap<String,Object>();
		String mid = request.getParameter("mid");

		try{
			if(StringUtils.isNotBlank(mid)){
				Map<String,Object> media = mediaAttributeService.selectByPrimaryKey(Integer.parseInt(mid));
				returnmap.put("result", "success");
				returnmap.put("message", "成功");
				returnmap.put("data", media);
			}else{
				returnmap.put("result", "error");
				returnmap.put("message", "必传参数不能为空");
				returnmap.put("data", null);
			}
		}catch(Exception e){
			log.error("查询终端详细信息异常！", e);
			returnmap.put("result", "error");
			returnmap.put("message", "查询终端详细信息异常！");
			returnmap.put("data", null);
		}

		return returnmap;
	}

}
