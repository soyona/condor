package sample.review.integer;

/**
 * @author soyona
 * @Package sample.review.integer
 * @Desc:
 * ByteCache缓存了[-128,127]中的整数
 * Byte的声明：
 * Byte b=(byte)128;
 * Byte a=(byte)128;
 * a==b?
 * 结果：===true，因为Byte声明必须经过类型强转，强转后的值一定在[-128,127]中，一定都在Cache中，所以恒等。
 * @date 2018/5/31 16:00
 */
public class ByteCacheReview {
    public static void main(String[] args) {
        Byte a=(byte)229,b = (byte)229;

        System.out.println("a="+a+",b="+b+(a==b));

        Byte c = Byte.valueOf((byte) 128);
        Byte d = Byte.valueOf((byte) 128);
        System.out.println(c==d);



    }
}
