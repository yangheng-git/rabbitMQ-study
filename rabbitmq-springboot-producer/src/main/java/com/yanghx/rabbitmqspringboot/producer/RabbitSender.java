package com.yanghx.rabbitmqspringboot.producer;

import com.yanghx.rabbitmqspringboot.entity.Order;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 消息发送者
 *
 * @author yangHX
 * createTime  2019/4/6 15:48
 */
@Component
public class RabbitSender {
    /**
     * 自动注入RabbitTemplate模板类
     */
    @Resource
    private RabbitTemplate rabbitTemplate;


    /**
     * 回调函数: confirm确认
     */
    private final ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            System.err.println("correlationData: " + correlationData.getId());
            System.err.println("ack: " + ack);
            System.err.println("cause: " + cause);
            if (!ack) {
                System.err.println("异常处理....");
            }
        }
    };


    private final RabbitTemplate.ReturnCallback returnCallback = new RabbitTemplate.ReturnCallback() {
        @Override
        public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
            System.err.println("return exchange: " + exchange + ", routingKey: "
                    + routingKey + ", replyCode: " + replyCode + ", replyText: " + replyText);
        }
    };


    /**
     * 发送消息方法调用。构建message消息
     *
     * @param message    信息体
     * @param properties 消息头
     */
    public void send(Object message, Map<String, Object> properties) throws Exception {

        MessageHeaders messageHeaders = new MessageHeaders(properties);
        org.springframework.messaging.Message<Object> msg = MessageBuilder.createMessage(message, messageHeaders);
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);

        //id + 时间戳 全局唯一
        CorrelationData correlationData = new CorrelationData("123456789011");
        rabbitTemplate.convertAndSend("exchange-1", "springboot.abc", msg, correlationData);
    }


    /**
     * 发送消息方法调用。 构建自定义消息对象
     *
     * @param order 订单类实体
     */
    public void sendOrder(Order order) {
        rabbitTemplate.setReturnCallback(returnCallback);
        rabbitTemplate.setConfirmCallback(confirmCallback);
        //id + 时间戳 全局唯一
        CorrelationData correlationData = new CorrelationData("0987654321");
        rabbitTemplate.convertAndSend("exchange-2", "springboot.def", order, correlationData);
    }


}
