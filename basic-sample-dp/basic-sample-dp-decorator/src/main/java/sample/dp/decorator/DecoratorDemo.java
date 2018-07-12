package sample.dp.decorator;

/**
 * @author soyona
 * @Package sample.dp.decorator
 * @Desc:
 * @date 2018/7/12 10:10
 */
public class DecoratorDemo {
    public static void main(String[] args) {
        // 核心业务：论文内容
        // 可自行选择 添加 概述  还是 添加 参考文献，称：动态

        // 1. 添加 概述，装饰器
        PaperCreator paper = new ItPrefaceDecorator(new ItPaperCreator());
        paper.write();


        // 2. 添加 参考文献，装饰器
        PaperCreator paper_1 =  new ItReferenceDecorator(new ItPaperCreator());
        paper_1.write();

    }
}
