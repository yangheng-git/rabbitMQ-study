package com.yanghx.rabbitmqspringcloudstreamconsumer.stream;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author yangHX
 * createTime  2019/4/6 23:18
 */
@EnableBinding(Barista.class)
@Service
public class RabbitmqReceiver {

    @StreamListener(Barista.INPUT_CHANNEL)
    public void receiver(Message message) throws IOException {

        Channel channel = (Channel) message.getHeaders().get(AmqpHeaders.CHANNEL);
        long deliveryTag = (long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);

        System.err.println("Input Stream 1 接收到消息：" + message);
        System.out.println("消费完毕");
        channel.basicAck(deliveryTag, false);

    }

}
