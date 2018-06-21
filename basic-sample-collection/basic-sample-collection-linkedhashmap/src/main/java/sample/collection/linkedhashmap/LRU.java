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
    public LRU(){
        this(16,0.75f,true);
    }
    private LRU(int initialCapacity,float loadFactor,boolean accessOrder){
        super(initialCapacity,loadFactor,accessOrder);
    }

    @Override
    protected boolean removeEldestEntry(Entry<K,V> eldest) {
        if(size()>60){
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        LRU<Character, Integer> lru = new LRU<Character, Integer>(16, 0.75f, true);

        String s = "abcdefghijkk";
        for (int i = 0; i < s.length(); i++) {
            lru.put(s.charAt(i), i);
        }
        System.out.println("LRU中key为h的Entry的值为： " + lru.get('h'));
        System.out.println("LRU的大小 ：" + lru.size());
        System.out.println("LRU ：" + lru);
    }
}
