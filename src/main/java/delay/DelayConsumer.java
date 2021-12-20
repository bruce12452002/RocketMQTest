package delay;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import uitl.Common;

import java.util.List;

public class DelayConsumer {
    public static void main(String[] args) throws Exception {
        new DelayConsumer().consumer();
    }

    void consumer() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(Common.DELAY_CONSUMER_GROUP.getValue());
        consumer.setNamesrvAddr(Common.NAME_SERVER_ADDRESS.getValue());
        consumer.subscribe(Common.DELAY_TOPIC.getValue(), "*");

        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                for (MessageExt msg : list) {
                    System.out.println("consumer delay msg id=" + msg.getMsgId());
                    System.out.println("consumer delay time=" + (System.currentTimeMillis() - msg.getStoreTimestamp()));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        consumer.start();
        System.out.println("delay consumer start");
    }
}
