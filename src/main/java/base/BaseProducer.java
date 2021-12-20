package base;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import uitl.Common;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class BaseProducer {
    public static void main(String[] args) throws Exception {
        new BaseProducer().producerSendMany();
    }

    void producerSendOne() throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQProducer producer = startProducer();

        Message message = new Message(Common.BASE_TOPIC.getValue(), "base_topic producerSendOne".getBytes());
        SendResult result = producer.send(message);
        System.out.println("producerSendOne 發送成功=" + result);
        producer.shutdown();
        System.out.println("producerSendOne 結束");
    }

    void producerSendMany() throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQProducer producer = startProducer();

        List<Message> list = new ArrayList<>();
        Stream.iterate(1, i -> ++i).limit(3).forEach(i ->
                list.add(new Message(Common.BASE_TOPIC.getValue(), ("base_topic producerSendMany" + i).getBytes()))
        );

        SendResult result = producer.send(list);
        System.out.println("producerSendMany 發送成功=" + result);
        producer.shutdown();
        System.out.println("producerSendMany 結束");
    }

    void producerSendCallBack() throws MQClientException, RemotingException, InterruptedException {
        DefaultMQProducer producer = startProducer();

        Message message = new Message(Common.BASE_TOPIC.getValue(), "base_topic producerSendCallBack".getBytes());
        producer.send(message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("producerSendCallBack 發送成功=" + sendResult);
            }

            @Override
            public void onException(Throwable e) {
                e.printStackTrace();
                System.out.println("producerSendCallBack 發送失敗=" + e.getMessage());
            }
        });

        TimeUnit.SECONDS.sleep(1); // 有可能還沒發送出去就關閉了，所以先睡一下
        producer.shutdown();
        System.out.println("producerSendCallBack 結束");
    }

    void producerSendOneWay() throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQProducer producer = startProducer();

        Message message = new Message(Common.BASE_TOPIC.getValue(), "base_topic producerSendOneWay".getBytes());
        producer.sendOneway(message); // sendOneway 像 UDP，只送出去而已
        System.out.println("producerSendOneWay 發送出去了");
        producer.shutdown();
        System.out.println("producerSendOneWay 結束");
    }

    private DefaultMQProducer startProducer() throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer(Common.BASE_PRODUCER_GROUP.getValue());
        producer.setNamesrvAddr(Common.NAME_SERVER_ADDRESS.getValue());
        producer.start();
        return producer;
    }
}
