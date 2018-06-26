package sample.serializable.jdk;

import java.io.Serializable;

/**
 * @author soyona
 * @Package sample.serializable.jdk
 * @Desc: 作为 Order父类，若需序列化和反序列化，必须实现Serializable，或者提供无参构造，否则java.io.InvalidClassException: sample.serializable.jdk.Order; no valid constructor
 * @date 2018/6/25 12:41
 */
public class Parent  implements Serializable{

    private static final long serialVersionUID = -9169204044985312609L;
    private String CNO ;

    public String getCNO() {
        return CNO;
    }

    public void setCNO(String CNO) {
        this.CNO = CNO;
    }
}
