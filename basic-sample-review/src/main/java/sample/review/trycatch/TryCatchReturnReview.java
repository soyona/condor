package sample.review.trycatch;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author soyona
 * @Package sample.trycatch
 * @Desc:
 * Review try{return;} finally{return;}中的 执行逻辑
 * 总结：
 * 1、如果finally中有return，finally中对返回值的修改起作用
 * 2、如果finally中无return，分情况：
 * 2.1、有异常：try中return无法正常返回，finally中对返回值修改起作用
 * 2.2、无异常：try中return正常返回，finally中对返回值修改不起作用
 * 3、如果try{return;}catch(Exception e){return;}finally{return;}
 * 3.1: try中return前出现异常，return不执行，其余处return 都会被执行
 * 3.2: finally中return决定最后返回值
 * @date 2018/5/30 12:29
 */
public class TryCatchReturnReview {
    public static void main(String[] args) {
        System.out.println("get():"+get());
        System.out.println("getA():"+getA());
        System.out.println("getB():"+getB());
        System.out.println("getC():"+getC());
        System.out.println("getD():"+getD());
    }

    /**E
     * finally中无 return
     * try中无异常抛出
     * finally中对t的更新不会影响try块中的t
     * @return
     */
    public static  int get(){
        int t = 20;
        try{
            return t;//1、返回t
        }catch (Exception e){
            t=100;
            System.out.println("get-- catch");
        }finally{
            t=15; //2、??? 此处对t的更改，会不会影响 1、处 return的值？
            System.out.println("get-- finally");
        }
        return t;
    }

    /**E
     * try中 有return
     * finally中有 return
     * catch 无异常
     * finally中对t的更新会影响try块中的t
     * @return
     */
    public static  int getA(){
        int t = 20;
        try{
            return t;//1、返回t
        }catch (Exception e){
            t=100;
            System.out.println("getA-- catch");
        }finally{
            //2、??? 此处对t的更改，会不会影响 1、处 return的值？
            t=15;
            System.out.println("getA-- finally");
            return t;
        }
//        return t;
    }

    /**
     * finally 中无 return
     * 如果抛出异常，finally块中对t的更改生效，
     * @return
     */
    public static int getB(){
        int t = 20;
        try{
            int b=2/0;
            return t;//1、返回t
        }catch (Exception e){
            t=100;//2、如果进入catch块中，1、处代码不会被执行
            System.out.println("getB-- catch");
        }finally{
            t=15;//3、??? 此处对t的更改，会不会影响 1、处 return的值？
            System.out.println("getB-- finally");
        }
        return t;
    }


    /**
     * Map
     * finally 中有 return
     * 如果抛出异常，catch中执行map.put();
     * finally 中 对map=null 生效，getC()返回null
     * @return
     */
    public static Map getC(){
        Map map = new HashMap();

        try{
            int  a = 2/0;
            return map;//1、返回t
        }catch (Exception e){
            map.put("name","kanglei");//2、如果进入catch块中，1、处代码不会被执行
            System.out.println("getC-- catch");
        }finally{

            map.put("age","12");//3、此处对map的更改，增加age
            map = null;//4、此处导致 getC()返回null;
            System.out.println("getC-- finally");
            return map;
        }
//        return map;
    }

    /**
     * try中return 由于异常不会被执行
     * catch中return 会被执行
     * finally中return 会被执行，这里决定最后的返回值
     * @return
     */
    public static String getD(){
        String s="A";

        try{
            int  a = 2/0;
            return p(s);//1、返回t
        }catch (Exception e){
            s+="-catch-";//2、如果进入catch块中，1、处代码不会被执行
            System.out.println("getC-- catch");
            return p("catch") ;
        }finally{
            s += "-finally";//3、此处对map的更改，增加age
            System.out.println("getC-- finally");
            return p("finally");
        }
    }

    private static String p(String s){
        System.out.println((new Date()).getTime()+s);
        return s;
    }


}
