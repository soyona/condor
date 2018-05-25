package sample.collection.hashmap;

/**
 * @author soyona
 * @Package sample.collection.hashmap
 * @Desc:  tableSizeFor 保证返回2的幂次
 * @date 2018/4/26 11:51
 */
public class HashMapMethodOfTableSizeFor {
    static final int MAXIMUM_CAPACITY = 1 << 30;
    /**
     * HashMap 之方法，
     * Returns a power of two size for the given target capacity.
     */
    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }
    public static void main(String[] args) {
        System.out.println(tableSizeFor(8));
    }
}
