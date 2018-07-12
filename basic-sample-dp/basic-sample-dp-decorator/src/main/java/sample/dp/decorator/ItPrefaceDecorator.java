package sample.dp.decorator;

/**
 * @author soyona
 * @Package sample.dp.decorator
 * @Desc: 装饰器 添加 概述
 * @date 2018/7/12 11:07
 */
public class ItPrefaceDecorator extends ItPaperDecorator {
    public ItPrefaceDecorator(PaperCreator paperCreator){
        super(paperCreator);
    }
    @Override
    public void write(){
        //在核心功能前，增强
        System.out.println("装饰器 添加 论文 【概述】。");
        component.write();
    }
}
