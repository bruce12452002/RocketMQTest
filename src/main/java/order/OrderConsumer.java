package order;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import uitl.Common;

import java.util.List;

public class OrderConsumer {
    public static void main(String[] args) throws Exception {
        new OrderConsumer().consumer();
    }

    void consumer() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(Common.ORDER_CONSUMER_GROUP.getValue());
        consumer.setNamesrvAddr(Common.NAME_SERVER_ADDRESS.getValue());
        consumer.subscribe(Common.ORDER_TOPIC.getValue(), "*");
//        consumer.setMessageModel(MessageModel.BROADCASTING);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        consumer.registerMessageListener(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list, ConsumeOrderlyContext consumeOrderlyContext) {
                for (MessageExt msg : list) {
                    System.out.println("thread name=" + Thread.currentThread().getName() + " " + new String(msg.getBody()));
                }
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });

        consumer.start();
        System.out.println("order consumer start");
    }
}
