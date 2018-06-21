package sample.collection.linkedhashmap;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * @author soyona
 * @Package sample.collection.linkedhashmap
 * @Desc:
 * @date 2018/5/23 09:30
 */
public class LinkedHashMapDemo {
    public static void main(String[] args) {
        LinkedHashMap lmap = new LinkedHashMap();
        String s = "kabcdefghijkk";
        for (int i = 0; i < s.length(); i++) {
            lmap.put(s.charAt(i), i);
        }
        System.out.println(lmap);


        HashMap map = new HashMap();
        for (int i = 0; i < s.length(); i++) {
            map.put(s.charAt(i), i);
        }
        System.out.println(map);

    }



}
