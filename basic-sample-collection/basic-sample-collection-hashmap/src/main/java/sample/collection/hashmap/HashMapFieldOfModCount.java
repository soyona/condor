package sample.collection.hashmap;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * @author soyona
 * @Package sample.collection.hashmap
 * @Desc: modCount,结构更改的次数，具体指put，remove操作
 * @date 2018/4/26 12:14
 */
public class HashMapFieldOfModCount {
    /**
     * The number of times this HashMap has been structurally modified
     * Structural modifications are those that change the number of mappings in
     * the HashMap or otherwise modify its internal structure (e.g.,
     * rehash).  This field is used to make iterators on Collection-views of
     * the HashMap fail-fast.  (See ConcurrentModificationException).
     */
    transient int modCount;

    public static void main(String[] args) throws IllegalAccessException {
        HashMap map = new HashMap();

        //10次put
        for (int i = 0; i <10 ; i++) {
            map.put("key"+i,"value"+i);
            printField("modCount",map);
        }
        //5次remove
        for (int i = 0; i <5 ; i++) {
            map.remove("key"+i);
            printField("modCount",map);
        }

        //共计15次
        printField("modCount",map);

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
