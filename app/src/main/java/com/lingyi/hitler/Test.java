package com.lingyi.hitler;

import android.util.Log;
import android.view.View;

import com.lingyi.annotation.protocol.Cmd;
import com.lingyi.annotation.protocol.CmdType;
import com.lingyi.annotation.protocol.VariantsType;

/**
 * @author sunchuanwen
 * @time 2019/5/7.
 */

public class Test  {

    private String value = TargetTest.class.getSimpleName();



    @Cmd(funType = CmdType.INSERTHEAD,
            targetClass = "com.lingyi.hitler.test.*",
            methodSignature = "(Landroid/view/View;)V")
    public static void autoTrace(View view){
        Log.i("lingyilog", "自动全埋点");
    }


    @Cmd(funType = CmdType.INSERTHEAD,
            targetClass = "com.lingyi.hitler.test.*",
            targetMethodName = "show",
            methodSignature = "([Ljava/lang/String;)Ljava/lang/String;")
    public static void inserMethod(String []params){
        Log.i("lingyilog", "inserMethod: 我是插入的方法");
    }

/*    @Cmd(funType = CmdType.INSERTHEAD,
            targetClass = "com.lingyi.hitler.test.TestMethodInsert",
            methodSignature = "insertParams")
    public static void inserMethodParams(String params){
        params+="我插入了一段";
        Log.i("lingyilog", "inserMethodParams: 我是插入的方法");
    }*/
/*
    @Cmd(funType = CmdType.ADDTRYCATCH,
            targetClass = "com.lingyi.hitler.test.TestMethodInsert" ,
            targetMethod = "testTryCatch")
    public static void tryCatch(Throwable  throwable){
        Log.i("lingyilog", "tryCatch: 发生了crash:"+throwable.getMessage());
    }


    @Cmd(funType = CmdType.ADDTRYCATCH,
            targetClass = "com.lingyi.hitler.test.TestMethodInsert" ,
            targetMethod = "testTryCatchReturn")
    public static int tryCatchReturn(Throwable  throwable){
        Log.i("lingyilog", "tryCatchReturn: 发生了crash:"+throwable.getMessage());
        return 0;
    }



    @Cmd(funType = CmdType.METHODREPLACE,
            targetClass = "com.lingyi.hitler.test.TestMethodInsert",
            targetMethod = "replaceMethodReturn")
    public static String replaceMethod(String params,int a){
        Log.i("lingyilog", "replaceMethod: 我替换方法");
        return params;
    }*/

}
