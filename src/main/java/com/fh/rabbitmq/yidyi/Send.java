package com.fh.rabbitmq.yidyi;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.SneakyThrows;

public class Send {

    private final static String QUEUE_NAME = "rabbit";

    @SneakyThrows
    public static void main(String[] args) throws IOException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.126.136");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");
        Connection  connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String message = "我是生产者!";
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();


    }
}
