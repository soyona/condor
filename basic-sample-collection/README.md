# LinkedHashMap
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
[entry](https://github.com/soyona/condor/blob/master/basic-sample-collection/src/main/resources/linkedhashmap-entry.png)
