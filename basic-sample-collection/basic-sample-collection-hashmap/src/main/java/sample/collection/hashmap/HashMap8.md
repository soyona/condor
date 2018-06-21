HashMap分析
---
    package sample.collection.hashmap;
    
    import java.io.IOException;
    import java.io.InvalidObjectException;
    import java.io.Serializable;
    import java.lang.reflect.ParameterizedType;
    import java.lang.reflect.Type;
    import java.util.*;
    import java.util.function.BiConsumer;
    import java.util.function.BiFunction;
    import java.util.function.Consumer;
    import java.util.function.Function;
    
    /**
     * Hash table based implementation of the <tt>Map</tt> interface.  This
     * implementation provides all of the optional map operations, and permits
     * <tt>null</tt> values and the <tt>null</tt> key.  (The <tt>HashMap</tt>
     * class is roughly equivalent to <tt>Hashtable</tt>, except that it is
     * unsynchronized and permits nulls.)  This class makes no guarantees as to
     * the order of the map; in particular, it does not guarantee that the order
     * will remain constant over time.
     *
     * <p>This implementation provides constant-time performance for the basic
     * operations (<tt>get</tt> and <tt>put</tt>), assuming the hash function
     * disperses the elements properly among the buckets.  Iteration over
     * collection views requires time proportional to the "capacity" of the
     * <tt>HashMap</tt> instance (the number of buckets) plus its size (the number
     * of key-value mappings).  Thus, it's very important not to set the initial
     * capacity too high (or the load factor too low) if iteration performance is
     * important.
     *
     * <p>An instance of <tt>HashMap</tt> has two parameters that affect its
     * performance: <i>initial capacity</i> and <i>load factor</i>.  The
     * <i>capacity</i> is the number of buckets in the hash table, and the initial
     * capacity is simply the capacity at the time the hash table is created.  The
     * <i>load factor</i> is a measure of how full the hash table is allowed to
     * get before its capacity is automatically increased.  When the number of
     * entries in the hash table exceeds the product of the load factor and the
     * current capacity, the hash table is <i>rehashed</i> (that is, internal data
     * structures are rebuilt) so that the hash table has approximately twice the
     * number of buckets.
     *
     * <p>As a general rule, the default load factor (.75) offers a good
     * tradeoff between time and space costs.  Higher values decrease the
     * space overhead but increase the lookup cost (reflected in most of
     * the operations of the <tt>HashMap</tt> class, including
     * <tt>get</tt> and <tt>put</tt>).  The expected number of entries in
     * the map and its load factor should be taken into account when
     * setting its initial capacity, so as to minimize the number of
     * rehash operations.  If the initial capacity is greater than the
     * maximum number of entries divided by the load factor, no rehash
     * operations will ever occur.
     *
     * <p>If many mappings are to be stored in a <tt>HashMap</tt>
     * instance, creating it with a sufficiently large capacity will allow
     * the mappings to be stored more efficiently than letting it perform
     * automatic rehashing as needed to grow the table.  Note that using
     * many keys with the same {@code hashCode()} is a sure way to slow
     * down performance of any hash table. To ameliorate impact, when keys
     * are {@link Comparable}, this class may use comparison order among
     * keys to help break ties.
     *
     * <p><strong>Note that this implementation is not synchronized.</strong>
     * If multiple threads access a hash map concurrently, and at least one of
     * the threads modifies the map structurally, it <i>must</i> be
     * synchronized externally.  (A structural modification is any operation
     * that adds or deletes one or more mappings; merely changing the value
     * associated with a key that an instance already contains is not a
     * structural modification.)  This is typically accomplished by
     * synchronizing on some object that naturally encapsulates the map.
     *
     * If no such object exists, the map should be "wrapped" using the
     * {@link Collections#synchronizedMap Collections.synchronizedMap}
     * method.  This is best done at creation time, to prevent accidental
     * unsynchronized access to the map:<pre>
     *   Map m = Collections.synchronizedMap(new HashMap(...));</pre>
     *
     * <p>The iterators returned by all of this class's "collection view methods"
     * are <i>fail-fast</i>: if the map is structurally modified at any time after
     * the iterator is created, in any way except through the iterator's own
     * <tt>remove</tt> method, the iterator will throw a
     * {@link ConcurrentModificationException}.  Thus, in the face of concurrent
     * modification, the iterator fails quickly and cleanly, rather than risking
     * arbitrary, non-deterministic behavior at an undetermined time in the
     * future.
     *
     * <p>Note that the fail-fast behavior of an iterator cannot be guaranteed
     * as it is, generally speaking, impossible to make any hard guarantees in the
     * presence of unsynchronized concurrent modification.  Fail-fast iterators
     * throw <tt>ConcurrentModificationException</tt> on a best-effort basis.
     * Therefore, it would be wrong to write a program that depended on this
     * exception for its correctness: <i>the fail-fast behavior of iterators
     * should be used only to detect bugs.</i>
     *
     * <p>This class is a member of the
     * <a href="{@docRoot}/../technotes/guides/collections/index.html">
     * Java Collections Framework</a>.
     *
     * @param <K> the type of keys maintained by this map
     * @param <V> the type of mapped values
     *
     * @author  Doug Lea
     * @author  Josh Bloch
     * @author  Arthur van Hoff
     * @author  Neal Gafter
     * @see     Object#hashCode()
     * @see     Collection
     * @see     Map
     * @see     TreeMap
     * @see     Hashtable
     * @since   1.2
     */
    public class HashMap<K,V> extends AbstractMap<K,V>
        implements Map<K,V>, Cloneable, Serializable {
    
        private static final long serialVersionUID = 362498820763181265L;
说明
---    
        /*
         * Implementation notes.
         *
         * This map usually acts as a binned (bucketed) hash table, but
         * when bins get too large, they are transformed into bins of
         * TreeNodes, each structured similarly to those in
         * java.util.TreeMap. Most methods try to use normal bins, but
         * relay to TreeNode methods when applicable (simply by checking
         * instanceof a node).  Bins of TreeNodes may be traversed and
         * used like any others, but additionally support faster lookup
         * when overpopulated. However, since the vast majority of bins in
         * normal use are not overpopulated, checking for existence of
         * tree bins may be delayed in the course of table methods.
```        
    在表方法期间，检查tree箱的操作可能被延迟
```
         *
         * Tree bins (i.e., bins whose elements are all TreeNodes) are
         * ordered primarily by hashCode, but in the case of ties, if two
         * elements are of the same "class C implements Comparable<C>",
         * type then their compareTo method is used for ordering. (We
    
```         
    树箱（即，他的元素都是树节点）主要根据hashCode排序，但是在这种情况下，          
    如果两个元素都是同样实现Comparable接口的类C，那他们的compareTo方法被用来排序。_
```
         *
         * conservatively check generic types via reflection to validate
         * this -- see method comparableClassFor).  The added complexity
```
    (我们检查范型 稳妥的做法是通过反射区检验--查看comparableClassFor方法)         
```         
         * of tree bins is worthwhile in providing worst-case O(log n)
         * operations when keys either have distinct hashes or are
         * orderable, Thus, performance degrades gracefully under
         * accidental or malicious usages in which hashCode() methods
         * return values that are poorly distributed, as well as those in
         * which many keys share a hashCode, so long as they are also
         * Comparable. (If neither of these apply, we may waste about a
```   
    当keys具有不同的哈希或者有序的，会增加树箱的复杂度，最坏的情况是O(log n)个操作，
    这样的话，在偶然或者恶意的用法时，性能将大幅度下降，这种情况是hashCode()方法的返回值不分散，  
    与许多keys共享一个hashCode一样，
    只要他们也是可比较的。（如果两者都不实用，如果不采取措施，我们可能浪费1倍的时间和空间，
    但是从糟糕的用户编程实践中得出的唯一情况是，已经太慢，使得也没什么区别！）_
        
```         
         * factor of two in time and space compared to taking no
         * precautions. But the only known cases stem from poor user
         * programming practices that are already so slow that this makes
         * little difference.)
         *
         * Because TreeNodes are about twice the size of regular nodes, we
         * use them only when bins contain enough nodes to warrant use
         * (see TREEIFY_THRESHOLD). And when they become too small (due to
         * removal or resizing) they are converted back to plain bins.  In
         * usages with well-distributed user hashCodes, tree bins are
         * rarely used.  Ideally, under random hashCodes, the frequency of
         * nodes in bins follows a Poisson distribution
         * (http://en.wikipedia.org/wiki/Poisson_distribution) with a
         * parameter of about 0.5 on average for the default resizing
         * threshold of 0.75, although with a large variance because of
         * resizing granularity. Ignoring variance, the expected
         * occurrences of list size k are (exp(-0.5) * pow(0.5, k) /
         * factorial(k)). The first values are:
         *
```
    因为树节点大约是普通节点大小的两倍，我们仅当箱包含足够的节点保证使用的情况下使用（查看 TREEIFY_THRESHOLD）
    当节点大小因为删除或者扩容变小，他们将再转换成普通的箱子。如果用户hashCodes分布良好，树箱很少使用。
    理想的情况，在随机的hashCodes情况下，箱中的节点的频率遵循泊松分布：
    泊松分布适合于描述单位时间内随机事件发生的次数的概率分布】
        
```         
         * 0:    0.60653066
         * 1:    0.30326533
         * 2:    0.07581633
         * 3:    0.01263606
         * 4:    0.00157952
         * 5:    0.00015795
         * 6:    0.00001316
         * 7:    0.00000094
         * 8:    0.00000006
         * more: less than 1 in ten million
         *
         * The root of a tree bin is normally its first node.  However,
         * sometimes (currently only upon Iterator.remove), the root might
         * be elsewhere, but can be recovered following parent links
         * (method TreeNode.root()).
         *
```
    树箱的根节点通常是它的第一个节点。然而，有时（目前只在迭代器的remove时），根节点可能在别处，但是沿着父链接（TreeNode.root()方法）一定能找到。

```         
         * All applicable internal methods accept a hash code as an
         * argument (as normally supplied from a public method), allowing
         * them to call each other without recomputing user hashCodes.
         * Most internal methods also accept a "tab" argument, that is
         * normally the current table, but may be a new or old one when
         * resizing or converting.
         *
```
    所有使用的内部方法接收一个hashcode作为参数，允许他们相互调用而不用计算用户hashCodes。
    大多数内部方法也接收一个"tab"参数，通常是当前 table，但是当扩容或是转换时可能是一个新的或者是旧的。 

```         
         * When bin lists are treeified, split, or untreeified, we keep
         * them in the same relative access/traversal order (i.e., field
         * Node.next) to better preserve locality, and to slightly
         * simplify handling of splits and traversals that invoke
         * iterator.remove. When using comparators on insertion, to keep a
         * total ordering (or as close as is required here) across
         * rebalancings, we compare classes and identityHashCodes as
         * tie-breakers.
         *
```
    当箱列表转换为树，分裂，或是 从树结构转换回时，我们保持他们处于同样相对访问/遍历的顺序（即，Node.next）
    
    当插入操作使用比较器时，为了保持总体重新平衡顺序，我们比较类和唯一表示HashCodes 作为权衡标准。

```         
         * The use and transitions among plain vs tree modes is
         * complicated by the existence of subclass LinkedHashMap. See
         * below for hook methods defined to be invoked upon insertion,
         * removal and access that allow LinkedHashMap internals to
         * otherwise remain independent of these mechanics. (This also
         * requires that a map instance be passed to some utility methods
         * that may create new nodes.)
         *
```
   子类LinkedHashMap的存在，普通模式与树模式之间使用和转换被复杂化了，查看下面定义的钩子方法，这些方法在插入、删除和访问时被调用
   以使LinkedHashMap内部保持独立于这些机制。（这也需要一个map实例传递给一些可以创建新节点的工具方法）

```         
         * The concurrent-programming-like SSA-based coding style helps
         * avoid aliasing errors amid all of the twisty pointer operations.
         */
### DEFAULT_INITIAL_CAPACITY 默认的初始化容量16，2的幂，   
        /**
         * The default initial capacity - MUST be a power of two.
         */
        static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16
### 容器的最大容量 1 << 30     
        /**
         * The maximum capacity, used if a higher value is implicitly specified
         * by either of the constructors with arguments.
         * MUST be a power of two <= 1<<30.
         */
        static final int MAXIMUM_CAPACITY = 1 << 30;
### 默认的加载因子，构造器中没有指定的情况    
        /**
         * The load factor used when none specified in constructor.
         */
        static final float DEFAULT_LOAD_FACTOR = 0.75f;
树化的相关阈值
===
#### TREEIFY_THRESHOLD 此时的容器指 链表，链表元素个数到8时，转为树，
  
        /**
         * The bin count threshold for using a tree rather than list for a
         * bin.  Bins are converted to trees when adding an element to a
         * bin with at least this many nodes. The value must be greater
         * than 2 and should be at least 8 to mesh with assumptions in
         * tree removal about conversion back to plain bins upon
         * shrinkage.
         */
        static final int TREEIFY_THRESHOLD = 8;
#### UNTREEIFY_THRESHOLD ，当树节点数小于6时，转换成链表   
        /**
         * The bin count threshold for untreeifying a (split) bin during a
         * resize operation. Should be less than TREEIFY_THRESHOLD, and at
         * most 6 to mesh with shrinkage detection under removal.
         */
        static final int UNTREEIFY_THRESHOLD = 6;
#### 容器转化为树的最小容量，当整个哈希表的容量大于这个数时，才进行转树，
#### 指数组长度>MIN_TREEIFY_CAPACITY 时，转树
   
        /**
         * The smallest table capacity for which bins may be treeified.
         * (Otherwise the table is resized if too many nodes in a bin.)
         * Should be at least 4 * TREEIFY_THRESHOLD to avoid conflicts
         * between resizing and treeification thresholds.
         */
        static final int MIN_TREEIFY_CAPACITY = 64;
基础哈希表节点定义  
---  
        /**
         * Basic hash bin node, used for most entries.  (See below for
         * TreeNode subclass, and in LinkedHashMap for its Entry subclass.)
         */
        static class Node<K,V> implements Entry<K,V> {
            final int hash;
            final K key;
            V value;
            Node<K,V> next;
    
            Node(int hash, K key, V value, Node<K,V> next) {
                this.hash = hash;
                this.key = key;
                this.value = value;
                this.next = next;
            }
    
            public final K getKey()        { return key; }
            public final V getValue()      { return value; }
            public final String toString() { return key + "=" + value; }
    
            public final int hashCode() {
                return Objects.hashCode(key) ^ Objects.hashCode(value);
            }
    
            public final V setValue(V newValue) {
                V oldValue = value;
                value = newValue;
                return oldValue;
            }
    
            public final boolean equals(Object o) {
                if (o == this)
                    return true;
                if (o instanceof Map.Entry) {
                    Entry<?,?> e = (Entry<?,?>)o;
                    if (Objects.equals(key, e.getKey()) &&
                        Objects.equals(value, e.getValue()))
                        return true;
                }
                return false;
            }
        }
    
        /* ---------------- Static utilities -------------- */
    
##  static final int hash(Object key)
### 0. 重新计算key的哈希
### 1. key.hashCode()，对象的内部地址转换成的整型数字
### 2. h>>> 16 //右移16位，取高位
    
        /**
        * Computes key.hashCode() and spreads (XORs) higher bits of hash
        * 计算key的哈希值， 通过异或运算（XORs) 使高位扩散到低位。
        * to lower.  
        * Because the table uses power-of-two masking, sets of
        * hashes that vary only in bits above the current mask will
        * always collide. (Among known examples are sets of Float keys
        * holding consecutive whole numbers in small tables.) 
        * So we
        * apply a transform that spreads the impact of higher bits
        * downward. There is a tradeoff between speed, utility, and
        * quality of bit-spreading. 
        * 因此 我们采用把高位向下扩展的转移方式
        
        * Because many common sets of hashes
        * are already reasonably distributed (so don't benefit from
        * spreading), and because we use trees to handle large sets of
        * collisions in bins, we just XOR some shifted bits in the
        * cheapest possible way to reduce systematic lossage, as well as
        * to incorporate impact of the highest bits that would otherwise
        * never be used in index calculations because of table bounds.
        
        * 因为许多常见的 hash 值都是适度分散的（因此位扩散的收益不大），又因为
        * 我们使用树，来管控大数量的冲突元素。使用XOR异或运算来移位，可以尽可能低成本地
        * 减少系统性损耗，也将原本不参与数组下标计算的高位的也给包含进来了。
        */
        static final int hash(Object key) {
            int h;
            return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
        }
    
        /**
         * Returns x's Class if it is of the form "class C implements
         * Comparable<C>", else null.
         */
        static Class<?> comparableClassFor(Object x) {
            if (x instanceof Comparable) {
                Class<?> c; Type[] ts, as; Type t; ParameterizedType p;
                if ((c = x.getClass()) == String.class) // bypass checks
                    return c;
                if ((ts = c.getGenericInterfaces()) != null) {
                    for (int i = 0; i < ts.length; ++i) {
                        if (((t = ts[i]) instanceof ParameterizedType) &&
                            ((p = (ParameterizedType)t).getRawType() ==
                             Comparable.class) &&
                            (as = p.getActualTypeArguments()) != null &&
                            as.length == 1 && as[0] == c) // type arg is c
                            return c;
                    }
                }
            }
            return null;
        }
    
        /**
         * Returns k.compareTo(x) if x matches kc (k's screened comparable
         * class), else 0.
         */
        @SuppressWarnings({"rawtypes","unchecked"}) // for cast to Comparable
        static int compareComparables(Class<?> kc, Object k, Object x) {
            return (x == null || x.getClass() != kc ? 0 :
                    ((Comparable)k).compareTo(x));
        }
### int tableSizeFor(int cap)
##### 保证返回2的幂
##### 参考：https://blog.csdn.net/fan2012huan/article/details/51097331 
##### 参考：https://www.jianshu.com/p/020b76824aa0
##### n!=0时，如下：
```
    ------------------------------------------------
    0   1   0   0   0    <-----  n=8 
    0   0   1   0   0    <-----  n >>> 1
    0   1   1   0   0    <-----  n|=n >>> 1
    0   0   0   1   1    <-----  n >>> 2
    0   1   1   1   1    <-----  n|=n >>> 2
    0   0   0   0   0    <-----  n >>> 4 
    0   1   1   1   1    <-----  n|=n >>> 4
    0   0   0   0   0    <-----  n >>> 8 
    0   1   1   1   1    <-----  n|=n >>> 8  
    0   0   0   0   0    <-----  n >>> 16 
    0   1   1   1   1    <-----  n|=n >>> 16  
    ------------------------------------------------
    + 如上算法会使从最高位的1开始，低位方向全部转化为1，最后：n+1=2的幂    
```    
#####         
        /**
         * Returns a power of two size for the given target capacity.
         */
        static final int tableSizeFor(int cap) {
            int n = cap - 1;
            n |= n >>> 1;
            n |= n >>> 2;
            n |= n >>> 4;
            n |= n >>> 8;
            n |= n >>> 16;
            return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
        }
属性
---    
        /* ---------------- Fields -------------- */
### table，Node数组，大小：2的幂
##### 第一次使用时初始化，在必要时扩容。 当分配内存时，长度总是2的幂次  
        /**
         * The table, initialized on first use, and resized as
         * necessary. When allocated, length is always a power of two.
         * (We also tolerate length zero in some operations to allow
         * bootstrapping mechanics that are currently not needed.)
         *
         *
         */
        transient Node<K,V>[] table;
### entrySet，持有缓存的entry集合    
        /**
         * Holds cached entrySet(). Note that AbstractMap fields are used
         * for keySet() and values().
         */
        transient Set<Entry<K,V>> entrySet;
### size，Map中k-v映射个数    
        /**
         * The number of key-value mappings contained in this map.
         */
        transient int size;
### modCount
#### map中元素个数的改变，由put/remove方法操作引起，用于迭代器的快速失败    
        /**
         * The number of times this HashMap has been structurally modified
         * Structural modifications are those that change the number of mappings in
         * the HashMap or otherwise modify its internal structure (e.g.,
         * rehash).  This field is used to make iterators on Collection-views of
         * the HashMap fail-fast.  (See ConcurrentModificationException).
         */
        transient int modCount;
### 变量threshold，下次进行扩容的 size 大小 ，
#### 第一次初始化DEFAULT_INITIAL_CAPACITY=16，加载因子=0.75，threshold=16*0.75=12 
#### 如果table 数组还没有申请空间，该值为 初始的数组容量 
        /**
         * The next size value at which to resize (capacity * load factor).
         *
         * @serial
         */
        // (The javadoc description is true upon serialization.
        // Additionally, if the table array has not been allocated, this
        // field holds the initial array capacity, or zero signifying
        // DEFAULT_INITIAL_CAPACITY.)
        int threshold;
### loadFactor,哈希表的加载因子    
        /**
         * The load factor for the hash table.
         *
         * @serial
         */
        final float loadFactor;
公开操作
---    
        /* ---------------- Public operations -------------- */
### 有参构造，初始化容量，加载因子
###    
        /**
         * Constructs an empty <tt>HashMap</tt> with the specified initial
         * capacity and load factor.
         *
         * @param  initialCapacity the initial capacity
         * @param  loadFactor      the load factor
         * @throws IllegalArgumentException if the initial capacity is negative
         *         or the load factor is nonpositive
         */
        public HashMap(int initialCapacity, float loadFactor) {
            if (initialCapacity < 0)
                throw new IllegalArgumentException("Illegal initial capacity: " +
                                                   initialCapacity);
            if (initialCapacity > MAXIMUM_CAPACITY)
                initialCapacity = MAXIMUM_CAPACITY;
            if (loadFactor <= 0 || Float.isNaN(loadFactor))
                throw new IllegalArgumentException("Illegal load factor: " +
                                                   loadFactor);
            this.loadFactor = loadFactor;
            this.threshold = tableSizeFor(initialCapacity);
        }
### 指定容量构造    
        /**
         * Constructs an empty <tt>HashMap</tt> with the specified initial
         * capacity and the default load factor (0.75).
         *
         * @param  initialCapacity the initial capacity.
         * @throws IllegalArgumentException if the initial capacity is negative.
         */
        public HashMap(int initialCapacity) {
            this(initialCapacity, DEFAULT_LOAD_FACTOR);
        }
### 默认无参构造，加载因子为DEFAULT_LOAD_FACTOR=0.75   
        /**
         * Constructs an empty <tt>HashMap</tt> with the default initial capacity
         * (16) and the default load factor (0.75).
         */
        public HashMap() {
            this.loadFactor = DEFAULT_LOAD_FACTOR; // all other fields defaulted
        }
    
        /**
         * Constructs a new <tt>HashMap</tt> with the same mappings as the
         * specified <tt>Map</tt>.  The <tt>HashMap</tt> is created with
         * default load factor (0.75) and an initial capacity sufficient to
         * hold the mappings in the specified <tt>Map</tt>.
         *
         * @param   m the map whose mappings are to be placed in this map
         * @throws  NullPointerException if the specified map is null
         */
        public HashMap(Map<? extends K, ? extends V> m) {
            this.loadFactor = DEFAULT_LOAD_FACTOR;
            putMapEntries(m, false);
        }
    
        /**
         * Implements Map.putAll and Map constructor
         *
         * @param m the map
         * @param evict false when initially constructing this map, else
         * true (relayed to method afterNodeInsertion).
         */
        final void putMapEntries(Map<? extends K, ? extends V> m, boolean evict) {
            int s = m.size();
            if (s > 0) {
                if (table == null) { // pre-size
                    float ft = ((float)s / loadFactor) + 1.0F;
                    int t = ((ft < (float)MAXIMUM_CAPACITY) ?
                             (int)ft : MAXIMUM_CAPACITY);
                    if (t > threshold)
                        threshold = tableSizeFor(t);
                }
                else if (s > threshold)
                    resize();
                for (Entry<? extends K, ? extends V> e : m.entrySet()) {
                    K key = e.getKey();
                    V value = e.getValue();
                    putVal(hash(key), key, value, false, evict);
                }
            }
        }
## size()
###    
        /**
         * Returns the number of key-value mappings in this map.
         *
         * @return the number of key-value mappings in this map
         */
        public int size() {
            return size;
        }
## isEmpty()
###    
        /**
         * Returns <tt>true</tt> if this map contains no key-value mappings.
         *
         * @return <tt>true</tt> if this map contains no key-value mappings
         */
        public boolean isEmpty() {
            return size == 0;
        }
### get(Object key) 
#### 实现方法：getNode(int hash,Object key) 
        /**
         * Returns the value to which the specified key is mapped,
         * or {@code null} if this map contains no mapping for the key.
         * 返回指定key映射的值，如果这个map中没有key的映射，返回null
         * <p>More formally, if this map contains a mapping from a key
         * {@code k} to a value {@code v} such that {@code (key==null ? k==null :
         * key.equals(k))}, then this method returns {@code v}; otherwise
         * it returns {@code null}.  (There can be at most one such mapping.)
         *
         * <p>A return value of {@code null} does not <i>necessarily</i>
         * indicate that the map contains no mapping for the key; it's also
         * possible that the map explicitly maps the key to {@code null}.
         * The {@link #containsKey containsKey} operation may be used to
         * distinguish these two cases.
         *
         * @see #put(Object, Object)
         */
        public V get(Object key) {
            Node<K,V> e;
            return (e = getNode(hash(key), key)) == null ? null : e.value;
        }
getNode(int hash,Object key) 
---
### 具体实现   
        /**
         * Implements Map.get and related methods
         *
         * @param hash hash for key
         * @param key the key
         * @return the node, or null if none
         */
        final Node<K,V> getNode(int hash, Object key) {
            Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
####    1. 确定数组下标   tab[(n - 1) & hash] 掩码 位与          
            if ((tab = table) != null && (n = tab.length) > 0 &&
                (first = tab[(n - 1) & hash]) != null) {
####    1.0 头节点=first = tab[(n - 1) & hash] ，哈希值相等并且 key是同一个对象或者key equals相等，返回第一个节点               
                if (first.hash == hash && // always check first node
                    ((k = first.key) == key || (key != null && key.equals(k))))
                    return first;
####    1.1 头节点不相等，遍历后驱节点，e=后驱节点                    
                if ((e = first.next) != null) {
#####   1.1.1 如果头节点是TreeNode，getTreeNode(hash, key)，从红黑树中查找该key               
                    if (first instanceof TreeNode)
                        return ((TreeNode<K,V>)first).getTreeNode(hash, key);
#####   1.2.1 如果头节点不是TreeNode，链表节点，遍历链表节点                       
                    do {
#####   1.2.3 如果相等返回 e                    
                        if (e.hash == hash &&
                            ((k = e.key) == key || (key != null && key.equals(k))))
                            return e;
                    } while ((e = e.next) != null);
                }
            }
            return null;
        }
containsKey(Object key)
---
### 实现方法：getNode(hash(key),key)    
        /**
         * Returns <tt>true</tt> if this map contains a mapping for the
         * specified key.
         *
         * @param   key   The key whose presence in this map is to be tested
         * @return <tt>true</tt> if this map contains a mapping for the specified
         * key.
         */
        public boolean containsKey(Object key) {
            return getNode(hash(key), key) != null;
        }
public V put(K key, V value)
---   
        /**
         * Associates the specified value with the specified key in this map.
         * If the map previously contained a mapping for the key, the old
         * value is replaced.
         * 关联指定的key与指定的值，如果map先前包含此key的映射，旧值被替换
         * @param key key with which the specified value is to be associated
         * @param value value to be associated with the specified key
         * @return the previous value associated with <tt>key</tt>, or
         *         <tt>null</tt> if there was no mapping for <tt>key</tt>.
         *         (A <tt>null</tt> return can also indicate that the map
         *         previously associated <tt>null</tt> with <tt>key</tt>.)
         */
        public V put(K key, V value) {
            return putVal(hash(key), key, value, false, true);
        }
    
        /**
         * Implements Map.put and related methods
         *
         * @param hash hash for key
         * @param key the key
         * @param value the value to put
         * @param onlyIfAbsent if true, don't change existing value
         * @param evict if false, the table is in creation mode.
         * @return previous value, or null if none
         */
        final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
                       boolean evict) {
---                       
##### 0.定义局部变量tab：Node数组；p：Node节点； n：数组长度；i：数组下标                      
            Node<K,V>[] tab; Node<K,V> p; int n, i;
##### 1.判断table数组是否为null 或者 table的长度是否为0 
            if ((tab = table) == null || (n = tab.length) == 0)
#####   1.1 扩容数组并赋值给tab，新数组的长度赋值给n
                n = (tab = resize()).length;
#####   2.判断数组在下标为i=(n - 1) & hash 处的Node节点是否为null             
            if ((p = tab[i = (n - 1) & hash]) == null)
######  2.0 为什么 采用掩码 和 位& 运算： i = (n - 1) & hash    
    数组长度n===2的幂
    n=2的3次幂：二进制表示为   1   0   0   0
    n=2的4次幂：二进制表示为   1   0   0   0   0
    n=2的5次幂：二进制表示为   1   0   0   0   0   0   
    

    n-1
    n=2的3次幂-1：二进制表示为   0   1   1   1
    n=2的4次幂-1：二进制表示为   0   1   1   1   1
    n=2的5次幂-1：二进制表示为   0   1   1   1   1   1   
   
    (n - 1) & hash 意义：截取hash的 后n位
    
    比如：77的二进制表示为：1001101，与2的3次幂 - 1 做&运算的结果二进制表示为：101，（十进制：5）
    结论1：任何一个数字与2的3次幂-1 做&运算的结果的取值范围是：000~111，（十进制：0~7），永远超不过2的3次幂
    结论2：设数组长度为2的n次幂，有整型数字x，那么，通过掩码&运算：（2的n次幂-1）& x 总能得到合法的数组下标
######  2.1 如果为null，创建节点newNode(hash, key, value, null) 并赋值给tab[i]          
                tab[i] = newNode(hash, key, value, null);
#####   3. 如果p=tab[i = (n - 1) & hash]) != null
            else {
######  3.0 判断p节点 是否与新增值相同，如果哈希相同并且节点key相同，声明一个新引用e指向p节点
                Node<K,V> e; K k;
                if (p.hash == hash &&
                    ((k = p.key) == key || (key != null && key.equals(k))))
                    e = p;
######  3.1 如果p节点是TreeNode，插入树节点 putTreeVal(this, tab, hash, key, value)                    
                else if (p instanceof TreeNode)
                    e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
######  3.2 如果p节点不是TreeNode，那么，是链表，循环链表节点进行判断                   
                else {
                    for (int binCount = 0; ; ++binCount) {
                        if ((e = p.next) == null) {
######  3.3 如果p.next==null，创建新节点newNode(hash, key, value, null)，作为p的后继节点，变量e指向p.next
                            p.next = newNode(hash, key, value, null);
######  3.4 TREEIFY_THRESHOLD=8，链表转换成红黑树                            
                            if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                                treeifyBin(tab, hash);
                            break;
                        }
######  3.5 如果e = p.next 与新增节点哈希 key相同，跳出循环                   
                        if (e.hash == hash &&
                            ((k = e.key) == key || (key != null && key.equals(k))))
                            break;
######  3.6 如果p.next 即不为空，也不与新增节点相同，继续把p节点变量后移: p=p.next ,或者p=e                           
                        p = e;
                    }
                }
#####   4. 如果原有节点的key与 新key相同，如何处理 新值  与 旧值              
                if (e != null) { // existing mapping for key
                    V oldValue = e.value;
#####   4.1 新值覆盖旧值得情况：onlyIfAbsent为false或者旧值为null                                        
                    if (!onlyIfAbsent || oldValue == null)
                        e.value = value;
                    afterNodeAccess(e);
                    return oldValue;
                }
            }
#####   5.修改次数 transient int modCount，指整个HashMap结构发生改变的次数，            
            ++modCount;
#####   6. threshold 扩容阈值判断           
            if (++size > threshold)
                resize();
            afterNodeInsertion(evict);
            return null;
        }
