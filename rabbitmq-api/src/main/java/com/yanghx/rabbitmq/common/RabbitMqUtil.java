package com.yanghx.rabbitmq.common;

import com.rabbitmq.client.ConnectionFactory;

/**
 * @author yangHX
 * createTime  2019/3/10 22:05
 */
public class RabbitMqUtil {

    public static ConnectionFactory getConnectionFactory() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("132.232.33.80");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        return connectionFactory;
    }
}
