# String类

# String类介绍


# intern()

## 常量池与运行时常量池的区别


```text
1、常量池表（constant_pool table）是静态概念，在class文件范畴，存储字面量及符号引用
2、运行时常量池（runtime constant pool）是JVM内存方法区的一部分，在JVM内存范畴
3、JDK1.7之后，运行时常量池转移到堆中

```


```text
1、编译期间，"Hello"字符串常量在Class文件结构的常量池表（constant_pool table）中 `[^1]`
2、在class文件"加载阶段"，字符串 "Hello" 随着Class文件的字节流转为为方法区的运行时常量池（runtime constant pool），
3、运行期间
4、运行时常量池（runtime constant pool）是class文件中每一个类或接口的常量池表（constant_pool table）的运行时表示形式 [^2]
```

``` java
        public static void main(String[] args) {
            String s1 = "Hello";
        }
```

[^1]: A
[^2]: Java虚拟机规范（Java SE 8版）P11

```java
/**
     * Returns a canonical representation for the string object.
     * <p>
     * A pool of strings, initially empty, is maintained privately by the
     * class {@code String}.
     * <p>
     * When the intern method is invoked, if the pool already contains a
     * string equal to this {@code String} object as determined by
     * the {@link #equals(Object)} method, then the string from the pool is
     * returned. Otherwise, this {@code String} object is added to the
     * pool and a reference to this {@code String} object is returned.
     * <p>
```
```text
返回字符串对象规范的表示。
一个字符串池，初始化为空，被String类单独维护
当intern方法被调用时，如果池中已经包含一个字符串与这个String对象equals相等，那么，返回常量池中的字符串。
否则，该String对象被添加到池中，并且返回该String对象的引用
``` 
```java  
     * It follows that for any two strings {@code s} and {@code t},
     * {@code s.intern() == t.intern()} is {@code true}
     * if and only if {@code s.equals(t)} is {@code true}.
     * <p>
     * All literal strings and string-valued constant expressions are
     * interned. String literals are defined in section 3.10.5 of the
     * <cite>The Java&trade; Language Specification</cite>.
     *
     * @return  a string that has the same contents as this string, but is
     *          guaranteed to be from a pool of unique strings.
     */
```
```text
返回和这个字符串相同内容的字符串，但是，保证是来自于池中
```
```java   
    public native String intern();
```

# REF:
    * https://blog.csdn.net/fan2012huan/article/details/50911220
    * https://blog.csdn.net/fan2012huan/article/details/50894541
    * https://blog.csdn.net/fan2012huan/article/details/50804682
    
# equals()
# hash()
# trim()

String +操作符优化
---
```text
String +经过编译器优化后，采用StringBuilder对象，通过append方法实现相加，最后toString()new出新的字符串
StringBuilder的toString()方法实现如下：
```

```java
    @Override
    public String toString() {
        // Create a copy, don't share the array
        return new String(value, 0, count);
    }
```
```text
查看字节码指令，代码：

```
```java
    String s="A"+new String("B");
```


```java
  public static void main(java.lang.String[]);
    descriptor: ([Ljava/lang/String;)V
    flags: ACC_PUBLIC, ACC_STATIC
    Code:
      stack=4, locals=2, args_size=1
         0: new           #2                  // class java/lang/StringBuilder
         3: dup
         4: invokespecial #3                  // Method java/lang/StringBuilder."<init>":()V
         7: ldc           #4                  // String A
         9: invokevirtual #5                  // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        12: new           #6                  // class java/lang/String
        15: dup
        16: ldc           #7                  // String B
        18: invokespecial #8                  // Method java/lang/String."<init>":(Ljava/lang/String;)V
        21: invokevirtual #5                  // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        24: invokevirtual #9                  // Method java/lang/StringBuilder.toString:()Ljava/lang/String;
        27: astore_1
        28: return
      LineNumberTable:
        line 13: 0
        line 14: 28
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      29     0  args   [Ljava/lang/String;
           28       1     1     s   Ljava/lang/String;
}
SourceFile: "StringBuilderDemo.java"

```

# JDK7+ substring(int beginIndex, int endIndex)
> substring(int beginIndex, int endIndex)

    ```java
    public String substring(int beginIndex, int endIndex) {
        if (beginIndex < 0) {
            throw new StringIndexOutOfBoundsException(beginIndex);
        }
        if (endIndex > value.length) {
            throw new StringIndexOutOfBoundsException(endIndex);
        }
        int subLen = endIndex - beginIndex;
        if (subLen < 0) {
            throw new StringIndexOutOfBoundsException(subLen);
        }
        return ((beginIndex == 0) && (endIndex == value.length)) ? this
                : new String(value, beginIndex, subLen);
    }
    ```

