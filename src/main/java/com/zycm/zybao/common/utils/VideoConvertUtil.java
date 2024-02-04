package com.zycm.zybao.common.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;

/**
 * @ClassName: VideoConvertUtil
 * @Description: 视频转换工具类
 * @author sy
 * @date 2018年7月5日
 *
 */
@Slf4j
public class VideoConvertUtil {

    /**
     * @Title: convertMp4
     * @Description: 视频转成mp4格式
     * @param ffmpegPath  转码工具的存放路径
     * @param upFilePath 用于指定要转换格式的文件
     * @param codcFilePath 格式转换后的的文件保存路径
     * @return    参数
     * @author sy
     * @throws
     * @return boolean    返回类型
     *
     */
    public static boolean convertMp4(String ffmpegPath,String upFilePath, String codcFilePath){
        return convertMp4(ffmpegPath, upFilePath, codcFilePath, "");
    }
    /**
     * 视频转码
     * @param ffmpegPath    转码工具的存放路径
     * @param upFilePath    用于指定要转换格式的文件,要截图的视频源文件
     * @param codcFilePath    格式转换后的的文件保存路径
     * @param mediaPicPath    截图保存路径
     * @return
     * @throws Exception
     */
    public static boolean convertMp4(String ffmpegPath, String upFilePath, String codcFilePath,
                                     String mediaPicPath) {
        // 创建一个List集合来保存转换视频文件为mp4格式的命令
        List<String> convert = new ArrayList<String>();
        convert.add(ffmpegPath); // 添加转换工具路径
        convert.add("-i"); // 添加参数＂-i＂，该参数指定要转换的文件
        convert.add(upFilePath); // 添加要转换格式的视频文件的路径
        //convert.add("-vcodec");     //指定转换的质量 取值0.01-255，约小质量越好
        //convert.add("-c:v");
        //convert.add("h265");
        //convert.add("-profile");     //设置音视频的profile，默认为-99
        //convert.add("high");
        //convert.add("-level");     //设置音视频的profile，默认为-99
        //convert.add("4");
        //convert.add("-vbr");
        //convert.add("3");
        convert.add("-qscale");     //指定转换的质量 取值0.01-255，约小质量越好
        convert.add("6");
        convert.add("-ab");        //设置音频码率,后面-ac设为立体声时要以一半比特率来设置，比如192kbps的就设成96，转换君默认比特率都较小，要听到较高品质声音的话建议设到160kbps（80）以上
        convert.add("64");
        convert.add("-ac");        //设置声道数,设定声道数，1就是单声道，2就是立体声，转换单声道的TVrip可以用1（节省一半容量），高品质的DVDrip就可以用2
        convert.add("2");
        convert.add("-ar");        //设置声音的采样频率,PSP只认24000
        convert.add("22050");
        convert.add("-r");        //设置帧频 缺省25
        convert.add("24");
        //convert.add("-f");        //设置视频格式
        //convert.add("mp4");
        convert.add("-y"); // 添加参数＂-y＂，该参数指定将覆盖已存在的文件
        convert.add(codcFilePath);

        // 创建一个List集合来保存从视频中截取图片的命令
      /*  List<String> cutpic = new ArrayList<String>();
        cutpic.add(ffmpegPath);
        cutpic.add("-i");
        cutpic.add(upFilePath); // 同上（指定的文件即可以是转换为flv格式之前的文件，也可以是转换的flv文件）
        cutpic.add("-y");
        cutpic.add("-f");
        cutpic.add("image2");
        cutpic.add("-ss"); // 添加参数＂-ss＂，该参数指定截取的起始时间
        cutpic.add("17"); // 添加起始时间为第17秒
        cutpic.add("-t"); // 添加参数＂-t＂，该参数指定持续时间
        cutpic.add("0.001"); // 添加持续时间为1毫秒
        cutpic.add("-s"); // 添加参数＂-s＂，该参数指定截取的图片大小
        cutpic.add("800*280"); // 添加截取的图片大小为350*240
        cutpic.add(mediaPicPath); */// 添加截取的图片的保存路径

        boolean mark = true;
        ProcessBuilder builder = new ProcessBuilder();
        try {
            builder.command(convert);
            builder.redirectErrorStream(true);
            builder.start();

            /*builder.command(cutpic);
            builder.redirectErrorStream(true);
            // 如果此属性为 true，则任何由通过此对象的 start() 方法启动的后续子进程生成的错误输出都将与标准输出合并，
            //因此两者均可使用 Process.getInputStream() 方法读取。这使得关联错误消息和相应的输出变得更容易
            builder.start();*/
            log.info(upFilePath+"转换成"+codcFilePath+"成功！");
        } catch (Exception e) {
            mark = false;
            log.error(upFilePath+"转换成"+codcFilePath+"失败！", e);
        }
        return mark;
    }

