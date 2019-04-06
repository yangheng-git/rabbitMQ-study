package com.yanghx.rabbitmqspring.convert;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

/**
 * spring amqp 消息转换器
 *
 * @author yangHX
 * createTime  2019/4/6 12:28
 */
public class TextMessageConverter implements MessageConverter {


    /**
     * 将数据转化为 message 类
     *
     * @param o                 要发送的数据
     * @param messageProperties 消息头
     * @return Message
     * @throws MessageConversionException ex
     */
    @Override
    public Message toMessage(Object o, MessageProperties messageProperties) throws MessageConversionException {
        return new Message(o.toString().getBytes(), messageProperties);
    }

    /**
     * 将message转换为想要的数据类型
     *
     * @param message message
     * @return Object
     * @throws MessageConversionException ex
     */
    @Override
    public Object fromMessage(Message message) throws MessageConversionException {

        String contentType = message.getMessageProperties().getContentType();
        if (null != contentType && contentType.contains("text")) {
            return new String(message.getBody());
        }
        return message.getBody();
    }
}
