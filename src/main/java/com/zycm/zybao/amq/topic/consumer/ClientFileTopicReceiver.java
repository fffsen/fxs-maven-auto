package com.zycm.zybao.amq.topic.consumer;

import com.zycm.zybao.common.config.MqConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSslConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;

/** 发布系统使用的接收队列处理器
* @ClassName: FBQueueReceiver
* @Description: TODO
* @author sy
* @date 2017年9月18日 上午10:55:56
*
*/
@Slf4j
@Component
public class ClientFileTopicReceiver {

	/*@Autowired(required = false)
	private ActiveMQConnectionFactory amqConnectionFactory;*/
	@Autowired(required = false)
	private ActiveMQConnectionFactory amqSslConnectionFactory;

	/*@Autowired(required = false)
	private MediaAttributeDao mediaAttributeDao;*/
	@Autowired(required = false)
	private ConsumerClientFileMessageListener consumerClientFileMessageListener;

	// Connection ：JMS 客户端到JMS Provider 的连接
	private static Connection connection = null;
    // Session： 一个发送或接收消息的线程
	private static Session session;
    // Destination ：消息的目的地;消息发送给谁.
	private static Destination destination;
    // 消费者，消息接收者
	private static MessageConsumer consumer;

/*	private static String RECEIVER_TOP;

	private static String receiverServiceClientId;
	private static String certificateName;
	private static String certificatePwd;*/
	@PostConstruct
    private void init(){
    	log.warn("\n>>>>>>>>>>>>>>>>>>>>>>>>初始化AMQ接收监听器>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    	try {
    		amqSslConnectionFactory.setClientID(MqConfig.mqReceiverServiceClientId);
//			amqSslConnectionFactory.setTrustStore(MqConfig.mqCertificateName);
//			amqSslConnectionFactory.setTrustStorePassword(MqConfig.mqCertificatePwd);
//			amqSslConnectionFactory.setKeyStore(MqConfig.mqCertificateName);
//			amqSslConnectionFactory.setKeyStorePassword(MqConfig.mqCertificatePwd);

    		// 构造从工厂得到连接对象
            connection = amqSslConnectionFactory.createConnection();

            //connection.setClientID("server-clientfile-topic");
            // 启动
            connection.start();
            // 获取操作连接
            session = connection.createSession(Boolean.FALSE,Session.AUTO_ACKNOWLEDGE);
            // 获取session注意参数值xingbo.xu-queue是一个服务器的queue，须在在ActiveMq的console配置
            destination = session.createTopic(MqConfig.mqReceiverTop);

            consumer = session.createConsumer(destination);
            consumer.setMessageListener(consumerClientFileMessageListener);
            log.debug("\n<<<<<<<<<<<<<<<<<<<<<<<<初始化AMQ接收监听器完成<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		} catch (Exception e) {
			log.error("AMQ初始化异常",e);
		}
    }

/*	@Value("${mq.receiver_top}")
	public static void setRECEIVER_TOP(String rECEIVER_TOP) {
		RECEIVER_TOP = rECEIVER_TOP;
	}

	@Value("${mq.receiverServiceClientId}")
	public static void setReceiverServiceClientId(String receiverServiceClientId) {
		ClientFileTopicReceiver.receiverServiceClientId = receiverServiceClientId;
	}

	@Value("${mq.certificate.certificateName}")
	public static void setCertificateName(String certificateName) {
		ClientFileTopicReceiver.certificateName = certificateName;
	}

	@Value("${mq.certificate.certificatePwd}")
	public static void setCertificatePwd(String certificatePwd) {
		ClientFileTopicReceiver.certificatePwd = certificatePwd;
	}*/

}
