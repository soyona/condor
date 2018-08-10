package sample.reflect.api;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author soyona
 * @Package sample.reflect.api
 * @Desc:
 * @date 2018/5/4 12:10
 */
public class MethodOfInvoke {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Book book = new Book();
        Method method = Book.class.getMethod("setName",String.class);

        method.invoke(book,"a");

        System.out.println(book.getName());;

    }
}
