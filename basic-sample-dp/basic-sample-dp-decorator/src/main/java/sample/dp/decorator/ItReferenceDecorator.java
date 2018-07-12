package sample.dp.decorator;

/**
 * @author soyona
 * @Package sample.dp.decorator
 * @Desc:  装饰器 添加 参考文献
 * @date 2018/7/12 11:11
 */
public class ItReferenceDecorator extends ItPaperDecorator {
    public ItReferenceDecorator(PaperCreator paperCreator){
        super(paperCreator);
    }

    @Override
    public void write(){
        component.write();
        //在核心功能后，增强
        System.out.println("装饰器 添加 论文 【参考文献】。");
    }
}
