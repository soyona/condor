package sample.collection.hashmap;

/**
 * @author soyona
 * @Package sample.collection.hashmap
 * @Desc: 比较两个版本重新哈希的结果
 * @date 2018/4/26 17:18
 */
public class HashMapMethodOfhash {

    /**
     * A randomizing value associated with this instance that is applied to
     * hash code of keys to make hash collisions harder to find. If 0 then
     * alternative hashing is disabled.
     */
    transient static int hashSeed = 0;
    /*
      * @desc: JDK7 rehash
     * @author: soyona
     * @date: 2018/4/26 17:19
     * @param: [h]
     * @return: int
     */
    /**
     * Retrieve object hash code and applies a supplemental hash function to the
     * result hash, which defends against poor quality hash functions.  This is
     * critical because HashMap uses power-of-two length hash tables, that
     * otherwise encounter collisions for hashCodes that do not differ
     * in lower bits. Note: Null keys always map to hash 0, thus index 0.
     */
    final static int hash7(Object k) {
        int h = hashSeed;
//        if (0 != h && k instanceof String) {
//            return sun.misc.Hashing.stringHash32((String) k);
//        }

        h ^= k.hashCode();

        // This function ensures that hashCodes that differ only by
        // constant multiples at each bit position have a bounded
        // number of collisions (approximately 8 at default load factor).
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }

    /*
     * @desc: JDK8 rehash
     *
     * key的哈希值h 与 h的高16位  ^
     *
     * @author: soyona
     * @date: 2018/4/26 17:19
     * @param: [key]
     * @return: int
     */
    static final int hash8(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }


    public static void main(String[] args) {
        Object key = 9988777;
        System.out.println("JDK7 重新哈希："+hash7(key));
        System.out.println("JDK8 重新哈希："+hash8(key));

        //100110000110101010101001
        System.out.println(Integer.toBinaryString(key.hashCode()));
        //000000000000000010011000
        System.out.println(Integer.toBinaryString(key.hashCode()>>>16));
        //100110000110101000110001
        System.out.println(Integer.toBinaryString(key.hashCode() ^ (key.hashCode()>>>16)) );




    }
}
