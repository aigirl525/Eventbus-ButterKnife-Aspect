package com.example.myeventbus;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 自定义的注释
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Subscribe {
    ThreadMode threadMode() default ThreadMode.PostThread;

}
