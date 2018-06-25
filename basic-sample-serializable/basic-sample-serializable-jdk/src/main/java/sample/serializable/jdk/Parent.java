package sample.serializable.jdk;

import java.io.Serializable;

/**
 * @author soyona
 * @Package sample.serializable.jdk
 * @Desc:
 * @date 2018/6/25 12:41
 */
public class Parent implements Serializable {
    private String CNO ;

    public String getCNO() {
        return CNO;
    }

    public void setCNO(String CNO) {
        this.CNO = CNO;
    }
}
