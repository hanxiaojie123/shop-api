package com.fh.rabbitmq.gzzhe;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class WorkProducer {
    //向队列中发送100条消息。 工作模式:多个人一起消费一个队列消息.内部轮询机制
    //定义队列名称
    private final static String QUEUE_NAME = "test_queue_work";
    public static void main(String[] argv) throws Exception {
        // 获取到连接以及mq通道
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        // 声明队列 参数依次 队列名称  是否持久化  是否排它(仅此连接) 是否自动删除  其他构造参数
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        for (int i = 0; i < 10; i++) {
            // 消息内容
            String message = "我是工作模式" + i;
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println(QUEUE_NAME+" ==  Sent '" + message + "'");
        }
        channel.close();
        connection.close();
    }

}
