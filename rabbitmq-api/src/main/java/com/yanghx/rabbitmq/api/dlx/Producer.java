package com.yanghx.rabbitmq.api.dlx;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.yanghx.rabbitmq.common.RabbitMqUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 死信队列
 *
 * @author yangHX
 * createTime  2019/3/28 22:51
 */
public class Producer {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = RabbitMqUtil.getConnectionFactory();
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        String exchangeName = "test_dlx_exchange";
        String routingKey = "dlx.sve";
        String msg = "Hello RabbitMQ DLX Message";

        for (int i = 0; i < 5; i++) {
            AMQP.BasicProperties build = new AMQP.BasicProperties().builder()
                    .deliveryMode(2)
                    .contentEncoding("UTF-8")
                    .expiration("10000")
                    .build();
            channel.basicPublish(exchangeName, routingKey, build, msg.getBytes());
        }
    }
}
