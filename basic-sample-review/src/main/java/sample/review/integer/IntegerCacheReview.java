package sample.review.integer;

/**
 * @author soyona
 * @Package sample.review.integer
 * @Desc:
 * Integer范围：[-2147483648,2147483647]
 * Integer a = 127 过程
 * 1、<clinit阶段> 变量 a 初始化 0值，
 * 1.1、调Integer.getInteger(String nm, int val)
 * 1.2、Integer.valueOf(int i)
 * 1.3、IntegerCache.cache[i + (-IntegerCache.low)]
 * 1.4、分析一下代码：
 *  if (i >= IntegerCache.low && i <= IntegerCache.high)//IntegerCache.low =-128，IntegerCache.high=127
 *      return IntegerCache.cache[i + (-IntegerCache.low)];//返回缓存
 *  return new Integer(i);//否则 new 新对象
 * @date 2018/5/30 14:22
 */
class IntegerCacheReview {

    public static void main(String[] args) {
        Integer a = 127;
        Integer b = 127;
        System.out.println("a1=127,b1=127 :"+(a == b));

        Integer a1 = 128;
        Integer b1 = 128;
        System.out.println("a1=128,b1=128 :"+(a1 == b1));
    }
}
