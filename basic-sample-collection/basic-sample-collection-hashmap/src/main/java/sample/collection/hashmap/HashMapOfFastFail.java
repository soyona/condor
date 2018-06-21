package sample.collection.hashmap;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author soyona
 * @Package sample.collection.hashmap
 * @Desc: 阐述 HashMap的fast-fail机制是如何运行
 * @date 2018/4/27 11:23
 */
public class HashMapOfFastFail {
    public static int INIT_SIZE=10;

    public static void main(String[] args) throws Exception {
        Map map = new HashMap();
        for (int i = 0; i <INIT_SIZE ; i++) {
            map.put("key-"+i,"value="+i);
        }

        //map.keySet().iterator()，return new KeyIterator() 每次创建一个KeyIterator实例
        Iterator<String> iterator = map.keySet().iterator();

        printField("modCount",map);
        printSuperField("expectedModCount",iterator);
        System.out.println("===================");
        while(iterator.hasNext()){
            try {
                String key = iterator.next();
                //iterator中的expectedModCount = INIT_SIZE
                map.remove("key-"+5);
                //删除之后modCount=11，    此时，expectedModCount!=modCount 因此抛出异常
            }catch (Exception e){
                //打印11
                printField("modCount",map);
                //打印10，属性expectedModCount HashIterator属性，KeyIterator extends HashIterator，所以此处获取父类的属性expectedModCount
                printSuperField("expectedModCount",iterator);
                e.printStackTrace();
                throw new Exception(e.getMessage());
            }
        }
    }


    /**
     * 打印类的属性值
     * @param fieldName
     */
    public static void printField(String fieldName,Object object){
        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if(field.getName().equals(fieldName)){
                try {
                    System.out.println(field.getName() + ":" + field.get(object) );
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 打印父类的属性值
     * @param fieldName
     */
    public static void printSuperField(String fieldName,Object object){
        for (Field field : object.getClass().getSuperclass().getDeclaredFields()) {
            field.setAccessible(true);
            if(field.getName().equals(fieldName)){
                try {
                    System.out.println(field.getName() + ":" + field.get(object) );
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
