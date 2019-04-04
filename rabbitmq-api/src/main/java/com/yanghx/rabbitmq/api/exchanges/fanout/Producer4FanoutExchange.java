package com.yanghx.rabbitmq.api.exchanges.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.yanghx.rabbitmq.common.RabbitMqUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * fanout 交换机 生产者
 *
 * @author yangHX
 * createTime  2019/3/10 23:45
 */
public class Producer4FanoutExchange {

    public static void main(String[] args) throws IOException, TimeoutException {
        //1 创建ConnectionFactory
        ConnectionFactory connectionFactory = RabbitMqUtil.getConnectionFactory();
        //2 创建连接
        Connection connection = connectionFactory.newConnection();
        //3 创建通道 channel
        Channel channel = connection.createChannel();
        //4 声明
        String exchangeName = "test_fanout_exchange";
        //5 发送
        String msg = "hello word RabbitMQ For  Fanout Exchange Message .... ";
        for (int i = 10; i > 0; i--) {
            channel.basicPublish(exchangeName, "", null, msg.getBytes());
        }

        channel.close();
        connection.close();

    }
}
