
# 算法

## 1.单链表找倒数第n个
    参考：https://blog.csdn.net/wangyukl/article/details/67071892
     
    - 解题思路：创建两个指向head的指针p q,让p遍历，p先开始移动，p走到第n-1个节点是，之后p q 一起往后移动，这时候当p指向最后一个节点的时候，q就指向了倒数第n个节点的位置，这时候返回q就可以了。