>> String(char value[], int offset, int count)
```java
public String(char value[], int offset, int count) {
        if (offset < 0) {
            throw new StringIndexOutOfBoundsException(offset);
        }
        if (count <= 0) {
            if (count < 0) {
                throw new StringIndexOutOfBoundsException(count);
            }
            if (offset <= value.length) {
                this.value = "".value;
                return;
            }
        }
        // Note: offset or count might be near -1>>>1.
        if (offset > value.length - count) {
            throw new StringIndexOutOfBoundsException(offset + count);
        }
        this.value = Arrays.copyOfRange(value, offset, offset+count);
    }
```

>>> Arrays.copyOfRange 
```java
 public static char[] copyOfRange(char[] original, int from, int to) {
        int newLength = to - from;
        if (newLength < 0)
            throw new IllegalArgumentException(from + " > " + to);
        char[] copy = new char[newLength];
        System.arraycopy(original, from, copy, 0,
                         Math.min(original.length - from, newLength));
        return copy;
    }
```
# JDK6 substring(int beginIndex, int endIndex)  
> substring
```java
public String substring(int beginIndex, int endIndex) {
	if (beginIndex < 0) {
	    throw new StringIndexOutOfBoundsException(beginIndex);
	}
	if (endIndex > count) {
	    throw new StringIndexOutOfBoundsException(endIndex);
	}
	if (beginIndex > endIndex) {
	    throw new StringIndexOutOfBoundsException(endIndex - beginIndex);
	}
	return ((beginIndex == 0) && (endIndex == count)) ? this :
	    new String(offset + beginIndex, endIndex - beginIndex, value);
    }
```


>>  String(int offset, int count, char value[])
>>  新的字符串引用源字符串value，GC无法回收，造成内存溢出
```java
    // Package private constructor which shares value array for speed.
    String(int offset, int count, char value[]) {
	this.value = value;
	this.offset = offset;
	this.count = count;
    }

```

>> 在JDK6 下运行 sample.string.MethodOfSubString class 抛出异常
```text
/Library/Java/JavaVirtualMachines/1.6.0_65-b14-462.jdk/Contents/Home/bin/java -Dfile.encoding=UTF-8 -classpath /Library/Java/JavaVirtualMachines/1.6.0_65-b14-462.jdk/Contents/Classes/charsets.jar:/Library/Java/JavaVirtualMachines/1.6.0_65-b14-462.jdk/Contents/Classes/classes.jar:/Library/Java/JavaVirtualMachines/1.6.0_65-b14-462.jdk/Contents/Classes/jsse.jar:/Library/Java/JavaVirtualMachines/1.6.0_65-b14-462.jdk/Contents/Classes/ui.jar:/Library/Java/JavaVirtualMachines/1.6.0_65-b14-462.jdk/Contents/Home/lib/deploy.jar:/Library/Java/JavaVirtualMachines/1.6.0_65-b14-462.jdk/Contents/Home/lib/dt.jar:/Library/Java/JavaVirtualMachines/1.6.0_65-b14-462.jdk/Contents/Home/lib/ext/apple_provider.jar:/Library/Java/JavaVirtualMachines/1.6.0_65-b14-462.jdk/Contents/Home/lib/ext/dnsns.jar:/Library/Java/JavaVirtualMachines/1.6.0_65-b14-462.jdk/Contents/Home/lib/ext/localedata.jar:/Library/Java/JavaVirtualMachines/1.6.0_65-b14-462.jdk/Contents/Home/lib/ext/sunjce_provider.jar:/Library/Java/JavaVirtualMachines/1.6.0_65-b14-462.jdk/Contents/Home/lib/ext/sunpkcs11.jar:/Library/Java/JavaVirtualMachines/1.6.0_65-b14-462.jdk/Contents/Home/lib/javaws.jar:/Library/Java/JavaVirtualMachines/1.6.0_65-b14-462.jdk/Contents/Home/lib/jce.jar:/Library/Java/JavaVirtualMachines/1.6.0_65-b14-462.jdk/Contents/Home/lib/jconsole.jar:/Library/Java/JavaVirtualMachines/1.6.0_65-b14-462.jdk/Contents/Home/lib/management-agent.jar:/Library/Java/JavaVirtualMachines/1.6.0_65-b14-462.jdk/Contents/Home/lib/plugin.jar:/Library/Java/JavaVirtualMachines/1.6.0_65-b14-462.jdk/Contents/Home/lib/sa-jdi.jar:/Users/kanglei/GitHub/condor/basic-sample-string/target/classes sample.string.MethodOfSubString
Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
at java.util.Arrays.copyOf(Arrays.java:2882)
at java.lang.StringValue.from(StringValue.java:24)
at java.lang.String.<init>(String.java:178)
at sample.string.MethodOfSubString$MyString.<init>(MethodOfSubString.java:23)
at sample.string.MethodOfSubString.main(MethodOfSubString.java:16)
```
