package src.edu.hitsz.prop;

public interface Observer {
    /**
     * 更新观察者的状态
     * @param flag 是否需要销毁当前飞行物
     */
    void update(boolean flag);
}