### final Node<K,V>[] resize()
#####   初始化 或者 扩容    
        /**
         * Initializes or doubles table size.  If null, allocates in
         * accord with initial capacity target held in field threshold.
         * Otherwise, because we are using power-of-two expansion, the
         * elements from each bin must either stay at same index, or move
         * with a power of two offset in the new table.
         *
         * @return the table
         */
        final Node<K,V>[] resize() {
####    1.扩容时，确定 oldThr,oldThr,newCap, newThr           
            Node<K,V>[] oldTab = table;
            int oldCap = (oldTab == null) ? 0 : oldTab.length;
            int oldThr = threshold;
            int newCap, newThr = 0;
            if (oldCap > 0) {
                if (oldCap >= MAXIMUM_CAPACITY) {
                    threshold = Integer.MAX_VALUE;
                    return oldTab;
                }
#####   1.1 扩容时，新的数组大小等于原数组的2倍，新的newThr 等于thr的2倍：newCap= oldCap << 1; newThr = oldThre << 1 ;                
                else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                         oldCap >= DEFAULT_INITIAL_CAPACITY)
                    newThr = oldThr << 1; // double threshold
            }
            else if (oldThr > 0) // initial capacity was placed in threshold
                newCap = oldThr;
            else {               // zero initial threshold signifies using defaults
#####   1.2 初始化h时 DEFAULT_INITIAL_CAPACITY=16,newThre=0.75*16 = 12       
                newCap = DEFAULT_INITIAL_CAPACITY;
                newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
            }
            if (newThr == 0) {
                float ft = (float)newCap * loadFactor;
                newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                          (int)ft : Integer.MAX_VALUE);
            }
            threshold = newThr;
