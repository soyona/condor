# 1. 对象序列化和反序列化过程
> ![](https://github.com/soyona/condor/blob/master/basic-sample-serializable/basic-sample-serializable-jdk/src/main/resources/img/jdk-serialization.png)
 
> [Import New 参考](http://www.importnew.com/24490.html)
 
> [Oracle参考](https://docs.oracle.com/javase/7/docs/platform/serialization/spec/serial-arch.html)
  
> 对象序列化应用
```text

> 序列化是对象持久化的手段，普遍应用在网络传输、RMI等场景中，
 
> Java对象是在JVM运行时创建的对象，其生命周期不会比JVM生命周期长。在现实应用中，可能要求在JVM停止运行之后能够持久化JAVA对象，并在将来重新读取被持久化的对象。Java对象序列化就是其中一种手段。
   
> 对象序列化是保存对象的状态（对象的成员变量），因此，对象序列化不会关注类中的静态变量。

```

# 2. Java对象序列化与反序列化
> 1. Java中被序列化的对象条件：
    >> 1.1. 实现 java.io.Serializable接口; 或者  Externalizable
    >> 1.2. Enum
    >> 1.3. Array
    >> 1.4. String 
```text
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
 
> 6. Transient 关键字的作用是控制变量的序列化，在变量声明前加上该关键字，可以阻止该变量被序列化到文件中，在被反序列化后，transient 变量的值被设为初始值，如 int 型的是 0，对象型的是 null。
 
> 7. 服务器端给客户端发送序列化对象数据，对象中有一些数据是敏感的，比如密码字符串等，希望对该密码字段在序列化时，进行加密，而客户端如果拥有解密的密钥，只有在客户端进行反序列化时，才可以对密码进行读取，这样可以一定程度保证序列化对象的数据安全。
 
> 8. 在类中增加writeObject 和 readObject 方法可以实现自定义序列化策略  


## 2.1 对象引用的序列化
> Order类中 有Member引用，那么，Member必须实现java.io.Serializable接口，否则 抛出java.io.NotSerializableException: sample.serializable.jdk.Member

## 2.2 继承关系的序列化
> Parent类若需序列化，必须实现 Parent 必须实现 java.io.Serializable接口 或者 提供默认无参构造。否则抛出 java.io.InvalidClassException: sample.serializable.jdk.Order; no valid constructor 
 
## 2.3 BlockDataOutputStream
> Buffered output stream

## 2.4 ArrayList 序列化
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
# 3. serialVersionUID的作用
> serialVersionUID用来表明类的不同版本，
 
> 如果希望类的不同版本（类的方法，属性等修改增加导致版本不一致）兼容，需要指定serialVersionUID，使得不同版本有相同的serialVersionUID。
 
> 如果不希望类的不同版本不兼容，需要指定不同的serialVersionUID，使得类的不同版本有不同的serialVersionUID，此时，可以使用默认的serialVersionUID生成方式。 
 
> 如果实例化后，修改了类（增加字段），如果该类没有指定serialVersionUID，那么，反序列化将抛异常。如果有serialVersionUID，反序列化时，更改的字段将设置为null，或者基本能类型的默认初始值。

 
## 3.1 serialVersionUID的两种生成方式：

### 3.1.1 默认 1L
 
### 3.1.2 根据类名、接口名、成员方法名、属性生成64位的哈希字段


# 4. Serialization's 5 problems
## 4.1 Serialization allows for refactoring
- Adding new fields to a class
- Changing the field from static to nonstatic
- Changing the fields from transient to nontransient
```text
We need serialVersionUID in the class.
```
## 4.2 Serialization is not secure
```text
Because the serialization binary format is fully documented and entirely reversible.
In fact,just dumping the contents of the binary serialized stream to the console is sufficient
to figure out what the class look like and contains.
```
## 4.3 Serialized data can be signed and sealed
```text
If you need to encrypt and sign an entire object,the simplest way is to put it in the 
javax.crypto.SealedObject and/or java.security.SignedObject wrapper.
```
## 4.4 Serialization can put a proxy in your stream
```text
Providing a writeReplace method on the original Person allows a different kind of object to be serialized in its place; 
similarly, if a readResolve method is found during deserialization, it is called to supply a replacement object back to the caller.
```
## 4.5 Trust,But Validate
```text
In the case of serialized objects, 
this means validating the fields to ensure that they hold legitimate values after deserialization, 
"just in case." 
We can do this by implementing the ObjectInputValidation interface and overriding the validateObject() method. 
If something looks amiss when it is called, we throw an InvalidObjectException.
```
