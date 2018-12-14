package com.heizi.uitopbar.util.top_ui;
/**
 * describe: UIWindowInsetHelper的接口
 * authors: heizi
 * email: 2867058917@qq.com
 * className: INotchInsetConsumer
 * createTime: 2018/11/20 17:28
 */
public interface INotchInsetConsumer {
    /**
     *
     * true是停止发送到子视图
     */
    boolean notifyInsetMaybeChanged();
}