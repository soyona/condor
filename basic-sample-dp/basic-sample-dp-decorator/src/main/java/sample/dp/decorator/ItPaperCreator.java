package sample.dp.decorator;

/**
 * @author soyona
 * @Package sample.dp.decorator
 * @Desc: 具体核心组件实现
 * @date 2018/7/12 09:38
 */
public class ItPaperCreator implements PaperCreator {
    @Override
    public void write() {
        System.out.println("It 论文内容是：分布式事务概念");
    }
}
