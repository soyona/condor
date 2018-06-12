package sample.rmi.client;

import sample.rmi.bean.Order;
import sample.rmi.service.OrderService;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * @author soyona
 * @Package sample.rmi.client
 * @Desc:
 * @date 2018/6/12 14:32
 */
public class Client {

    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException, NamingException {
        Order order = new Order();
        order.setNo("20160904");
        order.setMoney(999.3);
        /**
         * 非序列化
         */
        order.setStatus(100);
        //获取服务1
        OrderService orderService = (OrderService) Naming.lookup("order");
        if(orderService != null){
            System.out.println(
                    orderService.getOrderNo(order)
            );
            System.out.println(
                    orderService.getMoney(order)
            );
            System.out.println(
                    orderService.getStatus(order)
            );

            System.out.println(
                    orderService.getOrderByNo("20150304")
            );
        }
        System.out.println("==========");

        //获取服务2
        Context namingContext = new InitialContext();
        OrderService orderService2 = (OrderService)namingContext.lookup("rmi://localhost:1099/OrderService");
        if(orderService2 != null){
            System.out.println(
                    orderService2.getOrderNo(order)
            );
            System.out.println(
                    orderService2.getMoney(order)
            );
            System.out.println(
                    orderService2.getStatus(order)
            );
            System.out.println(
                    orderService2.getOrderByNo("20150304")
            );
        }

    }
}
