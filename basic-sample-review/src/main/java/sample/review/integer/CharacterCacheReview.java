package sample.review.integer;

/**
 * @author soyona
 * @Package sample.review.integer
 * @Desc:
 * CharacterCache范围：[0,128]
 * @date 2018/5/31 16:44
 */
public class CharacterCacheReview {
    public static void main(String[] args) {
        Character a =(char)128,b=(char)128;
        System.out.println("a="+a+",b="+b+",(a==b)="+(a==b));

        Character c =(char)127,d=(char)127;
        System.out.println("c="+c+",d="+d+",(c==d)="+(c==d));

        Character f = (char)1270,e=(char)1270;
        System.out.println(f==e);


    }
}
