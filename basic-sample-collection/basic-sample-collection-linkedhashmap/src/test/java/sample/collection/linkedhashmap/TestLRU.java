package sample.collection.linkedhashmap;

import org.junit.Before;
import org.junit.Test;

/**
 * @author soyona
 * @Package sample.collection.linkedhashmap
 * @Desc:
 * @date 2018/8/5 13:05
 */
public class TestLRU {
    LRU lru;

    @Before
    public void setUp(){
        lru = new LRU<Character, Integer>(10, 0.75f, true);
        String s = "abcdefgZZZhXYijkkfdafda";
        for (int i = 0; i < s.length(); i++) {
            lru.put(s.charAt(i), i);
        }
        System.out.println("Init LRU size;"+ lru.size());
        System.out.println("Init LRU："+ lru);
    }
    @Test
    public void testLRUSize(){
        System.out.println(""+lru.size());
        lru.put('X',100);
        System.out.println("添加元素后："+lru.size());
    }
    @Test
    /**
     * 测试访问元素 顺序改变
     */
    public void testLRUAccessOrder(){
        System.out.println("缓存数据："+lru);
        System.out.println("访问X元素后，顺序变为：");
        lru.get('X');
        System.out.println("缓存数据："+lru);
    }


}
