package com.yanghx.rabbitmq.api.quickstart;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 生产者
 *
 * @author yangHX
 * createTime  2019/3/10 18:06
 */
public class Producer {


    public static void main(String[] args) throws IOException, TimeoutException {
        //1、创建一个ConnectionFactory并进行配置
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("132.232.33.80");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        //2、 通过连接工厂创建连接
        Connection connection = connectionFactory.newConnection();
        //3、通过connection创建一个Channel  ：网络信道，几乎所有操作都在Channel中进行，是进行消息读写的通道。客户端可建立多个Channel，每个代表一个会话任务。
        Channel channel = connection.createChannel();
        //4、通过channel发送数据
        String message = "hello rabbitMq!";
        //发送5条
        for (int i = 5; i > 0; i--) {
            //exchange  交换机 |routingKey 路由key |props 配置文件
            //不指定Exchange时，交换机默认是AMQP default，此时就看RoutingKey，RoutingKey要等于队列名才能被路由，否则消息会被删除。
            channel.basicPublish("", "test001", null, message.getBytes());
        }

        //5、关闭连接
        channel.close();
        connection.close();

    }
}
