IntegerCache类说明
---
```java
/**
 * Cache to support the object identity semantics of autoboxing for values between
 * -128 and 127 (inclusive) as required by JLS.
 *
 * The cache is initialized on first usage.  The size of the cache
 * may be controlled by the {@code -XX:AutoBoxCacheMax=<size>} option.
 * During VM initialization, java.lang.Integer.IntegerCache.high property
 * may be set and saved in the private system properties in the
 * sun.misc.VM class.
 */
```

```text
缓存，用来支持-128 到127 之间的值自动装箱语义
缓存在第一次使用时初始化。缓存的大小可以通过 选项`-XX:AutoBoxCacheMax=<size>` 控制。
在VM初始化期间，属性java.lang.Integer.IntegerCache.high可能设置保存在sum.misc.VM类的私有的系统属性中

例如：
在IDEA中，运行时配置-XX:AutoBoxCacheMax=200;
配置方法：Run->Edit Configurations...->左侧：选择运行的类->右侧：Configuration选项->VM options:AutoBoxCacheMax=200

此时：
 Integer a1,b1= 128;
 **System.out.println("a1=128,b1=128 :"+(a1 == b1));//结果为true;**
```
```java
private static class IntegerCache {
    static final int low = -128;
    static final int high;
    static final Integer cache[];

    static {
        // high value may be configured by property
        int h = 127;
        String integerCacheHighPropValue = sun.misc.VM.getSavedProperty("java.lang.Integer.IntegerCache.high");
        if (integerCacheHighPropValue != null) {
            try {
                int i = parseInt(integerCacheHighPropValue);
                i = Math.max(i, 127);
                // Maximum array size is Integer.MAX_VALUE
                h = Math.min(i, Integer.MAX_VALUE - (-low) -1);
            } catch( NumberFormatException nfe) {
                // If the property cannot be parsed into an int, ignore it.
            }
        }
        high = h;
        //128+127+1=256
        cache = new Integer[(high - low) + 1];
        int j = low;
        for(int k = 0; k < cache.length; k++)
            cache[k] = new Integer(j++);

        // range [-128, 127] must be interned (JLS7 5.1.7)
        assert IntegerCache.high >= 127;
    }

    private IntegerCache() {}
}

/**
 * Returns an {@code Integer} instance representing the specified
 * {@code int} value.  If a new {@code Integer} instance is not
 * required, this method should generally be used in preference to
 * the constructor {@link #Integer(int)}, as this method is likely
 * to yield significantly better space and time performance by
 * caching frequently requested values.
 *
 * This method will always cache values in the range -128 to 127,
 * inclusive, and may cache other values outside of this range.
 *
 * @param  i an {@code int} value.
 * @return an {@code Integer} instance representing {@code i}.
 * @since  1.5
 */
public static Integer valueOf(int i) {
    if (i >= IntegerCache.low && i <= IntegerCache.high)
        return IntegerCache.cache[i + (-IntegerCache.low)];
    return new Integer(i);
}

```
---

ByteCache类说明
---
```text
ByteCache缓存了[-128,127]中的整数
Byte的声明：

Byte b=(byte)128;
Byte a=(byte)128;
a==b?
结果：===true，因为Byte声明必须经过类型强转，强转后的值一定在[-128,127]中，一定都在Cache中，所以恒等。
```

```java
private static class ByteCache {
    private ByteCache(){}

    static final Byte cache[] = new Byte[-(-128) + 127 + 1];

    static {
        for(int i = 0; i < cache.length; i++)
            cache[i] = new Byte((byte)(i - 128));
    }
}

```
  

ShortCache类
---
```text
ShortCache缓存[-128,127]之间的整数，超过该范围重新new
```

```java
private static class ShortCache {
    private ShortCache(){}

    static final Short cache[] = new Short[-(-128) + 127 + 1];

    static {
        for(int i = 0; i < cache.length; i++)
            cache[i] = new Short((short)(i - 128));
    }
}
```

```text
范围之外[-128,127]，new Short(s);
```
```java
public static Short valueOf(short s) {
    final int offset = 128;
    int sAsInt = s;
    if (sAsInt >= -128 && sAsInt <= 127) { // must cache
        return ShortCache.cache[sAsInt + offset];
    }
    return new Short(s);
}
```

```text
LongCache 缓存[-128,127]之间的整数，超过该范围重新new
```
```java
private static class LongCache {
    private LongCache(){}

    static final Long cache[] = new Long[-(-128) + 127 + 1];

    static {
        for(int i = 0; i < cache.length; i++)
            cache[i] = new Long(i - 128);
    }
}
```
