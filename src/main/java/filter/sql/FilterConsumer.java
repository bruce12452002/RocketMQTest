package filter.sql;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import uitl.Common;

import java.util.List;

/**
 * broker 在啟動時要加 -c 的參數，然後指定設定檔的路徑
 * 設定檔裡要有 enablePropertyFilter=true
 * 才不會報 The broker does not support consumer to filter message by SQL92
 */
public class FilterConsumer {
    public static void main(String[] args) throws Exception {
        new FilterConsumer().consumer();
    }

    void consumer() throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(Common.FILTER_CONSUMER_GROUP.getValue());
        consumer.setNamesrvAddr(Common.NAME_SERVER_ADDRESS.getValue());
        consumer.subscribe(Common.FILTER_TOPIC.getValue(), MessageSelector.bySql("xxx > 5"));

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
