package sample.rmi.service;

import sample.rmi.bean.Order;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author soyona
 * @Package sample.rmi.server
 * @Desc:
 * @date 2018/6/12 14:54
 */
public class OrderServiceImpl extends UnicastRemoteObject implements OrderService {

    public OrderServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public String getOrderNo(Order order) throws RemoteException {

        return order.getNo();
    }

    @Override
    public Double getMoney(Order order) throws RemoteException {
        return order.getMoney()*0.5f;
    }

    @Override
    public int getStatus(Order order) throws RemoteException {
        return order.getStatus();
    }

    @Override
    public Order getOrderByNo(String no) throws RemoteException {
        Order order = new Order();
        order.setNo(no);
        order.setStatus(999);
        order.setMoney(87.8);
        return order;
    }
}
