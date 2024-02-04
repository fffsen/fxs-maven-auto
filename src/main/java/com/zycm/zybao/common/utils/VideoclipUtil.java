package com.zycm.zybao.common.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;

import com.enterprisedt.net.ftp.FTPException;

/**
* @ClassName: VideoclipUtil
* @Description: 视频拼接工具类
* @author sy
* @date 2021年7月25日
*
*/
@Slf4j
public class VideoclipUtil {
	//private static final String ffmpegPath = "D:\\Program Files\\ffmpeg\\bin\\ffmpeg.exe";
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public static void mergeVideo(String ffmpegPath,String upFilePath, String codcFilePath) throws Exception{
    	List<String> command = new ArrayList<String>();
    	/*command.add(ffmpegPath);
		command.add("-f");
		command.add("concat");
		command.add("-i");
		command.add(upFilePath);
		command.add("-c:v"); //视频编码设置选项
		command.add("copy");//视频编码使用源文件的
		command.add("-c:a");//音频编码设置选项
		command.add("copy");//音频参数复制源文件的
		command.add(codcFilePath);*/

    	command.add(ffmpegPath);
    	//command.add("-loglevel");
    	//command.add("8");
    	command.add("-y");
    	command.add("-f");
    	command.add("concat");
    	command.add("-safe");
    	command.add("0");
    	command.add("-i");
    	command.add(upFilePath);
    	command.add("-c");
    	command.add("copy");
    	command.add("-y");
    	command.add(codcFilePath);


		//String format3 = "%s -i \"concat:%s|%s\" -c copy -bsf:a aac_adtstoasc -movflags +faststart %s";
		//String format3 = "%s -i \"concat:%s|%s\" -c:v copy -c:a copy %s";
		//String command3 = String.format(format3, ffmpegPath, upFilePath, upFilePath2, codcFilePath);

		// 方案2
		Process videoProcess = new ProcessBuilder(command).redirectErrorStream(true).start();
		//获取进程的错误流
		new PrintStream(videoProcess.getErrorStream()).start();
		//获取进程的标准输入流
		new PrintStream(videoProcess.getInputStream()).start();
		videoProcess.waitFor();
		videoProcess.destroy();
    }

    public static void mergeVideo(List<String> filePaths, String outFilePath) throws Exception{
    	String os = System.getProperty("os.name");
    	String ffmpegexe = "G:\\无法播放的mp4\\ffmpeg.exe";
    	if(!os.toLowerCase().startsWith("win")){
			ffmpegexe = "ffmpeg";
		}
    	//生成txt文件
    	String txtPath = outFilePath.substring(0, outFilePath.lastIndexOf("."))+".txt";
		FileOutputStream outFile = new FileOutputStream(txtPath);
		for (String filePath : filePaths) {
			outFile.write(("file '"+filePath+"'"+System.lineSeparator()).getBytes());
		}
		outFile.close();
		if(new File(txtPath).exists()){
			//progressInfo.add("[消息]"+zipfilename+"工作文件的播放脚本生成成功!");
			mergeVideo(ffmpegexe, txtPath, outFilePath);
		}else{
			log.error("生成拼接txt脚本失败，文件不存在。"+txtPath);
		}
    }
   /* public static void mergeVideo2(List<String> upFilePath, String codcFilePath) throws Exception {
    	FileInputStream fis = null;
    	FileOutputStream fos = null;
    	fos = new FileOutputStream(codcFilePath);
    	byte buffer[] = new byte[1024 * 1024 * 2];//一次读取2M数据，将读取到的数据保存到byte字节数组中
    	int len;
    	for (int i = 0; i < upFilePath.size(); i++) {
    		fis = new FileInputStream(upFilePath.get(i));
	    	len = 0;
	    	while ((len = fis.read(buffer)) != -1) {
	    		fos.write(buffer, 0, len);//buffer从指定字节数组写入。buffer:数据中的起始偏移量,len:写入的字数。
	    	}
	    	if(fis != null)fis.close();
		}

		if(fos != null){
			fos.flush();
	    	fos.close();
		}
    }*/


    public static void main(String[] args) {
    	String ffmpegPath = "D:\\无法播放的mp4\\ffmpeg.exe";
    	try {
    		List<String> s = new ArrayList<String>();
    		s.add("F:\\zhongyi\\ffmpe\\1.mp4");
    		s.add("F:\\zhongyi\\ffmpe\\2.mp4");
    		s.add("F:\\zhongyi\\ffmpe\\3.mp4");
    		s.add("F:\\zhongyi\\ffmpe\\4.mp4");
			//VideoclipUtil.mergeVideo(ffmpegPath,"F:\\zhongyi\\ffmpe\\11.txt", "F:\\zhongyi\\ffmpe\\hehe2.mp4");
    		VideoclipUtil.mergeVideo(s, "F:\\zhongyi\\ffmpe\\hehe2.mp4");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//System.out.println(e.getMessage());
			//e.printStackTrace();
		}
    }
}
