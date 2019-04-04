package com.yanghx.rabbitmq.api.limit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.yanghx.rabbitmq.common.RabbitMqUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author yangHX
 * createTime  2019/3/27 22:06
 */
public class Consumer {


    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = RabbitMqUtil.getConnectionFactory();
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        String exchangeName = "test_qos_exchange";
        String queueName = "test_qos_queue";
        String routingKey = "qos.#";

        channel.exchangeDeclare(exchangeName, "topic", true, false, null);
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);


        //限流方式 第一件事就是将 autoAck 设置为false

        /**
         * @param prefetchSize  消息大小限制 一般为0 不限制
         * @param prefetchCount 一次最多处理消息个数
         *        会告诉RabbitMQ不要同时给一个消费者推送多余N个消息，即一旦有N个消息还没有ack,则该consumer将block掉。直到有消息ack
         * @param global       true/false 是否将上面设置应用于channel。
         *                                简单说。就是上面限制是channel级别还是consumer级别。
         */
        channel.basicQos(0, 1, false);

        channel.basicConsume(queueName, false, new MyConsumer(channel));


    }
}
