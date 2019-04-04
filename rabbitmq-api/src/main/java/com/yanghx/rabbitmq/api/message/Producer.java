package com.yanghx.rabbitmq.api.message;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.yanghx.rabbitmq.common.RabbitMqUtil;

import java.io.IOException;
import java.util.HashMap;
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
        ConnectionFactory connectionFactory = RabbitMqUtil.getConnectionFactory();
        //2、 通过连接工厂创建连接
        Connection connection = connectionFactory.newConnection();
        //3、通过connection创建一个Channel  ：网络信道，几乎所有操作都在Channel中进行，是进行消息读写的通道。客户端可建立多个Channel，每个代表一个会话任务。
        Channel channel = connection.createChannel();

        HashMap<String, Object> headers = new HashMap<>();
        headers.put("my1", 1111);
        headers.put("my2", 2222);


        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                //常用模式【2: 持久化投递】
                .deliveryMode(2)
                .contentEncoding("utf-8")
                .expiration("10000")
                .headers(headers)
                .build();

        //4、通过channel发送数据
        String message = "hello rabbitMq!";
        //发送5条
        for (int i = 5; i > 0; i--) {
            //exchange  交换机 |routingKey 路由key |props 配置文件
            //不指定Exchange时，交换机默认是AMQP default，此时就看RoutingKey，RoutingKey要等于队列名才能被路由，否则消息会被删除。
            channel.basicPublish("", "test001", properties, message.getBytes());
        }

        //5、关闭连接
        channel.close();
        connection.close();

    }
}
