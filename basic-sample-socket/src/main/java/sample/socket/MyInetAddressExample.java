package sample.socket;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * Created by kanglei on 08/07/2017.
 */
public class MyInetAddressExample {
     public    static void getNetWorkInterface(){
            try {
                Enumeration<NetworkInterface> interfaceList= NetworkInterface.getNetworkInterfaces();
                if(interfaceList == null){
                    System.out.println("No NetworkINterface found!");
                }else{
                    while(interfaceList.hasMoreElements()){
                        NetworkInterface networkInterface = interfaceList.nextElement();
                        System.out.println("Interface:"+networkInterface.getName()+",-"+networkInterface.getDisplayName()
                        +","+networkInterface.getInetAddresses()+","+networkInterface.getHardwareAddress());
                    }
                }

            } catch (SocketException e) {
                e.printStackTrace();
            }
        }

        //www.shopin.net/122.115.41.8
        public static void getInetAddress(){
            try {
                InetAddress address =  InetAddress.getByName("www.shopin.net");
                System.out.println(address);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }

    //www.shopin.net/122.115.41.8
    public static void getAllInetAddress(){
        try {
            InetAddress[] addresses =  InetAddress.getAllByName("www.oreilly.com");
            for (InetAddress address: addresses) {
                System.out.println(address);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
        public static void main(String[] args){
            MyInetAddressExample.getInetAddress();
            MyInetAddressExample.getAllInetAddress();
        }

}
