package com.yanghx.rabbitmq.api.confirmlisener;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.yanghx.rabbitmq.common.RabbitMqUtil;

/**
 * 确认投递模式 消费者
 *
 * @author yangHX
 * createTime  2019/3/20 23:17
 */
public class Consumer {

    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = RabbitMqUtil.getConnectionFactory();
        Connection connection = connectionFactory.newConnection();

        Channel channel = connection.createChannel();

        String exchangeName = "test_confirm_exchange";
        String routingKey = "confirmlisener.#";
        String queueName = "test_confirm_queue";

        //声明交换机和队列。然后进行绑定设置。最后指定路由key
        channel.exchangeDeclare(exchangeName, "topic", true);
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);

        //创建消费者
        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, queueingConsumer);

        while (true) {
            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
            String msg = new String(delivery.getBody());
            System.out.println("消费端" + msg);
        }


    }
}