#####   2. 创建数组     (Node<K,V>[])new Node[newCap]       
            @SuppressWarnings({"rawtypes","unchecked"})
                Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
            table = newTab;
#####   3. 迁移元素            
            if (oldTab != null) {
#####   3.1 遍历旧oldTab 数组，在每个数组中 遍历链表节点            
                for (int j = 0; j < oldCap; ++j) {
                    Node<K,V> e;
                    if ((e = oldTab[j]) != null) {
#####   3.2 旧引用置空，变量e指向旧元素                    
                        oldTab[j] = null;
#####   3.2 如果旧元素的next引用为null，即旧表仅有一个元素，e赋值给新表                        
                        if (e.next == null)
                            newTab[e.hash & (newCap - 1)] = e;
#####   3.3 如果e是树节点，加入》》》》》》                           
                        else if (e instanceof TreeNode)
                            ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
#####   3.4 链表，根据 (e.hash & oldCap) == 0 把链表节点分组，两组，为什么采用这种分组方法？                         
                        else { // preserve order
                            Node<K,V> loHead = null, loTail = null;
                            Node<K,V> hiHead = null, hiTail = null;
                            Node<K,V> next;
                            do {
                                next = e.next;
                                if ((e.hash & oldCap) == 0) {
                                    if (loTail == null)
                                        loHead = e;
                                    else
                                        loTail.next = e;
                                    loTail = e;
                                }
                                else {
                                    if (hiTail == null)
                                        hiHead = e;
                                    else
                                        hiTail.next = e;
                                    hiTail = e;
                                }
                            } while ((e = next) != null);
#####   3.5 分组后的两个链表 loHead与hihead ，分别放在位置newTab[j] 与 newTab[j + oldCap]中                          
                            if (loTail != null) {
                                loTail.next = null;
                                newTab[j] = loHead;
                            }
                            if (hiTail != null) {
                                hiTail.next = null;
                                newTab[j + oldCap] = hiHead;
                            }
                        }
                    }
                }
            }
            return newTab;
        }