    /**
     * @Title: processMp4
     * @Description: 把视频转换成MP4文件  视频编码为H264  音频为源视频文件的音频参数
     * @param ffmpegPath  ffmpeg对象
     * @param upFilePath  上传文件的地址
     * @param codcFilePath 转码后输出的文件地址
     * @param isOnlyConvertFormat 是不是需要无损转码
     * @return    参数
     * @author sy
     * @throws
     * @return boolean    返回类型
     *
     */
    public static void videoToMp4(String ffmpegPath,String upFilePath, String codcFilePath,boolean isOnlyConvertFormat) throws Exception{

        List<String> command = new ArrayList<String>();
        command.add(ffmpegPath);
        command.add("-i");
        command.add(upFilePath);
        command.add("-c:v"); //视频编码设置选项
        if(isOnlyConvertFormat){
            command.add("copy");//视频编码使用源文件的
        }else{
            command.add("libx264");//视频编码使用H264
        }
	  /*  command.add("-mbd");
	    command.add("0");*/
        command.add("-c:a");//音频编码设置选项
        command.add("copy");//音频参数复制源文件的
        /* command.add("aac");*/
        command.add("-strict");
        command.add("-2");
	    /*command.add("-pix_fmt");
	    command.add("yuv420p");
	    command.add("-movflags");
	    command.add("faststart");*/
        command.add("-y");
        command.add(codcFilePath);
        // try {

        // 方案1
        //Process videoProcess = Runtime.getRuntime().exec(ffmpegPath + "ffmpeg -i " + oldfilepath
        //	                + " -ab 56 -ar 22050 -qscale 8 -r 15 -s 600x500 "
        //	                + outputPath + "a.flv");

        // 方案2
        Process videoProcess = new ProcessBuilder(command).redirectErrorStream(true).start();
        new PrintStream(videoProcess.getErrorStream()).start();
        new PrintStream(videoProcess.getInputStream()).start();
        videoProcess.waitFor();

	    /*    return true;
	    } catch (Exception e) {
	        logger.error("执行ffmpeg视频转换命令失败！", e);
	        return false;
	    }*/
    }
    /**
     * @Title: getPicDecoder
     * @Description: 获取图片文件的编码
     * @param picFile
     * @return
     * @throws Exception    参数
     * @author sy
     * @throws
     * @return String    返回类型
     *
     */
    public static String getPicDecoder(File picFile) throws Exception {
        ImageInputStream imageInputStream = ImageIO.createImageInputStream(picFile);
        Iterator<ImageReader> iterator = ImageIO.getImageReaders(imageInputStream);
        if (!iterator.hasNext()) {
            throw new RuntimeException("No readers found!");
        }
        ImageReader reader = iterator.next();
        return reader.getFormatName();
    }

    /**
     * @Title: picToJPG
     * @Description: 图片转换成jpg  但是很纠结的tmp文件无法识别 也就是上传时生成的tmp临时文件该方法无法读取数据  该方法只能处理带图片后缀的文件 其他文件都无法读取
     * @return    参数
     * @author sy
     * @throws
     * @return boolean    返回类型
     *
     */
   /* public static void picToJPG(String sourceFile,String destFile) throws Exception{
    	JPGOptions options = new JPGOptions();
		options.setQuality(72);
		ImageProducer image = Jimi.getImageProducer(sourceFile);
		JimiWriter writer = Jimi.createJimiWriter(destFile);
		writer.setSource(image);
		writer.setOptions(options);
		writer.putImage(destFile);
    }*/

    public static void main(String[] args) throws Exception {
        String s = "D:\\workspace20180521\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp1\\wtpwebapps\\advert-publish-appstub\\ffpeg\\ffmpeg.exe";
        //String s1 = "D:\\workspace20180521\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp1\\wtpwebapps\\advert-publish-appstub\\ftp\\temp\\upload_588cf5c_164697486c6__7ffb_00000008.tmp";
        //String s2 = "D:\\workspace20180521\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp1\\wtpwebapps\\advert-publish-appstub\\ffpeg\\convert\\22221.mp4";
        String s1 = "D:\\无法播放的mp4\\20180904现代凯莱酒店楼宇上刊竖屏.mp4";
        String s2 = "D:\\无法播放的mp4\\Programs (1)\\20180904现代凯莱酒店楼宇上刊竖屏.mp4";
        //Convert1.executeCodecs("E:\\convert\\ffmpeg.exe", "E:\\convert\\123.avi", "E:\\convert\\mp4\\", "E:\\convert\\");
        VideoConvertUtil.convertMp4(s, s1, s2);
    }
}

class PrintStream extends Thread
{
    java.io.InputStream __is = null;
    public PrintStream(java.io.InputStream is)
    {
        __is = is;
    }

    public void run()
    {
        try
        {
            while(this != null)
            {
                int _ch = __is.read();
                if(_ch != -1)
                    System.out.print((char)_ch);
                else break;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}

