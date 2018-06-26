package sample.serializable.jdk;

import java.io.Serializable;

/**
 * @author soyona
 * @Package sample.serializable.jdk
 * @Desc: Member作为Order的对象引用，必须实现Serializable接口，否则 抛出java.io.NotSerializableException: sample.serializable.jdk.Member
 * @date 2018/6/26 11:40
 */
public class Member implements Serializable{
    private String SID;
    public Member(String sid){
        this.SID = sid;
        System.out.println("Member 无参构造。");
    }

    public String getSID() {
        return SID;
    }

    public void setSID(String SID) {
        this.SID = SID;
    }
}
