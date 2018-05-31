package sample.review.integer;

/**
 * @author soyona
 * @Package sample.review.integer
 * @Desc:
 * @date 2018/5/31 16:39
 */
public class LongCacheReview {
    public static void main(String[] args) {
        Long s= Long.valueOf(127),g = Long.valueOf(127);
        System.out.println(s==g);

        /**
         * Long的缓存范围：[-128,127]，超过该范围重新new
         */
        Long m= Long.valueOf(128),n = Long.valueOf(128);
        System.out.println(m==n);//返回false

        /**
         *Long的缓存范围：[-128,127]，超过该范围重新new
         */
        Long x=(long)32769,y = (long)32769;
        System.out.println("x="+x+",y="+y+",(x==y)="+(x==y));//结果false
    }
}
