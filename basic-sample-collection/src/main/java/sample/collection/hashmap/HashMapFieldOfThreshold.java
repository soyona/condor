package sample.collection.hashmap;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author soyona
 * @Package sample.collection.hashmap
 * @Desc:
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
        printField("loadFactor",map);
        printField("threshold",map);

        map.put("1","Hello");//初次，发生resize(),之后改变threshold的值

        printField("loadFactor",map);
        printField("threshold",map);

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
