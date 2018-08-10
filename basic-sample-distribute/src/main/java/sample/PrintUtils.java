package sample;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author soyona
 * @Package sample
 * @Desc:
 * @date 2018/7/29 21:47
 */
public class PrintUtils {
    public static String printTime(){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date());
    }

}
