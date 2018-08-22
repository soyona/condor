# CAS
> [JDK8 CAS增强](http://ifeve.com/enhanced-cas-in-jdk8/)
# 1. CAS 问题
## 1.1 CAS缺点-ABA
```text
对于一个变量，值从A->B ,再从 B->A过程，CAS检测不到该过程；
解决方式：添加版本号；变量的每次更新版本号递增；(v=1)A->(v=2)B->(v=3)A;
```
## 1.2 CAS缺点-循环时间长
## 1.3 CAS缺点-仅针对单个共享变量

