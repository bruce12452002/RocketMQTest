package base;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import uitl.Common;

import java.util.List;

public class BaseConsumer {
    public static void main(String[] args) throws Exception {
        new BaseConsumer().consumer();
    }

    void consumer() throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(Common.BASE_CONSUMER_GROUP.getValue());
        consumer.setNamesrvAddr(Common.NAME_SERVER_ADDRESS.getValue());
        consumer.subscribe(Common.BASE_TOPIC.getValue(), "*");
        // consumer.setMessageModel(MessageModel.BROADCASTING); // 預設是 CLUSTERING，也就是其中一個消費者消費即可
        // CLUSTERING：只會有一個消費者群組消費
        // BROADCASTING：所有消費者群組都會消費
        // 如果一個設 CLUSTERING，另一個設 BROADCASTING，BROADCASTING 每次都會消費，CLUSTERING 就不一定了，
        // 只有在被分配時，比 BROADCASTING 還快，才會消費

        // consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET); // 預設是 CONSUME_FROM_LAST_OFFSET
        // CONSUME_FROM_LAST_OFFSET：表示從上次消費過的地方之後開始消費，如果是新消費者，那就表示沒有上次的 offset，所以會從頭開始消費
        // CONSUME_FROM_FIRST_OFFSET：表示從頭開始消費

        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                msgs.forEach(me -> System.out.println("消費內容=" + new String(me.getBody())));
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
        System.out.println("consumer 啟動");
//        consumer.shutdown();
    }
}
