package sample.jcu.unsafe;

import org.junit.Test;

/**
 * @author soyona
 * @Package sample.jcu.unsafe
 * @Desc: 申请Integer.MAX_VALUE内存，VM 还是 direct byte buffers
 * @date 2018/7/14 12:49
 */
public class ArraySizeTest {

    /**
     * allocate an array that has more than Integer.MAX_VALUE entries.
     *
     * You will get the exception: java.lang.OutOfMemoryError: Requested array size exceeds VM limit
     */
    @Test
    public void arrayInVMTest(){
        // 2的31次幂-1，1Byte=8bit，1KByte = 1024Byte = 2的10次Byte
        // 1MByte = 1024 * 1KByte = 2的20次Byte
        // 1GByte = 1024 * 1MByte = 2的30次Byte
        // 2GByte = 1GByte * 2    = 2的31次Byte
        // Integer.MAX_VALUE
        long unit = 1L;
        long int_max = (unit << 31) -1;
        System.out.println(int_max);
        System.out.println(Integer.MAX_VALUE);
        // 大约2G-1内存，在堆中无法创建
        byte[] strings = new byte[Integer.MAX_VALUE];
    }
}
