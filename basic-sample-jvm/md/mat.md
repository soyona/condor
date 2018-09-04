# 分析案例
> [案例参考](https://www.cnblogs.com/trust-freedom/p/6744948.html)

## MAT认识
### 
```text
Shallow Size:是对象本身占据的内存的大小，不包含其引用的对象。对于常规对象（非数组）的Shallow Size由其成员变量的数量和类型来定，而数组的ShallowSize由数组类型和数组长度来决定，它为数组元素大小的总和。

Retained Size:当前对象大小+当前对象可直接或间接引用到的对象的大小总和。(间接引用的含义：A->B->C,C就是间接引用) ，并且排除被GC Roots直接或者间接引用的对象
```