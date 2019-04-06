package com.yanghx.rabbitmqspringcloutstreamproducer.steam;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 消息的发送者
 *
 * @author yangHX
 * createTime  2019/4/6 23:02
 */
@EnableBinding(Barista.class)
@Service
public class RabbitMqSender {

    @Resource
    private Barista barista;


    /**
     * 发送消息
     *
     * @param message    消息对象
     * @param properties 配置信息
     * @return String
     */
    public String sendMessage(Object message, Map<String, Object> properties) {
        try {
            MessageHeaders messageHeaders = new MessageHeaders(properties);
            Message<Object> mes = MessageBuilder.createMessage(message, messageHeaders);
            boolean send = barista.logoutput().send(mes);
            System.err.println("-------------------sending--------------------------");
            System.err.println("发送数据：" + message + " ,sendStatus: " + send);
        } catch (Exception e) {
            System.err.println("-------------------sending  error--------------------------");
            e.printStackTrace();
        }
        return null;
    }
}
