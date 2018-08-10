package sample.reflect.api;

import java.util.Date;

/**
 * @author soyona
 * @Package sample.reflect.api
 * @Desc:
 * @date 2018/5/3 16:36
 */
public class Parent {
    public String p_name;

    public void pMethod(){
        System.out.println("parent public method.");
    }


    private String pSay(Date date){
        return date.toString();
    }
}
