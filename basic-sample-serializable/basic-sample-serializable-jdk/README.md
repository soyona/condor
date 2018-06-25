# 对象序列化
> 序列化是对象持久化的手段，普遍应用在网络传输、RMI等场景中，
 
> Java对象是在JVM运行时创建的对象，其生命周期不会比JVM生命周期长。在现实应用中，可能要求在JVM停止运行之后能够持久化JAVA对象，并在将来重新读取被持久化的对象。Java对象序列化就是其中一种手段。
   
> 对象序列化是保存对象的状态（对象的成员变量），因此，对象序列化不会关注类中的静态变量。

# Java对象序列化与反序列化
> 1. Java中被序列化的对象条件：
    >> 1.1. 实现 java.io.Serializable接口;
    >> 1.2. Enum
    >> 1.3. Array
    >> 1.4. String 
```java
// remaining cases
if (obj instanceof String) {// String
    writeString((String) obj, unshared);
} else if (cl.isArray()) { // Array
    writeArray(obj, desc, unshared);
} else if (obj instanceof Enum) {// Enum
    writeEnum((Enum<?>) obj, desc, unshared);
} else if (obj instanceof Serializable) { //Serializable 
    writeOrdinaryObject(obj, desc, unshared);
} else {
    if (extendedDebugInfo) {
        throw new NotSerializableException(
            cl.getName() + "\n" + debugInfoStack.toString());
    } else {
        throw new NotSerializableException(cl.getName());
    }
}
```    
 
> 2. 通过 java.io.ObjectOutputStream 进行序列化
 
> 3. 通过 java.io.ObjectInputStream 进行反序列化
 
> 4. 反序列化条件：序列化ID必须一致(private static final long serialVersionUID)

> 5. 序列化不保存静态变量。
 
> 6. 父类若需序列化，必须实现 java.io.Serializable接口 
 
> 7. Transient 关键字的作用是控制变量的序列化，在变量声明前加上该关键字，可以阻止该变量被序列化到文件中，在被反序列化后，transient 变量的值被设为初始值，如 int 型的是 0，对象型的是 null。
 
> 8. 服务器端给客户端发送序列化对象数据，对象中有一些数据是敏感的，比如密码字符串等，希望对该密码字段在序列化时，进行加密，而客户端如果拥有解密的密钥，只有在客户端进行反序列化时，才可以对密码进行读取，这样可以一定程度保证序列化对象的数据安全。
 
> 9. 在类中增加writeObject 和 readObject 方法可以实现自定义序列化策略  


## BlockDataOutputStream
> Buffered output stream

# ArrayList 序列化
## ArrayList 自定义 writeObject 和 readObject 
 
## ArrayList transient Object[] elementData
```java
   /**
     * The array buffer into which the elements of the ArrayList are stored.
     * The capacity of the ArrayList is the length of this array buffer. Any
     * empty ArrayList with elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA
     * will be expanded to DEFAULT_CAPACITY when the first element is added.
     */
    transient Object[] elementData; // non-private to simplify nested class access
``` 
```text

防止序列化空对象null，所以transient
```