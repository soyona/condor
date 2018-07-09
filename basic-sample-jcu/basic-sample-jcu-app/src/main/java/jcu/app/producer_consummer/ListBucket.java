package jcu.app.producer_consummer;

import java.util.*;

/**
 * @author soyona
 * @Package jcu.app.producer_consummer
 * @Desc:  list 桶，模拟同步队列
 * @date 2018/7/9 19:05
 */
public class ListBucket{
    private LinkedList<String> list = new LinkedList<String>();
    public static final int MAX_SIZE=10;

    public synchronized void get() throws InterruptedException {
        while(list.size() == 0){
            System.out.println("list is empty. get() wait-"+Thread.currentThread().getId());
            wait();
            System.out.println("get() wait end.-"+Thread.currentThread().getId());
        }
        System.out.println("获取："+list.poll());
        notifyAll();
    }
    public synchronized void put() throws InterruptedException {
        while(list.size() == MAX_SIZE){
            System.out.println("list is full.put() wait.-"+ Thread.currentThread().getId());
            wait();
            System.out.println("put() wait end.-"+Thread.currentThread().getId());
        }
        String e = String.valueOf(new Random().nextInt(100));
        list.push(e);
        System.out.println("放入："+e);
        notifyAll();
    }


}
