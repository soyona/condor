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
 *
 * 首次添加元素时，map.put("1","hello");
 * table.capacity=4
 * thredhold=3
 * next_size=4<<1 =8
 * map.size=1
 *
 *扩容时机：
 *   if (++size > threshold)
        resize();
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

        //场景一：
        //第一步：初始化HashMap initialCapacity=3
        //initialCapacity=3，经过tableSizeFor(initialCapacity)之后获取最近的2的幂次的 数组长度，为4
        //thredhold初始化为initialCapacity，为3
        Map map = new HashMap(3);
//        printField("loadFactor",map);
        System.out.println("初始化HashMap时：===============");
        printField("threshold",map);
        System.out.println("map.size()="+map.size());
        printTableLength("table",map);

        //第二步：put第一个元素后
        for (int i = 1; i <50 ; i++) {
            map.put("i"+i,"kanglei");
            if(i==1){
                System.out.println("put第1个元素之后：发生resize(),之后改变threshold的值");
            }
            System.out.println("put第"+i+"个元素之后：===============");
            printField("threshold",map);
            System.out.println("map.size()="+map.size());
            printTableLength("table",map);
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

    /**
     * 获取HashMap.Node[]数组长度
     * @param fieldName
     * @param object
     */
    public static void printTableLength(String fieldName,Object object){
        try {
            Field table= getField(fieldName,object);
            Object o = table.get(object);
            if(o!=null){
                Object[] o_ = (Object[]) o;
                System.out.println("数组长度="+o_.length);
            }else{
                System.out.println("数组长度=0");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public static Field getField(String fieldName,Object object) throws IllegalAccessException {
        Field tmp = null;
        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if(field.getName().equals(fieldName)){
               tmp =  field;
               break;
            }
        }
        return tmp;
    }

}
