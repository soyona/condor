# 查看字节码
## javap -c PriceEvent.class
```text
kourais-MacBook-Pro:cglib kanglei$ javap -c PriceEvent.class 
Compiled from "PriceEvent.java"
public class sample.dp.proxy.cglib.PriceEvent {
  public sample.dp.proxy.cglib.PriceEvent();
    Code:
       0: aload_0
       1: invokespecial #1                  // Method java/lang/Object."<init>":()V
       4: return

  public static void main(java.lang.String[]);
    Code:
       0: new           #2                  // class net/sf/cglib/proxy/Enhancer
       3: dup
       4: invokespecial #3                  // Method net/sf/cglib/proxy/Enhancer."<init>":()V
       7: astore_1
       8: aload_1
       9: ldc           #4                  // class sample/dp/proxy/cglib/PriceService
      11: invokevirtual #5                  // Method net/sf/cglib/proxy/Enhancer.setSuperclass:(Ljava/lang/Class;)V
      14: aload_1
      15: new           #6                  // class sample/dp/proxy/cglib/PriceServiceProxy
      18: dup
      19: invokespecial #7                  // Method sample/dp/proxy/cglib/PriceServiceProxy."<init>":()V
      22: invokevirtual #8                  // Method net/sf/cglib/proxy/Enhancer.setCallback:(Lnet/sf/cglib/proxy/Callback;)V
      25: aload_1
      26: invokevirtual #9                  // Method net/sf/cglib/proxy/Enhancer.create:()Ljava/lang/Object;
      29: checkcast     #4                  // class sample/dp/proxy/cglib/PriceService
      32: astore_2
      33: aload_2
      34: invokevirtual #10                 // Method sample/dp/proxy/cglib/PriceService.getPromotionPrice:()Ljava/lang/String;
      37: pop
      38: return
}

```