### treeifyBin(Node<K,V>[] tab, int hash)，给定哈希值得桶，替换该桶中所有的链表节点
        /**
         * Replaces all linked nodes in bin at index for given hash unless
         * table is too small, in which case resizes instead.
         */
        final void treeifyBin(Node<K,V>[] tab, int hash) {
            int n, index; Node<K,V> e;
####    1. 如果数组==null 或者 数组大小 < MIN_TREEIFY_CAPACITY(=64) ,进行扩容          
            if (tab == null || (n = tab.length) < MIN_TREEIFY_CAPACITY)
                resize();
####    2.  否则，数组大小>MIN_TREEIFY_CAPACITY(64),需要树化，  tab[index = (n - 1) & hash] 指定hash数组下标e != null,指链表的头结点不为null，遍历链表转换红黑树               
            else if ((e = tab[index = (n - 1) & hash]) != null) {
####    2.1     定义红黑树的头尾节点引用
                TreeNode<K,V> hd = null, tl = null;
                do {
####    2.2                     
                    TreeNode<K,V> p = replacementTreeNode(e, null);
####    2.3 尾节点==null，头节点指向p，(确定头节点)                   
                    if (tl == null)
                        hd = p;
####    2.4 尾节点!=null，p的前驱指向尾节点，尾节点的后驱节点指向p                        
                    else {
                        p.prev = tl;
                        tl.next = p;
                    }
####    2.5 尾节点指向p                    
                    tl = p;
                } while ((e = e.next) != null);
####    3.  tab[index] 头节点  指向 红黑树的 头节点                
                if ((tab[index] = hd) != null)
                    hd.treeify(tab);
            }
        }
    
        /**
         * Copies all of the mappings from the specified map to this map.
         * These mappings will replace any mappings that this map had for
         * any of the keys currently in the specified map.
         *
         * @param m mappings to be stored in this map
         * @throws NullPointerException if the specified map is null
         */
        public void putAll(Map<? extends K, ? extends V> m) {
            putMapEntries(m, true);
        }
    
        /**
         * Removes the mapping for the specified key from this map if present.
         *
         * @param  key key whose mapping is to be removed from the map
         * @return the previous value associated with <tt>key</tt>, or
         *         <tt>null</tt> if there was no mapping for <tt>key</tt>.
         *         (A <tt>null</tt> return can also indicate that the map
         *         previously associated <tt>null</tt> with <tt>key</tt>.)
         */
        public V remove(Object key) {
            Node<K,V> e;
            return (e = removeNode(hash(key), key, null, false, true)) == null ?
                null : e.value;
        }
    
        /**
         * Implements Map.remove and related methods
         *
         * @param hash hash for key
         * @param key the key
         * @param value the value to match if matchValue, else ignored
         * @param matchValue if true only remove if value is equal
         * @param movable if false do not move other nodes while removing
         * @return the node, or null if none
         */
        final Node<K,V> removeNode(int hash, Object key, Object value,
                                   boolean matchValue, boolean movable) {
            Node<K,V>[] tab; Node<K,V> p; int n, index;
            if ((tab = table) != null && (n = tab.length) > 0 &&
                (p = tab[index = (n - 1) & hash]) != null) {
                Node<K,V> node = null, e; K k; V v;
                if (p.hash == hash &&
                    ((k = p.key) == key || (key != null && key.equals(k))))
                    node = p;
                else if ((e = p.next) != null) {
                    if (p instanceof TreeNode)
                        node = ((TreeNode<K,V>)p).getTreeNode(hash, key);
                    else {
                        do {
                            if (e.hash == hash &&
                                ((k = e.key) == key ||
                                 (key != null && key.equals(k)))) {
                                node = e;
                                break;
                            }
                            p = e;
                        } while ((e = e.next) != null);
                    }
                }
                if (node != null && (!matchValue || (v = node.value) == value ||
                                     (value != null && value.equals(v)))) {
                    if (node instanceof TreeNode)
                        ((TreeNode<K,V>)node).removeTreeNode(this, tab, movable);
                    else if (node == p)
                        tab[index] = node.next;
                    else
                        p.next = node.next;
                    ++modCount;
                    --size;
                    afterNodeRemoval(node);
                    return node;
                }
            }
            return null;
        }
    
        /**
         * Removes all of the mappings from this map.
         * The map will be empty after this call returns.
         */
        public void clear() {
            Node<K,V>[] tab;
            modCount++;
            if ((tab = table) != null && size > 0) {
                size = 0;
                for (int i = 0; i < tab.length; ++i)
                    tab[i] = null;
            }
        }
    
        /**
         * Returns <tt>true</tt> if this map maps one or more keys to the
         * specified value.
         *
         * @param value value whose presence in this map is to be tested
         * @return <tt>true</tt> if this map maps one or more keys to the
         *         specified value
         */
        public boolean containsValue(Object value) {
            Node<K,V>[] tab; V v;
            if ((tab = table) != null && size > 0) {
                for (int i = 0; i < tab.length; ++i) {
                    for (Node<K,V> e = tab[i]; e != null; e = e.next) {
                        if ((v = e.value) == value ||
                            (value != null && value.equals(v)))
                            return true;
                    }
                }
            }
            return false;
        }
    
        /**
         * Returns a {@link Set} view of the keys contained in this map.
         * The set is backed by the map, so changes to the map are
         * reflected in the set, and vice-versa.  If the map is modified
         * while an iteration over the set is in progress (except through
         * the iterator's own <tt>remove</tt> operation), the results of
         * the iteration are undefined.  The set supports element removal,
         * which removes the corresponding mapping from the map, via the
         * <tt>Iterator.remove</tt>, <tt>Set.remove</tt>,
         * <tt>removeAll</tt>, <tt>retainAll</tt>, and <tt>clear</tt>
         * operations.  It does not support the <tt>add</tt> or <tt>addAll</tt>
         * operations.
         *
         * @return a set view of the keys contained in this map
         */
        public Set<K> keySet() {
            Set<K> ks;
            return (ks = keySet) == null ? (keySet = new KeySet()) : ks;
        }
