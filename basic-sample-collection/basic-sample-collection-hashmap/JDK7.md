# JDK7 HashMap由 数组+链表
> [参考：](http://www.importnew.com/28263.html)

# JDK7 bin元素结构定义：
```java
static class Entry<K,V> implements Map.Entry<K,V> {
    final K key;
    V value;
    Entry<K,V> next;
    int hash;
}
```
# JDK7 数组桶定义：
```
 transient Entry<K,V>[] table = (Entry<K,V>[]) EMPTY_TABLE;
```
# JDK7 定位桶位置：
> 简单说就是取 hash 值的低 n 位
```text
static int indexFor(int h, int length) {
    // assert Integer.bitCount(length) == 1 : "length must be a non-zero power of 2";
    return h & (length-1);
}
```
# JDK7 数组扩容
> 添加元素时：
```text
void addEntry(int hash, K key, V value, int bucketIndex) {
    // 如果当前 HashMap 大小已经达到了阈值，并且新值要插入的数组位置已经有元素了，那么要扩容
    if ((size >= threshold) && (null != table[bucketIndex])) {
        // 扩容，后面会介绍一下
        resize(2 * table.length);
        // 扩容以后，重新计算 hash 值
        hash = (null != key) ? hash(key) : 0;
        // 重新计算扩容后的新的下标
        bucketIndex = indexFor(hash, table.length);
    }
    // 往下看
    createEntry(hash, key, value, bucketIndex);
}
// 这个很简单，其实就是将新值放到链表的表头，然后 size++
void createEntry(int hash, K key, V value, int bucketIndex) {
    Entry<K,V> e = table[bucketIndex];
    table[bucketIndex] = new Entry<>(hash, key, value, e);
    size++;
}
```
> 扩容方法：
```text
void resize(int newCapacity) {
    Entry[] oldTable = table;
    int oldCapacity = oldTable.length;
    if (oldCapacity == MAXIMUM_CAPACITY) {
        threshold = Integer.MAX_VALUE;
        return;
    }
    // 新的数组
    Entry[] newTable = new Entry[newCapacity];
    // 将原来数组中的值迁移到新的更大的数组中
    transfer(newTable, initHashSeedAsNeeded(newCapacity));
    table = newTable;
    threshold = (int)Math.min(newCapacity * loadFactor, MAXIMUM_CAPACITY + 1);
}
```
```text
迁移过程中，会将原来 table[i] 中的链表的所有节点，分拆到新的数组的 newTable[i] 和 newTable[i + oldLength] 位置上。如原来数组长度是 16，那么扩容后，原来 table[0] 处的链表中的所有元素会被分配到新数组中 newTable[0] 和 newTable[16] 这两个位置。
```
# JDK7 get(key)中 key的重hash
```text
   final int hash(Object k) {
        int h = hashSeed;
        if (0 != h && k instanceof String) {
            return sun.misc.Hashing.stringHash32((String) k);
        }

        h ^= k.hashCode();

        // This function ensures that hashCodes that differ only by
        // constant multiples at each bit position have a bounded
        // number of collisions (approximately 8 at default load factor).
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }
```
# JDK7 数组大小是2的幂，通过什么方法得到
> inflateTable()
```
/**
 * Inflates the table.
 */
private void inflateTable(int toSize) {
    // Find a power of 2 >= toSize
    int capacity = roundUpToPowerOf2(toSize);

    threshold = (int) Math.min(capacity * loadFactor, MAXIMUM_CAPACITY + 1);
    table = new Entry[capacity];
    initHashSeedAsNeeded(capacity);
}
```
> 方法：roundUpToPowerOf2
```text
private static int roundUpToPowerOf2(int number) {
            // assert number >= 0 : "number must be non-negative";
            return number >= MAXIMUM_CAPACITY
                    ? MAXIMUM_CAPACITY
                    : (number > 1) ? Integer.highestOneBit((number - 1) << 1) : 1;
}
```
> 方法：Integer.highestOneBit
```text
Integer.highestOneBit((number - 1) << 1)
```
```text
public static int highestOneBit(int i) {
        // HD, Figure 3-1
        i |= (i >>  1);
        i |= (i >>  2);
        i |= (i >>  4);
        i |= (i >>  8);
        i |= (i >> 16);
        return i - (i >>> 1);
    }
```
# JDK7 数组长度为什么是2的n次方

> 取模替代？位运算 效率 高于 % 
```text
2的n次方的前提下，以下才成立：
hash%length==hash&(length-1)
```
> 为了均匀？
对，为了均匀散布在各个桶，

> h&(length-1)
```text
length -1 取掩码，后位全是1，&h=> 取h得后n位，对，是n位
```
> hash=h^h>>>16
```text
>>>16，高16位参与运算，复合产物
```

# JDK7 put(k,v)过程：
- 第一个元素插入时，初始化数组大小
- 数组大小为2 的 n 次方
- 计算数组的位置
- 定位到数组下标，判重key，如果无重复 ，插入链表头部
    - 是否需要扩容，需要先扩容2倍
    - 再计算hash
    - 扩容后迁移元素
# JDK get(K)过程：
- hash(K)
- 定位数组下标：h & (length-1)
- 遍历链表找到 ==/equals 的key

