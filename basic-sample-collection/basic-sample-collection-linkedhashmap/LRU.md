# LinkedHashMap实现LRU缓存
> [LRU缓存实现](./src/main/java/sample/collection/linkedhashmap/LRU.java)

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
