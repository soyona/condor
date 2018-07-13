# 1. Unsafe
> Unsafe提供Java直接操作内存空间的能力 
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
> theUnsafe实例初始化
```text
static {
    theUnsafe = new Unsafe();
}
```
## 1.2 Unsafe提供单例模式
> Unsafe.getUnsafe()单例实现,静态工厂方法：
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
> Reflection.getCallerClass()，反射获取调用者Class
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
## 1.3 开发者如果获取Unsafe实例
> [代码实现](https://github.com/soyona/condor/blob/master/basic-sample-jcu/basic-sample-jcu-lock/src/main/java/sample/jcu/unsafe/AtomicOrder.java)

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
# 2 Unsafe用途
> [Oracle Usage](https://docs.google.com/document/d/1GDm_cAxYInmoHMor-AkStzWvwE9pw6tnz_CebJQxuUE/edit#) 

## 2.1 反序列化

