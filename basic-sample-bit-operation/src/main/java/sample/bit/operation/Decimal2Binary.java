package sample.bit.operation;

/**
 * @author soyona
 * @Package sample.algorithm.logic.operation
 * @Desc: 逻辑运算示例
 * @date 2018/4/24 18:02
 */
public class Decimal2Binary {


    public static void main(String[] args) {
        for(int i=0; i<116; i++){
            System.out.println(i+" 的二进制表示为："+Integer.toBinaryString(i));
        }
    }
}
