package sample.jcu.unsafe;


import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * @author soyona
 * @Package sample.jcu.unsafe
 * @Desc:
 * @date 2018/7/14 13:53
 */
public class DirectIntArrayTest {

    class  DirectIntArray{
        //Every entry size is :4byte
        private final static long INT_SIZE_IN_BYTES = 4;
        private final long startIndex;
        public DirectIntArray(long size) {
            // the native pointer
            startIndex = UnsafeUtils.unsafe.allocateMemory(size * INT_SIZE_IN_BYTES);
            // initialize to 0
            UnsafeUtils.unsafe.setMemory(startIndex, size * INT_SIZE_IN_BYTES, (byte) 0);
        }

        private long index(long offset) {
            return startIndex + offset * INT_SIZE_IN_BYTES;
        }

        public void setValue(long index, int value) {
            UnsafeUtils.unsafe.putInt(index(index), value);
        }

        public int getValue(long index) {
            return UnsafeUtils.unsafe.getInt(index(index));
        }

        public void destroy() {
            /**
             *Be aware of that directly allocated memory is always native memory and therefore not garbage collected.
             You therefore have to free memory explictly by a call to Unsafe#freeMemory(long).

             */
            UnsafeUtils.unsafe.freeMemory(startIndex);
        }
    }

    @Test
    public void testDirectIntArray(){
        //allocate 2GB * 4 memory,
        long maximum = Integer.MAX_VALUE + 1L;
        DirectIntArray directIntArray = new DirectIntArray(maximum);
        directIntArray.setValue(0L, 10);
        directIntArray.setValue(maximum, 20);
        directIntArray.setValue(maximum+1, 30);

        assertEquals(10, directIntArray.getValue(0L));
        assertEquals(20, directIntArray.getValue(maximum));

        assertEquals(30, directIntArray.getValue(maximum+1));
        directIntArray.destroy();
    }

}
