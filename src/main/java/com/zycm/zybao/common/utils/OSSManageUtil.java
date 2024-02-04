package com.zycm.zybao.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.green.model.v20180509.ImageSyncScanRequest;
import com.aliyuncs.green.model.v20180509.TextScanRequest;
import com.aliyuncs.green.model.v20180509.VideoAsyncScanRequest;
import com.aliyuncs.green.model.v20180509.VideoAsyncScanResultsRequest;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.zycm.zybao.common.config.AICheckConfig;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @ClassName: OSSManageUtil
 * @Description: oss文件内容 智能审核
 * @author sy
 * @date 2021年3月24日
 *
 */
@Slf4j
public class OSSManageUtil {

	/**
	 * 图片内容安全审核
	 *
	 * @param url
	 *            图片url地址
	 * @return
	 * @throws ClientException
	 * @throws UnsupportedEncodingException
	 */
	public static Boolean contentSecurity(String url, Integer initRate)
			throws ClientException, UnsupportedEncodingException {
		// 请替换成你自己的accessKeyId、accessKeySecret
		IClientProfile profile = DefaultProfile.getProfile(AICheckConfig.regionId, AICheckConfig.accessKeyId,
				AICheckConfig.accessKeySecret);
		DefaultProfile.addEndpoint(AICheckConfig.regionId, "Green", AICheckConfig.endPointName);
		// DefaultProfile.addEndpoint(ossConfig.getEndPointName(),
		// ossConfig.getRegionId(), "Green", ossConfig.getDomain());
		IAcsClient client = new DefaultAcsClient(profile);

		ImageSyncScanRequest imageSyncScanRequest = new ImageSyncScanRequest();
		imageSyncScanRequest.setSysAcceptFormat(FormatType.JSON);
		imageSyncScanRequest.setSysMethod(MethodType.POST);
		imageSyncScanRequest.setSysEncoding("utf-8");
		// imageSyncScanRequest.setSysRegionId(ossConfig.getRegionId());
		imageSyncScanRequest.setSysProtocol(ProtocolType.HTTP);

		/*
		 * imageSyncScanRequest.setAcceptFormat(FormatType.JSON); // 指定api返回格式
		 * imageSyncScanRequest.setMethod(com.aliyuncs.http.MethodType.POST); //
		 * 指定请求方法 imageSyncScanRequest.setEncoding("utf-8");
		 * imageSyncScanRequest.setRegionId(ossConfig.getRegionId());
		 */

		List<Map<String, Object>> tasks = new ArrayList<Map<String, Object>>();
		Map<String, Object> task = new LinkedHashMap<String, Object>();
		task.put("dataId", UUID.randomUUID().toString());
		task.put("url", url);
		task.put("time", new Date());

		tasks.add(task);
		JSONObject data = new JSONObject();
		/**
		 * porn: 色情 terrorism: 暴恐 qrcode: 二维码 ad: 图片广告 ocr: 文字识别
		 */
		data.put("scenes", Arrays.asList("porn", "terrorism"));
		data.put("tasks", tasks);

		imageSyncScanRequest.setHttpContent(data.toJSONString().getBytes("UTF-8"), "UTF-8", FormatType.JSON);
		/**
		 * 请务必设置超时时间
		 */
		imageSyncScanRequest.setSysConnectTimeout(3000);
		imageSyncScanRequest.setSysReadTimeout(6000);

		try {
			HttpResponse httpResponse = client.doAction(imageSyncScanRequest);

			if (httpResponse != null && httpResponse.isSuccess()) {
				JSONObject scrResponse = JSON.parseObject(new String(httpResponse.getHttpContent(), "UTF-8"));
				System.out.println(JSON.toJSONString(scrResponse, true));
				if (200 == scrResponse.getInteger("code")) {
					JSONArray taskResults = scrResponse.getJSONArray("data");
					for (Object taskResult : taskResults) {
						if (200 == ((JSONObject) taskResult).getInteger("code")) {
							JSONArray sceneResults = ((JSONObject) taskResult).getJSONArray("results");
							System.out.println("图片审核结果：" + sceneResults);
							Boolean result = true;
							for (Object sceneResult : sceneResults) {
								try {
									String scene = ((JSONObject) sceneResult).getString("scene");
									String suggestion = ((JSONObject) sceneResult).getString("suggestion");
									Double rate = ((JSONObject) sceneResult).getDouble("rate");
									/**
									 * porn: 反色情 terrorism: 反暴恐 qrcode: 二维码 ad:
									 * 图片广告 ocr: 文字识别
									 */
									if ("porn".equals(scene)) {
										// 反色情,true表示合格
										result = result && ("pass".equals(suggestion) && rate >= initRate);
									}
									if ("terrorism".equals(scene)) {
										// 反暴恐，true表示合格
										result = result && ("pass".equals(suggestion) && rate >= initRate);
									}
								} catch (Exception e) {
									log.error("图片内容安全审核,返回解析信息错误");
									e.printStackTrace();
									return false;
								}
							}
							return result;
						} else {
							log.error("task process fail:" + ((JSONObject) taskResult).getInteger("code"));
						}
					}
				} else {
					log.error("detect not success. code:" + scrResponse.getInteger("code"));
				}
			} else {
				log.error("response not success. status:" + httpResponse.getStatus());
			}
		} catch (ServerException e) {
			e.printStackTrace();
		} catch (ClientException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}



	/**
	 * Description: 判断OSS服务文件上传时文件的contentType @Version1.0
	 *
	 * @param FilenameExtension
	 *            文件后缀
	 * @return String
	 */
	public static String contentType(String FilenameExtension) {
		if (FilenameExtension.equals("BMP") || FilenameExtension.equals("bmp")) {
			return "image/bmp";
		}
		if (FilenameExtension.equals("GIF") || FilenameExtension.equals("gif")) {
			return "image/gif";
		}
		if (FilenameExtension.equals("JPEG") || FilenameExtension.equals("jpeg") || FilenameExtension.equals("JPG")
				|| FilenameExtension.equals("jpg") || FilenameExtension.equals("PNG")
				|| FilenameExtension.equals("png")) {
			return "image/jpeg";
		}
		if (FilenameExtension.equals("HTML") || FilenameExtension.equals("html")) {
			return "text/html";
		}
		if (FilenameExtension.equals("TXT") || FilenameExtension.equals("txt")) {
			return "text/plain";
		}
		if (FilenameExtension.equals("VSD") || FilenameExtension.equals("vsd")) {
			return "application/vnd.visio";
		}
		if (FilenameExtension.equals("PPTX") || FilenameExtension.equals("pptx") || FilenameExtension.equals("PPT")
				|| FilenameExtension.equals("ppt")) {
			return "application/vnd.ms-powerpoint";
		}
		if (FilenameExtension.equals("DOCX") || FilenameExtension.equals("docx") || FilenameExtension.equals("DOC")
				|| FilenameExtension.equals("doc")) {
			return "application/msword";
		}
		if (FilenameExtension.equals("XML") || FilenameExtension.equals("xml")) {
			return "text/xml";
		}
		if (FilenameExtension.equals("MP4") || FilenameExtension.equals("mp4")) {
			return "video/mp4";
		}
		return "text/html";
	}

	/**
	 * 视频内容安全审核--任务发送
	 *
	 * @param url
	 *            视频的url地址
	 * @return
	 * @throws ClientException
	 * @throws UnsupportedEncodingException
	 */
	public static String videoContentSecurity(String url) throws ClientException, UnsupportedEncodingException {
		// 请替换成你自己的accessKeyId、accessKeySecret
		IClientProfile profile = DefaultProfile.getProfile(AICheckConfig.regionId, AICheckConfig.accessKeyId,
				AICheckConfig.accessKeySecret);
		// DefaultProfile.addEndpoint(ossConfig.getEndPointName(),
		// ossConfig.getRegionId(), "Green", ossConfig.getDomain());
		DefaultProfile.addEndpoint(AICheckConfig.regionId, "Green", AICheckConfig.endPointName);
		IAcsClient client = new DefaultAcsClient(profile);

		VideoAsyncScanRequest videoAsyncScanRequest = new VideoAsyncScanRequest();
		videoAsyncScanRequest.setSysAcceptFormat(FormatType.JSON); // 指定api返回格式
		videoAsyncScanRequest.setSysMethod(MethodType.POST); // 指定请求方法

		/*
		 * videoAsyncScanRequest.setAcceptFormat(FormatType.JSON); // 指定api返回格式
		 * videoAsyncScanRequest.setMethod(com.aliyuncs.http.MethodType.POST);
		 * // 指定请求方法
		 */
		List<Map<String, Object>> tasks = new ArrayList<Map<String, Object>>();
		Map<String, Object> task = new LinkedHashMap<String, Object>();
		task.put("dataId", UUID.randomUUID().toString());
		task.put("url", url);

		tasks.add(task);
		/**
		 * 设置要检测的场景, 计费是按照该处传递的场景进行
		 * 视频默认1秒截取一帧，您可以自行控制截帧频率，收费按照视频的截帧数量以及每一帧的检测场景进行计费
		 * 举例：1分钟的视频截帧60张，检测色情和暴恐涉政2个场景，收费按照60张暴恐+60张暴恐涉政进行计费 porn:
		 * porn表示色情场景检测,terrorism表示暴恐涉政场景检测
		 */
		JSONObject data = new JSONObject();
		data.put("scenes", Arrays.asList("porn", "terrorism"));
		data.put("tasks", tasks);

		videoAsyncScanRequest.setHttpContent(data.toJSONString().getBytes("UTF-8"), "UTF-8", FormatType.JSON);

		/**
		 * 请务必设置超时时间
		 */
		videoAsyncScanRequest.setSysConnectTimeout(3000);
		videoAsyncScanRequest.setSysReadTimeout(6000);
		try {
			HttpResponse httpResponse = client.doAction(videoAsyncScanRequest);

			if (httpResponse.isSuccess()) {
				JSONObject jsonObject = JSON.parseObject(new String(httpResponse.getHttpContent(), "UTF-8"));
				System.out.println(JSON.toJSONString(jsonObject, true));
				if (200 == jsonObject.getInteger("code")) {
					JSONArray taskResults = jsonObject.getJSONArray("data");
					for (Object taskResult : taskResults) {
						if (200 == ((JSONObject) taskResult).getInteger("code")) {
							String taskId = ((JSONObject) taskResult).getString("taskId");
							return taskId;
						} else {
							log.error("task process fail:" + ((JSONObject) taskResult).getInteger("code"));
						}
					}
				} else {
					log.error("detect not success. code:" + jsonObject.getInteger("code"));
				}
			} else {
				System.out.println("response not success. status:" + httpResponse.getStatus());
			}
		} catch (ServerException e) {
			e.printStackTrace();
		} catch (ClientException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 视频内容安全审核--异步结果解析
	 *
	 * @param taskId
	 *            解析任务id
	 * @return
	 * @throws ClientException
	 * @throws UnsupportedEncodingException
	 */
	public static Boolean videoParseResult(String taskId) throws ClientException, UnsupportedEncodingException {
		IClientProfile profile = DefaultProfile.getProfile(AICheckConfig.regionId, AICheckConfig.accessKeyId,
				AICheckConfig.accessKeySecret);
		// DefaultProfile.addEndpoint(ossConfig.getEndPointName(),
		// ossConfig.getRegionId(), "Green", ossConfig.getDomain());
		DefaultProfile.addEndpoint(AICheckConfig.regionId, "Green", AICheckConfig.endPointName);
		IAcsClient client = new DefaultAcsClient(profile);

		VideoAsyncScanResultsRequest videoAsyncScanResultsRequest = new VideoAsyncScanResultsRequest();
		videoAsyncScanResultsRequest.setSysAcceptFormat(FormatType.JSON);

		List<String> taskList = new ArrayList<String>();
		// 这里添加要查询的taskId，提交任务的时候需要自行保存taskId
		taskList.add(taskId);

		videoAsyncScanResultsRequest.setHttpContent(JSON.toJSONString(taskList).getBytes("UTF-8"), "UTF-8",
				FormatType.JSON);

		/**
		 * 请务必设置超时时间
		 */
		videoAsyncScanResultsRequest.setSysConnectTimeout(3000);
		videoAsyncScanResultsRequest.setSysReadTimeout(6000);
		try {
			HttpResponse httpResponse = client.doAction(videoAsyncScanResultsRequest);
			if (httpResponse.isSuccess()) {
				JSONObject jsonObject = JSON.parseObject(new String(httpResponse.getHttpContent(), "UTF-8"));
				log.info("视频内容解析结果 " + jsonObject);
				if (200 == jsonObject.getInteger("code")) {
					JSONArray taskResults = jsonObject.getJSONArray("data");
					for (Object taskResult : taskResults) {
						if (200 == ((JSONObject) taskResult).getInteger("code")) {
							JSONArray sceneResults = ((JSONObject) taskResult).getJSONArray("results");
							Boolean result = true;
							for (Object sceneResult : sceneResults) {
								// 对每一个常见进行解析
								Boolean checkResult = parseResult(sceneResult);
								result = result && checkResult;
							}
							return result;
						} else {
							log.error("task process fail:" + ((JSONObject) taskResult).getInteger("code"));
							return false;
						}
					}
				} else {
					log.error("detect not success. code:" + jsonObject.getInteger("code"));
					return false;
				}
			} else {
				log.error("response not success. status:" + httpResponse.getStatus());
				return false;
			}
		} catch (ServerException e) {
			e.printStackTrace();
		} catch (ClientException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 返回内容解析
	 *
	 * @param sceneResult
	 *            解析对象
	 * @return
	 */
	private static Boolean parseResult(Object sceneResult) {
		try {
			String scene = ((JSONObject) sceneResult).getString("scene");
			String suggestion = ((JSONObject) sceneResult).getString("suggestion");
			Double rate = ((JSONObject) sceneResult).getDouble("rate");
			/**
			 * porn: 反色情 terrorism: 反暴恐 qrcode: 二维码 ad: 图片广告 ocr: 文字识别
			 */
			if ("porn".equals(scene)) {
				// 反色情,true表示合格
				return "pass".equals(suggestion) && rate >= 99;
			}
			if ("terrorism".equals(scene)) {
				// 反暴恐，true表示合格
				return "pass".equals(suggestion) && rate >= 99;
			}
			return false;
		} catch (Exception e) {
			log.error("图片内容安全审核,返回解析信息错误");
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 视频内容安全审核，通过视频截帧
	 *
	 * @param image
	 * @param duration
	 * @return
	 */
	public static Boolean videoContentSecurityByDuration(String image, Integer duration)
			throws UnsupportedEncodingException, ClientException {
		for (int i = 1; i <= duration; i++) {
			/**
			 * 视频截至图片的url t 截图时间 单位ms，[0,视频时长] 如t_1000, 表示: 视频第一秒 w
			 * 截图宽度，如果指定为0则自动计算 像素值：[0,视频宽度], 如：w_0 h
			 * 截图高度，如果指定为0则自动计算，如果w和h都为0则输出为原视频宽高 像素值：[0,视频高度]，如：h_0 m
			 * 截图模式，不指定则为默认模式，根据时间精确截图，如果指定为fast则截取该时间点之前的最近的一个关键帧
			 * 枚举值：fast,如m_fast f 输出图片格式 枚举值：jpg、png
			 */
			Boolean result = contentSecurity(
					image + "?x-oss-process=video/snapshot,t_" + i * 1000 + ",f_jpg,w_0,h_0,f_jpg,m_fast", 99);
			if (!result) {
				// result=false,表示违法视频
				return false;
			}
		}
		return true;
	}

	/***************************************************** 黄金分割线 ********************************************************/

	/**
	 * @Title: contentSecurity
	 * @Description: TODO(图片智能审核-返回具体违规字段)
	 * @param url
	 * @return
	 * @throws ClientException
	 * @throws UnsupportedEncodingException
	 *             参数
	 * @author sy
	 * @throws @return
	 *             String 返回类型
	 *
	 */
	public static String picAiAudit(String url) throws ClientException, UnsupportedEncodingException {
		//String result = "normal";
		// 请替换成你自己的accessKeyId、accessKeySecret
		IClientProfile profile = DefaultProfile.getProfile(AICheckConfig.regionId, AICheckConfig.accessKeyId,
				AICheckConfig.accessKeySecret);
		DefaultProfile.addEndpoint(AICheckConfig.regionId, "Green", AICheckConfig.endPointName);
		// DefaultProfile.addEndpoint(ossConfig.getEndPointName(),
		// ossConfig.getRegionId(), "Green", ossConfig.getDomain());
		IAcsClient client = new DefaultAcsClient(profile);

		ImageSyncScanRequest imageSyncScanRequest = new ImageSyncScanRequest();
		imageSyncScanRequest.setSysAcceptFormat(FormatType.JSON);
		imageSyncScanRequest.setSysMethod(MethodType.POST);
		imageSyncScanRequest.setSysEncoding("utf-8");
		// imageSyncScanRequest.setSysRegionId(ossConfig.getRegionId());
		imageSyncScanRequest.setSysProtocol(ProtocolType.HTTP);

		/*
		 * imageSyncScanRequest.setAcceptFormat(FormatType.JSON); // 指定api返回格式
		 * imageSyncScanRequest.setMethod(com.aliyuncs.http.MethodType.POST); //
		 * 指定请求方法 imageSyncScanRequest.setEncoding("utf-8");
		 * imageSyncScanRequest.setRegionId(ossConfig.getRegionId());
		 */

		List<Map<String, Object>> tasks = new ArrayList<Map<String, Object>>();
		Map<String, Object> task = new LinkedHashMap<String, Object>();
		task.put("dataId", UUID.randomUUID().toString());
		task.put("url", url);
		task.put("time", new Date());

		tasks.add(task);
		JSONObject data = new JSONObject();
		/**
		 * porn: 色情 terrorism: 暴恐 qrcode: 二维码 ad: 图片广告 ocr: 文字识别
		 */
		data.put("scenes", Arrays.asList("porn", "terrorism"));
		data.put("tasks", tasks);

		imageSyncScanRequest.setHttpContent(data.toJSONString().getBytes("UTF-8"), "UTF-8", FormatType.JSON);
		/**
		 * 请务必设置超时时间
		 */
		imageSyncScanRequest.setSysConnectTimeout(3000);
		imageSyncScanRequest.setSysReadTimeout(6000);

		try {
			HttpResponse httpResponse = client.doAction(imageSyncScanRequest);

			if (httpResponse != null && httpResponse.isSuccess()) {
				JSONObject scrResponse = JSON.parseObject(new String(httpResponse.getHttpContent(), "UTF-8"));
				System.out.println(JSON.toJSONString(scrResponse, true));
				if (200 == scrResponse.getInteger("code")) {
					JSONArray taskResults = scrResponse.getJSONArray("data");
					for (Object taskResult : taskResults) {
						if (200 == ((JSONObject) taskResult).getInteger("code")) {
							JSONArray sceneResults = ((JSONObject) taskResult).getJSONArray("results");
							System.out.println("图片审核结果：" + sceneResults);
							// Boolean result = true;
							for (Object sceneResult : sceneResults) {
								try {
									String label = ((JSONObject) sceneResult).getString("label");
									if(!"normal".equals(label)){
										return label;
									}
								} catch (Exception e) {
									log.error("图片内容安全审核,返回解析信息错误", e);
									return "exception";
								}
							}
							return "normal";
						} else {
							log.error("task process fail:" + ((JSONObject) taskResult).getInteger("code"));
							return "exception";
						}
					}
					return "normal";
				} else {
					log.error("detect not success. code:" + scrResponse.getInteger("code"));
				}
			} else {
				log.error("response not success. status:" + httpResponse.getStatus());
			}
		} catch (ServerException e) {
			log.error("图片内容安全审核错误", e);
		} catch (ClientException e) {
			log.error("图片内容安全审核错误", e);
		} catch (Exception e) {
			log.error("图片内容安全审核错误", e);
		}
		return "exception";// 表示审核异常
	}

	/**
	* @Title: videoAiAudit
	* @Description: TODO(视频内容安全审核提交任务后--异步根据taskId查询结果解析)
	* @param taskId
	* @return
	* @throws ClientException
	* @throws UnsupportedEncodingException    参数
	* @author sy
	* @throws
	* @return String    返回label  具体参数见https://help.aliyun.com/document_detail/70292.html?spm=a2c4g.11186623.6.628.3e01aba5W3wEhF
	*
	*/
	public static String videoAiAudit(String taskId) throws Exception {
		IClientProfile profile = DefaultProfile.getProfile(AICheckConfig.regionId, AICheckConfig.accessKeyId,
				AICheckConfig.accessKeySecret);
		DefaultProfile.addEndpoint(AICheckConfig.regionId, "Green", AICheckConfig.endPointName);
		IAcsClient client = new DefaultAcsClient(profile);

		VideoAsyncScanResultsRequest videoAsyncScanResultsRequest = new VideoAsyncScanResultsRequest();
		videoAsyncScanResultsRequest.setSysAcceptFormat(FormatType.JSON);

		List<String> taskList = new ArrayList<String>();
		// 这里添加要查询的taskId，提交任务的时候需要自行保存taskId
		taskList.add(taskId);

		videoAsyncScanResultsRequest.setHttpContent(JSON.toJSONString(taskList).getBytes("UTF-8"), "UTF-8",
				FormatType.JSON);

		/**
		 * 请务必设置超时时间
		 */
		videoAsyncScanResultsRequest.setSysConnectTimeout(3000);
		videoAsyncScanResultsRequest.setSysReadTimeout(6000);
		/*try {*/
			HttpResponse httpResponse = client.doAction(videoAsyncScanResultsRequest);
			if (httpResponse.isSuccess()) {
				JSONObject jsonObject = JSON.parseObject(new String(httpResponse.getHttpContent(), "UTF-8"));
				log.info("视频内容解析结果 " + jsonObject);
				System.out.println("视频内容解析结果 " + jsonObject);
				if (200 == jsonObject.getInteger("code")) {
					JSONArray taskResults = jsonObject.getJSONArray("data");
					for (Object taskResult : taskResults) {
						if (200 == ((JSONObject) taskResult).getInteger("code")) {
							JSONArray sceneResults = ((JSONObject) taskResult).getJSONArray("results");
							for (Object sceneResult : sceneResults) {
								// 对每一个常见进行解析
								try {
									String label = ((JSONObject) sceneResult).getString("label");
									if(!"normal".equals(label)){
										return label;
									}
								} catch (Exception e) {
									log.error("视频的图片内容安全审核,返回解析信息错误",e);
									return "myexception";
								}
							}
							return "normal";
						} else {
							log.error("task process fail:" + ((JSONObject) taskResult).getInteger("code")+" "+((JSONObject) taskResult).getString("msg"));
							return "exception";
						}
					}
					return "normal";
				} else {
					log.error("detect not success. code:" + jsonObject.getInteger("code")+" "+jsonObject.getString("msg"));
					return "exception";
				}
			} else {
				log.error("response not success. status:" + httpResponse.getStatus());
			}
		/*} catch (ServerException e) {
			log.error("视频的图片内容安全审核错误", e);
		} catch (ClientException e) {
			log.error("视频的图片内容安全审核错误", e);
		}*/
		return "myexception";//myexception与exception的区别是myexception表示不是审核过程中的异常不需要修改审核状态，exception表示是审核结果的异常表现
	}

	public static String txtAiAudit(String txt) throws ClientException, UnsupportedEncodingException {
		IClientProfile profile = DefaultProfile.getProfile(AICheckConfig.regionId, AICheckConfig.accessKeyId,
				AICheckConfig.accessKeySecret);
		DefaultProfile.addEndpoint(AICheckConfig.regionId, "Green", AICheckConfig.endPointName);
        IAcsClient client = new DefaultAcsClient(profile);
        TextScanRequest textScanRequest = new TextScanRequest();
        textScanRequest.setSysAcceptFormat(FormatType.JSON); // 指定API返回格式。
        //textScanRequest.setHttpContentType(FormatType.JSON);
        textScanRequest.setSysMethod(MethodType.POST); // 指定请求方法。
        textScanRequest.setSysEncoding("UTF-8");
        //textScanRequest.setSysRegionId("cn-shanghai");
        List<Map<String, Object>> tasks = new ArrayList<Map<String, Object>>();
        Map<String, Object> task1 = new LinkedHashMap<String, Object>();
        task1.put("dataId", UUID.randomUUID().toString());
        /**
         * 待检测的文本，长度不超过10000个字符。
         */
        task1.put("content", txt);
        tasks.add(task1);
        JSONObject data = new JSONObject();

        /**
         * 检测场景。文本垃圾检测请传递antispam。
         **/
        data.put("scenes", Arrays.asList("antispam"));
        data.put("tasks", tasks);
        System.out.println(JSON.toJSONString(data, true));
        textScanRequest.setHttpContent(data.toJSONString().getBytes("UTF-8"), "UTF-8", FormatType.JSON);
        // 请务必设置超时时间。
        textScanRequest.setSysConnectTimeout(3000);
        textScanRequest.setSysReadTimeout(6000);
        try {
            HttpResponse httpResponse = client.doAction(textScanRequest);
            if(httpResponse.isSuccess()){
                JSONObject scrResponse = JSON.parseObject(new String(httpResponse.getHttpContent(), "UTF-8"));
                System.out.println(JSON.toJSONString(scrResponse, true));
                if (200 == scrResponse.getInteger("code")) {
                    JSONArray taskResults = scrResponse.getJSONArray("data");
                    //单个任务执行
                    for (Object taskResult : taskResults) {
                        if(200 == ((JSONObject)taskResult).getInteger("code")){
                            JSONArray sceneResults = ((JSONObject)taskResult).getJSONArray("results");
                            for (Object sceneResult : sceneResults) {
                            	try {
									String label = ((JSONObject) sceneResult).getString("label");
									if(!"normal".equals(label)){
										return label;
									}
								} catch (Exception e) {
									log.error("文本内容安全审核,返回解析信息错误",e);
									return "exception";
								}
							}
							return "normal";
                        }else{
                            System.out.println("task process fail:" + ((JSONObject)taskResult).getInteger("code"));
                            return "exception";
                        }
                    }
                    return "normal";
                } else {
                    System.out.println("detect not success. code:" + scrResponse.getInteger("code"));
                }
            }else{
                System.out.println("response not success. status:" + httpResponse.getStatus());
            }
        } catch (ServerException e) {
        	log.error("文本内容安全审核错误", e);
        } catch (ClientException e) {
        	log.error("文本内容安全审核错误", e);
        } catch (Exception e) {
        	log.error("文本内容安全审核错误", e);
        }
        return "exception";
	}



	public static void main(String[] args) {
		try {
			OSSManageUtil om = new OSSManageUtil();
			//文本
			String txt = "我草 ";
			System.out.println(om.txtAiAudit(txt));

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

