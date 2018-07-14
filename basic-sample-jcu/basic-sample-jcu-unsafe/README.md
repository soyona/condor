# 1. Unsafe
> [source code](http://hg.openjdk.java.net/jdk7/jdk7/jdk/file/9b8c96f96a0f/src/share/classes/sun/misc/Unsafe.java) 
 
> sun.misc.Unsafe
## 1.1 Unsafe 常量
> 用法：
```text
AtomicReference 类中：
private static final Unsafe unsafe = Unsafe.getUnsafe();
```
> Unsafe 常量theUnsafe
```text
private static final Unsafe theUnsafe;
```
> theUnsafe 实例初始化
```text
static {
    theUnsafe = new Unsafe();
}
```
## 1.2 Getting hold of an instance of sum.misc.Unsafe
> private constructor: 
```text
private Unsafe() {}
```

> Unsafe.getUnsafe(),The public getter method for this instances  performs a security check in order to avoid its public use:
```text
@CallerSensitive
public static Unsafe getUnsafe() {
    Class var0 = Reflection.getCallerClass();
    // 判断 var0.getClassLoader() 是否为空
    if (!VM.isSystemDomainLoader(var0.getClassLoader())) {
        throw new SecurityException("Unsafe");
    } else {
        return theUnsafe;
    }
}
```
```text
    Firstly,this mothod looks up the calling class from the current thread's method stack.This lookup is implemented by another intern class named `Reflection.getCallerClass()`
which basically browsing down the given number of stack frames and then returns the method's defining class.This securiy check is however likely to change in the future version.
When browsing the stack, the first found class(index 0) will obviously be the Reflection class itself, and the second (index 1) class will be the Unsafe class such as that index 2
will hold your application class that was calling Unsafe.getUnsafe();
 
    This looked-up class is then checked for its   `classloader`  where a null reference is used to represent the bootstrap class loader on a 
HotSpot virtual machine.(This is documented in Class#getClassLoader() where it says that "some implementations may use null to represent the bootstrap class loader".)
You will never be able to call this directly but receive a thrown SecurityException as an answer.(Technically,you could force the VM to load your application classes using the bootstrap 
loader by adding it to be -Xbootclasspath,but this would require some setup outside of your application code which you might want to avoid .) Thus ,the following test will succeed:
 
```
```text
@Test(expected = SecurityException.class)
public void testSingletonGetter() throws Exception {
  Unsafe.getUnsafe();
}
```
```text
you simply read the singlton field's value:
```
```text
 Field theUnsafeField = Unsafe.class.getDeclaredField("theUnsafe");
 theUnsafeField.setAccessible(true);
 Unsafe temp = (Unsafe)theUnsafeField.get(null);
```
> Alternatively,you can invoke the private constructor .
```text
Constructor constructor = Unsafe.class.getDeclaredConstructor();
constructor.setAccessible(true);
Unsafe unsafe = (Unsafe)constructor.newInstance();

```
> Reflection.getCallerClass()，looking up the calling class.
```text
@CallerSensitive
    public static native Class<?> getCallerClass();
```
> VM.isSystemDomainLoader，判断是不是系统域类加载器
```text
public static boolean isSystemDomainLoader(ClassLoader var0) {
        return var0 == null;
}
```
## 1.3 How to get the instance of Unsafe
> [Kanglei's Code](./src/main/java/sample/jcu/unsafe/AtomicOrder.java)

```text
/**
     * 自行实现获取Unsafe实例
     * @return
     */
    static Unsafe getUnsafe(){
        try {
            Field theUnsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafeField.setAccessible(true);
            Unsafe temp = (Unsafe)theUnsafeField.get(null);
            return temp;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
```
# 2. Unsafe API Cagetory

## 2.1 Get Some info
```text
    public native int addressSize();
 
    public native int pageSize();
```
## 2.2 Object operations
```text
    public native long objectFieldOffset(Field var1);
 
    public native Object staticFieldBase(Field var1);
    
    public native Object allocateInstance(Class<?> var1) throws InstantiationException;
```
## 2.3 Class and static field
```text
    public native Class<?> defineClass(String var1, byte[] var2, int var3, int var4, ClassLoader var5, ProtectionDomain var6);
    
    public native Class<?> defineAnonymousClass(Class<?> var1, byte[] var2, Object[] var3);
     
    public native long staticFieldOffset(Field var1);
    
    public native void ensureClassInitialized(Class<?> var1);
```

## 2.4 Array operations
```text

    public native int arrayBaseOffset(Class<?> var1);

    public native int arrayIndexScale(Class<?> var1);
```
## 2.5 Synchronization
```text
 /** @deprecated */
    @Deprecated
    public native void monitorEnter(Object var1);

    /** @deprecated */
    @Deprecated
    public native void monitorExit(Object var1);

    /** @deprecated */
    @Deprecated
    public native boolean tryMonitorEnter(Object var1);
    
    //Will atomicly read a value of type XXX from target's address at the specified offset and set the given value if the current value at this offset equals the expected value.
    
    /**
     * Atomically update Java variable to <tt>x</tt> if it is currently
     * holding <tt>expected</tt>.
     * @return <tt>true</tt> if successful
     */
    public final native boolean compareAndSwapObject(Object var1, long var2, Object var4, Object var5);
    /**
     * Atomically update Java variable to <tt>x</tt> if it is currently
     * holding <tt>expected</tt>.
     * @return <tt>true</tt> if successful
     */
    public final native boolean compareAndSwapInt(Object var1, long var2, int var4, int var5);
    /**
     * Atomically update Java variable to <tt>x</tt> if it is currently
     * holding <tt>expected</tt>.
     * @return <tt>true</tt> if successful
     */
    public final native boolean compareAndSwapLong(Object var1, long var2, long var4, long var6);

```

## 2.6 Memory
```
    /**
     * Allocates a new block of native memory, of the given size in bytes.  The
     * contents of the memory are uninitialized; they will generally be
     * garbage.  The resulting native pointer will never be zero, and will be
     * aligned for all value types.  Dispose of this memory by calling {@link
     * #freeMemory}, or resize it with {@link #reallocateMemory}.
     *
     * @throws IllegalArgumentException if the size is negative or too large
     *         for the native size_t type
     *
     * @throws OutOfMemoryError if the allocation is refused by the system
     *
     * @see #getByte(long)
     * @see #putByte(long, byte)
     */
    public native long allocateMemory(long var1);
```
```
    public native long reallocateMemory(long var1, long var3);
    /**
     * Sets all bytes in a given block of memory to a fixed value
     * (usually zero).
     *
     * <p>This method determines a block's base address by means of two parameters,
     * and so it provides (in effect) a <em>double-register</em> addressing mode,
     * as discussed in {@link #getInt(Object,long)}.  When the object reference is null,
     * the offset supplies an absolute base address.
     *
     * <p>The stores are in coherent (atomic) units of a size determined
     * by the address and length parameters.  If the effective address and
     * length are all even modulo 8, the stores take place in 'long' units.
     * If the effective address and length are (resp.) even modulo 4 or 2,
     * the stores take place in units of 'int' or 'short'.
     *
     * @since 1.7
     */
    public native void setMemory(Object var1, long var2, long var4, byte var6);
```
```
    /**
     * Sets all bytes in a given block of memory to a fixed value
     * (usually zero).  This provides a <em>single-register</em> addressing mode,
     * as discussed in {@link #getInt(Object,long)}.
     *
     * <p>Equivalent to <code>setMemory(null, address, bytes, value)</code>.
     */
    public void setMemory(long var1, long var3, byte var5) {
        this.setMemory((Object)null, var1, var3, var5);
    }
```
```

    /**
     * Sets all bytes in a given block of memory to a copy of another
     * block.
     *
     * <p>This method determines each block's base address by means of two parameters,
     * and so it provides (in effect) a <em>double-register</em> addressing mode,
     * as discussed in {@link #getInt(Object,long)}.  When the object reference is null,
     * the offset supplies an absolute base address.
     *
     * <p>The transfers are in coherent (atomic) units of a size determined
     * by the address and length parameters.  If the effective addresses and
     * length are all even modulo 8, the transfer takes place in 'long' units.
     * If the effective addresses and length are (resp.) even modulo 4 or 2,
     * the transfer takes place in units of 'int' or 'short'.
     *
     * @since 1.7
     */
    public native void copyMemory(Object var1, long var2, Object var4, long var5, long var7);
```

```
    /**
     * Sets all bytes in a given block of memory to a copy of another
     * block.  This provides a <em>single-register</em> addressing mode,
     * as discussed in {@link #getInt(Object,long)}.
     *
     * Equivalent to <code>copyMemory(null, srcAddress, null, destAddress, bytes)</code>.
     */
    public void copyMemory(long var1, long var3, long var5) {
        this.copyMemory((Object)null, var1, (Object)null, var3, var5);
    }
```
    
```
    /**
     * Disposes of a block of native memory, as obtained from {@link
     * #allocateMemory} or {@link #reallocateMemory}.  The address passed to
     * this method may be null, in which case no action is taken.
     *
     * @see #allocateMemory
     */
    public native void freeMemory(long var1);
``` 
  
```    
    public native int getInt(Object var1, long var2);
```
```

    /**
     * Stores a value into a given Java variable.
     * <p>
     * The first two parameters are interpreted exactly as with
     * {@link #getInt(Object, long)} to refer to a specific
     * Java variable (field or array element).  The given value
     * is stored into that variable.
     * <p>
     * The variable must be of the same type as the method
     * parameter <code>x</code>.
     *
     * @param o Java heap object in which the variable resides, if any, else
     *        null
     * @param offset indication of where the variable resides in a Java heap
     *        object, if any, else a memory address locating the variable
     *        statically
     * @param x the value to store into the indicated Java variable
     * @throws RuntimeException No defined exceptions are thrown, not even
     *         {@link NullPointerException}
     */
    public native void putInt(Object var1, long var2, int var4);
```
```
    public native Object getObject(Object var1, long var2);

    public native void putObject(Object var1, long var2, Object var4);

    public native boolean getBoolean(Object var1, long var2);

    public native void putBoolean(Object var1, long var2, boolean var4);

    public native byte getByte(Object var1, long var2);

    public native void putByte(Object var1, long var2, byte var4);

    public native short getShort(Object var1, long var2);

    public native void putShort(Object var1, long var2, short var4);

    public native char getChar(Object var1, long var2);

    public native void putChar(Object var1, long var2, char var4);

    public native long getLong(Object var1, long var2);

    public native void putLong(Object var1, long var2, long var4);

    public native float getFloat(Object var1, long var2);

    public native void putFloat(Object var1, long var2, float var4);

    public native double getDouble(Object var1, long var2);
    
    public native void putDouble(Object var1, long var2, double var4);
```
```text
   /**
     * Fetches a reference value from a given Java variable, with volatile
     * load semantics. Otherwise identical to {@link #getObject(Object, long)}
     * Will read a value of type XXX from target's address at the specified offset and not hit any thread local caches.
     */
    public native Object getObjectVolatile(Object o, long offset);

    /**
     * Stores a reference value into a given Java variable, with
     * volatile store semantics. Otherwise identical to {@link #putObject(Object, long, Object)}
     * Will place value at target's address at the specified offset and not hit any thread local caches.
     */
    public native void    putObjectVolatile(Object o, long offset, Object x);
```

```text
    /**
     * Version of {@link #putObjectVolatile(Object, long, Object)}
     * that does not guarantee immediate visibility of the store to
     * other threads. This method is generally only useful if the
     * underlying field is a Java volatile (or if an array cell, one
     * that is otherwise only accessed using volatile accesses).
     */
    public native void    putOrderedObject(Object o, long offset, Object x);

    /** Ordered/Lazy version of {@link #putIntVolatile(Object, long, int)}  */
    public native void    putOrderedInt(Object o, long offset, int x);
```
## 2.7 Thread
```text
    public native void unpark(Object var1);

    public native void park(boolean var1, long var2);
```
# 3. Unsafe Usage
> [Oracle Usage](https://docs.google.com/document/d/1GDm_cAxYInmoHMor-AkStzWvwE9pw6tnz_CebJQxuUE/edit#) 
## 3.1 Create an Instance of a Class Without Calling a Constructor
> [UnsafeUtils.java](./src/main/java/sample/jcu/unsafe/UnsafeUtils.java)
 
> [Instance Sample](./src/main/java/sample/jcu/unsafe/GetInstanceDemo.java)
```text
class Order{
        private String orderNo = "2019";// this field will be uninitialized
        public String getOrderNo(){
            System.out.println("calling the method of getOrderNo()");
            return this.orderNo;
        }
    }

public static void main(String[] args) {
    try {
        Order order = (Order)UnsafeUtils.unsafe.allocateInstance(Order.class);
        System.out.println(order.getOrderNo());
    } catch (InstantiationException e) {
        e.printStackTrace();
    }
}
```
### 3.1.1 Create an Instance of a Class without Calling a Constructor but ReflectionFactory

> [ReflectionFactoryTest Code](https://github.com/soyona/condor/blob/master/basic-sample-reflect/src/main/java/sample/reflectionfactory/ReflectionFactoryTest.java)

## 3.2 Native Memory Allocation
> If you want to allocate an array  that has more than Integer.MAX_VALUE entries.You can create such an array by allocating native memory.

### 3.2.1 Allocate in VM
```text
@Test
public void arrayTest(){
    // 2的31次幂-1，1Byte=8bit，1KByte = 1024Byte = 2的10次Byte
    // 1MByte = 1024 * 1KByte = 2的20次Byte
    // 1GByte = 1024 * 1MByte = 2的30次Byte
    // 2GByte = 1GByte * 2    = 2的31次Byte
    // Integer.MAX_VALUE
    long unit = 1L;
    long int_max = (unit << 31) -1;
    System.out.println(int_max);
    System.out.println(Integer.MAX_VALUE);
    // 大约2G-1内存，在堆中无法创建
    byte[] strings = new byte[Integer.MAX_VALUE];
}
```
```text
<font color='red'> You could not create an array that more than Integer.MAX_VALUE entries. Otherwise, you'll get the exception :
'java.lang.OutOfMemoryError: Requested array size exceeds VM limit' </font>
```
```text
You could not create an array that more than Integer.MAX_VALUE entries. Otherwise, you'll get the exception :
java.lang.OutOfMemoryError: Requested array size exceeds VM limit
```
### 3.2.2 Allocate in native memory
```
Native memory allocation is used for example direct byte buffers that are offered in Java's NIO packages.
Other than heap memory,native memory is not part of the heap area and can be used non-exclusively for example
for communicating with other processes.
 
As a result, Java's heap space is in competition with the native space: the more memory you assign to the JVM,the less native memory is left. 
```
```text
Let's look up an example for using native memory in Java with creating the mentioned oversized array:

class  DirectIntArray{
        //Every entry size is :4byte
        private final static long INT_SIZE_IN_BYTES = 4;
        private final long startIndex;
        public DirectIntArray(long size) {
            // the native pointer
            startIndex = UnsafeUtils.unsafe.allocateMemory(size * INT_SIZE_IN_BYTES);
            // initialize to 0
            UnsafeUtils.unsafe.setMemory(startIndex, size * INT_SIZE_IN_BYTES, (byte) 0);
        }

        private long index(long offset) {
            return startIndex + offset * INT_SIZE_IN_BYTES;
        }

        public void setValue(long index, int value) {
            UnsafeUtils.unsafe.putInt(index(index), value);
        }

        public int getValue(long index) {
            return UnsafeUtils.unsafe.getInt(index(index));
        }

        
        public void destroy() {
            UnsafeUtils.unsafe.freeMemory(startIndex);
        }
    }

    @Test
    public void testDirectIntArray(){
        //allocate 2GB * 4 memory,
        long maximum = Integer.MAX_VALUE + 1L;
        DirectIntArray directIntArray = new DirectIntArray(maximum);
        directIntArray.setValue(0L, 10);
        directIntArray.setValue(maximum, 20);
        directIntArray.setValue(maximum+1, 30);

        assertEquals(10, directIntArray.getValue(0L));
        assertEquals(20, directIntArray.getValue(maximum));

        assertEquals(30, directIntArray.getValue(maximum+1));
        directIntArray.destroy();
    }
```


#### 3.2.2.1 How to computing an object's size
```text
The most conanical [kəˈnɒnɪkl] way of computing an object's size is using the Instrumented class from Java's attach API 
which offers a dedicated method for this purpose called getObjectSize. 
``` 
> [See basic-sample-object](https://github.com/soyona/condor/tree/master/basic-sample-object)


## 3.3 Throwing Checked Exceptions Without Declaration
```text
There are some other interesting methods to find in Unsafe. Did you ever want to throw a specific exception to be handled in a lower layer but you high layer interface type did not declare this checked exception? 
```
```text
@Test(expected = Exception.class)
public void testThrowChecked() throws Exception {
  throwChecked();
}

public void throwChecked() {
  unsafe.throwException(new Exception());
}
```

