# OutOfMemoryError: Java heap space
> sample.jvm.gc.oo.OOMOfHeap.java
```text
Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
	at sample.jvm.oo.OOMOfHeap.main(OOMOfHeap.java:14)
	
```

# OutOfMemoryError: Metaspace
> sample.jvm.gc.oo.OOMOfMetaSpace.java
```text
Exception in thread "main" javassist.CannotCompileException: by java.lang.OutOfMemoryError: Metaspace
    at javassist.ClassPool.toClass(ClassPool.java:1085)
    at javassist.ClassPool.toClass(ClassPool.java:1028)
    at javassist.ClassPool.toClass(ClassPool.java:986)
    at javassist.CtClass.toClass(CtClass.java:1079)
    at sample.jvm.oo.OOMOfMetaSpace.javassist(OOMOfMetaSpace.java:26)
    at sample.jvm.oo.OOMOfMetaSpace.main(OOMOfMetaSpace.java:20)
    Caused by: java.lang.OutOfMemoryError: Metaspace
    at java.lang.ClassLoader.defineClass1(Native Method)
    at java.lang.ClassLoader.defineClass(ClassLoader.java:760)
    at java.lang.ClassLoader.defineClass(ClassLoader.java:642)
    at sun.reflect.GeneratedMethodAccessor1.invoke(Unknown Source)
    at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
    at java.lang.reflect.Method.invoke(Method.java:497)
    at javassist.ClassPool.toClass2(ClassPool.java:1098)
    at javassist.ClassPool.toClass(ClassPool.java:1079)
```

# OutOfMemoryError: GC overhead limit exceeded
> sample.jvm.gc.oo.OOMOfOverheadLimitExceeded.java
```text
Exception in thread "main" java.lang.OutOfMemoryError: GC overhead limit exceeded
	at java.util.Hashtable.addEntry(Hashtable.java:435)
	at java.util.Hashtable.put(Hashtable.java:476)
	at sample.jvm.oo.OOMOfOverheadLimitExceeded.main(OOMOfOverheadLimitExceeded.java:19)
```
# StackOverflowError
> sample.jvm.gc.oo.StackOverflowErrorDemo.java
```text
10080
10081
10082
Exception in thread "main" java.lang.StackOverflowError
	at sun.nio.cs.UTF_8$Encoder.encodeLoop(UTF_8.java:691)
	at java.nio.charset.CharsetEncoder.encode(CharsetEncoder.java:579)
	at sun.nio.cs.StreamEncoder.implWrite(StreamEncoder.java:271)
	at sun.nio.cs.StreamEncoder.write(StreamEncoder.java:125)
	at java.io.OutputStreamWriter.write(OutputStreamWriter.java:207)
	at java.io.BufferedWriter.flushBuffer(BufferedWriter.java:129)
	at java.io.PrintStream.write(PrintStream.java:526)
	at java.io.PrintStream.print(PrintStream.java:597)
	at java.io.PrintStream.println(PrintStream.java:736)
```

# StackOverflowError 2
> sample.jvm.oo.StackOverflowErrorDemo2
 
> toString()方法中有相互依赖，造成方法相互调用 