package sample.string;

/**
 * @author soyona
 * @Package sample.string
 * @Desc: String.intern()方法，
 * @date 2018/5/28 15:44
 */
public class StringMethodOfIntern {
    public static void main(String[] args) {
        System.out.println("*******************1*************************");
        //intern流程：如果常量池有，返回常量池的引用
        String s1 = "kanglei";
        System.out.println(s1 == s1.intern());//true，都是常量池的引用
        System.out.println("*******************2*************************");
        //new 字符串在堆
        String s3 = new String("Hello");//堆中的引用
        String s4 = "Hello";//常量池中的引用
        System.out.println(s3 == s4);//false

        System.out.println("*******************3*************************");
        String s5 = new String("World");//堆中的引用
        s5.intern();//把World字符串 "引用" 放到 常量池中
        String s6 = "World";//常量池中已有，返回已有的引用
        System.out.println(s6 == s5.intern());//true,同为 堆中的引用
        System.out.println(s6 == s5);//true,同为 堆中的引用


        System.out.println("*******************4*************************");
        String s7 = new String("KK");//堆中的引用
        String s8 = "KK";//常量池中已有，返回已有的引用
        s7.intern();//"KK" 引用放到 常量池中
        System.out.println(s8 == s7.intern());//true
        System.out.println(s7 == s8);//false

        System.out.println("*******************5*************************");
        String  s9 = "FF";//常量池中已有，返回已有的引用
        String s10 = new String("FF");//堆中的引用
        System.out.println(s9 == s10.intern());//true，"FF"已存在常量池中，s10.inter()首先去常量池中查找有，故返回常量池的引用

        System.out.println("*******************6*************************");
        String s11 = new String("11");//堆中的引用
        String s12 = new String("11");//堆中的引用
        System.out.println(s11.intern() == s12.intern());//true，s11.inter()使常量池中有了"11"的引用，故s12.inter()查找时直接返回引用

        System.out.println("*******************7*************************");
        String s13 = new String("G")+new String("Z");
        s13.intern();//JDK1.7之后，常量池存储"GZ"字符串的引用，常量池存储的是"引用"，而不是字符串"GZ"本身
        String s14 = "GZ";//JDK1.7之后，从常量池中查找，直接返回 s13 的"引用"
        System.out.println(s14 == s13);//true

        System.out.println("*******************8*************************");
        String s15 = new String("LA")+new String("LA");//s15引用的字符串 "LALA"在堆中
        String s16 = "LALA";//s16在常量池中
        s15.intern();//查找常量池中有"LALA"字符串存在，返回常量池地址
        System.out.println(s16 == s15.intern());//true，
        System.out.println(s16 == s15);//false，但是，s15还是指向堆的引用，s16是常量池中的引用

        System.out.println("*******************9*************************");
        //对象new String("17") 与 "17" 并不是同一个对象
        String s17 = new String("17");
        System.out.println(s17.intern() == "17");//true,s17.intern() 指向字符串"17"，一定是 "17"，都在堆中
        System.out.println("********************************************");
        System.out.println(s17 == s17.intern());//false,s17指向堆，
        System.out.println("********************************************");

        System.out.println("*******************10*************************");
        String s18 = new String("a")+new String("b");
        System.out.println(s18 == s18.intern());//true,

        System.out.println("*******************11*************************");
        String s19 = new String("a11");
        System.out.println(s19 == s19.intern());//false,
    }
}
