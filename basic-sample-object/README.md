# 1. How to Computing an object's size
> [StackOverFlow Discuss](https://stackoverflow.com/questions/52353/in-java-what-is-the-best-way-to-determine-the-size-of-an-object)

# 2. Java Object在内存中的布局
> 对象头、实例数据、对齐填充
## 2.1 对象头
 
## 2.2 实例数据
> 存放类的属性信息，包括父类属性信息，如果是数组还包括数组长度，这部分按照4个字节对齐
## 2.3 对齐填充
> 由于JVM要求对象的起始地址必须是8的整数倍，必须填充补齐