### 类 KeySet  
        final class KeySet extends AbstractSet<K> {
            public final int size()                 { return size; }
            public final void clear()               { HashMap.this.clear(); }
            public final Iterator<K> iterator()     { return new KeyIterator(); }
            public final boolean contains(Object o) { return containsKey(o); }
            public final boolean remove(Object key) {
                return removeNode(hash(key), key, null, false, true) != null;
            }
            public final Spliterator<K> spliterator() {
                return new KeySpliterator<>(HashMap.this, 0, -1, 0, 0);
            }
            public final void forEach(Consumer<? super K> action) {
                Node<K,V>[] tab;
                if (action == null)
                    throw new NullPointerException();
                if (size > 0 && (tab = table) != null) {
                    int mc = modCount;
                    for (int i = 0; i < tab.length; ++i) {
                        for (Node<K,V> e = tab[i]; e != null; e = e.next)
                            action.accept(e.key);
                    }
                    if (modCount != mc)
                        throw new ConcurrentModificationException();
                }
            }
        }
    
        /**
         * Returns a {@link Collection} view of the values contained in this map.
         * The collection is backed by the map, so changes to the map are
         * reflected in the collection, and vice-versa.  If the map is
         * modified while an iteration over the collection is in progress
         * (except through the iterator's own <tt>remove</tt> operation),
         * the results of the iteration are undefined.  The collection
         * supports element removal, which removes the corresponding
         * mapping from the map, via the <tt>Iterator.remove</tt>,
         * <tt>Collection.remove</tt>, <tt>removeAll</tt>,
         * <tt>retainAll</tt> and <tt>clear</tt> operations.  It does not
         * support the <tt>add</tt> or <tt>addAll</tt> operations.
         *
         * @return a view of the values contained in this map
         */
        public Collection<V> values() {
            Collection<V> vs;
            return (vs = values) == null ? (values = new Values()) : vs;
        }
    
        final class Values extends AbstractCollection<V> {
            public final int size()                 { return size; }
            public final void clear()               { HashMap.this.clear(); }
            public final Iterator<V> iterator()     { return new ValueIterator(); }
            public final boolean contains(Object o) { return containsValue(o); }
            public final Spliterator<V> spliterator() {
                return new ValueSpliterator<>(HashMap.this, 0, -1, 0, 0);
            }
            public final void forEach(Consumer<? super V> action) {
                Node<K,V>[] tab;
                if (action == null)
                    throw new NullPointerException();
                if (size > 0 && (tab = table) != null) {
                    int mc = modCount;
                    for (int i = 0; i < tab.length; ++i) {
                        for (Node<K,V> e = tab[i]; e != null; e = e.next)
                            action.accept(e.value);
                    }
                    if (modCount != mc)
                        throw new ConcurrentModificationException();
                }
            }
        }
    
        /**
         * Returns a {@link Set} view of the mappings contained in this map.
         * The set is backed by the map, so changes to the map are
         * reflected in the set, and vice-versa.  If the map is modified
         * while an iteration over the set is in progress (except through
         * the iterator's own <tt>remove</tt> operation, or through the
         * <tt>setValue</tt> operation on a map entry returned by the
         * iterator) the results of the iteration are undefined.  The set
         * supports element removal, which removes the corresponding
         * mapping from the map, via the <tt>Iterator.remove</tt>,
         * <tt>Set.remove</tt>, <tt>removeAll</tt>, <tt>retainAll</tt> and
         * <tt>clear</tt> operations.  It does not support the
         * <tt>add</tt> or <tt>addAll</tt> operations.
         *
         * @return a set view of the mappings contained in this map
         */
        public Set<Entry<K,V>> entrySet() {
            Set<Entry<K,V>> es;
            return (es = entrySet) == null ? (entrySet = new EntrySet()) : es;
        }
    
        final class EntrySet extends AbstractSet<Entry<K,V>> {
            public final int size()                 { return size; }
            public final void clear()               { HashMap.this.clear(); }
            public final Iterator<Entry<K,V>> iterator() {
                return new EntryIterator();
            }
            public final boolean contains(Object o) {
                if (!(o instanceof Map.Entry))
                    return false;
                Entry<?,?> e = (Entry<?,?>) o;
                Object key = e.getKey();
                Node<K,V> candidate = getNode(hash(key), key);
                return candidate != null && candidate.equals(e);
            }
            public final boolean remove(Object o) {
                if (o instanceof Map.Entry) {
                    Entry<?,?> e = (Entry<?,?>) o;
                    Object key = e.getKey();
                    Object value = e.getValue();
                    return removeNode(hash(key), key, value, true, true) != null;
                }
                return false;
            }
            public final Spliterator<Entry<K,V>> spliterator() {
                return new EntrySpliterator<>(HashMap.this, 0, -1, 0, 0);
            }
            public final void forEach(Consumer<? super Entry<K,V>> action) {
                Node<K,V>[] tab;
                if (action == null)
                    throw new NullPointerException();
                if (size > 0 && (tab = table) != null) {
                    int mc = modCount;
                    for (int i = 0; i < tab.length; ++i) {
                        for (Node<K,V> e = tab[i]; e != null; e = e.next)
                            action.accept(e);
                    }
                    if (modCount != mc)
                        throw new ConcurrentModificationException();
                }
            }
        }
    
        // Overrides of JDK8 Map extension methods
    
        @Override
        public V getOrDefault(Object key, V defaultValue) {
            Node<K,V> e;
            return (e = getNode(hash(key), key)) == null ? defaultValue : e.value;
        }
    
        @Override
        public V putIfAbsent(K key, V value) {
            return putVal(hash(key), key, value, true, true);
        }
    
        @Override
        public boolean remove(Object key, Object value) {
            return removeNode(hash(key), key, value, true, true) != null;
        }
    
        @Override
        public boolean replace(K key, V oldValue, V newValue) {
            Node<K,V> e; V v;
            if ((e = getNode(hash(key), key)) != null &&
                ((v = e.value) == oldValue || (v != null && v.equals(oldValue)))) {
                e.value = newValue;
                afterNodeAccess(e);
                return true;
            }
            return false;
        }
    
        @Override
        public V replace(K key, V value) {
            Node<K,V> e;
            if ((e = getNode(hash(key), key)) != null) {
                V oldValue = e.value;
                e.value = value;
                afterNodeAccess(e);
                return oldValue;
            }
            return null;
        }
    
        @Override
        public V computeIfAbsent(K key,
                                 Function<? super K, ? extends V> mappingFunction) {
            if (mappingFunction == null)
                throw new NullPointerException();
            int hash = hash(key);
            Node<K,V>[] tab; Node<K,V> first; int n, i;
            int binCount = 0;
            TreeNode<K,V> t = null;
            Node<K,V> old = null;
            if (size > threshold || (tab = table) == null ||
                (n = tab.length) == 0)
                n = (tab = resize()).length;
            if ((first = tab[i = (n - 1) & hash]) != null) {
                if (first instanceof TreeNode)
                    old = (t = (TreeNode<K,V>)first).getTreeNode(hash, key);
                else {
                    Node<K,V> e = first; K k;
                    do {
                        if (e.hash == hash &&
                            ((k = e.key) == key || (key != null && key.equals(k)))) {
                            old = e;
                            break;
                        }
                        ++binCount;
                    } while ((e = e.next) != null);
                }
                V oldValue;
                if (old != null && (oldValue = old.value) != null) {
                    afterNodeAccess(old);
                    return oldValue;
                }
            }
            V v = mappingFunction.apply(key);
            if (v == null) {
                return null;
            } else if (old != null) {
                old.value = v;
                afterNodeAccess(old);
                return v;
            }
            else if (t != null)
                t.putTreeVal(this, tab, hash, key, v);
            else {
                tab[i] = newNode(hash, key, v, first);
                if (binCount >= TREEIFY_THRESHOLD - 1)
                    treeifyBin(tab, hash);
            }
            ++modCount;
            ++size;
            afterNodeInsertion(true);
            return v;
        }
    
        public V computeIfPresent(K key,
                                  BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
            if (remappingFunction == null)
                throw new NullPointerException();
            Node<K,V> e; V oldValue;
            int hash = hash(key);
            if ((e = getNode(hash, key)) != null &&
                (oldValue = e.value) != null) {
                V v = remappingFunction.apply(key, oldValue);
                if (v != null) {
                    e.value = v;
                    afterNodeAccess(e);
                    return v;
                }
                else
                    removeNode(hash, key, null, false, true);
            }
            return null;
        }
    
        @Override
        public V compute(K key,
                         BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
            if (remappingFunction == null)
                throw new NullPointerException();
            int hash = hash(key);
            Node<K,V>[] tab; Node<K,V> first; int n, i;
            int binCount = 0;
            TreeNode<K,V> t = null;
            Node<K,V> old = null;
            if (size > threshold || (tab = table) == null ||
                (n = tab.length) == 0)
                n = (tab = resize()).length;
            if ((first = tab[i = (n - 1) & hash]) != null) {
                if (first instanceof TreeNode)
                    old = (t = (TreeNode<K,V>)first).getTreeNode(hash, key);
                else {
                    Node<K,V> e = first; K k;
                    do {
                        if (e.hash == hash &&
                            ((k = e.key) == key || (key != null && key.equals(k)))) {
                            old = e;
                            break;
                        }
                        ++binCount;
                    } while ((e = e.next) != null);
                }
            }
            V oldValue = (old == null) ? null : old.value;
            V v = remappingFunction.apply(key, oldValue);
            if (old != null) {
                if (v != null) {
                    old.value = v;
                    afterNodeAccess(old);
                }
                else
                    removeNode(hash, key, null, false, true);
            }
            else if (v != null) {
                if (t != null)
                    t.putTreeVal(this, tab, hash, key, v);
                else {
                    tab[i] = newNode(hash, key, v, first);
                    if (binCount >= TREEIFY_THRESHOLD - 1)
                        treeifyBin(tab, hash);
                }
                ++modCount;
                ++size;
                afterNodeInsertion(true);
            }
            return v;
        }
    
        @Override
        public V merge(K key, V value,
                       BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
            if (value == null)
                throw new NullPointerException();
            if (remappingFunction == null)
                throw new NullPointerException();
            int hash = hash(key);
            Node<K,V>[] tab; Node<K,V> first; int n, i;
            int binCount = 0;
            TreeNode<K,V> t = null;
            Node<K,V> old = null;
            if (size > threshold || (tab = table) == null ||
                (n = tab.length) == 0)
                n = (tab = resize()).length;
            if ((first = tab[i = (n - 1) & hash]) != null) {
                if (first instanceof TreeNode)
                    old = (t = (TreeNode<K,V>)first).getTreeNode(hash, key);
                else {
                    Node<K,V> e = first; K k;
                    do {
                        if (e.hash == hash &&
                            ((k = e.key) == key || (key != null && key.equals(k)))) {
                            old = e;
                            break;
                        }
                        ++binCount;
                    } while ((e = e.next) != null);
                }
            }
            if (old != null) {
                V v;
                if (old.value != null)
                    v = remappingFunction.apply(old.value, value);
                else
                    v = value;
                if (v != null) {
                    old.value = v;
                    afterNodeAccess(old);
                }
                else
                    removeNode(hash, key, null, false, true);
                return v;
            }
            if (value != null) {
                if (t != null)
                    t.putTreeVal(this, tab, hash, key, value);
                else {
                    tab[i] = newNode(hash, key, value, first);
                    if (binCount >= TREEIFY_THRESHOLD - 1)
                        treeifyBin(tab, hash);
                }
                ++modCount;
                ++size;
                afterNodeInsertion(true);
            }
            return value;
        }
    
        @Override
        public void forEach(BiConsumer<? super K, ? super V> action) {
            Node<K,V>[] tab;
            if (action == null)
                throw new NullPointerException();
            if (size > 0 && (tab = table) != null) {
                int mc = modCount;
                for (int i = 0; i < tab.length; ++i) {
                    for (Node<K,V> e = tab[i]; e != null; e = e.next)
                        action.accept(e.key, e.value);
                }
                if (modCount != mc)
                    throw new ConcurrentModificationException();
            }
        }
    
        @Override
        public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
            Node<K,V>[] tab;
            if (function == null)
                throw new NullPointerException();
            if (size > 0 && (tab = table) != null) {
                int mc = modCount;
                for (int i = 0; i < tab.length; ++i) {
                    for (Node<K,V> e = tab[i]; e != null; e = e.next) {
                        e.value = function.apply(e.key, e.value);
                    }
                }
                if (modCount != mc)
                    throw new ConcurrentModificationException();
            }
        }
