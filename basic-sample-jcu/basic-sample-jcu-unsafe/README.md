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
    
    public final native boolean compareAndSwapObject(Object var1, long var2, Object var4, Object var5);

    public final native boolean compareAndSwapInt(Object var1, long var2, int var4, int var5);

    public final native boolean compareAndSwapLong(Object var1, long var2, long var4, long var6);

```

## 2.6 Memory
```text
    public native long allocateMemory(long var1);

    public native long reallocateMemory(long var1, long var3);

    public native void setMemory(Object var1, long var2, long var4, byte var6);

    public void setMemory(long var1, long var3, byte var5) {
        this.setMemory((Object)null, var1, var3, var5);
    }

    public native void copyMemory(Object var1, long var2, Object var4, long var5, long var7);

    public void copyMemory(long var1, long var3, long var5) {
        this.copyMemory((Object)null, var1, (Object)null, var3, var5);
    }

    public native void freeMemory(long var1);
    
    
    public native int getInt(Object var1, long var2);

    public native void putInt(Object var1, long var2, int var4);

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



