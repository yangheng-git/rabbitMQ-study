package com.yanghx.rabbitmq.api.exchanges.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.yanghx.rabbitmq.common.RabbitMqUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * topic exchange 生产者
 *
 * @author yangHX
 * createTime  2019/3/10 23:13
 */
public class Producer4TopicExchange {

    public static void main(String[] args) throws IOException, TimeoutException {
        //1 创建ConnectionFactory
        ConnectionFactory connectionFactory = RabbitMqUtil.getConnectionFactory();
        //2 创建连接
        Connection connection = connectionFactory.newConnection();
        //3 创建通道 channel
        Channel channel = connection.createChannel();
        //4 声明
        String exchangeName = "test_topic_exchange";
        String routingKey1 = "user.save";
        String routingKey2 = "user.update";
        String routingKey3 = "user.delete.abc";
        //5 发送
        String msg = "hello word RabbitMQ For  Topic Exchange Message .... ";
        channel.basicPublish(exchangeName, routingKey1, null, msg.getBytes());
        channel.basicPublish(exchangeName, routingKey2, null, msg.getBytes());
        channel.basicPublish(exchangeName, routingKey3, null, msg.getBytes());

        channel.close();
        connection.close();

    }
}
