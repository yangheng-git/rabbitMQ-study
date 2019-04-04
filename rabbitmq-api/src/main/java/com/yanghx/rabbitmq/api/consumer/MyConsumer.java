package com.yanghx.rabbitmq.api.consumer;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

/**
 * 自定义消息监听
 *
 * @author yangHX
 * createTime  2019/3/21 23:09
 */
public class MyConsumer extends DefaultConsumer {


    public MyConsumer(Channel channel) {
        super(channel);
    }


    /**
     * @param consumerTag 消费标记
     * @param envelope    里面重要消息
     * @param properties  配置信息
     * @param body        消息体
     * @throws IOException ex
     */
    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        System.err.println("___________consumer message____________________");
        System.err.println("consumerTag        : " + consumerTag);
        System.err.println("envelope           : " + envelope);
        System.err.println("properties         : " + properties);
        System.err.println("body               : " + new String(body));
        ;
    }
}
