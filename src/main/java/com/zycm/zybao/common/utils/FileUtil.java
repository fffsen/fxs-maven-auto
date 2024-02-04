package com.zycm.zybao.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
* @ClassName: FileUtil
* @Description: 文件对象处理工具
* @author sy
* @date 2018年7月12日
*
*/
@Slf4j
public class FileUtil {

	/**
	* @Fields 换行
	*/
	public static final String sysLine = System.lineSeparator();
	//private static final String sys_line2 = System.getProperty("line.separator");
	/**
	* @Fields 系统路径路径分割符
	*/
	public static final String sysFileSep = File.separator;
	//private static final String sysFileSep2 = System.getProperty("file.separator");
	/**
	* @Title: copyFile
	* @Description: 拷贝文件
	* @param oldFile
	* @param newFile
	* @throws Exception    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	public static void copyFile(String oldFile,String newFile) throws Exception{
		FileInputStream fis = new FileInputStream(oldFile);
		FileOutputStream fos = new FileOutputStream(newFile);
		int ch;
		byte[] by = new byte[2048];
		while((ch = fis.read(by)) != -1) {
			fos.write(by, 0, ch);
		}
		fos.close();
		fis.close();

	}

	/**
	* @Title: deleteDirectory
	* @Description: 删除目录及子文件
	* @param file    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	public static void deleteDirectory(File file) {
	 	if(!file.exists()) return;

        File files[] = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                files[i].delete();
            }
            else if (files[i].isDirectory()) {
                deleteDirectory(files[i]);
            }
        }
        file.delete();
    }

	/**
	* @Title: convertTxtAsUTF8
	* @Description: 转换txt成utf8
	* @param inputFile txt源文件
	* @param inputFileEncode  源文件编码
	* @param outputFileUrl 转换后文件路径
	* @throws Exception    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	public static void convertTxtAsUTF8(File inputFile,String inputFileEncode, String outputFileUrl) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(new FileInputStream(inputFile), inputFileEncode));
        BufferedWriter bufferedWriter = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(outputFileUrl), "UTF-8"));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            bufferedWriter.write(line + "\r\n");
        }
        bufferedWriter.close();
        bufferedReader.close();
        String outputFileEncode = EncodingDetect.getJavaEncode(outputFileUrl);
        log.info(inputFileEncode+"转换txt后："+outputFileUrl+" 编码："+outputFileEncode);
    }

	/**
	* @Title: convertTxtAsUTF8
	* @Description: 转换txt成utf8
	* @param inputFileUrl  txt源文件路径
	* @param inputFileEncode  源文件编码
	* @param outputFileUrl  转换后文件路径
	* @throws Exception    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	public static void convertTxtAsUTF8(String inputFileUrl,String inputFileEncode,String outputFileUrl) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(new FileInputStream(inputFileUrl), inputFileEncode));
        BufferedWriter bufferedWriter = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(outputFileUrl), "UTF-8"));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            bufferedWriter.write(line + "\r\n");
        }
        bufferedWriter.close();
        bufferedReader.close();
        String outputFileEncode = EncodingDetect.getJavaEncode(outputFileUrl);
        log.info(inputFileEncode+"转换txt后："+outputFileUrl+" 编码："+outputFileEncode);
    }

	/**
	* @Title: createTxt
	* @Description:本地服务器创建文本文件
	* @param txt
	* @param path    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	public static void createTxt(String txt,String path){
    	FileWriter out = null;
		try {
			 File fout = new File(path);
			 out = new FileWriter(fout);
			 out.write(txt);
		} catch (Exception e) {
			log.error("生成视频拼接文件失败", e);
		}  finally {
			try {
				if(out != null){
					 out.flush();
					 out.close();
				}
			} catch (IOException e) {
				log.error("关闭生成视频拼接文件失败", e);
			}
		}
    }

	public static boolean concatVideoTxt(List<String> fileList,String path){
		if(fileList.size() > 0){
			String txt = "";
			for (String filePath : fileList) {
				txt += "file '"+filePath+"'"+sysLine;
			}
			createTxt(txt, path);
			return true;
		}else{
			log.error("视频文件信息为空不能生成拼接文件");
		}
		return false;
	}

	public static void main(String[] args) throws Exception {
		System.out.println(sysLine);
		System.out.println(sysFileSep);
		List<String> fileList = new ArrayList<String>();
		fileList.add("c://sss/aa.mp4");
		fileList.add("c://sss/cc.mp4");
		fileList.add("c://sss/bb.mp4");
		System.out.println(concatVideoTxt(fileList, "D://test/11.txt"));
	}
}
