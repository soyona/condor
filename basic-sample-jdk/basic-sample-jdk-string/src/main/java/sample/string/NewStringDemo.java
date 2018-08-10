package sample.string;

/**
 * @author soyona
 * @Package sample.string
 * @Desc:
 *  字符串在内存中的原理
 *
 * @date 2018/5/28 20:10
 */
public class NewStringDemo {
    public static void main(String[] args) {
        //1、编译期间，"Hello"字符串常量在Class文件结构的常量池中
        //2、在class文件"加载阶段"，字符串 "Hello" 随着Class文件的字节流转为为方法区的运行时常量池，
        //运行时 变量s1入栈，s1是字符串"Hello" 在常量池中引用地址
        String s1 = "Hello";

        /**
         *1、在class文件加载时，字符串"AA" 进入常量池，
         *2、new 在堆开辟空间，存放String对象，并复制 常量池中的"AA"字符串，一个新对象，
         *3、将堆中创建的对象地址 赋值给栈中局部变量表的s2
         */
        String s2 = new String("AA");


        /**
         *1、在编译期优化，"A" + "B" 优化拼接为 "AB"，
         *2、在class文件加载时，字符串"AA" 进入常量池，
         *
         */
        String s3 = "A" + "B";
    }

}
