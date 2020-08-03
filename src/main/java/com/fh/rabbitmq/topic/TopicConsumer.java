package com.fh.rabbitmq.topic;

import com.fh.rabbitmq.gzzhe.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;

public class TopicConsumer {
    //主题(通配符)模式(topic) 消费类
    // 主题模式（通配符模式） *号只匹配一个  #号匹配一个词或多个
    private final static String QUEUE_NAME = "test_queue_topic_work_3"; //定义队列名称

    private final static String EXCHANGE_NAME = "test_exchange_topic";//定义交换机名称

    public static void main(String[] argv) throws Exception {
        // 获取到连接以及mq通道
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        //定义交换机模式  // 声明exchange     fanout广播模式    redirect 路由模式    topic 主题模式
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

        // 声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 绑定队列到交换机    routingKey  通配"routekey.test.app"
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "#.test.*");
        // 同一时刻服务器只会发一条消息给消费者
        channel.basicQos(1);
        // 定义队列的消费者
        QueueingConsumer consumer = new QueueingConsumer(channel);
        // 监听队列，手动返回完成
        channel.basicConsume(QUEUE_NAME, false, consumer);
        // 获取消息
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            System.out.println("主题模式-消费者3  " + EXCHANGE_NAME+"    "+QUEUE_NAME+" ====  Received 3 '" + message + "'");
            //创建我们的监听的消息 auto Ack 默认自动签收  必须手动ack
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        }
    }

}
