package com.yanghx.rabbitmq.api.message;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * 消费者
 *
 * @author yangHX
 * createTime  2019/3/10 18:08
 */
public class Consumer {


    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        //1、创建一个ConnectionFactory并进行配置
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setPort(5672);
        connectionFactory.setHost("132.232.33.80");
        connectionFactory.setVirtualHost("/");

        //2、 通过连接工厂创建连接
        Connection connection = connectionFactory.newConnection();

        //3、通过connection创建一个Channel  ：网络信道，几乎所有操作都在Channel中进行，是进行消息读写的通道。客户端可建立多个Channel，每个代表一个会话任务。
        Channel channel = connection.createChannel();

        //4、创建一个队列 （声明）
        /*
        queueName: 队列名称
        durable:  持久化。true 即使服务重启也不会删除这个队列
        exclusive: 独占、 true 队列只能使用一个连接。连接断开，队列删除
        autoDelete: 自动删除： true 脱离了Exchange(连接断开) 即队列没有Exchange关联时。自动删除
        arguments : 扩展参数
        */

        String queueName = "test001";
        channel.queueDeclare(queueName, true, false, false, null);

        //5、创建消费者
        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);

        //6、设置channel
        channel.basicConsume(queueName, true, queueingConsumer);

        //7、获取消息
        while (true) {
            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
            String msg = new String(delivery.getBody());
            System.out.println("消费端：" + msg);

            AMQP.BasicProperties properties = delivery.getProperties();
            Map<String, Object> headers = delivery.getProperties().getHeaders();
            Object o = headers.get("my1");
            System.out.println(o);


        }


    }

}
