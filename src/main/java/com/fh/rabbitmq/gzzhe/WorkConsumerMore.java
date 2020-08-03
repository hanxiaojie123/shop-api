package com.fh.rabbitmq.gzzhe;

import com.rabbitmq.client.*;

import java.io.IOException;

public class WorkConsumerMore {
    //工作模式:多个人一起消费一个队列消息.能者多劳
    //定义队列名称
    private final static String QUEUE_NAME = "test_queue_work";

    public static void main(String[] argv) throws Exception {
        // 获取到连接以及mq通道
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        // 声明队列 参数依次 队列名称  是否持久化  是否排它(仅此连接) 是否自动删除  其他构造参数
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        //定义消费数  每次只能消费一条记录.当消息执行后需要返回ack确认消息 才能执行下一条
        channel.basicQos(1);  //******
        //***方式一***
        // 定义队列的消费者
//        QueueingConsumer consumer = new QueueingConsumer(channel);
//        // 监听队列，false表示手动返回完成状态，true表示自动  false表示手动返回ack
//        channel.basicConsume(QUEUE_NAME, false, consumer);   //*****
//        // 获取消息
//        while (true) {
//            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
//            String message = new String(delivery.getBody());
//            System.out.println(QUEUE_NAME+" == Received '" + message + "'");
//            // 表示使用手动确认模式 从队列中删除该消息   deliveryTag 队列下标位置
//            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);  //******
//        }

        //***方式二***
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, "UTF-8");
                System.out.println("短信消费者获取消息:" + msg);
                // 手动发送消息告诉给mq服务器端  从队列删除该消息
                channel.basicAck(envelope.getDeliveryTag(), false); //*****
            }
        };
        //创建我们的监听的消息 auto Ack 默认自动签收  必须手动ack
        channel.basicConsume(QUEUE_NAME, false, defaultConsumer); //*****
    }

}