克隆及序列化 
---   
        /* ------------------------------------------------------------ */
        // Cloning and serialization
    
        /**
         * Returns a shallow copy of this <tt>HashMap</tt> instance: the keys and
         * values themselves are not cloned.
         *
         * @return a shallow copy of this map
         */
        @SuppressWarnings("unchecked")
        @Override
        public Object clone() {
            HashMap<K,V> result;
            try {
                result = (HashMap<K,V>)super.clone();
            } catch (CloneNotSupportedException e) {
                // this shouldn't happen, since we are Cloneable
                throw new InternalError(e);
            }
            result.reinitialize();
            result.putMapEntries(this, false);
            return result;
        }
    
        // These methods are also used when serializing HashSets
        final float loadFactor() { return loadFactor; }
        final int capacity() {
            return (table != null) ? table.length :
                (threshold > 0) ? threshold :
                DEFAULT_INITIAL_CAPACITY;
        }
    
        /**
         * Save the state of the <tt>HashMap</tt> instance to a stream (i.e.,
         * serialize it).
         *
         * @serialData The <i>capacity</i> of the HashMap (the length of the
         *             bucket array) is emitted (int), followed by the
         *             <i>size</i> (an int, the number of key-value
         *             mappings), followed by the key (Object) and value (Object)
         *             for each key-value mapping.  The key-value mappings are
         *             emitted in no particular order.
         */
        private void writeObject(java.io.ObjectOutputStream s)
            throws IOException {
            int buckets = capacity();
            // Write out the threshold, loadfactor, and any hidden stuff
            s.defaultWriteObject();
            s.writeInt(buckets);
            s.writeInt(size);
            internalWriteEntries(s);
        }
    
        /**
         * Reconstitute the {@code HashMap} instance from a stream (i.e.,
         * deserialize it).
         */
        private void readObject(java.io.ObjectInputStream s)
            throws IOException, ClassNotFoundException {
            // Read in the threshold (ignored), loadfactor, and any hidden stuff
            s.defaultReadObject();
            reinitialize();
            if (loadFactor <= 0 || Float.isNaN(loadFactor))
                throw new InvalidObjectException("Illegal load factor: " +
                                                 loadFactor);
            s.readInt();                // Read and ignore number of buckets
            int mappings = s.readInt(); // Read number of mappings (size)
            if (mappings < 0)
                throw new InvalidObjectException("Illegal mappings count: " +
                                                 mappings);
            else if (mappings > 0) { // (if zero, use defaults)
                // Size the table using given load factor only if within
                // range of 0.25...4.0
                float lf = Math.min(Math.max(0.25f, loadFactor), 4.0f);
                float fc = (float)mappings / lf + 1.0f;
                int cap = ((fc < DEFAULT_INITIAL_CAPACITY) ?
                           DEFAULT_INITIAL_CAPACITY :
                           (fc >= MAXIMUM_CAPACITY) ?
                           MAXIMUM_CAPACITY :
                           tableSizeFor((int)fc));
                float ft = (float)cap * lf;
                threshold = ((cap < MAXIMUM_CAPACITY && ft < MAXIMUM_CAPACITY) ?
                             (int)ft : Integer.MAX_VALUE);
                @SuppressWarnings({"rawtypes","unchecked"})
                    Node<K,V>[] tab = (Node<K,V>[])new Node[cap];
                table = tab;
    
                // Read the keys and values, and put the mappings in the HashMap
                for (int i = 0; i < mappings; i++) {
                    @SuppressWarnings("unchecked")
                        K key = (K) s.readObject();
                    @SuppressWarnings("unchecked")
                        V value = (V) s.readObject();
                    putVal(hash(key), key, value, false, false);
                }
            }
        }
抽象迭代器，带有fast-fail 标识   
--- 
        /* ------------------------------------------------------------ */
        // iterators
    
        abstract class HashIterator {
            Node<K,V> next;        // next entry to return
            Node<K,V> current;     // current entry
####    expectedModCount属性 期望modCount，            
            int expectedModCount;  // for fast-fail
            int index;             // current slot
    
            HashIterator() {
####    expectedModCount，构造发生时的 modCount,后续的put/remove会引起 expectedModCount != modCount(for fast-fail)          
                expectedModCount = modCount;
                Node<K,V>[] t = table;
                current = next = null;
                index = 0;
                if (t != null && size > 0) { // advance to first entry
                    do {} while (index < t.length && (next = t[index++]) == null);
                }
            }
    
            public final boolean hasNext() {
                return next != null;
            }
#### 迭代器遍历，fast-fail=>判断modCount != expectedModCount    
            final Node<K,V> nextNode() {
                Node<K,V>[] t;
                Node<K,V> e = next;
                if (modCount != expectedModCount)
                    throw new ConcurrentModificationException();
                if (e == null)
                    throw new NoSuchElementException();
                if ((next = (current = e).next) == null && (t = table) != null) {
                    do {} while (index < t.length && (next = t[index++]) == null);
                }
                return e;
            }
#### 迭代器删除操作    
            public final void remove() {
                Node<K,V> p = current;
                if (p == null)
                    throw new IllegalStateException();
#####   如果容器的modCount 和 期望值不相等 抛出异常                    
                if (modCount != expectedModCount)
                    throw new ConcurrentModificationException();
                current = null;
                K key = p.key;
                removeNode(hash(key), key, null, false, false);
#####   删除节点后modCount被更新，expectedModCount同时被更新                
                expectedModCount = modCount;
            }
        }
### 迭代器 - KeyIterator   
        final class KeyIterator extends HashIterator
            implements Iterator<K> {
            public final K next() { return nextNode().key; }
        }
### 迭代器 - ValueIterator    
        final class ValueIterator extends HashIterator
            implements Iterator<V> {
            public final V next() { return nextNode().value; }
        }
### 迭代器 - EntryIterator    
        final class EntryIterator extends HashIterator
            implements Iterator<Entry<K,V>> {
            public final Entry<K,V> next() { return nextNode(); }
        }
    
        /* ------------------------------------------------------------ */
        // spliterators
    
        static class HashMapSpliterator<K,V> {
            final HashMap<K,V> map;
            Node<K,V> current;          // current node
            int index;                  // current index, modified on advance/split
            int fence;                  // one past last index
            int est;                    // size estimate
            int expectedModCount;       // for comodification checks
    
            HashMapSpliterator(HashMap<K,V> m, int origin,
                               int fence, int est,
                               int expectedModCount) {
                this.map = m;
                this.index = origin;
                this.fence = fence;
                this.est = est;
                this.expectedModCount = expectedModCount;
            }
    
            final int getFence() { // initialize fence and size on first use
                int hi;
                if ((hi = fence) < 0) {
                    HashMap<K,V> m = map;
                    est = m.size;
                    expectedModCount = m.modCount;
                    Node<K,V>[] tab = m.table;
                    hi = fence = (tab == null) ? 0 : tab.length;
                }
                return hi;
            }
    
            public final long estimateSize() {
                getFence(); // force init
                return (long) est;
            }
        }
    
        static final class KeySpliterator<K,V>
            extends HashMapSpliterator<K,V>
            implements Spliterator<K> {
            KeySpliterator(HashMap<K,V> m, int origin, int fence, int est,
                           int expectedModCount) {
                super(m, origin, fence, est, expectedModCount);
            }
    
            public KeySpliterator<K,V> trySplit() {
                int hi = getFence(), lo = index, mid = (lo + hi) >>> 1;
                return (lo >= mid || current != null) ? null :
                    new KeySpliterator<>(map, lo, index = mid, est >>>= 1,
                                            expectedModCount);
            }
    
            public void forEachRemaining(Consumer<? super K> action) {
                int i, hi, mc;
                if (action == null)
                    throw new NullPointerException();
                HashMap<K,V> m = map;
                Node<K,V>[] tab = m.table;
                if ((hi = fence) < 0) {
                    mc = expectedModCount = m.modCount;
                    hi = fence = (tab == null) ? 0 : tab.length;
                }
                else
                    mc = expectedModCount;
                if (tab != null && tab.length >= hi &&
                    (i = index) >= 0 && (i < (index = hi) || current != null)) {
                    Node<K,V> p = current;
                    current = null;
                    do {
                        if (p == null)
                            p = tab[i++];
                        else {
                            action.accept(p.key);
                            p = p.next;
                        }
                    } while (p != null || i < hi);
                    if (m.modCount != mc)
                        throw new ConcurrentModificationException();
                }
            }
    
            public boolean tryAdvance(Consumer<? super K> action) {
                int hi;
                if (action == null)
                    throw new NullPointerException();
                Node<K,V>[] tab = map.table;
                if (tab != null && tab.length >= (hi = getFence()) && index >= 0) {
                    while (current != null || index < hi) {
                        if (current == null)
                            current = tab[index++];
                        else {
                            K k = current.key;
                            current = current.next;
                            action.accept(k);
                            if (map.modCount != expectedModCount)
                                throw new ConcurrentModificationException();
                            return true;
                        }
                    }
                }
                return false;
            }
    
            public int characteristics() {
                return (fence < 0 || est == map.size ? Spliterator.SIZED : 0) |
                    Spliterator.DISTINCT;
            }
        }
    
        static final class ValueSpliterator<K,V>
            extends HashMapSpliterator<K,V>
            implements Spliterator<V> {
            ValueSpliterator(HashMap<K,V> m, int origin, int fence, int est,
                             int expectedModCount) {
                super(m, origin, fence, est, expectedModCount);
            }
    
            public ValueSpliterator<K,V> trySplit() {
                int hi = getFence(), lo = index, mid = (lo + hi) >>> 1;
                return (lo >= mid || current != null) ? null :
                    new ValueSpliterator<>(map, lo, index = mid, est >>>= 1,
                                              expectedModCount);
            }
    
            public void forEachRemaining(Consumer<? super V> action) {
                int i, hi, mc;
                if (action == null)
                    throw new NullPointerException();
                HashMap<K,V> m = map;
                Node<K,V>[] tab = m.table;
                if ((hi = fence) < 0) {
                    mc = expectedModCount = m.modCount;
                    hi = fence = (tab == null) ? 0 : tab.length;
                }
                else
                    mc = expectedModCount;
                if (tab != null && tab.length >= hi &&
                    (i = index) >= 0 && (i < (index = hi) || current != null)) {
                    Node<K,V> p = current;
                    current = null;
                    do {
                        if (p == null)
                            p = tab[i++];
                        else {
                            action.accept(p.value);
                            p = p.next;
                        }
                    } while (p != null || i < hi);
                    if (m.modCount != mc)
                        throw new ConcurrentModificationException();
                }
            }
    
            public boolean tryAdvance(Consumer<? super V> action) {
                int hi;
                if (action == null)
                    throw new NullPointerException();
                Node<K,V>[] tab = map.table;
                if (tab != null && tab.length >= (hi = getFence()) && index >= 0) {
                    while (current != null || index < hi) {
                        if (current == null)
                            current = tab[index++];
                        else {
                            V v = current.value;
                            current = current.next;
                            action.accept(v);
                            if (map.modCount != expectedModCount)
                                throw new ConcurrentModificationException();
                            return true;
                        }
                    }
                }
                return false;
            }
    
            public int characteristics() {
                return (fence < 0 || est == map.size ? Spliterator.SIZED : 0);
            }
        }
    
        static final class EntrySpliterator<K,V>
            extends HashMapSpliterator<K,V>
            implements Spliterator<Entry<K,V>> {
            EntrySpliterator(HashMap<K,V> m, int origin, int fence, int est,
                             int expectedModCount) {
                super(m, origin, fence, est, expectedModCount);
            }
    
            public EntrySpliterator<K,V> trySplit() {
                int hi = getFence(), lo = index, mid = (lo + hi) >>> 1;
                return (lo >= mid || current != null) ? null :
                    new EntrySpliterator<>(map, lo, index = mid, est >>>= 1,
                                              expectedModCount);
            }
    
            public void forEachRemaining(Consumer<? super Entry<K,V>> action) {
                int i, hi, mc;
                if (action == null)
                    throw new NullPointerException();
                HashMap<K,V> m = map;
                Node<K,V>[] tab = m.table;
                if ((hi = fence) < 0) {
                    mc = expectedModCount = m.modCount;
                    hi = fence = (tab == null) ? 0 : tab.length;
                }
                else
                    mc = expectedModCount;
                if (tab != null && tab.length >= hi &&
                    (i = index) >= 0 && (i < (index = hi) || current != null)) {
                    Node<K,V> p = current;
                    current = null;
                    do {
                        if (p == null)
                            p = tab[i++];
                        else {
                            action.accept(p);
                            p = p.next;
                        }
                    } while (p != null || i < hi);
                    if (m.modCount != mc)
                        throw new ConcurrentModificationException();
                }
            }
    
            public boolean tryAdvance(Consumer<? super Entry<K,V>> action) {
                int hi;
                if (action == null)
                    throw new NullPointerException();
                Node<K,V>[] tab = map.table;
                if (tab != null && tab.length >= (hi = getFence()) && index >= 0) {
                    while (current != null || index < hi) {
                        if (current == null)
                            current = tab[index++];
                        else {
                            Node<K,V> e = current;
                            current = current.next;
                            action.accept(e);
                            if (map.modCount != expectedModCount)
                                throw new ConcurrentModificationException();
                            return true;
                        }
                    }
                }
                return false;
            }
    
            public int characteristics() {
                return (fence < 0 || est == map.size ? Spliterator.SIZED : 0) |
                    Spliterator.DISTINCT;
            }
        }
    
        /* ------------------------------------------------------------ */
        // LinkedHashMap support
    
    
        /*
         * The following package-protected methods are designed to be
         * overridden by LinkedHashMap, but not by any other subclass.
         * Nearly all other internal methods are also package-protected
         * but are declared final, so can be used by LinkedHashMap, view
         * classes, and HashSet.
         */
    
        // Create a regular (non-tree) node
        Node<K,V> newNode(int hash, K key, V value, Node<K,V> next) {
            return new Node<>(hash, key, value, next);
        }
    
        // For conversion from TreeNodes to plain nodes
        Node<K,V> replacementNode(Node<K,V> p, Node<K,V> next) {
            return new Node<>(p.hash, p.key, p.value, next);
        }
    
        // Create a tree bin node
        TreeNode<K,V> newTreeNode(int hash, K key, V value, Node<K,V> next) {
            return new TreeNode<>(hash, key, value, next);
        }
    
        // For treeifyBin
        TreeNode<K,V> replacementTreeNode(Node<K,V> p, Node<K,V> next) {
            return new TreeNode<>(p.hash, p.key, p.value, next);
        }
    
        /**
         * Reset to initial default state.  Called by clone and readObject.
         */
        void reinitialize() {
            table = null;
            entrySet = null;
            keySet = null;
            values = null;
            modCount = 0;
            threshold = 0;
            size = 0;
        }
    
        // Callbacks to allow LinkedHashMap post-actions
        void afterNodeAccess(Node<K,V> p) { }
        void afterNodeInsertion(boolean evict) { }
        void afterNodeRemoval(Node<K,V> p) { }
    
        // Called only from writeObject, to ensure compatible ordering.
        void internalWriteEntries(java.io.ObjectOutputStream s) throws IOException {
            Node<K,V>[] tab;
            if (size > 0 && (tab = table) != null) {
                for (int i = 0; i < tab.length; ++i) {
                    for (Node<K,V> e = tab[i]; e != null; e = e.next) {
                        s.writeObject(e.key);
                        s.writeObject(e.value);
                    }
                }
            }
        }
