package sample.thread.exchanger;

import java.util.concurrent.Exchanger;

/**
 * Created by kanglei on 20/10/2017.
 */
public class ExchangerDemo {


    public static void main(String[] args) {
        Exchanger exchanger = new Exchanger();
        ThreadA tA = new ThreadA(exchanger);
        ThreadB tB = new ThreadB(exchanger);
        tA.start();
        tB.start();


    }



   static class ThreadA extends Thread{
        Exchanger exchanger = null;
        public ThreadA(Exchanger exchanger){
            this.exchanger = exchanger;
            this.setName("A");
        }
        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName()+"="+ exchanger.exchange("Dog."));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static  class ThreadB extends Thread{
        Exchanger exchanger = null;
        public ThreadB(Exchanger exchanger){
            this.exchanger = exchanger;
            this.setName("B");
        }

        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName()+"="+ exchanger.exchange("Pig."));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }        }
    }
}
