# RMI是什么？
>1. RMI(Remote Method Invocation)，能让客户端Java虚拟机上的对象像d调用本地一样调用服务端Java虚拟机中对象的方法。
>2. RMI基于TCP/IP协议



# RMI 编码DEMO

## RMI服务类实现

### 1、OrderService接口
```text
 继承： 接口java.rmi.Remote，表示它的方法将被非本地虚拟机调用
       
 用途： 表明是远程服务
```
```java
public interface OrderService extends Remote{
    String getOderNo()throws RemoteException;
}

```


### 2、OrderServiceImpl实现类
```text
继承 java.rmi.server.UnicastRemoteObject
实现 OrderService接口
```
```java
public class OrderServiceImpl extends UnicastRemoteObject implements OrderService {

    private Order order;
    public OrderServiceImpl(Order order) throws RemoteException {
        super();
        this.order = order;
    }

    @Override
    public String getOderNo() throws RemoteException {
        return this.order.getNo();
    }
}
```

## RMI服务器实现

## RMI序列化类Order
```text
Order用于传递数据，
实现：java.io.Serializable
```
```java
public class Order implements Serializable{
    private String no;
    private Double money;
    /**
     * 非序列化
     */
    private transient int status;
}
```
