package sample.jcu.unsafe;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author soyona
 * @Package sample.jcu.unsafe
 * @Desc: Unsafe.copyMemory()
 * @date 2018/7/14 15:29
 */
public class CopyMemoryTest {
    @Test
    public void copyMemoryTest(){
        // allocate 4 byte
        long address = UnsafeUtils.unsafe.allocateMemory(4L);
        UnsafeUtils.unsafe.putInt(address,128);
        long address_1 = UnsafeUtils.unsafe.allocateMemory(4L);
        // look up the value at address_1
        System.out.println(UnsafeUtils.unsafe.getInt(address_1));
        // copyMemory,
        UnsafeUtils.unsafe.copyMemory(address,address_1,4L);
        // look up the value at address_1
        System.out.println(UnsafeUtils.unsafe.getInt(address_1));
        assertEquals(128,UnsafeUtils.unsafe.getInt(address_1));
    }
}
