package com.yanghx.rabbitmq.api.consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.yanghx.rabbitmq.common.RabbitMqUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 自定义消息监听
 * 生产者
 *
 * @author yangHX
 * createTime  2019/3/21 23:02
 */
public class Producer {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = RabbitMqUtil.getConnectionFactory();
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();


        String exchangeName = "test_consumer_exchange";
        String routingKey = "consumer.save";

        String msg = "Hello RabbitMQ Consumer Message";

        for (int i = 5; i > 0; i--) {
            channel.basicPublish(exchangeName, routingKey, true, null, msg.getBytes());
        }
    }
}
