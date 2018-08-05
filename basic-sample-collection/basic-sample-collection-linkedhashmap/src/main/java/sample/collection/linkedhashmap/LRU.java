package sample.collection.linkedhashmap;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author soyona
 * @Package sample.collection.linkedhashmap
 * @Desc: LRU算法实现，LinkedHashMap的accessOrder设为true
 * @date 2018/6/11 16:12
 */
public class LRU<K,V> extends LinkedHashMap<K,V> implements Map<K,V>{

    //默认的缓存容量
    public static final int default_capacity = 10;
    //初始化容器大小
    public  static final int init_capacity = default_capacity;
    public int capacity = default_capacity;
    public LRU(){
        this(16,0.75f,true);
    }

    public LRU(int capacity){
        capacity = capacity;
    }
    public LRU(int capacity,float loadFactor,boolean accessOrder){
        super(init_capacity,loadFactor,accessOrder);
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Entry<K,V> eldest) {
        if(size() > capacity){
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<K, V> entry : entrySet()) {
            sb.append(String.format("%s:%s ", entry.getKey(), entry.getValue()));
        }
        return sb.toString();
    }


    public static void main(String[] args) {
        LRU<Character, Integer> lru = new LRU<Character, Integer>(10, 0.75f, true);

        String s = "abcdefgZZZhXYijkkfdafda";
        for (int i = 0; i < s.length(); i++) {
            lru.put(s.charAt(i), i);
        }
        System.out.println("LRU的大小 ：" + lru.size());
        System.out.println(lru);
        // 读取'X' 使得X 放到最后
        lru.get('X');
        System.out.println(lru);
        // 读取'h' 使得h 放到最后
        lru.get('h');
        System.out.println(lru);

    }
}
