package sample.jcu.lock.readwritelock;

import java.util.Random;

/**
 * @author soyona
 * @Package sample.jcu.lock.readwritelock
 * @Desc: 提供一个 场所
 * @date 2018/7/9 20:54
 */
public class School {
    static class Author implements Runnable{
        Paper paper;
        public Author(Paper paper){
            this.paper = paper;
        }
        @Override
        public void run() {
            System.out.println("我是作者："+Thread.currentThread().getId());
            for (int i = 0; i <5 ; i++) {
                paper.write();
            }
        }
    }

    static class Reader implements Runnable{
        Paper paper;
        public Reader(Paper paper){
            this.paper = paper;
        }

        @Override
        public void run() {
            System.out.println("我是读者："+Thread.currentThread().getId());
            for (int i = 0; i <10 ; i++) {
                paper.read();
                try {
                    Thread.sleep(new Random().nextInt(500));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void main(String[] args) {
        Paper paper = new Paper();
        Thread[] readers = new Thread[10];
        for (int i = 0; i <readers.length ; i++) {
            readers[i] = new Thread(new Reader(paper));
        }
        Thread author = new Thread(new Author(paper));

        // 开启读写操作
        for (int i = 0; i <readers.length ; i++) {
            readers[i].start();
        }
        author.start();



    }

}
