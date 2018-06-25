package sample.serializable.jdk;

import java.io.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author soyona
 * @Package sample.serializable.jdk
 * @Desc:
 * @date 2018/6/22 17:38
 */
public class Demo {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        //序列化磁盘文件
        File file = new File("./test.out");
        //对象序列化到 文件 ./test.out
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        Order order = new Order();
        order.setCNO("SHOPIN");
        order.setOrderNo("20180625");
        order.setNeedSaleMoney(new BigDecimal(2.03));
        order.setStatus(1);
        Order.setPNO("TMALL");
        System.out.println("序列化对象信息。。"+order);
        oos.writeObject(order);
        oos.flush();
        oos.close();



        //对象反序列化
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);

        Order order_x = (Order)ois.readObject();
        ois.close();
        System.out.println("反序列化对象信息。。"+order_x);
    }
}
