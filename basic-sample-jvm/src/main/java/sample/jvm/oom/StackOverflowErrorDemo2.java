package sample.jvm.oom;

/**
 * @author soyona
 * @Package sample.jvm.oom
 * @Desc:
 * @date 2018/6/29 15:38
 */
public class StackOverflowErrorDemo2 {
    private StackOverflowErrorDemo2 instance;
    public StackOverflowErrorDemo2(){
        instance = new StackOverflowErrorDemo2();
    }

    @Override
    public String toString() {
        // instance 引用，触发toString(),造成toString()相互调用
        return "instance="+instance;
    }

    public static void main(String[] args) {
        StackOverflowErrorDemo2 stackOverflowErrorDemo2 = new StackOverflowErrorDemo2();
        stackOverflowErrorDemo2.toString();
    }
}
