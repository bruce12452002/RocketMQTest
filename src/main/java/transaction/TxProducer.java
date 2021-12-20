package transaction;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import uitl.Common;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class TxProducer {
    public static void main(String[] args) throws Exception {
        new TxProducer().producer();
    }

    void producer() throws Exception {
        TransactionMQProducer producer = new TransactionMQProducer(Common.TRANSACTION_PRODUCER_GROUP.getValue());
        producer.setNamesrvAddr(Common.NAME_SERVER_ADDRESS.getValue());

        producer.setTransactionListener(new TransactionListener() {
            @Override
            public LocalTransactionState executeLocalTransaction(Message message, Object o) {
                if (Objects.equals(message.getTags(), "transaction_tag1")) {
                    return LocalTransactionState.COMMIT_MESSAGE;
                } else if (Objects.equals(message.getTags(), "transaction_tag2")) {
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }
                return LocalTransactionState.UNKNOW; // 會到下面的 checkLocalTransaction 方法
            }

            /**
             * broker 收到 LocalTransactionState.UNKNOW 才會執行這個方法
             */
            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
                System.out.println("checkLocalTransaction transaction tag=" + messageExt.getTags());
                return LocalTransactionState.COMMIT_MESSAGE;
            }
        });
        producer.start();

        Stream.iterate(1, i -> ++i).limit(3).forEach(i -> {
            Message message = new Message(Common.TRANSACTION_TOPIC.getValue(), "transaction_tag" + i, ("transaction" + i).getBytes());

            try {
                SendResult sendResult = producer.sendMessageInTransaction(message, null); // 第二個參數可以用在控制某一個 Message，null 表示全部控制
                SendStatus sendStatus = sendResult.getSendStatus();
                System.out.println("producer transaction status=" + sendStatus);
                System.out.println("producer transaction sendResult=" + sendResult);
            } catch (MQClientException e) {
                e.printStackTrace();
            }

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

//        producer.shutdown();
    }
}
