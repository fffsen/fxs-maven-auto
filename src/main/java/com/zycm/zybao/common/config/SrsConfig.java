package com.zycm.zybao.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @description: srs流媒体配置
 * @author: sy
 * @create: 2023-04-25 19:05
 */
@Data
@Configuration
public class SrsConfig {
    public static String apiGetStream;
    public static String apiGetClient;

    @Value("${srs.api.getStream}")
    public void setApiGetStream(String apiGetStream) {
        SrsConfig.apiGetStream = apiGetStream;
    }
    @Value("${srs.api.getClient}")
    public void setApiGetClient(String apiGetClient) {
        SrsConfig.apiGetClient = apiGetClient;
    }
}
