package com.zycm.zybao.common.constant;

import com.zycm.zybao.common.config.RedisConfig;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 通用常量信息
 *
 */
public class Constants
{
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";

    /**
     * 通用成功标识
     */
    public static final String SUCCESS = "0";

    /**
     * 通用失败标识
     */
    public static final String FAIL = "1";

    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "Success";

    /**
     * 注销
     */
    public static final String LOGOUT = "Logout";

    /**
     * 注册
     */
    public static final String REGISTER = "Register";

    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL = "Error";

    /**
     * 系统用户授权缓存
     */
    public static final String SYS_AUTH_CACHE = "sys-authCache";

    /**
     * 参数管理 cache name
     */
    public static final String SYS_CONFIG_CACHE = "sys-config";

    /**
     * 参数管理 cache key
     */
    public static final String SYS_CONFIG_KEY = "sys_config:";

    /**
     * 字典管理 cache name
     */
    public static final String SYS_DICT_CACHE = "sys-dict";

    /**
     * 字典管理 cache key
     */
    public static final String SYS_DICT_KEY = "sys_dict:";

    /**
     * 资源映射路径 前缀
     */
    public static final String RESOURCE_PREFIX = "/profile";

    /**
     * RMI 远程方法调用
     */
    public static final String LOOKUP_RMI = "rmi:";

    /**
     * LDAP 远程方法调用
     */
    public static final String LOOKUP_LDAP = "ldap:";

    /**
     * LDAPS 远程方法调用
     */
    public static final String LOOKUP_LDAPS = "ldaps:";

    /**
     * 定时任务白名单配置（仅允许访问的包名，如其他需要可以自行添加）
     */
    public static final String[] JOB_WHITELIST_STR = { "com.zycm" };

    /**
     * 定时任务违规的字符
     */
    public static final String[] JOB_ERROR_STR = { "java.net.URL", "javax.naming.InitialContext", "org.yaml.snakeyaml",
            "org.springframework", "org.apache", "com.zycm.common.utils.file", "com.zycm.common.config" };








    /** 区别于接收到终端的日志消息   "(s)"前缀表示日志消息是服务端监听增加的
     * @Fields SERVICE_PREFIX : TODO
     */
    public static String SERVICE_PREFIX = "(s)";

    public static long ONLINE_REDIS_EXPIRETIME = 60;//存redis的数据过期时间

    public static String ESTABLISH_ONLINE = "establish_online";
    //public static String ESTABLISH_OFFLINE = "establish_offline";

    public static String REDIS_ONLINE_MEDIA_PREFIX = RedisConfig.redisPrefix;
    /**
     * @Fields 表示在redis中 在线的终端的key组成  的前缀部分
     */
    public static String ONLINE_REDIS_PREFIX = "status_online_" + RedisConfig.redisPrefix;
    /**
     * @Fields 表示在redis中 离线的终端的key组成  的前缀部分
     */
    //public static String OFFLINE_REDIS_PREFIX = "status_offline_" + RedisConfig.redisPrefix;

    /**
     * @Fields 表示在redis中 在线并通信异常的终端的key组成  的前缀部分
     */
    //public static String ERRORSTATUS_REDIS_PREFIX = "exception_status_" + RedisConfig.redisPrefix;
    /**
     * @Fields 表示在redis中 未激活的终端的key组成  的前缀部分
     */
    public static String DISTRUST_MEDIA_REDIS_PREFIX = RedisConfig.redisPrefix+"distrust_media";

    /**
     * @Fields 表示在redis中 上一次修改的在线的终端的key组成  的前缀部分
     */
    //public static String PREV_ONLINE_MEDIA_REDIS_PREFIX = "prev_online_"+RedisConfig.redisPrefix;
    //public static long PREV_ONLINE_REDIS_EXPIRETIME = 600;

    //public static String MYSQL_ONLINE_MEDIA_REDIS_KEY = "mysql_online_"+RedisConfig.redisPrefix;

    public static String LIVE_STREAM_REDIS_KEY = "live_stream_"+RedisConfig.redisPrefix;

    //保存同步组的信息 格式string类型 k，v;例如k = sync_master_cs_主终端组id;v=11,33,4,5同步组的终端组id
    public static String REDIS_SYNC_MASTER_PREFIX = "sync_master_" + RedisConfig.redisPrefix;

    //保存同步组的信息 格式string类型 k，v;例如k = sync_master_cs_主终端机器码;v=dsdsds,fsfdfs,adada同步组内的机器码以逗号隔开
    public static String REDIS_SYNC_MASTER_CODE_PREFIX = "sync_master_code_" + RedisConfig.redisPrefix;

    //保存同步组的节目发布状态信息 格式：k = sync_master_program_cs_终端组id ; v = 终端组发布生成的发布编号
    public static String REDIS_SYNC_MASTER_PUBLISH_PREFIX = "sync_master_publish_" + RedisConfig.redisPrefix;

    //保存同步组节目信息
    public static String REDIS_SYNC_MASTER_PROGRAM_PREFIX = "sync_master_program_" + RedisConfig.redisPrefix;

    public static String REDIS_MEDIA_PROGRAM_DOWNLOAD_STATUS_KEY = "media_program_download_status_" + RedisConfig.redisPrefix;

    //合并日志
    public static String LOG_REDIS_PREFIX = "log_" + RedisConfig.redisPrefix;
}
