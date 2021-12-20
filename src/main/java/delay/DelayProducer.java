package delay;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import uitl.Common;
import uitl.DelayTimeLevel;

import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class DelayProducer {
    public static void main(String[] args) throws Exception {
        new DelayProducer().producer();
    }

    void producer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer(Common.DELAY_PRODUCER_GROUP.getValue());
        producer.setNamesrvAddr(Common.NAME_SERVER_ADDRESS.getValue());
        producer.start();

        Stream.iterate(0, i -> ++i).limit(3).forEach(i -> {
            Message message = new Message(Common.DELAY_TOPIC.getValue(), "delay_tag", ("hahaha" + i).getBytes());

            // 官方文件：https://github.com/apache/rocketmq/blob/master/docs/cn/RocketMQ_Example.md#35-%E5%BB%B6%E6%97%B6%E6%B6%88%E6%81%AF%E7%9A%84%E4%BD%BF%E7%94%A8%E9%99%90%E5%88%B6
            // 1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h, 3表示10秒，依此類推，沒有 enum 可用，只好自己寫
            message.setDelayTimeLevel(DelayTimeLevel.SECOND_10.getLevel());
            try {
                SendResult sendResult = producer.send(message);
                SendStatus sendStatus = sendResult.getSendStatus();
                System.out.println("producer delay status=" + sendStatus);
                System.out.println("producer delay sendResult=" + sendResult);
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
