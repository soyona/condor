String类
------
# String类介绍
 初始化
-----

* 1、编译期间，"Hello"字符串常量在Class文件结构的常量池中[^1]
* 2、在class文件"加载阶段"，字符串 "Hello" 随着Class文件的字节流转为为方法区的运行时常量池，
* 3、运行期间

``` java
        public static void main(String[] args) {
            String s1 = "Hello";
        }
```

[^1]: A
# equals()
# hash()
# trim()
# intern()

```$xslt
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
```$xslt
返回字符串对象规范的表示。
一个字符串池，初始化为空，被String类单独维护
当intern方法被调用时，如果池中已经包含一个字符串与这个String对象equals相等，那么，返回常量池中的字符串。
否则，该String对象被添加到池中，并且返回该String对象的引用
``` 
```    
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
```
```$xslt
返回和这个字符串相同内容的字符串，但是，保证是来自于池中
```
```     
     */
    public native String intern();
```