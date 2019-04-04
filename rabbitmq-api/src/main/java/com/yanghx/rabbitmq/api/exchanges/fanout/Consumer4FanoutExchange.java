package com.yanghx.rabbitmq.api.exchanges.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.yanghx.rabbitmq.common.RabbitMqUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Fanout Exchange 消费者
 *
 * @author yangHX
 * createTime  2019/3/10 23:46
 */
public class Consumer4FanoutExchange {


    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory connectionFactory = RabbitMqUtil.getConnectionFactory();

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        //声明
        String exchangeName = "test_fanout_exchange";
        String exchangeType = "fanout";
        String queueName = "test_fanout_queue";
        String routingKey = "";
        //声明交换机
        channel.exchangeDeclare(exchangeName, exchangeType, true, false, false, null);
        //声明队列
        //durable 是否持久化
        channel.queueDeclare(queueName, false, false, false, null);
        //建立交换机和队列的绑定关系
        channel.queueBind(queueName, exchangeName, routingKey);

        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
        //参数。队列名称。 是否自动ACK consumer
        channel.basicConsume(queueName, true, queueingConsumer);

        //循环获取消息
        while (true) {
            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
            String s = new String(delivery.getBody());
            System.out.println("收到 fanout 消息:  " + s);
        }


    }


}
