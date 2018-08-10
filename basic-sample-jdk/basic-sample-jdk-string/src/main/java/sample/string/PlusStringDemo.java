package sample.string;

/**
 * @author soyona
 * @Package sample.string
 * @Desc: 使用 javap -verbose StringDemo.class 查看编译结果
 * @date 2018/5/28 15:00
 */
public class PlusStringDemo {
    public static void main(String[] args) {
        String s0 = "kanglei"; //编译期 常量池 初始化
        String s1 = "kang";// 编译期 常量池初始化
        String s2 = "lei";// 编译期 常量池初始化
        String s3 = s1 + s2;//运行期，要产生新对象->新地址；会使用StringBuilder.append()实现相加
        String s4 = "kang"+"lei";// 编译期确定，故 运行期 等于 字符串："kanglei"
//        System.out.println(s0 == s3);
//        System.out.println(s0 == s4);
    }

}
