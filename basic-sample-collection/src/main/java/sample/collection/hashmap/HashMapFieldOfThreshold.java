package sample.collection.hashmap;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author soyona
 * @Package sample.collection.hashmap
 * @Desc:
 * 扩容门槛：threshold
 * 1、new HashMap()时， threshold=tableSizeFor(initialCapacity)
 * 2、threshold = (capacity * load factor)
 * 3、每次扩容时：
 *      capacity = old_capacity<<1,
 *      threshold = capacity * DEFAULT_LOAD_FACTOR;
 *
 * 场景一：
 * 构造HashMap时，Map map = new HashMap(3);
 * table.capacity=4
 * thredhold=4
 * next_size=4<<1 =8
 * map.size=0
 * 首次添加元素时，map.put("1","hello");
 * table.capacity=4
 * thredhold=3
 * next_size=4<<1 =8
 * map.size=1
 *
 *
 * @date 2018/4/26 12:14
 */
public class HashMapFieldOfThreshold {

    public static void main(String[] args) throws IllegalAccessException {
        /*
         *指定初始化initialCapacity=3
         *扩容门槛thredhold=tableSizeFor(3)
         *tableSizeFor(3)=4
         *
         */
        Map map = new HashMap(3);
//        printField("loadFactor",map);
        printField("threshold",map);

        map.put("1","Hello");//初次，发生resize(),之后改变threshold的值

//        printField("loadFactor",map);
        printField("threshold",map);

        for (int i = 0; i <50 ; i++) {
            map.put("1"+i,"Hello");
            System.out.println("Map.size="+map.size());
            printField("threshold",map);
        }

    }


    /**
     * 打印类的属性值
     * @param fieldName
     */
    public static void printField(String fieldName,Object object) throws IllegalAccessException {
        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if(field.getName().equals(fieldName)){
                System.out.println(field.getName() + ":" + field.get(object) );
            }
        }
    }

}
