# 1. Synchronized
## 1.1 Definition
### 1.1.1 Essentials
> Synchronization means to control the access of multiple threads to a shared resources.
```text

```
### 1.1.2 Intrinsic Lock
```text
Every Java object/class has an associanted intrinsic lock.A thread that needs exclusive access to an object's field
has to acquire the object's lock before accessing them,and releasing the intrinsic lock once it is done.
Other thread trying to access the object will block until the thread holding the lock releases it.
```
## 1.2 Three usages of Synchronized:
### 1.2.1 Synchronized Object's Methods
```text
    /**
     * synchronized object's method,the lock of Object
     */
    public synchronized void  say(){

    }
```
### 1.2.2 Synchronized static methods
```text
    /**
     * synchronized static method, the lock of class
     */
    public synchronized static void  read(){

    }
```
### 1.2.3 Synchronized Statements
````text
    public void sing(){
        /**
         *
         */
        synchronized(this){

        }

        synchronized(this.getClass()){

        }
    }
````
## 1.3 Synchronized Underlying
>[Underlying Analysis](https://www.artima.com/insidejvm/ed2/threadsynchP.html)

### 1.3.0 What's Monitor
> [What's Monitor](https://github.com/soyona/condor/tree/master/basic-sample-object#3objectmonitor)

### 1.3.1 Synchronized Statements

|Opcode|Operand(s)|Description|
|-|:-:|:-:|
|monitorenter|none|pop objectref, acquire the lock associated with objectref|
|monitorexit|none|pop objectref, acquire the lock associated with objectref|

### 1.3.2 Synchronized Methods
```text
When the virtual machine resovles the symbolic reference to a method,it determines whether the method is synchronized.
if so ,the virtual machine acquires a lock before invoking the method.
For instance method,the VM acquires the lock associated with the object upon which the method is beging invoked.
For a class method,the VM acquires the lock associated with the class to which the method belongs.
After a asynchronied method completes,whether it completes by returning    or by throwing an exception,the VM releases the lock.
```