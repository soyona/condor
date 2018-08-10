package sample.reflect.api;

import java.math.BigDecimal;

/**
 * @author soyona
 * @Package sample.reflect.api
 * @Desc:
 * @date 2018/5/3 14:16
 */
public class Book<T> extends Parent implements SingInterface,WorkInterface<T>{
    public String name;
    private int age;
    private String id;
    public BigDecimal price;
    //作者
    public String author;
    public Double weight;

    //出版日期
    protected String publishedDate;
    //版次
    String publishedTimes;

    /**
     * 构造函数
     */
    public Book(){

    }

    /**
     *
     */
    Book(String name, int age, String id){
        this.name=name;
        this.age=age;
        this.id=id;
    }

    /**
     * 私有 有参构造
     * @param name
     */
    private Book(String name){

    }
    /**
     * 私有参构造
     * @param name
     * @param age
     */
    protected Book(String name, int age){

    }

    /**
     * 公开方法
     * @return
     */
    public String getName(){
        return this.name;
    }


    /**
     * 公开方法，带参数,重载
     * @param parameter
     * @param id
     */
    public String getName(String parameter,String id){
        return this.name;
    }

    /**
     * 公开方法，带参数
     * @return
     */
    public void getName(String parameter){
        ;
    }


    /**
     * 私有方法
     */
    private void setName(){

    }

    /**
     * public方法
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * 私有方法
     */
    private void setAge(int age){
        this.age = age;
    }

    /**
     * protected 方法
     */
    protected void didRead(){

    }


    class InnerBook{
        private String name;
    }



    public class RedBook{

    }

    private interface ITWorkInterface{

    }

}
