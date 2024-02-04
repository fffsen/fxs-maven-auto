package com.zycm.zybao.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileUrlResource;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import java.io.IOException;

/**
 * @description: 上传文件兼容springnvc的CommonsMultipartFile  springboot默认使用的是MultipartFile 所以要剔除默认的
 * @author: sy
 */
@Configuration
@EnableAutoConfiguration(exclude = {MultipartAutoConfiguration.class})
public class MultipartConfig {

    //单个文件大小限制
    private Integer maxInMemorySize;

    //总文件大小限制
    private Long maxUploadSize;

    private String uploadTempDir;

    @Value("${spring.servlet.multipart.max-file-size}")
    public void setMaxInMemorySize(Integer maxInMemorySize) {
        this.maxInMemorySize = maxInMemorySize;
    }

    @Value("${spring.servlet.multipart.max-request-size}")
    public void setMaxUploadSize(Long maxUploadSize) {
        this.maxUploadSize = maxUploadSize;
    }

    @Value("${spring.servlet.uploadTempDir}")
    public void setUploadTempDir(String uploadTempDir) {
        this.uploadTempDir = uploadTempDir;
    }

    /**
     * 文件上传配置
     * @return
     */
    @Bean(name = "multipartResolver")
    public MultipartResolver multipartResolver() throws IOException {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding("UTF-8");
        resolver.setResolveLazily(true); //resolveLazily属性启用是为了推迟文件解析，在UploadAction中捕获文件大小异常
        resolver.setMaxInMemorySize(100); //小于值 缓存内存 否则存磁盘
        resolver.setMaxUploadSize(maxInMemorySize);//上传文件大小kb 30M 30*1024*1024
       /* if(SysConfig.systemIsWin()){
           resolver.setUploadTempDir(new ClassPathResource("/ftp/temp/"));//上传文件临时地址 target 包下没有启动报错
        } else {*/
            resolver.setUploadTempDir(new FileUrlResource(uploadTempDir));
        /*}*/

        return resolver;
    }
}
