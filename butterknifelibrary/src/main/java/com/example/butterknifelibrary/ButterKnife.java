package com.example.butterknifelibrary;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ButterKnife {

    public static void inJect(Activity activity){
        injectContentView(activity);
        findViewById(activity);
        setOnClickListener(activity);
    }

    /**
     * 通过反射来给注解的变量创建实例
     * @param activity
     */
    public static void findViewById(Activity activity){
        //获取Activity的class
        Class clazz = activity.getClass();
        //获取该类中的所有声明的属性
        Field[] fields = clazz.getDeclaredFields();
        //遍历所有属性，找到用@BindView注解了的属性
        for (int i = 0 ; i < fields.length;i++){
            Field field = fields[i];
            //获取属性上的注解对象
            BindView bindView = field.getAnnotation(BindView.class);
            if (bindView != null){
                int viewId = bindView.value();
                View view = activity.findViewById(viewId);
                try {
                    //私有属性也可以动态注入（不写该句代码，private声明的属性会报异常）
                    field.setAccessible(true);
                    field.set(activity,view);
                }catch (IllegalAccessException e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 实现事件绑定的注解
     * @param activity
     */
    public static void setOnClickListener(final Activity activity){
        //获取该ACtivity的所有方法
        Class<?> clazz = activity.getClass();
        try {
            final Method method = clazz.getDeclaredMethod("onClick",View.class);
            //该方法上是否有OnClick里面所有的值
            OnClick onClick = method.getAnnotation(OnClick.class);
            if (onClick != null){
                //获取onclick方法里面所有的值
                int[] viewIds = onClick.value();
                //先findviewbyid，setonclick
                for (int viewId : viewIds){
                    //先findviewbyid
                    final View view = activity.findViewById(viewId);
                    //后设置setonclick
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //首先需要判断 方法是否需要检测网络
                            //反射调用原来配置了OnClick的方法
                            method.setAccessible(true);//私有的方法
                            try {
                                method.invoke(activity);//调用无参的方法
                            }catch (Exception e){
                                e.printStackTrace();
                                try {
                                    method.invoke(activity,view);//调用有餐的方法
                                }catch (Exception e1){
                                    e1.printStackTrace();
                                }
                            }
                        }
                    });
                }
            }
        }catch (NoSuchMethodException e){
            e.printStackTrace();
        }

    }
    /**
     * 布局注入的具体实现
     */
    private static void injectContentView(Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();
        ContentView contentView = clazz.getAnnotation(ContentView.class);
        if (contentView != null){// 存在
            int contentViewLayoutId = contentView.value();
            try {
                Method method = clazz.getMethod("setContentView", int.class);
                method.setAccessible(true);
                method.invoke(activity, contentViewLayoutId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
