# LinkedHashMap实现LRU缓存
> [LRU缓存实现](./src/main/java/sample/collection/linkedhashmap/LRU.java)
## 1.1 固定大小维护
```
1. LRU 缓存有固定大小capacity，超过固定大小，删除"特定元素"，根据 LRU策略，删除最近最少使用的元素。
通过重写removeEldestEntry方法来删除 顶端元素
```
```
@Override
protected boolean removeEldestEntry(Entry<K,V> eldest) {
    if(size() > capacity){
        return true;
    }
    return false;
}
```
## 1.2 访问时 改变顺序
> 访问方法：
```
public V get(Object key) {
    Node<K,V> e;
    if ((e = getNode(hash(key), key)) == null)
        return null;
    if (accessOrder)// 如果启动顺序访问，把访问元素放到链表最后
        afterNodeAccess(e);// 把访问元素放到链表最后，下面分析
    return e.value;
}
```
> 通过 afterNodeAccess(e) 把元素e放到链表最后
```
    void afterNodeAccess(Node<K,V> e) { // move node to last
        LinkedHashMap.Entry<K,V> last;
        if (accessOrder && (last = tail) != e) {
            LinkedHashMap.Entry<K,V> p =
                (LinkedHashMap.Entry<K,V>)e, b = p.before, a = p.after;
            p.after = null;
            if (b == null)
                head = a;
            else
                b.after = a;
            if (a != null)
                a.before = b;
            else
                last = b;
            if (last == null)
                head = p;
            else {
                p.before = last;
                last.after = p;
            }
            tail = p;
            ++modCount;
        }
    }
```