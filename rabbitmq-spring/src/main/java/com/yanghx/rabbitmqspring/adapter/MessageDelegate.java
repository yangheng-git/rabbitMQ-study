package com.yanghx.rabbitmqspring.adapter;

/**
 * 通过`simpleMessageListenerContainer` 配置消息监听适配器。 指向这个类
 *
 * @author yangHX
 * createTime  2019/4/6 12:16
 */
public class MessageDelegate {


    /**
     * MessageListenerAdapter 默认指定接收消息的方法的名字就是 handleMessage .当然也可以手动设置
     *
     * @param messageBody message信息
     */
    public void handleMessage(byte[] messageBody) {
        System.err.println("默认方法，消息内容： " + new String(messageBody));
    }

    public void consumeMessage(byte[] messageBody) {
        System.err.println("字节数组方法, 消息内容:" + new String(messageBody));
    }

    public void consumeMessage(String messageBody) {
        System.err.println("字符串方法, 消息内容:" + messageBody);
    }

    public void method1(String messageBody) {
        System.err.println("method1 收到消息内容:" + new String(messageBody));
    }

    public void method2(String messageBody) {
        System.err.println("method2 收到消息内容:" + messageBody);
    }

}
