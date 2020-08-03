package com.fh.rabbitmq.topic;

import com.fh.rabbitmq.gzzhe.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class TopicProducer {
    //主题(通配符)模式(topic) 生产类
    //主题模式（通配符模式）  定义交换机
    private final static String EXCHANGE_NAME = "test_exchange_topic";

    public static void main(String[] argv) throws Exception {
        // 获取到连接以及mq通道
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        /*
           声明exchange
           fanout广播模式  把接收到的消息推送给所有它知道的队列
           direct 路由模式
           topic 主题模式
         */
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        //消息内容
        String message = "主题模式!";
        //  routingKey="routekey.test.app"
        channel.basicPublish(EXCHANGE_NAME, "routekey.test.app", null, message.getBytes());//*****
        System.out.println(EXCHANGE_NAME+" ==== Sent '" + message + "'");
        channel.close();
        connection.close();
    }


}
