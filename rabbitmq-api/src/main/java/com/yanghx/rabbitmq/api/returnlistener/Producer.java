package com.yanghx.rabbitmq.api.returnlistener;

import com.rabbitmq.client.*;
import com.yanghx.rabbitmq.common.RabbitMqUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * return listener producer
 *
 * @author yangHX
 * createTime  2019/3/21 22:20
 */
public class Producer {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = RabbitMqUtil.getConnectionFactory();
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        String exchange = "test_return_exchange";
        String routingKey = "return.save";
        String routingKeyError = "abc.save";

        String msg = "Hello RabbitMQ Return Message";

        //return listener
        channel.addReturnListener(new ReturnListener() {
            @Override
            public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("------------handle return ---------------");
                System.err.println("replyCode   :" + replyCode);
                System.err.println("replyText   :" + replyText);
                System.err.println("exchange    :" + exchange);
                System.err.println("routingKey  :" + routingKey);
                System.err.println("properties  :" + properties);
                System.err.println("body        :" + new String(body));
            }
        });

///        channel.basicPublish(exchange, routingKey, true, null, msg.getBytes());
        ///如果消息没被接收。会被删除  mandatory
        channel.basicPublish(exchange, routingKeyError, true, null, msg.getBytes());


    }
}
