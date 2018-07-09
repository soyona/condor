package sample.jcu.future;//package juc.future;
//
//import sun.nio.ch.ThreadPool;
//
//import java.util.concurrent.*;
//
///**
// * Created by kanglei on 20/10/2017.
// */
//public class StudyFuture {
//    static class MyCallable implements Callable{
//        @Override
//        public Object call() throws Exception {
//            TimeUnit.SECONDS.sleep(5);
//            return "CALL ME.";
//        }
//    }
//    public static void main(String[] args) {
//        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1,1,5, TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(2));
//        Future future = threadPoolExecutor.submit(new MyCallable());
//        try {
//            System.out.println(future.get());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//        threadPoolExecutor.shutdown();//线程池等待任务完成后 关闭
//    }
//
//
//
//}
