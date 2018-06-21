# JVM内存结构
## 运行时栈帧结构



### 1. 局部变量表
```text
摘自：[JAVA虚拟机规范-P13]
1、每个栈帧内部都包含一组称为局部变量表的变量列表。栈帧中局部变量表的长度在编译期决定，并且存储于类或者接口的二进制表示之中
2、一个局部变量可以保存一个类型为boolean、byte、char、short、int、float、reference、或returnAddress的数据。两个局部变量可以保存一个long和double的数据。
3、局部变量表使用索引来进行定位访问，从0开始访问。
4、Java虚拟机使用局部变量表来完成方法调用时的参数传递。当调用一个方法时，它的参数将会传递至从0开始的连续的局部变量表位置上。
4.1、当调用一个实例方法时，第0个局部变量一定是用来存储被调用的实例方法所在的对象的引用（Java语言中的this关键字）。后续的其他参数将会传递至从1开始的连续的局部变量位置上。
```
### 2. 操作数栈

> **<font color=#00ffff>操作数栈也称操作栈，它是一个</font>**

```text
 *操作数栈也称操作栈，它是一个*

```
### 3. 动态连接
```text

```

### 4. 方法返回地址
```text
1、正常完成出口(Normal Method Invocation Completion)


2、异常完成出口(Abrupt Method Invocation Completion)

无论如何完成，方法都需要回到被调用的位置，程序才能继续运行，

```
### 5. 附加信息
```text
虚拟机规范允许具体的虚拟机实现增加一些规范里没有描述的信息到栈帧中。
```
##### 问题：没有指明局部变量表每个局部变量的单位大小

```text
摘自：[深入理解Java虚拟机-P238]
1、局部变量表（Local Variable Table）是一个组变量值存储空间，用于存放方法参数和方法内部定义的局部变量。
2、局部变量表的容量以变量槽（Variable Slot，下称Slot）为最小单位，虚拟机规范中并没有明确指明一个Slot应占用的内存大小。
3、一个Slot可以存放32位以内的数据类型。Java占用32位以内的数据类型有boolean、byte、char、short、int、float、refrence和returnAddress 8种类型。
4、对于64位的数据类型，虚拟机会以高位对齐的方式为其分配两个连续的Slot空间。Java语言中明确的64位数据类型只有long和double。
5、局部变量表的Slot可以重用，方法体中定义的变量，其作用于不一定会覆盖整个方法体。
```
# 二、常量池

参考
---
```text
https://www.jianshu.com/p/c7f47de2ee80
```

## 常量池表（constant_pool table）
---
###### 存放什么？
```text
1、字面量（Literal）：字符串，final修饰的（静态、实例、局部）变量
2、符号引用（Symblic References）
    2.1 类和接口的全限定名
    2.2 字段名称和描述符
    2.3 方法名称和描述符
```


## 运行时常量池（runtime constant pool）
----
###### 意义
```text
    1、避免频繁的创建和销毁对象，影响系统性能；
    2、实现对象共享；
    3、节省空间：所有相同的字符串常量被合并，在常量池中存放一份
    4、节省时间：比较字符串时，==比 equals快

```
###### 场景
```text
1、基本类型：

    1.1、基本类型Byte、Boolean、Character[0,127]、Short、Integer、Long，默认缓存[-128,127],浮点类型Float，Double无缓存
    1.2、使用缓存时机1：Integer i=127;
    1.3、使用缓存时机2：Integer x=Integer.valueOf(127);
    1.4、不使用缓存时机3：Integer y=new Integer(127);
 
2、String
    2.1、使用常量池时机1：String s="abc";
    2.2、使用常量池时机2：String s="字符串1" + "字符串2"；
    2.3、不使用常量池：new String("字符串1") + new String("字符串2") 只会在堆里
 
3、new String() + new String()实现
    3.1、StringBuilder.append()
```
###### 面试题-String
```text
String s = new String("s"); //创建几个对象？
    1、编译期：字符串"s"存放在class文件的常量池表中
    2、类加载期：class文件的常量池对象 在 运行时常量池中创建，此时"s" 在常量池中创建
    3、运行期：字符串-字面量"s"已经在常量池中存在，new ，它将常量池中的字符串"s"对象 复制一份 在堆中创建一个对象
    综述：总共是两个对象
```
###### 面试题-String.intern()


