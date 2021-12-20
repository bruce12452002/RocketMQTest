package order;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import uitl.Common;

import java.util.List;

public class OrderProducer {
    public static void main(String[] args) throws Exception {
        new OrderProducer().producer();
    }

    void producer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer(Common.ORDER_PRODUCER_GROUP.getValue());
        producer.setNamesrvAddr(Common.NAME_SERVER_ADDRESS.getValue());
        producer.start();

        List<OrderTestData> testList = OrderTestData.getTestList();

        for (int i = 0; i < testList.size(); i++) {
            OrderTestData orderDetail = testList.get(i);
            Message message = new Message(Common.ORDER_TOPIC.getValue(), "order_tag", i + "", orderDetail.getItem().getBytes());

            SendResult sendResult = producer.send(message, new MessageQueueSelector() {
                /**
                 *
                 * @param list broker 裡的所有 MessageQueue
                 * @param message
                 * @param o
                 * @return
                 */
                @Override
                public MessageQueue select(List<MessageQueue> list, Message message, Object o) {
                    int orderId = (int) o;
                    return list.get(orderId % list.size());
                }
            }, orderDetail.getOrderId()); // orderDetail.getOrderId() 會傳到裡面的 select 方法的第三個參數
            System.out.println("order produce result=" + sendResult);
        }
        producer.shutdown();
    }
}
