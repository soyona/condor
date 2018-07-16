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
### 1.3.0 Object's Head
> [Object's Head figure](https://wiki.openjdk.java.net/display/HotSpot/Synchronization)

> [Object's Header](https://wiki.openjdk.java.net/download/attachments/11829266/Synchronization.gif?version=4&modificationDate=1208918680000&api=v2)

### 1.3.1 Synchronized Statements
### 1.3.2 Synchronized Methods