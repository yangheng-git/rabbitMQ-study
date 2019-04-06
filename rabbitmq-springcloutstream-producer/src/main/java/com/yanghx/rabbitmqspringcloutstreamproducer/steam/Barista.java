package com.yanghx.rabbitmqspringcloutstreamproducer.steam;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * 这里的Barista接口是定义来作为后面类的参数，这一接口定义来通道类型和通道名称
 * 通道名称是作为配置用，通道类型则决定了app会使用这一通道进行发送消息还是从中取消息
 *
 * @author yangHX
 * createTime  2019/4/6 22:51
 */
public interface Barista {

    /**
     * output 输出。 生产端进行输出操作
     */
    public static String OUTPUT_CHANNEL = "output_channel";

    /**
     * 注解@Iutput 声明了它是一个输入类型的通道。名字是Barista.IUTPUT_CHANNEL。 也就是position2的input_channel 。
     * 这一名字和上述app2的配置文件中positionl应该一致。表明了注入了一个名字叫input_channel的通道。他的类型是input.
     * 订阅他的主题是position2这个主题
     * <p>
     * <p>
     * 注解@Output声明了它是一个输出类型的通道。名字是output_channel. 这一名字与app1中通道名称一致。表明注入一个名为output_channel的通道
     * 类型是output 发布的主题是mydest.
     *
     * @return MessageChannel
     */
    @Output(Barista.OUTPUT_CHANNEL)
    MessageChannel logoutput();

}
