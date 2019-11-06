package com.example.aspectlibrary;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * 网络判断，权限管理，Log日志的统一管理，登录判断,防按钮多次点击
 * 处理网络检测切面
 */

@Aspect
public class SectionAspect {

    /**
     * 找到处理的切点
     *
     */
    @Pointcut("execution(@com.example.aspectlibrary.CheckLogin * *(..)) && @annotation(islogin)")
    public void CheckLoginBehavior(CheckLogin islogin){
    }
    /**
     * 处理切面
     */
    @Around("CheckLoginBehavior(islogin)")
    public Object CheckLogin(ProceedingJoinPoint joinPoint,CheckLogin islogin) throws Throwable{
        Log.e("TAG","checkLogin");
        //这里只要可以进来，就可以去做下边的操作了
        //在这里可以做一些共有代码的处理
        //比如做埋点，日志上传，权限检测，网络监测
        //网络检测
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //1.获取CheckNet注解 NDK图片压缩 C++调用Java方法
        CheckLogin checkLogin = signature.getMethod().getAnnotation(CheckLogin.class);
        if (checkLogin != null){
            //2.判断有没有网络，如何获取context
            Context context = (Context)joinPoint.getThis();
            if (islogin.value()) {
                Log.e("TAG","checklogin: 登录成功");
                return joinPoint.proceed();
            }else {
                Log.e("TAG","checklogin: 登录失败");
                Toast.makeText(context , "请先登录" , Toast.LENGTH_SHORT).show();
                return null;
            }
        }
        return joinPoint.proceed();
    }

    /**
     * 找到处理的切点
     *
     */
    @Pointcut("execution(@com.example.aspectlibrary.CheckNet * *(..))")
    public void checkNetBehavior(){
    }
    /**
     * 处理切面
     */
    @Around("checkNetBehavior()")
    public Object checkNet(ProceedingJoinPoint joinPoint) throws Throwable{
        Log.e("TAG","checkNet");
        //这里只要可以进来，就可以去做下边的操作了
        //在这里可以做一些共有代码的处理
        //比如做埋点，日志上传，权限检测，网络监测
        //网络检测
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //1.获取CheckNet注解 NDK图片压缩 C++调用Java方法
        CheckNet checkNet = signature.getMethod().getAnnotation(CheckNet.class);
        if (checkNet != null){
            //2.判断有没有网络，如何获取context
            Object object = joinPoint.getThis();//View,Activity,Fragment;getThis()方法表示当前切点方法所在的类
            Context context = getContext(object);
            if (context != null){
                if (!CheckNetUtil.isNetworkAvailable(context)){
                    //3.没有网络就不要再往下边执行
                    Toast.makeText(context , "请检查您的网络" , Toast.LENGTH_SHORT).show();
                    return null ;
                }
            }
        }
        return joinPoint.proceed();
    }


    /**
     * 通过对象获取上下文
     * @param object
     * @return
     */
   private Context getContext(Object object){
       if (object instanceof Activity){
           return (Activity)object;
       }else if (object instanceof Fragment){
           Fragment fragment = (Fragment)object;
           return fragment.getActivity();
       }else if (object instanceof View){
           View view = (View)object;
           return view.getContext();
       }
       return null;
   }

}
