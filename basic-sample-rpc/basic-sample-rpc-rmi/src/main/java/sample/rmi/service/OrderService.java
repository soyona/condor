package sample.rmi.service;

import sample.rmi.bean.Order;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author soyona
 * @Package sample.rmi.server
 * @Desc:
 * @date 2018/6/12 14:53
 */
public interface OrderService extends Remote{
    /**
     * 测试，客户端传递序列化对象
     * @param order
     * @return
     * @throws RemoteException
     */
    String getOrderNo(Order order)throws RemoteException;

    /**
     * 测试，客户端传递序列化对象
     * @param order
     * @return
     * @throws RemoteException
     */
    Double getMoney(Order order)throws RemoteException;

    /**
     * 测试，客户端传递序列化对象
     * @param order
     * @return
     * @throws RemoteException
     */
    int getStatus(Order order)throws RemoteException;


    /**
     * 测试，服务端传回序列化对象
     * @return
     * @throws RemoteException
     */
    Order getOrderByNo(String no)throws RemoteException;
}
