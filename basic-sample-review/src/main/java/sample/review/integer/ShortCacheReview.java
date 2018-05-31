package sample.review.integer;

/**
 * @author soyona
 * @Package sample.review.integer
 * @Desc:
 * short范围：[-32768,32767]
 * Short的缓存范围：[-128,127]，超过该范围重新new
 * @date 2018/5/31 16:20
 */
public class ShortCacheReview {
    public static void main(String[] args) {
        Short s=127,g = 127;
        System.out.println(s==g);

        /**
         * Short的缓存范围：[-128,127]，超过该范围重新new
         */
        Short m=128,n = 128;
        System.out.println(m==n);//返回false

        /**
         *Short的缓存范围：[-128,127]，超过该范围重新new
         */
        Short x=(short)32769,y = (short)32769;
        System.out.println("x="+x+",y="+y+",(x==y)="+(x==y));//结果false
    }
}