红黑树定义  
---  
        /* ------------------------------------------------------------ */
        // Tree bins
    
        /**
         * Entry for Tree bins. Extends LinkedHashMap.Entry (which in turn
         * extends Node) so can be used as extension of either regular or
         * linked node.
         */
        static final class TreeNode<K,V> extends LinkedHashMap.Entry<K,V> {
            TreeNode<K,V> parent;  // red-black tree links
            TreeNode<K,V> left;
            TreeNode<K,V> right;
            TreeNode<K,V> prev;    // needed to unlink next upon deletion
            boolean red;
            TreeNode(int hash, K key, V val, Node<K,V> next) {
                super(hash, key, val, next);
            }
    
            /**
             * Returns root of tree containing this node.
             */
            final TreeNode<K,V> root() {
                for (TreeNode<K,V> r = this, p;;) {
                    if ((p = r.parent) == null)
                        return r;
                    r = p;
                }
            }
    
            /**
             * Ensures that the given root is the first node of its bin.
             */
            static <K,V> void moveRootToFront(Node<K,V>[] tab, TreeNode<K,V> root) {
                int n;
                if (root != null && tab != null && (n = tab.length) > 0) {
                    int index = (n - 1) & root.hash;
                    TreeNode<K,V> first = (TreeNode<K,V>)tab[index];
                    if (root != first) {
                        Node<K,V> rn;
                        tab[index] = root;
                        TreeNode<K,V> rp = root.prev;
                        if ((rn = root.next) != null)
                            ((TreeNode<K,V>)rn).prev = rp;
                        if (rp != null)
                            rp.next = rn;
                        if (first != null)
                            first.prev = root;
                        root.next = first;
                        root.prev = null;
                    }
                    assert checkInvariants(root);
                }
            }
### find(int h, Object k, Class<?> kc)   红黑树查找节点，从root开始
####    该树有序，折半查找，    
            /**
             * Finds the node starting at root p with the given hash and key.
             * The kc argument caches comparableClassFor(key) upon first use
             * comparing keys.
             */
            final TreeNode<K,V> find(int h, Object k, Class<?> kc) {
                TreeNode<K,V> p = this;
                do {
                    int ph, dir; K pk;
#####   pl 左子树，pr 右子树                    
                    TreeNode<K,V> pl = p.left, pr = p.right, q;
                    if ((ph = p.hash) > h)
                        p = pl;
                    else if (ph < h)
                        p = pr;
#####   如果p节点 key == k 或者 k equals p.key，返回p                        
                    else if ((pk = p.key) == k || (k != null && k.equals(pk)))
                        return p;
                    else if (pl == null)
                        p = pr;
                    else if (pr == null)
                        p = pl;
                    else if ((kc != null ||
                              (kc = comparableClassFor(k)) != null) &&
                             (dir = compareComparables(kc, k, pk)) != 0)
                        p = (dir < 0) ? pl : pr;
#####   递归查找                        
                    else if ((q = pr.find(h, k, kc)) != null)
                        return q;
                    else
                        p = pl;
                } while (p != null);
                return null;
            }
### getTreeNode(int h, Object k)
####    查找根节点
####    实现：find(h, k, null)   
            /**
             * Calls find for root node.
             */
            final TreeNode<K,V> getTreeNode(int h, Object k) {
                return ((parent != null) ? root() : this).find(h, k, null);
            }
    
            /**
             * Tie-breaking utility for ordering insertions when equal
             * hashCodes and non-comparable. We don't require a total
             * order, just a consistent insertion rule to maintain
             * equivalence across rebalancings. Tie-breaking further than
             * necessary simplifies testing a bit.
             */
            static int tieBreakOrder(Object a, Object b) {
                int d;
                if (a == null || b == null ||
                    (d = a.getClass().getName().
                     compareTo(b.getClass().getName())) == 0)
                    d = (System.identityHashCode(a) <= System.identityHashCode(b) ?
                         -1 : 1);
                return d;
            }
### 形成树，设置红黑树的属性值    
            /**
             * Forms tree of the nodes linked from this node.
             * @return root of tree
             */
            final void treeify(Node<K,V>[] tab) {
                TreeNode<K,V> root = null;
                for (TreeNode<K,V> x = this, next; x != null; x = next) {
                    next = (TreeNode<K,V>)x.next;
                    x.left = x.right = null;
####    1. 确定根节点                    
                    if (root == null) { 
                        x.parent = null;
                        x.red = false;
                        root = x;
                    }
                    else {
                        K k = x.key;
                        int h = x.hash;
                        Class<?> kc = null;
                        for (TreeNode<K,V> p = root;;) {
                            int dir, ph;
                            K pk = p.key;
                            if ((ph = p.hash) > h)
                                dir = -1;
                            else if (ph < h)
                                dir = 1;
                            else if ((kc == null &&
                                      (kc = comparableClassFor(k)) == null) ||
                                     (dir = compareComparables(kc, k, pk)) == 0)
                                dir = tieBreakOrder(k, pk);
    
                            TreeNode<K,V> xp = p;
                            if ((p = (dir <= 0) ? p.left : p.right) == null) {
                                x.parent = xp;
                                if (dir <= 0)
                                    xp.left = x;
                                else
                                    xp.right = x;
                                root = balanceInsertion(root, x);
                                break;
                            }
                        }
                    }
                }
                moveRootToFront(tab, root);
            }
    
            /**
             * Returns a list of non-TreeNodes replacing those linked from
             * this node.
             */
            final Node<K,V> untreeify(HashMap<K,V> map) {
                Node<K,V> hd = null, tl = null;
                for (Node<K,V> q = this; q != null; q = q.next) {
                    Node<K,V> p = map.replacementNode(q, null);
                    if (tl == null)
                        hd = p;
                    else
                        tl.next = p;
                    tl = p;
                }
                return hd;
            }
### putTreeVal(HashMap<K,V> map, Node<K,V>[] tab,int h, K k, V v)   红黑树版的putVal    
            /**
             * Tree version of putVal.
             */
            final TreeNode<K,V> putTreeVal(HashMap<K,V> map, Node<K,V>[] tab,
                                           int h, K k, V v) {
                Class<?> kc = null;
                boolean searched = false;
                TreeNode<K,V> root = (parent != null) ? root() : this;
####    1. 根节点开始遍历                
                for (TreeNode<K,V> p = root;;) {
                    int dir, ph; K pk;
                    if ((ph = p.hash) > h)
                        dir = -1;
                    else if (ph < h)
                        dir = 1;
                    else if ((pk = p.key) == k || (k != null && k.equals(pk)))
                        return p;
                    else if ((kc == null &&
                              (kc = comparableClassFor(k)) == null) ||
                             (dir = compareComparables(kc, k, pk)) == 0) {
                        if (!searched) {
                            TreeNode<K,V> q, ch;
                            searched = true;
                            if (((ch = p.left) != null &&
                                 (q = ch.find(h, k, kc)) != null) ||
                                ((ch = p.right) != null &&
                                 (q = ch.find(h, k, kc)) != null))
                                return q;
                        }
                        dir = tieBreakOrder(k, pk);
                    }
    
                    TreeNode<K,V> xp = p;
                    if ((p = (dir <= 0) ? p.left : p.right) == null) {
                        Node<K,V> xpn = xp.next;
                        TreeNode<K,V> x = map.newTreeNode(h, k, v, xpn);
                        if (dir <= 0)
                            xp.left = x;
                        else
                            xp.right = x;
                        xp.next = x;
                        x.parent = x.prev = xp;
                        if (xpn != null)
                            ((TreeNode<K,V>)xpn).prev = x;
                        moveRootToFront(tab, balanceInsertion(root, x));
                        return null;
                    }
                }
            }
    
            /**
             * Removes the given node, that must be present before this call.
             * This is messier than typical red-black deletion code because we
             * cannot swap the contents of an interior node with a leaf
             * successor that is pinned by "next" pointers that are accessible
             * independently during traversal. So instead we swap the tree
             * linkages. If the current tree appears to have too few nodes,
             * the bin is converted back to a plain bin. (The test triggers
             * somewhere between 2 and 6 nodes, depending on tree structure).
             */
            final void removeTreeNode(HashMap<K,V> map, Node<K,V>[] tab,
                                      boolean movable) {
                int n;
                if (tab == null || (n = tab.length) == 0)
                    return;
                int index = (n - 1) & hash;
                TreeNode<K,V> first = (TreeNode<K,V>)tab[index], root = first, rl;
                TreeNode<K,V> succ = (TreeNode<K,V>)next, pred = prev;
                if (pred == null)
                    tab[index] = first = succ;
                else
                    pred.next = succ;
                if (succ != null)
                    succ.prev = pred;
                if (first == null)
                    return;
                if (root.parent != null)
                    root = root.root();
                if (root == null || root.right == null ||
                    (rl = root.left) == null || rl.left == null) {
                    tab[index] = first.untreeify(map);  // too small
                    return;
                }
                TreeNode<K,V> p = this, pl = left, pr = right, replacement;
                if (pl != null && pr != null) {
                    TreeNode<K,V> s = pr, sl;
                    while ((sl = s.left) != null) // find successor
                        s = sl;
                    boolean c = s.red; s.red = p.red; p.red = c; // swap colors
                    TreeNode<K,V> sr = s.right;
                    TreeNode<K,V> pp = p.parent;
                    if (s == pr) { // p was s's direct parent
                        p.parent = s;
                        s.right = p;
                    }
                    else {
                        TreeNode<K,V> sp = s.parent;
                        if ((p.parent = sp) != null) {
                            if (s == sp.left)
                                sp.left = p;
                            else
                                sp.right = p;
                        }
                        if ((s.right = pr) != null)
                            pr.parent = s;
                    }
                    p.left = null;
                    if ((p.right = sr) != null)
                        sr.parent = p;
                    if ((s.left = pl) != null)
                        pl.parent = s;
                    if ((s.parent = pp) == null)
                        root = s;
                    else if (p == pp.left)
                        pp.left = s;
                    else
                        pp.right = s;
                    if (sr != null)
                        replacement = sr;
                    else
                        replacement = p;
                }
                else if (pl != null)
                    replacement = pl;
                else if (pr != null)
                    replacement = pr;
                else
                    replacement = p;
                if (replacement != p) {
                    TreeNode<K,V> pp = replacement.parent = p.parent;
                    if (pp == null)
                        root = replacement;
                    else if (p == pp.left)
                        pp.left = replacement;
                    else
                        pp.right = replacement;
                    p.left = p.right = p.parent = null;
                }
    
                TreeNode<K,V> r = p.red ? root : balanceDeletion(root, replacement);
    
                if (replacement == p) {  // detach
                    TreeNode<K,V> pp = p.parent;
                    p.parent = null;
                    if (pp != null) {
                        if (p == pp.left)
                            pp.left = null;
                        else if (p == pp.right)
                            pp.right = null;
                    }
                }
                if (movable)
                    moveRootToFront(tab, r);
            }
    
            /**
             * Splits nodes in a tree bin into lower and upper tree bins,
             * or untreeifies if now too small. Called only from resize;
             * see above discussion about split bits and indices.
             *
             * @param map the map
             * @param tab the table for recording bin heads
             * @param index the index of the table being split
             * @param bit the bit of hash to split on
             */
            final void split(HashMap<K,V> map, Node<K,V>[] tab, int index, int bit) {
                TreeNode<K,V> b = this;
                // Relink into lo and hi lists, preserving order
                TreeNode<K,V> loHead = null, loTail = null;
                TreeNode<K,V> hiHead = null, hiTail = null;
                int lc = 0, hc = 0;
                for (TreeNode<K,V> e = b, next; e != null; e = next) {
                    next = (TreeNode<K,V>)e.next;
                    e.next = null;
                    if ((e.hash & bit) == 0) {
                        if ((e.prev = loTail) == null)
                            loHead = e;
                        else
                            loTail.next = e;
                        loTail = e;
                        ++lc;
                    }
                    else {
                        if ((e.prev = hiTail) == null)
                            hiHead = e;
                        else
                            hiTail.next = e;
                        hiTail = e;
                        ++hc;
                    }
                }
    
                if (loHead != null) {
                    if (lc <= UNTREEIFY_THRESHOLD)
                        tab[index] = loHead.untreeify(map);
                    else {
                        tab[index] = loHead;
                        if (hiHead != null) // (else is already treeified)
                            loHead.treeify(tab);
                    }
                }
                if (hiHead != null) {
                    if (hc <= UNTREEIFY_THRESHOLD)
                        tab[index + bit] = hiHead.untreeify(map);
                    else {
                        tab[index + bit] = hiHead;
                        if (loHead != null)
                            hiHead.treeify(tab);
                    }
                }
            }
    
            /* ------------------------------------------------------------ */
            // Red-black tree methods, all adapted from CLR
    
            static <K,V> TreeNode<K,V> rotateLeft(TreeNode<K,V> root,
                                                  TreeNode<K,V> p) {
                TreeNode<K,V> r, pp, rl;
                if (p != null && (r = p.right) != null) {
                    if ((rl = p.right = r.left) != null)
                        rl.parent = p;
                    if ((pp = r.parent = p.parent) == null)
                        (root = r).red = false;
                    else if (pp.left == p)
                        pp.left = r;
                    else
                        pp.right = r;
                    r.left = p;
                    p.parent = r;
                }
                return root;
            }
    
            static <K,V> TreeNode<K,V> rotateRight(TreeNode<K,V> root,
                                                   TreeNode<K,V> p) {
                TreeNode<K,V> l, pp, lr;
                if (p != null && (l = p.left) != null) {
                    if ((lr = p.left = l.right) != null)
                        lr.parent = p;
                    if ((pp = l.parent = p.parent) == null)
                        (root = l).red = false;
                    else if (pp.right == p)
                        pp.right = l;
                    else
                        pp.left = l;
                    l.right = p;
                    p.parent = l;
                }
                return root;
            }
    
            static <K,V> TreeNode<K,V> balanceInsertion(TreeNode<K,V> root,
                                                        TreeNode<K,V> x) {
                x.red = true;
                for (TreeNode<K,V> xp, xpp, xppl, xppr;;) {
                    if ((xp = x.parent) == null) {
                        x.red = false;
                        return x;
                    }
                    else if (!xp.red || (xpp = xp.parent) == null)
                        return root;
                    if (xp == (xppl = xpp.left)) {
                        if ((xppr = xpp.right) != null && xppr.red) {
                            xppr.red = false;
                            xp.red = false;
                            xpp.red = true;
                            x = xpp;
                        }
                        else {
                            if (x == xp.right) {
                                root = rotateLeft(root, x = xp);
                                xpp = (xp = x.parent) == null ? null : xp.parent;
                            }
                            if (xp != null) {
                                xp.red = false;
                                if (xpp != null) {
                                    xpp.red = true;
                                    root = rotateRight(root, xpp);
                                }
                            }
                        }
                    }
                    else {
                        if (xppl != null && xppl.red) {
                            xppl.red = false;
                            xp.red = false;
                            xpp.red = true;
                            x = xpp;
                        }
                        else {
                            if (x == xp.left) {
                                root = rotateRight(root, x = xp);
                                xpp = (xp = x.parent) == null ? null : xp.parent;
                            }
                            if (xp != null) {
                                xp.red = false;
                                if (xpp != null) {
                                    xpp.red = true;
                                    root = rotateLeft(root, xpp);
                                }
                            }
                        }
                    }
                }
            }
    
            static <K,V> TreeNode<K,V> balanceDeletion(TreeNode<K,V> root,
                                                       TreeNode<K,V> x) {
                for (TreeNode<K,V> xp, xpl, xpr;;)  {
                    if (x == null || x == root)
                        return root;
                    else if ((xp = x.parent) == null) {
                        x.red = false;
                        return x;
                    }
                    else if (x.red) {
                        x.red = false;
                        return root;
                    }
                    else if ((xpl = xp.left) == x) {
                        if ((xpr = xp.right) != null && xpr.red) {
                            xpr.red = false;
                            xp.red = true;
                            root = rotateLeft(root, xp);
                            xpr = (xp = x.parent) == null ? null : xp.right;
                        }
                        if (xpr == null)
                            x = xp;
                        else {
                            TreeNode<K,V> sl = xpr.left, sr = xpr.right;
                            if ((sr == null || !sr.red) &&
                                (sl == null || !sl.red)) {
                                xpr.red = true;
                                x = xp;
                            }
                            else {
                                if (sr == null || !sr.red) {
                                    if (sl != null)
                                        sl.red = false;
                                    xpr.red = true;
                                    root = rotateRight(root, xpr);
                                    xpr = (xp = x.parent) == null ?
                                        null : xp.right;
                                }
                                if (xpr != null) {
                                    xpr.red = (xp == null) ? false : xp.red;
                                    if ((sr = xpr.right) != null)
                                        sr.red = false;
                                }
                                if (xp != null) {
                                    xp.red = false;
                                    root = rotateLeft(root, xp);
                                }
                                x = root;
                            }
                        }
                    }
                    else { // symmetric
                        if (xpl != null && xpl.red) {
                            xpl.red = false;
                            xp.red = true;
                            root = rotateRight(root, xp);
                            xpl = (xp = x.parent) == null ? null : xp.left;
                        }
                        if (xpl == null)
                            x = xp;
                        else {
                            TreeNode<K,V> sl = xpl.left, sr = xpl.right;
                            if ((sl == null || !sl.red) &&
                                (sr == null || !sr.red)) {
                                xpl.red = true;
                                x = xp;
                            }
                            else {
                                if (sl == null || !sl.red) {
                                    if (sr != null)
                                        sr.red = false;
                                    xpl.red = true;
                                    root = rotateLeft(root, xpl);
                                    xpl = (xp = x.parent) == null ?
                                        null : xp.left;
                                }
                                if (xpl != null) {
                                    xpl.red = (xp == null) ? false : xp.red;
                                    if ((sl = xpl.left) != null)
                                        sl.red = false;
                                }
                                if (xp != null) {
                                    xp.red = false;
                                    root = rotateRight(root, xp);
                                }
                                x = root;
                            }
                        }
                    }
                }
            }
    
            /**
             * Recursive invariant check
             */
            static <K,V> boolean checkInvariants(TreeNode<K,V> t) {
                TreeNode<K,V> tp = t.parent, tl = t.left, tr = t.right,
                    tb = t.prev, tn = (TreeNode<K,V>)t.next;
                if (tb != null && tb.next != t)
                    return false;
                if (tn != null && tn.prev != t)
                    return false;
                if (tp != null && t != tp.left && t != tp.right)
                    return false;
                if (tl != null && (tl.parent != t || tl.hash > t.hash))
                    return false;
                if (tr != null && (tr.parent != t || tr.hash < t.hash))
                    return false;
                if (t.red && tl != null && tl.red && tr != null && tr.red)
                    return false;
                if (tl != null && !checkInvariants(tl))
                    return false;
                if (tr != null && !checkInvariants(tr))
                    return false;
                return true;
            }
        }
    
    }


##### 参考：http://www.importnew.com/18633.html
