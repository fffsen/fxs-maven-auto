package com.zycm.zybao;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zycm.zybao.dao")
public class ZycmAdvertPublishApplication {

    public static void main(String[] args) {

        SpringApplication.run(ZycmAdvertPublishApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  启动成功   ლ(´ڡ`ლ)ﾞ  \n" );

    }

}
