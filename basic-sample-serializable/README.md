# 0. Disclaimer
## 0.1 Some serializers support cycle detection object sharing,others just write non-cyclic tree structrues.
## 0.2 Some include full metadata in serialized output,some don't;
## 0.3 Some cross platform,some are language specific;
## 0.4 Some are text based,some are binary;
## 0.5 Some support versioning forward/backward,both,some don't

> [Benchmarks serializers](https://github.com/eishay/jvm-serializers/wiki)

# 1. Serialization and Deserialization in Java
## 1.1 Serialization: 
> Serialization is a mechanism of converting the state of an object into a byte stream.
## 1.2 Deserialization:
> Deserialization is a mechanism to convert back to object from the byte stream.

# 2. What is stream?
> A stream is nothing but the sequence of data elements. 
In a computer system, there is a source that generates data in the form of stream and there is a destination which reads and consumes this stream. Here are the types of streams:

- Byte Stream:
    > Byte stream does not have any encoding scheme and it uses 8-bit (or one byte) to store a single character. Byte stream is a low level input output (I/O) operation that can be performed by a JAVA program. To process such byte stream, we need to use a buffered approach for IO operations. 
- Character Stream:
    > Character stream is composed of the streams of characters with proper encoding scheme such as ASCII, UNICODE, or any other format. Unlike byte stream, the character stream will automatically translate to and from the local character set. JAVA language uses UNICODE system to store characters as character stream.
- Data Stream:
    > Data Stream is used to perform binary IO operations for all the primitive data types in Java. We can perform I/O operations in an efficient and convenient way for Boolean, char, byte, short, int, long, float, double, and Strings, etc. by using data stream. It also allows us to read-write Java primitive data types instead of raw bytes.
- Object Stream:
    > the state of a JAVA object can be converted into a byte stream that can be stored into a database, file, or transported to any known location like from web tier to app tier as data value object in client-server RMI applications. This process of writing the object state into a byte stream is known as Serialization.        

# 3.  When we will use serialization and deserialization?
> In multi-tier JAVA/J2EE application (client-server RMI applications), 
when we make a remote invocation method or RMI from a web tier to app tier, we need to send the data value object that transfers the required business information from web tier to app tier after Serialization(we implement java.io. Serializable (Marker Interface) that we are now going to discuss in detail).

> [Reference](http://www.codenuclear.com/serialization-deserialization-java/) 
