package com.example.aspectlibrary;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记切点 注解
 */

//FIFLD：如果是在MainActivity中使用的话，就只能用于peivate String name 类似的这些属性，不能用于方法、类上边
//METHOD：如果是在MainActivity中使用的话，就只能用于方法上边，不能用于属性、类上
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)//RUNTIME：用于app运行 CLASS：用于编译时，比如ButterKnife注解 SOURCE：代表资源
public @interface CheckNet {
}
