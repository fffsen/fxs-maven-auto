package com.zycm.zybao.common.shiro;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @description:
 * @author: sy
 * @create: 2023-04-27 11:19
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "shiro")
public class ShiroFilterChainMap {

    public Map<String,String> filterChainDefinitionMap;

   /* @Value("${shiro.filterChainDefinitionMap}")
    public void setFilterChainDefinitionMap(Map<String, String> filterChainDefinitionMap) {
        this.filterChainDefinitionMap = filterChainDefinitionMap;
    }*/

    public Map<String, String> getFilterChainDefinitionMap() {
        return filterChainDefinitionMap;
    }
}
