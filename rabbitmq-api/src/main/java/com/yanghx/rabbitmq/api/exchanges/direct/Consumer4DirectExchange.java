package com.yanghx.rabbitmq.api.exchanges.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.yanghx.rabbitmq.common.RabbitMqUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 直接 交换机
 * direct Exchange  消费者
 *
 * @author yangHX
 * createTime  2019/3/10 22:02
 */
public class Consumer4DirectExchange {

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

        //1 创建ConnectionFactory
        ConnectionFactory connectionFactory = RabbitMqUtil.getConnectionFactory();
        //支持重连
        connectionFactory.setAutomaticRecoveryEnabled(true);
        //3秒
        connectionFactory.setNetworkRecoveryInterval(3000);

        //2 创建连接
        Connection connection = connectionFactory.newConnection();
        //3 创建通信信道
        Channel channel = connection.createChannel();

        //4 声明
        String exchangeName = "test-direct-exchange";
        String exchangeType = "direct";
        String queueName = "test-direct-queue";
        String routingKey = "test.direct";

        //声明exchange（交换机）   declare (宣布。声明)
        channel.exchangeDeclare(exchangeName, exchangeType, true, false, false, null);
        //声明队列
        channel.queueDeclare(queueName, false, false, false, null);
        //建立一个绑定关系。
        channel.queueBind(queueName, exchangeName, routingKey);

        //durable 是否持久化。
        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
        //参数。队列名称 是否自动ACK Consumer
        //ACK 自动签收
        channel.basicConsume(queueName, true, queueingConsumer);
        //循环获取消息
        while (true) {
            //获取消息。如果没有消息。这一步将会阻塞
            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
            String s = new String(delivery.getBody());
            System.out.println("收到消息：" + s);
        }
    }
}
