package com.lingyi.hitler;

import android.util.Log;

import com.lingyi.annotation.protocol.Cmd;
import com.lingyi.annotation.protocol.CmdType;
import com.lingyi.annotation.protocol.VariantsType;

/**
 * @author sunchuanwen
 * @time 2019/5/7.
 */

public class Test  {

    private String value = TargetTest.class.getSimpleName();



    @Cmd(funType = CmdType.INSERTHEAD,targetClass = "com.lingyi.hitler.TestMethodInsert",originClass = "com.lingyi.hitler.Test",targetMethod = "show")
    public static void inserMethod(){
        Log.i("lingyilog", "inserMethod: 我是插入的方法");
    }

    @Cmd(funType = CmdType.ADDTRYCATCH,targetClass = "com.lingyi.hitler.TestMethodInsert" ,originClass ="com.lingyi.hitler.Test",targetMethod = "testTryCatch")
    public static void tryCatch(Throwable  throwable){
        Log.i("lingyilog", "tryCatch: 发生了crash:"+throwable.getMessage());
    }

}
