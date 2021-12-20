package filter.tag;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import uitl.Common;

import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class FilterProducer {
    public static void main(String[] args) throws Exception {
        new FilterProducer().producer();
    }

    void producer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer(Common.FILTER_PRODUCER_GROUP.getValue());
        producer.setNamesrvAddr(Common.NAME_SERVER_ADDRESS.getValue());
        producer.start();

        Stream.iterate(0, i -> ++i).limit(10).forEach(i -> {
            Message message = new Message(Common.FILTER_TOPIC.getValue(), "filter_tag", ("hohoho" + i).getBytes());

            try {
                SendResult sendResult = producer.send(message);
                SendStatus sendStatus = sendResult.getSendStatus();
                System.out.println("producer filter tag status=" + sendStatus);
                System.out.println("producer filter tag sendResult=" + sendResult);
            } catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
                e.printStackTrace();
            }

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        producer.shutdown();
    }
}
