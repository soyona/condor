package sample.string;

/**
 * @author soyona
 * @Package sample.string
 * @Desc:
 * 使用 javap -verbose FinalStringDemo.class 查看编译结果
 * final关键字编译期
 * @date 2018/5/28 15:23
 */
public class FinalStringDemo {
    public static void main(String[] args) {
        String s0 = "kanglei"; //编译期 常量池 初始化
        final String s1 = "kang";// final关键字编译期 常量池初始化
        final String s2 = "lei";// final关键字 编译期 常量池初始化
        String s3 = s1 + s2;//由于s1,s2是final的，此行代码编译期 会初始化为：s3="kang"+"lei"，进一步初始化为 s3="kanglei"，故s0==s3
//        System.out.println(s0==s3);
    }
}
