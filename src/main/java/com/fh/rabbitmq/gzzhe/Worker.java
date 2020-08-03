package com.fh.rabbitmq.gzzhe;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Worker{

    private final static Random random = new Random();

    private final static String QUEUE_NAME = "work_queue";
    // 获取到连接以及mq通道


    public static void main(String[] args) {
        try {
            Connection connection = ConnectionUtil.getConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            QueueingConsumer consumer = new QueueingConsumer(channel);
            channel.basicConsume(QUEUE_NAME, true, consumer);

            System.out.println("waiting for task....");
            while (true) {
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                String task = new String(delivery.getBody());
                System.out.println("worker start to handler the task3:" + task);
                //模拟处理任务花费时间
                handlerTask();
                System.out.println("worker end to handler the task3:" + task);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handlerTask() {
        try {
            TimeUnit.SECONDS.sleep(random.nextInt(5) + 1);
        } catch (InterruptedException e) {
            //ignore
        }
    }
}
