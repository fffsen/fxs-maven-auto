package com.zycm.zybao.amq.topic.producer;

import com.zycm.zybao.amq.listener.ConsumerStatusListener;
import com.zycm.zybao.common.config.MqConfig;
import com.zycm.zybao.common.config.RedisConfig;
import com.zycm.zybao.common.redis.RedisHandle;
import com.zycm.zybao.dao.MediaAttributeDao;
import com.zycm.zybao.dao.MediaRunLogDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSslConnectionFactory;
import org.apache.activemq.advisory.ConsumerEventSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.jms.*;

/** 发布系统使用的topic发送指令处理器
* @ClassName: FBTopicSender
* @Description: TODO
* @author sy
* @date 2017年9月18日 上午10:57:42
*
*/
@Slf4j
@Component
public class FBTopicSender {

	@Autowired(required = false)
	private ActiveMQConnectionFactory amqSslConnectionFactory;

	@Autowired(required = false)
	private MediaAttributeDao mediaAttributeDao;
	@Autowired(required = false)
	private MediaRunLogDao mediaRunLogDao;
	@Autowired(required = false)
	private RedisHandle redisHandle;

	// Connection ：JMS 客户端到JMS Provider 的连接
	private static Connection connection = null;
    // Session： 一个发送或接收消息的线程
	public static Session session;
    // Destination ：消息的目的地;消息发送给谁.
	private static Destination destination;
    // MessageProducer：消息发送者
	private static MessageProducer producer;

	/*private static String SENDER_TOP;
	private static String senderServiceClientId;

	private static String certificateName;
	private static String certificatePwd;*/

	private String redis_prefix = RedisConfig.redisPrefix;

	@PostConstruct
    private void init(){
    	log.warn("\n>>>>>>>>>>>>>>>>>>>>>>>>初始化MQ发送器及终端连接状态的监听器>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    	try {
    		amqSslConnectionFactory.setClientID(MqConfig.mqSenderServiceClientId);
//    		amqSslConnectionFactory.setTrustStore(MqConfig.mqCertificateName);
//    		amqSslConnectionFactory.setTrustStorePassword(MqConfig.mqCertificatePwd);
//    		amqSslConnectionFactory.setKeyStore(MqConfig.mqCertificateName);
//    		amqSslConnectionFactory.setKeyStorePassword(MqConfig.mqCertificatePwd);
    		// 构造从工厂得到连接对象
            connection = amqSslConnectionFactory.createConnection();
            // 启动
            connection.start();
            // 获取操作连接  true为启用事务
            session = connection.createSession(Boolean.TRUE,Session.AUTO_ACKNOWLEDGE);
            // 获取session注意参数值xingbo.xu-queue是一个服务器的queue，须在在ActiveMq的console配置
            destination = session.createTopic(MqConfig.mqSenderTop);


            ConsumerEventSource source = new ConsumerEventSource(connection, destination);
            source.setConsumerListener(new ConsumerStatusListener(mediaAttributeDao,mediaRunLogDao,redisHandle));
            source.start();

            // 得到消息生成者【发送者】
            producer = session.createProducer(destination);
            // 设置是否持久化
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            log.debug("\n<<<<<<<<<<<<<<<<<<<<<<<<初始化MQ发送器及终端连接状态的监听器完成<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		} catch (Exception e) {
			e.printStackTrace();
		}

    }

	/**
	* 说明：发送指令给所有终端
	* @Title: sendMsg
	* @Description: TODO
	* @author sy
	* @param msg
	* @return void
	*
	*/
	/*public void sendMsg(String msg){
		log.debug("####平台群发的指令是："+msg);
		try {
			if(null == connection){
				init();
			}

			TextMessage message = session.createTextMessage(msg);
			此处设置是群发的关键 “R”相对于别名 ， 每个终端在注册的时候都设置成这个别名，  这里发送的时候 终端就是接收到 ，  但是此设置不支持mqtt模式 ，
			*而现在android端只能用mqtt  因此mqtt默认都是群发

			message.setStringProperty("receiver", "R");
			producer.send(message);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	/**
	* 说明：发送单个事务的指令给指定的终端
	* @Title: sendMsg
	* @Description: TODO
	* @author sy
	* @param msg
	* @param machineCode
	* @return void
	*
	*/
	public void sendSingleMsg(String msg,String machineCode){
		try {
			if(null == connection){
				init();
			}
			//判断未激活列表
			Object val = redisHandle.getMapField(redis_prefix+"distrust_media", machineCode);
			if(val != null){//不信任终端不发送指令
				log.warn("####不信任的终端"+machineCode+",不发送指令");
				return;
			}
			log.info("\n平台发给【"+machineCode+"】的指令是："+msg);
			TextMessage message = session.createTextMessage(msg);
			message.setStringProperty("receiver", machineCode);
			/*此处设置是一对一发送的关键   因为android只支持mqtt  所以目前mqtt版本只有群发模式
			*一下设置是修改了amq的源代码而成  在mq源代码中增加了一个一对一发送的控制器  加入以下的设置   发送器就会过滤所有终端 只选择指定终端发送
			*/
			message.setStringProperty("PTP_CLIENTID", machineCode);
			producer.send(message);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	* 说明：发送多个指令给指定的终端  再统一提交事务 注意session.commit()是在所有指令发送完后再执行
	* @Title: sendMsg
	* @Description: TODO
	* @author sy
	* @param msg
	* @param machineCode
	* @return void
	*
	*/
	public void sendMsg(String msg,String machineCode){
		try {
			if(null == connection){
				init();
			}
			//判断未激活列表
			Object val = redisHandle.getMapField(redis_prefix+"distrust_media", machineCode);
			if(val != null){//不信任终端不发送指令
				log.warn("####不信任的终端"+machineCode+",不发送指令");
				return;
			}
			log.info("\n平台发给【"+machineCode+"】的指令是："+msg);
			TextMessage message = session.createTextMessage(msg);
			message.setStringProperty("receiver", machineCode);
			/*此处设置是一对一发送的关键   因为android只支持mqtt  所以目前mqtt版本只有群发模式
			*一下设置是修改了amq的源代码而成  在mq源代码中增加了一个一对一发送的控制器  加入以下的设置   发送器就会过滤所有终端 只选择指定终端发送
			*/
			message.setStringProperty("PTP_CLIENTID", machineCode);
			producer.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*@Value("${mq.sender_top}")
	public static void setSENDER_TOP(String sENDER_TOP) {
		SENDER_TOP = sENDER_TOP;
	}
	@Value("${mq.senderServiceClientId}")
	public static void setSenderServiceClientId(String senderServiceClientId) {
		FBTopicSender.senderServiceClientId = senderServiceClientId;
	}

	@Value("${mq.certificate.certificateName}")
	public static void setCertificateName(String certificateName) {
		FBTopicSender.certificateName = certificateName;
	}
	@Value("${mq.certificate.certificatePwd}")
	public static void setCertificatePwd(String certificatePwd) {
		FBTopicSender.certificatePwd = certificatePwd;
	}*/

}
