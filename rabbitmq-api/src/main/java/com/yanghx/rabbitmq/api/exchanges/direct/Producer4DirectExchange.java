package com.yanghx.rabbitmq.api.exchanges.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.yanghx.rabbitmq.common.RabbitMqUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 直接 交换机
 * direct Exchange模式生产者
 *
 * @author yangHX
 * createTime  2019/3/10 22:01
 */
public class Producer4DirectExchange {

    public static void main(String[] args) throws IOException, TimeoutException {
        //1 创建ConnectionFactory
        ConnectionFactory connectionFactory = RabbitMqUtil.getConnectionFactory();
        //2 创建连接
        Connection connection = connectionFactory.newConnection();
        //3 创建通道 channel
        Channel channel = connection.createChannel();
        //4 声明
        String exchangeName = "test-direct-exchange";
        String routingKey = "test.direct";
        //5 发送
        String msg = "hello word RabbitMQ For  Direct Exchange Message .... ";
        channel.basicPublish(exchangeName, routingKey, null, msg.getBytes());

        channel.close();
        connection.close();
    }
}
