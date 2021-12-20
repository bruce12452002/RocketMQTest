package order;

import java.util.ArrayList;
import java.util.List;

public class OrderTestData {
    public static void main(String[] args) {
        List<OrderTestData> testList = OrderTestData.getTestList();
        testList.forEach(f -> System.out.println(f.getOrderId() + f.getItem()));
    }

    private int orderId;
    private String item;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    static List<OrderTestData> getTestList() {
        OrderTestData o1 = new OrderTestData();
        OrderTestData o2 = new OrderTestData();
        OrderTestData o3 = new OrderTestData();
        List<OrderTestData> rtn = new ArrayList<>();

        o1.setOrderId(111);
        o1.setItem("one");
        o2.setOrderId(222);
        o2.setItem("one");
        o3.setOrderId(333);
        o3.setItem("one");
        rtn.add(o3);
        rtn.add(o1);
        rtn.add(o2);

        o1 = new OrderTestData();
        o2 = new OrderTestData();
        o3 = new OrderTestData();
        o1.setOrderId(111);
        o1.setItem("two");
        o2.setOrderId(222);
        o2.setItem("two");
        o3.setOrderId(333);
        o3.setItem("two");
        rtn.add(o1);
        rtn.add(o2);
        rtn.add(o3);

        o1 = new OrderTestData();
        o2 = new OrderTestData();
        o3 = new OrderTestData();
        o1.setOrderId(111);
        o1.setItem("three");
        o2.setOrderId(222);
        o2.setItem("three");
        o3.setOrderId(333);
        o3.setItem("three");
        rtn.add(o3);
        rtn.add(o2);
        rtn.add(o1);

        return rtn;
    }
}
