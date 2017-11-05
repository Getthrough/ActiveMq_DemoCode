package queue;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.*;

@SuppressWarnings({"Duplicates", "UnusedAssignment"})
public class Producer {

    @Test
    public void produceMessage() {
        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;

        try {
            // 创建连接工厂
            ConnectionFactory factory = new ActiveMQConnectionFactory("admin", "admin", "tcp://192.168.25.128:61616");
            // 获得连接
            connection = factory.createConnection();
            // 开启连接
            connection.start();
            // 创建会话
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            // 创建队列
            Queue raw_mq_queue = session.createQueue("RAW_MQ_QUEUE");
            // 创建提供者
            producer = session.createProducer(raw_mq_queue);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);// 设置消息持久化
            // 创建消息,在构造内指定消息内容
            // TextMessage textMessage = session.createTextMessage("the textMessage content");
            TextMessage textMessage = session.createTextMessage();
            textMessage.setText("message test");
            // 发送消息
            producer.send(textMessage);
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            if (producer != null) {
                try {
                    producer.close();
                    producer = null;
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
            if (session != null) {
                try {
                    session.close();
                    session = null;
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                    connection = null;
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }

    }


}
