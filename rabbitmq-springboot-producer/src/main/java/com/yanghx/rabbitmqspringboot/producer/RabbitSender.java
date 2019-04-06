package com.yanghx.rabbitmqspringboot.producer;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 消息发送者
 *
 * @author yangHX
 * createTime  2019/4/6 15:48
 */
public class RabbitSender {
    /**
     * 自动注入RabbitTemplate模板类
     */
    @Resource
    private RabbitTemplate rabbitTemplate;


    /**
     * 回调函数 confirm确认模式
     */
    final ConfirmCallback confirmCallback = new ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {

            System.err.println("correlationData: " + correlationData);
            System.err.println("ack: " + ack);
            if (!ack) {
                System.out.println("-----异常处理");
            }
        }
    };


    final RabbitTemplate.ReturnCallback returnCallback = new RabbitTemplate.ReturnCallback() {
        @Override
        public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
            System.err.println("return exchange : " + exchange + " , routingKey : " + routingKey + " , replyCode:　" + replyCode + " , replyText: " + replyText);
        }
    };


    /**
     * 发送消息方法调用。构建message消息
     *
     * @param message    信息体
     * @param properties 消息头
     */
    public void send(Object message, Map<String, Object> properties) {
        MessageHeaders messageHeaders = new MessageHeaders(properties);
        Message msg = (Message) MessageBuilder.createMessage(message, messageHeaders);
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);

        //id + 时间戳 全局唯一
        CorrelationData correlationData = new CorrelationData("1234567890");
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
