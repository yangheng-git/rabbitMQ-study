/**
 * 交换机模式 direct 直接模式。
 * <p>
 * 这种模式。用的也是比较多
 * <p>
 * 所有发送到Direct Exchange 的消息被转发到RouteKey中指定的Queue
 * >注意 : Direct模式可以使用RabbitMQ自带的Exchange :default Exchange 。
 * 所以不需要将Exchange进行任何绑定（binding）操作，消息传递时，RouteKey必须完全匹配才会被队列接收、否则该消息会被抛弃。
 * <p>
 */
package com.yanghx.rabbitmq.api.exchanges.direct;