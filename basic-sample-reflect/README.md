总结
===
反射用途：
---
- 运行时创建类新实例对象、调用类的方法、修改对象的属性
- 获取类的信息
- 动态代理
- 获取运行时类的真实信息：编译类型/运行时类型

反射优缺点：
---
- 反射效率低、破坏封装、不安全(可访问私有方法)


获取Class三种方式：
---
- Object.getClass()
- X.class
- Class.forName()


Class API汇总：
---

- Constructor
    + getConstructor 
        * public Constructor<T> getConstructor(Class<?>... parameterTypes)
        * 根据指定参数类型 返回public 构造对象，不包含父类
    + getConstructors
        * public Constructor<?>[] getConstructors()
        * 返回Class中定义的public的 构造方法  public constructors，不包含父类中的
    + getDeclaredConstructor
        * public Constructor<T> getDeclaredConstructor(Class<?>... parameterTypes)
        * 指定构造参数类型列表返回 构造
    + getDeclaredConstructors
        * public Constructor<?>[] getDeclaredConstructors() 
        * 返回所有声明的构造方法，包括 public, protected, default,and private constructors
- Field
    + getField
        * public Field getField(String name)
        * 根据指定名称，获取类中声明的public Field ,包括父类
    + getFields
        * public Field[] getFields()
        * 返回public fields ，包含父类
    + getDeclaredField
        * public Field getDeclaredField(String name) 
        * 获取指定声明的Field，不包括继承的
    + getDeclaredFields
        * 获取在类中声明的所有Fields，包括  public, protected, default, package，private fiedls 访问权限，不包括继承的
        * public Field[] getDeclaredFields()
- Method
    + getMethod
        * public Method getMethod(String name, Class<?>... parameterTypes)
        * 指定方法名称，方法参数列表 获取 public 成员方法，包括父类
    + getMethods
        * public Method[] getMethods()
        * 获取Class中定义所有public的方法对象，包括父类
    + getDeclaredMethod
        * public Method getDeclaredMethod(String name, Class<?>... parameterTypes)
        * 获取声明的方法指定名称，指定参数类型列表，不包含父类
    + getDecalredMethods
        * public Method[] getDeclaredMethods()
        * 返回所有 public, protected, default, and private methods  ，不包含继承来的
- Interface
    + getInterfaces
        * public Class<?>[] getInterfaces()
        * 返回该类实现的接口，或者接口继承的接口，按照声明顺序，不包含泛型参数信息
    + getGenericInterfaces
        * public Type[] getGenericInterfaces()
        * 返回参数化类型的接口
- Class
    + getClasses
        * public Class<?>[] getClasses()
        * 类中定义的public的 成员类 或者 接口