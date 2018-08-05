# 1. 查找第n个元素
> 方法： E get(int index)
```text
public E get(int index) {
        checkElementIndex(index);//下面分析
        return node(index).item;//下面分析
}
```
> 调用方法：
```text
    private boolean isElementIndex(int index) {
        return index >= 0 && index < size;
    }
```
> 调用方法：node(index)
```text
    Node<E> node(int index) {
        // assert isElementIndex(index);

        if (index < (size >> 1)) {
            Node<E> x = first;
            for (int i = 0; i < index; i++)
                x = x.next;
            return x;
        } else {
            Node<E> x = last;
            for (int i = size - 1; i > index; i--)
                x = x.prev;
            return x;
        }
    }
```