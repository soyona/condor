#一、 LinkedHashMap
## 类定义：
```text
public class LinkedHashMap<K,V>
    extends HashMap<K,V>
    implements Map<K,V>
{

}
```
>LinkedHashMap是HashMap和双向链表的组合

## Entry
```
    static class Entry<K,V> extends HashMap.Node<K,V> {
        Entry<K,V> before, after;
        Entry(int hash, K key, V value, Node<K,V> next) {
            super(hash, key, value, next);
        }
    }
```
> 继承自HashMap.Node<K,V>，
> 增加了两个引用：Entry<K,V> before, after;

## LinkedHashMap中的Entry结构：
![entry](https://github.com/soyona/condor/blob/master/basic-sample-collection/src/main/resources/linkedhashmap-entry.png)

## LinkedHashMap属性：
* head节点，eldest节点
```
/**
 * The head (eldest) of the doubly linked list.
 */
transient LinkedHashMap.Entry<K,V> head;
```
* tail节点，youngest最年轻的节点
```
/**
 * The tail (youngest) of the doubly linked list.
 */
transient LinkedHashMap.Entry<K,V> tail;
```
* 迭代顺序：true是访问顺序，false是插入顺序
```
/**
 * The iteration ordering method for this linked hash map: <tt>true</tt>
 * for access-order, <tt>false</tt> for insertion-order.
 *
 * @serial
 */
final boolean accessOrder;
```
>可实现LRU算法，如果accessOrder为true，get和put方法都会调用void recordAccess(HashMap<K,V> m)方法将最近使用Entry放到双向链表的尾部。否则，do nothing！

## LinkedHashMap实现LRU算法
>使用LinkedHashMap实现LUR算法，需要把accessOrder设为true。
>实例请参考：
## LinkedHashMap 与 HashMap不同点
> LinkedHashMap在插入元素后，仍需插入双向链表以维护顺序；

> LinkedHashMap 是有序的；

> LinkedHashMap新元素会插入双向链表的尾部；

## 参考：
> https://blog.csdn.net/justloveyou_/article/details/71713781


# 二、List
## 参考：
> https://blog.csdn.net/justloveyou_/article/details/52955619
