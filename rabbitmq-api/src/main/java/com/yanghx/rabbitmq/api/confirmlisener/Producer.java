package com.yanghx.rabbitmq.api.confirmlisener;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.yanghx.rabbitmq.common.RabbitMqUtil;

import java.io.IOException;

/**
 * 消息投递模式
 * confirmlisener 模式
 * 消息确认模式
 *
 * @author yangHX
 * createTime  2019/3/20 23:09
 */
public class Producer {

    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = RabbitMqUtil.getConnectionFactory();
        Connection connection = connectionFactory.newConnection();
        //通过collection获取一个新的channel
        Channel channel = connection.createChannel();


        //指定我们消息的投递模式 ： 消息的确认模式
        channel.confirmSelect();

        String exchangeName = "test_confirm_exchange";
        String routingKey = "confirmlisener.save";

        //发送一条消息
        String msg = "Hello RabbitMQ Send message";
        channel.basicPublish(exchangeName, routingKey, null, msg.getBytes());

        //添加一个确认监听
        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("------------ACK------------");
            }

            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("-------------NO ACK------------------");
            }
        });

    }

}
