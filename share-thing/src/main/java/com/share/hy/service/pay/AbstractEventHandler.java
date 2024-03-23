package com.share.hy.service.pay;

import java.util.List;

public abstract class AbstractEventHandler<T> {


    public void handle(String rawMsg) {
        //将RawMsg转换为具体的类，并将其传入
        T t = this.parse(rawMsg);
        this.handleImpl(t);
    }

    protected abstract T parse(String rawMsg);


    protected abstract void handleImpl(T msg);

    public abstract List<String> getEventNames();

    public abstract String getPlatName();

}