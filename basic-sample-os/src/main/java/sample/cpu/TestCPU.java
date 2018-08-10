package sample.cpu;

import org.junit.Test;

/**
 * @author soyona
 * @Package sample.cpu
 * @Desc:
 * @date 2018/8/1 12:19
 */
public class TestCPU {
    @Test
    public void test1(){
        // 缓存行 64 字节
        // int每个单位 4字节，
        // 数组长度：64 * 1024 * 1024
        // 数组大小共计：256 M
        int[] arr = new int[64 * 1024 * 1024];
        long start = System.currentTimeMillis();
        /**
         * 缓存行 64 字节
         * 步长为1，
         * 第一次访问：数组下标：0，字节为0， 第一个缓存行 0~63
         * 第二次访问：数组下标：1，第4个字节 第一个缓存行 0~63
         * 第三次访问：数组下标：2，第8个字节 第一个缓存行 0~63
         * ....
         * 第十六次访问：数组下标：16，第64个字节 第二个缓存行 64~127
         * ...
         *
         */
        for (int i = 0; i < arr.length ; i++) {
            arr[i] *= 3;
        }
        System.out.println("耗时："+(System.currentTimeMillis() - start));
    }


    @Test
    public void test2(){
        // 缓存行 64 字节
        // int每个单位 4字节，
        // 数组长度：64 * 1024 * 1024
        // 数组大小共计：256 M
        int[] arr = new int[64 * 1024 * 1024];
        long start = System.currentTimeMillis();
        /**
         * 缓存行 64 字节
         * 步长为16，
         * 第一次访问：数组下标：0，字节为0， 第一个缓存行
         * 第二次访问：数组下标：16，第64个字节 第二个缓存行
         * 第三次访问：数组下标：32，第128个字节 第三个缓存行
         */
        for (int i = 0; i < arr.length ; i += 16) {
            arr[i] *= 3;
        }
        System.out.println("耗时："+(System.currentTimeMillis() - start));
    }

    public static void testK(int k){
        // 缓存行 64 字节
        // int每个单位 4字节，
        // 数组长度：64 * 1024 * 1024
        // 数组大小共计：256 M
        int[] arr = new int[64 * 1024 * 1024];
        long start = System.currentTimeMillis();
        for (int i = 0; i < arr.length ; i += k) {
            arr[i] *= 3;
        }
        System.out.println("k= "+k+" 耗时："+(System.currentTimeMillis() - start));
    }
    @Test
    public void testK(){
        for(int i=1 ;i< 64*1024*1024;i *= 2){
            testK(i);
        }
    }
}
