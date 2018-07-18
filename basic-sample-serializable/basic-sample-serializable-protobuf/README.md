# 1. Google Protocol Buffer
> [Google Developer Guide](https://developers.google.com/protocol-buffers/docs/overview)
 
> [Google protobuf GitHub](https://github.com/google/protobuf)
 
> [Google protobuf GitHub](https://github.com/google/protobuf/releases)
 
> [简书：语法](https://www.jianshu.com/p/e06ba6249edc)
 
> [简述：Protocal Buffer](https://www.jianshu.com/p/1538bf85dad1)


 
> Protobuf is Google's data interchange format
```text
Protocal Buffers is a language-neutral [ˈnju:trəl][中立],platform-neutral,extensible way of serializing structured data
for use in communications protocals,data storage,and more.
```
## 1.1 Varint
> [ProtoBuf 细节](https://www.ibm.com/developerworks/cn/linux/l-cn-gpb/index.html)

### 1.1.1
```text
ProtoBuf序列化所生成二进制消息非常紧凑，这得益于ProtoBuf采用的Varint方式。
```
### 1.1.2 Decoding
```text
只需将二进制序列读取到Java对应的结构类型中，速度非常快。
```