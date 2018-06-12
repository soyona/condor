package sample.rmi.server;

import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;
import sample.rmi.bean.Order;
import sample.rmi.service.OrderService;
import sample.rmi.service.OrderServiceImpl;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * @author soyona
 * @Package sample.rmi.server
 * @Desc:
 * @date 2018/6/12 14:17
 */
public class Startup {
    public static void main(String[] args) {
        OrderService orderService = null;
        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(1099);
            orderService = new OrderServiceImpl();
            //服务注册
            registry.rebind("order",orderService);
            //服务注册
            Context namingContext = new InitialContext();
            namingContext.rebind("rmi://localhost:1099/OrderService",orderService);

            System.out.println("Server is ready..");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
}
