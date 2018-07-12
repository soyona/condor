package sample.dp.decorator;

/**
 * @author soyona
 * @Package sample.dp.decorator
 * @Desc: 装饰器，实现被装饰的接口，包括 核心组件，核心功能 委托给 核心组件实现
 * @date 2018/7/12 10:06
 */
public class ItPaperDecorator implements PaperCreator {
    PaperCreator component;
    public ItPaperDecorator(PaperCreator paperCreator){
        this.component = paperCreator;
    }
    @Override
    public void write() {
        component.write();
    }
}
