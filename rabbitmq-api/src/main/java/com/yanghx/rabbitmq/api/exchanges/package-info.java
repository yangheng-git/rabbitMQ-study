/*
 * 介绍 exchange 的各个类型。 type
 * <p>
 * <p>
 * direct  直接模式
 *          所有发送到Direct Exchange 的消息被转发到RouteKey中指定的Queue
 *          注意 : Direct模式可以使用RabbitMQ自带的Exchange :default Exchange 。
 *          所以不需要将Exchange进行任何绑定（binding）操作，消息传递时，RouteKey必须完全匹配才会被队列接收、否则该消息会被抛弃。
 * topic 订阅模式
 *          所有发布到Topic Exchange 的消息被转发到所有关心RouteKey中指定Topic的Queue 上。
 *
 *           exchange 将RouteKey和某Topic进行模糊匹配。此时队列需要绑定一个Topic
 *
 *  fanout
 *          不处理路由键。只需要简单的将队列绑定到交换机上
 *          发送到交换机的消息都会被转发到于该交换机绑定的所有队列上
 *          交换机转发消息是最快的
 *
 */
package com.yanghx.rabbitmq.api.exchanges;