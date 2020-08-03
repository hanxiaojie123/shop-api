package com.fh.rabbitmq.gzzhe;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;

public class Task {
    private final static String QUEUE_NAME = "work_queue";

    public static void main(String[] args) {
        Connection connection = null;
        try {
             connection = ConnectionUtil.getConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            //定义8个任务
            String[] tasks = {"task1", "task2", "task3", "task4", "task5", "task6", "task7", "task8"};
            System.out.println("start to dispatch tasks...");
            //分发任务
            for (String task : tasks) {
                channel.basicPublish("", QUEUE_NAME, null, task.getBytes());
            }
            System.out.println("end to dispatch tasks...");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (IOException e) {
                    // ignore this
                }
            }
        }
    }
}