# 三、Java对象组成
* 对象组成
> 对象由三部分组成：
>> * 对象头（Header）
>> * 实例数据（Instance Data）
>> * 对齐填充（Pading）
      > (对象头+实例数据+padding) % 8等于0 且 0 <= padding < 8
* 对象大小受参数配置影响：
> -XX:+UseCompressedOops

* 原生类型(primitive type)占用内存情况

primitive type | Memory Required(bytes)
-|-
boolean | 1
byte|1
char|2
short|2
int|4
long|8
float|4
double|8

* 引用类型占用内存情况

reference type| Memory Required(bytes)|-XX: +UseCompressedOops|NOTE
-|:-:|:-:|:-:
32位|4|-|32位系统，需要32bit，方能遍及2^32=4G的内存
64位|8|4|64位系统，需要64bit，8个bytes

* 对象头占用内存情况(jvm对象头一般占用两个机器码)

对象头 | Memory Required(bytes) |-XX: +UseCompressedOops
-| :-: | :-:
32位|8|-
64位|16|12

* 数组对象头占用内存情况

对象头 | Memory Required(bytes) |-XX: +UseCompressedOops
-|:-:|:-:
32位|-|-
64位|24|16

* 举例一：在64位系统中，UseCompressedOops对对象头的影响
```
public class A{
    int a;
}
```

> 不开启 -XX: +UseCompressedOops ，分析：
>> * 对象头，占 16 bytes
>> * 实例数据：primitive 类型 int 占 4 bytes
>> * 对齐填充：16 + 4 = 20 不是8的倍数，所以 padding=4
>> * 对象头 + 实例数据 + 对齐填充 = 24

> 开启 -XX: +UseCompressedOops
>> * 对象头，占 12 bytes
>> * 实例数据：primitive 类型 int 占 4 bytes
>> * 对齐填充：12 + 4 = 16 是8的倍数，所以 padding=0
>> * 对象头 + 实例数据 + 对齐填充 = 16

* 举例二：在64位系统中，UseCompressedOops对reference的影响
```
public class A{
    int a;
    Object o;
}
```

> 不开启 -XX: +UseCompressedOops ，分析：
>> * 对象头，占 16 bytes
>> * 实例数据：primitive 类型 int 占 4 bytes
>> * 实例数据：reference 类型  占 8 bytes
>> * 对齐填充：16 + 4 + 8 = 28 不是8的倍数，所以 padding=4
>> * 对象头 + 实例数据 + 对齐填充 = 32

> 开启 -XX: +UseCompressedOops ，分析：
>> * 对象头，占 16 bytes，压缩后 12 bytes
>> * 实例数据：primitive 类型 int 占 4 bytes
>> * 实例数据：reference 类型  占 8 bytes，压缩后 4 bytes
>> * 对齐填充：12 + 4 + 4 = 20 不是8的倍数，所以 padding=4
>> * 对象头 + 实例数据 + 对齐填充 = 24

* 举例三：在64位系统中，UseCompressedOops对数组对象头的影响
```
  new Integer[0];
```
> 不开启 -XX: +UseCompressedOops ，分析：
>> * 数组对象头，占 24 bytes


> 开启 -XX: +UseCompressedOops ，分析：
>> * 数组对象头，占 16 bytes

* 举例四：在64位系统中，UseCompressedOops对数组的影响
```
new Integer[3];
```
> 不开启 -XX: +UseCompressedOops ，分析：
>> * 数组对象头，占 24 bytes
>> * 数组中 3 个 reference，占 3&times;8 bytes
>> * 对齐填充：24 + 3&times;8 = 48 ，8 的倍数，不填充
>> * 对象头 + 3个 reference = 48 无填充

> 开启 -XX: +UseCompressedOops ，分析：
>> * 数组对象头，占 24 bytes，压缩后 16
>> * 数组中 3 个 reference，占 3*8 bytes，压缩后 3 &times; 4 bytes
>> * 对齐填充：16 + 3 	&times; 4 =28 ，不是8的倍数，所以padding=4
>> * 对象头 + 实例数据 + padding = 32 bytes
