package com.zycm.zybao.common.config;

import lombok.Data;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSslConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: mq配置
 * @author: sy
 * @create: 2023-04-23 11:30
 */
@Data
@Configuration
public class MqConfig {

    /**
     *  MQ的接收topic
     */

    public static String mqReceiverTop;
    /**
     *  MQ的发送topic
     */

    public static String mqSenderTop;
    /**
     *  MQ的连接地址
     */

    public static String mqBrokerURL;
    /**
     *  MQ的连接用户
     */

    public static String mqUserName;
    /**
     *  MQ的连接密码
     */

    public static String mqPassword;
    /**
     *  MQ的接收id
     */

    public static String mqReceiverServiceClientId;
    /**
     *  MQ的发送id
     */

    public static String mqSenderServiceClientId;
    /**
     *  MQ的证书
     */

    public static String mqCertificateName;
    /**
     *  MQ的证书密码
     */

    public static String mqCertificatePwd;
    /**
     *  MQ的管理端用户
     */

    public static String mqWebUserName;
    /**
     *  MQ的web管理端密码
     */

    public static String mqWebPassword;
    /**
     *  MQ的api url
     */

    public static String mqApiUrl;
    /**
     *  MQ的api Request
     */

    public static String mqApiRequest;

    @Value("${mq.receiver_top}")
    public void setMqReceiverTop(String mqReceiverTop) {
        this.mqReceiverTop = mqReceiverTop;
    }
    @Value("${mq.sender_top}")
    public void setMqSenderTop(String mqSenderTop) {
        this.mqSenderTop = mqSenderTop;
    }
    @Value("${mq.brokerURL}")
    public void setMqBrokerURL(String mqBrokerURL) {
        this.mqBrokerURL = mqBrokerURL;
    }
    @Value("${mq.userName}")
    public void setMqUserName(String mqUserName) {
        this.mqUserName = mqUserName;
    }
    @Value("${mq.password}")
    public void setMqPassword(String mqPassword) {
        this.mqPassword = mqPassword;
    }
    @Value("${mq.receiverServiceClientId}")
    public void setMqReceiverServiceClientId(String mqReceiverServiceClientId) {
        this.mqReceiverServiceClientId = mqReceiverServiceClientId;
    }
    @Value("${mq.senderServiceClientId}")
    public void setMqSenderServiceClientId(String mqSenderServiceClientId) {
        this.mqSenderServiceClientId = mqSenderServiceClientId;
    }
    @Value("${mq.certificate.certificateName}")
    public void setMqCertificateName(String mqCertificateName) {
        this.mqCertificateName = mqCertificateName;
    }
    @Value("${mq.certificate.certificatePwd}")
    public void setMqCertificatePwd(String mqCertificatePwd) {
        this.mqCertificatePwd = mqCertificatePwd;
    }
    @Value("${mq.web.userName}")
    public void setMqWebUserName(String mqWebUserName) {
        this.mqWebUserName = mqWebUserName;
    }
    @Value("${mq.web.password}")
    public void setMqWebPassword(String mqWebPassword) {
        this.mqWebPassword = mqWebPassword;
    }
    @Value("${mq.api.url}")
    public void setMqApiUrl(String mqApiUrl) {
        this.mqApiUrl = mqApiUrl;
    }
    @Value("${mq.api.J4pReadRequest}")
    public void setMqApiRequest(String mqApiRequest) {
        this.mqApiRequest = mqApiRequest;
    }

    @Bean
    public ActiveMQConnectionFactory activeMQSslConnectionFactory() {
        ActiveMQConnectionFactory activeMQSslConnectionFactory = new ActiveMQConnectionFactory();
        activeMQSslConnectionFactory.setBrokerURL(mqBrokerURL);
        activeMQSslConnectionFactory.setUserName(mqUserName);
        activeMQSslConnectionFactory.setPassword(mqPassword);
        return activeMQSslConnectionFactory;
    }
}
