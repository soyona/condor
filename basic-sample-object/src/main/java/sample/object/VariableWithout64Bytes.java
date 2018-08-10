package sample.object;

/**
 * @author soyona
 * @Package sample.os.cacheline
 * @Desc: 对象的大小不是64字节的整数倍，不对齐缓存行
 * @date 2018/8/1 08:30
 */
public class VariableWithout64Bytes  implements ValueSupport{
    public volatile  long l1;

    @Override
    public long getValue() {
//        System.out.println(l1);
        return l1;

    }
}
