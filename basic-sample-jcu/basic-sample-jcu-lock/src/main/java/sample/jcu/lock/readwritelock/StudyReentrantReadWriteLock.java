package sample.jcu.lock.readwritelock;

/**
 * @author soyona
 * @Package sample.jcu.lock.readwritelock
 * @Desc:
 * @date 2018/7/10 10:48
 */
public class StudyReentrantReadWriteLock {


    static final int SHARED_SHIFT   = 16;
    static final int SHARED_UNIT    = (1 << SHARED_SHIFT);
    static final int MAX_COUNT      = (1 << SHARED_SHIFT) - 1;
    //16个1
    static final int EXCLUSIVE_MASK = (1 << SHARED_SHIFT) - 1;

    /** Returns the number of shared holds represented in count  */
    static int sharedCount(int c)    { return c >>> SHARED_SHIFT; }
    /** Returns the number of exclusive holds represented in count  */
    static int exclusiveCount(int c) { return c & EXCLUSIVE_MASK; }

    public static void main(String[] args) {
        System.out.println(sharedCount(2));
        System.out.println(exclusiveCount(4));
        System.out.println(SHARED_UNIT);
        //
        System.out.println(Integer.toBinaryString(EXCLUSIVE_MASK));

        int c = SHARED_UNIT;
        System.out.println(Integer.toBinaryString(c+SHARED_UNIT));
        System.out.println(sharedCount(c+SHARED_UNIT));
    }
}
