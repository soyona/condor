package sample.object;

/**
 * @author soyona
 * @Package sample.os.cacheline
 * @Desc: 对象大小 对齐缓存行 64字节, 7个long类型变量，对象头大小8byte
 * @date 2018/8/1 08:30
 */
public class VariableWith64Bytes implements ValueSupport {

    // long的大小8Byte,  7*8=56
    public volatile long l1, l2, l3, l4, l5, l6;

    @Override
    public long getValue() {
        return l1;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
