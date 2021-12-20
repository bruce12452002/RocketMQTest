package uitl;

public enum Common {
    // 記得要先啟動 name server 和 broker
    NAME_SERVER_ADDRESS("localhost:9876") // 有多個用「;」隔開

    , BASE_TOPIC("base_topic")
    , BASE_PRODUCER_GROUP("base_producer_group")
    , BASE_CONSUMER_GROUP("base_consumer_group")

    , DELAY_TOPIC("delay_topic")
    , DELAY_PRODUCER_GROUP("delay_producer_group")
    , DELAY_CONSUMER_GROUP("delay_consumer_group")

    , ORDER_TOPIC("order_topic")
    , ORDER_PRODUCER_GROUP("order_producer_group")
    , ORDER_CONSUMER_GROUP("order_consumer_group")

    , FILTER_TOPIC("filter_topic")
    , FILTER_PRODUCER_GROUP("filter_producer_group")
    , FILTER_CONSUMER_GROUP("filter_consumer_group")

    , TRANSACTION_TOPIC("transaction_topic")
    , TRANSACTION_PRODUCER_GROUP("transaction_producer_group")
    , TRANSACTION_CONSUMER_GROUP("transaction_consumer_group")

    ;


    private final String value;

    Common(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
