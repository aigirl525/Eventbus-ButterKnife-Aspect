package com.example.butterknifelibrary;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)//CLASS编译时注解 RUNTIME运行时注解 SOURCE源码注解
@Target(ElementType.TYPE)//注解作用范围：FIELD 属性 METHOD方法 TYPE 放在类上
public @interface ContentView {//@interface 则表明这个类是一个注解
    int value();//表面@BindView（）注解时，括号里面编写的为int类型的值
}
