package com.example.myeventbus;

import java.lang.reflect.Method;

/**
 * 有注释的方法类
 */
public class SubscribleMethod {
    private Method method;
    private ThreadMode threadMode;
    private Class<?> eventType;

    public SubscribleMethod(Method method, ThreadMode threadMode, Class<?> eventType) {
        this.method = method;
        this.threadMode = threadMode;
        this.eventType = eventType;
    }

    public Method getMethod() {
        return method;
    }

    public ThreadMode getThreadMode() {
        return threadMode;
    }

    public Class<?> getEventType() {
        return eventType;
    }
}
