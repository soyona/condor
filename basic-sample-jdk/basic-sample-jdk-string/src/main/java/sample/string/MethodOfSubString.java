package sample.string;

import java.util.ArrayList;

/**
 * @author soyona
 * @Package sample.string
 * @Desc:
 * JDK6 运行，抛出异常：
/Library/Java/JavaVirtualMachines/1.6.0_65-b14-462.jdk/Contents/Home/bin/java -Dfile.encoding=UTF-8 -classpath /Library/Java/JavaVirtualMachines/1.6.0_65-b14-462.jdk/Contents/Classes/charsets.jar:/Library/Java/JavaVirtualMachines/1.6.0_65-b14-462.jdk/Contents/Classes/classes.jar:/Library/Java/JavaVirtualMachines/1.6.0_65-b14-462.jdk/Contents/Classes/jsse.jar:/Library/Java/JavaVirtualMachines/1.6.0_65-b14-462.jdk/Contents/Classes/ui.jar:/Library/Java/JavaVirtualMachines/1.6.0_65-b14-462.jdk/Contents/Home/lib/deploy.jar:/Library/Java/JavaVirtualMachines/1.6.0_65-b14-462.jdk/Contents/Home/lib/dt.jar:/Library/Java/JavaVirtualMachines/1.6.0_65-b14-462.jdk/Contents/Home/lib/ext/apple_provider.jar:/Library/Java/JavaVirtualMachines/1.6.0_65-b14-462.jdk/Contents/Home/lib/ext/dnsns.jar:/Library/Java/JavaVirtualMachines/1.6.0_65-b14-462.jdk/Contents/Home/lib/ext/localedata.jar:/Library/Java/JavaVirtualMachines/1.6.0_65-b14-462.jdk/Contents/Home/lib/ext/sunjce_provider.jar:/Library/Java/JavaVirtualMachines/1.6.0_65-b14-462.jdk/Contents/Home/lib/ext/sunpkcs11.jar:/Library/Java/JavaVirtualMachines/1.6.0_65-b14-462.jdk/Contents/Home/lib/javaws.jar:/Library/Java/JavaVirtualMachines/1.6.0_65-b14-462.jdk/Contents/Home/lib/jce.jar:/Library/Java/JavaVirtualMachines/1.6.0_65-b14-462.jdk/Contents/Home/lib/jconsole.jar:/Library/Java/JavaVirtualMachines/1.6.0_65-b14-462.jdk/Contents/Home/lib/management-agent.jar:/Library/Java/JavaVirtualMachines/1.6.0_65-b14-462.jdk/Contents/Home/lib/plugin.jar:/Library/Java/JavaVirtualMachines/1.6.0_65-b14-462.jdk/Contents/Home/lib/sa-jdi.jar:/Users/kanglei/GitHub/condor/basic-sample-string/target/classes sample.string.MethodOfSubString
Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
at java.util.Arrays.copyOf(Arrays.java:2882)
at java.lang.StringValue.from(StringValue.java:24)
at java.lang.String.<init>(String.java:178)
at sample.string.MethodOfSubString$MyString.<init>(MethodOfSubString.java:23)
at sample.string.MethodOfSubString.main(MethodOfSubString.java:16)

JDK7+ 运行正常
 *
 * @date 2018/6/28 00:10
 */
public class MethodOfSubString {

    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < 1000 ; i++) {
            MyString myString = new MyString();
            list.add(myString.subString(5,10));
        }

    }

   static class MyString{
        String str = new String(new char[100000]);
        public String subString(int begin,int end){
            return str.substring(begin,end);
        }
    }
}
