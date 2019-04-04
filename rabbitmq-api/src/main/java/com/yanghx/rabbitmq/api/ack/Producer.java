package com.yanghx.rabbitmq.api.ack;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.yanghx.rabbitmq.common.RabbitMqUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

/**
 * 消费端ack与重回队列
 *
 * @author yangHX
 * createTime  2019/3/28 21:25
 */
public class Producer {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = RabbitMqUtil.getConnectionFactory();
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        String exchangeName = "test_ack_exchange";
        String routingKey = "ack.sve";

        for (int i = 0; i < 5; i++) {
            HashMap<String, Object> headers = new HashMap<>();
            headers.put("num", i);

            AMQP.BasicProperties build = new AMQP.BasicProperties.Builder()
                    //常用模式【2: 持久化投递】
                    .deliveryMode(2)
                    .contentEncoding("UTF-8")
                    .headers(headers).build();

            String msg = "Hello  RabbitMQ ACK Message" + i;
            channel.basicPublish(exchangeName, routingKey, true, build, msg.getBytes());
        }

    }
}
