package hessian;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import hessian.bean.StockBean;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author soyona
 * @Package hessian
 * @Desc:
 * @date 2018/9/1 21:45
 */
public class HessionDemo {
    public static byte[] serialize(StockBean bean){
        ByteArrayOutputStream byteArrayOutputStream = null;
        HessianOutput hessianOutput = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            // Hessian的序列化输出
            hessianOutput = new HessianOutput(byteArrayOutputStream);
            hessianOutput.writeObject(bean);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                hessianOutput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static StockBean deserialize(byte[] beanArray) {
        ByteArrayInputStream byteArrayInputStream = null;
        HessianInput hessianInput = null;
        try {
            byteArrayInputStream = new ByteArrayInputStream(beanArray);
            hessianInput = new HessianInput(byteArrayInputStream);
            return (StockBean) hessianInput.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                byteArrayInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                hessianInput.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        StockBean bean = new StockBean();
        bean.setPro_sum(100);
        // 序列化
        byte[] serialize = serialize(bean);
        System.out.println(serialize);
        // 反序列化
        StockBean bean_D = deserialize(serialize);
        System.out.println(bean_D.getPro_sum());
        System.out.println(bean_D.getPageSize());
    }

}
