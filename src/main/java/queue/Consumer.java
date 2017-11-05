package queue;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.*;

@SuppressWarnings({"Duplicates", "UnusedAssignment"})
public class Consumer {

    @Test
    public void consumeMessage() {

        Connection connection = null;
        Session session = null;
        MessageConsumer messageConsumer = null;

        try {
            // 创建连接工厂
            String username = "admin";
            String password = "admin";
            String url = "tcp://192.168.25.128:61616";
            ConnectionFactory factory = new ActiveMQConnectionFactory(url);
            // 创建连接
            connection = factory.createConnection(username, password);
            // 开启连接
            connection.start();
            // 获得会话
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            // 创建目的地,Destination是Queue和Topic的父接口
            Destination destination = session.createQueue("RAW_MQ_QUEUE");// 指定监听的队列
            // 创建消费者
            messageConsumer = session.createConsumer(destination);
            // 设置消息监听
            /**不使用匿名内部类也可以单独创建一个类，实现MessageListener接口，重写onMessage方法*/
            messageConsumer.setMessageListener(new MessageListener() {
                public void onMessage(Message message) {
                    try {
                        TextMessage textMessage = (TextMessage) message;
                        String text = textMessage.getText();
                        System.out.println("consumer has recieved the message:" + text);

                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            });

            System.in.read();// 只是为了不让消费线程死亡，可以持续监听消息。
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            // 释放资源
            if (messageConsumer != null) {
                try {
                    messageConsumer.close();
                    messageConsumer = null;
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
