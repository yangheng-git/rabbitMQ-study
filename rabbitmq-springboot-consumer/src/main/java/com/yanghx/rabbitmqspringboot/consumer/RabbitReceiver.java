package com.yanghx.rabbitmqspringboot.consumer;

import com.rabbitmq.client.Channel;
import com.yanghx.rabbitmqspringboot.entity.Order;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * 消息接收者
 *
 * @author yangHX
 * createTime  2019/4/6 18:56
 */
@Component
public class RabbitReceiver {


    /**
     * 使用注解的形式来声明。指定 queue  exchange 路由key 和进行绑定
     *
     * @param message 消息
     * @param channel 连接
     * @throws IOException ex
     */
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "queue-1", durable = "true")
                    , exchange = @Exchange(value = "exchange-1", durable = "true", type = "topic", ignoreDeclarationExceptions = "true")
                    , key = "springboot.*"
            )
    )
    @RabbitHandler
    public void onMessage(Message message, Channel channel) throws IOException {
        System.err.println("--------------------");
        System.err.println("消费端payLoad: " + message.getPayload());
        long deliveryTag = (Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
        //手工ack
        channel.basicAck(deliveryTag, false);
    }


    /**
     * 指定实体进行接收消息。 自动将Message对象解析为指定的数据
     * <p>
     * 将注解中的参数放入配置中
     * spring.rabbitmq.listener.order.queue.name=queue-2
     * spring.rabbitmq.listener.order.queue.durable=true
     * spring.rabbitmq.listener.order.exchange.name=exchange-1
     * spring.rabbitmq.listener.order.exchange.durable=true
     * spring.rabbitmq.listener.order.exchange.type=topic
     * spring.rabbitmq.listener.order.exchange.ignoreDeclarationExceptions=true
     * spring.rabbitmq.listener.order.key=springboot.*
     *
     * @param order   message对象中的内容。消息体对应的实体
     * @param channel 连接
     * @param headers message对象中的请求头
     * @throws IOException ex
     */


    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(
                            value = "${spring.rabbitmq.listener.order.queue.name}"
                            , durable = "${spring.rabbitmq.listener.order.queue.durable}"
                    ),
                    exchange = @Exchange(
                            value = "${spring.rabbitmq.listener.order.exchange.name}"
                            , durable = "${spring.rabbitmq.listener.order.exchange.durable}"
                            , type = "${spring.rabbitmq.listener.order.exchange.type}"
                            , ignoreDeclarationExceptions = "${spring.rabbitmq.listener.order.exchange.ignoreDeclarationExceptions}"
                    )
                    ,
                    key = "${spring.rabbitmq.listener.order.key}"
            )
    )
    @RabbitHandler
    public void onOrderMessage(
            @Payload Order order
            , Channel channel
            , @Headers Map<String, Object> headers
    ) throws IOException {

        System.err.println("--------------------------------------");
        System.err.println("消费端 order: " + order.getId());
        long deliveryTag = (long) headers.get(AmqpHeaders.DELIVERY_TAG);
        channel.basicAck(deliveryTag, false);


    }

}
