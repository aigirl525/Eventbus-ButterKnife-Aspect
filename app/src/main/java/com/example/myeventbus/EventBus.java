package com.example.myeventbus;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventBus {
    private ExecutorService executorService;
    private Handler handler;
    Map<Object, List<SubscribleMethod>> cacheMap;
    private static EventBus eventBus = new EventBus();

    private EventBus() {
        this.cacheMap = new HashMap<>();
        executorService = Executors.newCachedThreadPool();
        handler = new Handler(Looper.getMainLooper());
    }

    public static EventBus getDefault() {
        return eventBus;
    }

    /**
     * 通过方法的注解，记录当前类订阅的所有事件
     *
     * @param activity
     */
    public void register(Object activity) {

        List<SubscribleMethod> list = cacheMap.get(activity);

        if (list == null) {
            cacheMap.put(activity, getSubscribleMethod(activity));
        }
    }

    public void unregister(Object activity) {
        List<SubscribleMethod> list = cacheMap.get(activity);

        if (list == null) {
            cacheMap.remove(activity);
        }
    }

    /**
     * 通过类的注解，来获取处理事件的方法
     *
     * @param activity
     * @return
     */
    private List<SubscribleMethod> getSubscribleMethod(Object activity) {
        List<SubscribleMethod> list = new ArrayList<>();

        Class clazz = activity.getClass();
        //如果类全名以这些字符开头，则认为是jdk的，不是我们自定义的，自然没必要去拿注解
        while (clazz != null) {
            String name = clazz.getName();
            if (name.startsWith("java.")
                    || name.startsWith("javax.")
                    || name.startsWith("android.")) {
                break;
            }
            //获取当前class所有声明的public方法
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                Subscribe subscribe = method.getAnnotation(Subscribe.class);
                if (subscribe == null) {
                    continue;
                }
                Class[] paratems = method.getParameterTypes();
                if (paratems.length != 1) {
                    throw new RuntimeException("eventbus 只能接收到一个参数");
                }

                ThreadMode threadMode = subscribe.threadMode();

                SubscribleMethod subscribleMethod = new SubscribleMethod(method, threadMode, paratems[0]);
                list.add(subscribleMethod);
            }
            clazz = clazz.getSuperclass();

        }
        return list;
    }

    /**
     * 事件生产者分发事件
     *
     * @param senduoEvent
     */
    public void post(final Object senduoEvent) {
        Set<Object> set = cacheMap.keySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            final Object activity = iterator.next();
            List<SubscribleMethod> list = cacheMap.get(activity);
            for (final SubscribleMethod subscribleMethod : list) {
                //isAssignableFrom()方法是判断是否为某个类的父类，instanceof关键字是判断是否某个类的子类。
                //类.class.isAssignableFrom(子类.class)
                //子类实例 instanceof 父类类型
                if (subscribleMethod.getEventType().isAssignableFrom(senduoEvent.getClass())) {
                    switch (subscribleMethod.getThreadMode()) {
                        case Async:
                            if (Looper.myLooper() == Looper.getMainLooper()) {
                                executorService.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        invoke(subscribleMethod, activity, senduoEvent);
                                    }
                                });
                            } else {
                                invoke(subscribleMethod, activity, senduoEvent);
                            }
                            break;
                        case MainThread:
                            if (Looper.getMainLooper() == Looper.getMainLooper()) {
                                invoke(subscribleMethod, activity, senduoEvent);
                            } else {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        invoke(subscribleMethod, activity, senduoEvent);
                                    }
                                });
                            }
                            break;
                        case PostThread:
                            break;
                        case BackgroundThread:
                            break;
                        default:
                            break;
                    }
                }
            }

        }
    }

    private void invoke(SubscribleMethod subscribleMethod, Object activity, Object senduoEvent) {
        Method method = subscribleMethod.getMethod();
        try {
            method.invoke(activity, senduoEvent);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
