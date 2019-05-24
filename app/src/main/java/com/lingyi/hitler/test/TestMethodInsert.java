package com.lingyi.hitler.test;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * create by sunchuanwen on 19-5-10
 * Copyright © 2018 - sunchuanwen. All Rights Reserved.
 */
public class TestMethodInsert {

    private View view;

    public TestMethodInsert(View view){
        this.view = view;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    public String show(String  s[]){
        String hahah = "hahahhahah_sssss";
        return hahah.split("_")[0];
    }

    public void testTryCatch(){
        int a = 0;
        int b = 3;
        int c = b/a;
        Log.i("lingyilog", "testTryCatch: c:"+c);
    }


    public int testTryCatchReturn(){
        int a = 0;
        int b = 3;
        int c = b/a;
        return c;
    }

    public void insertParams(String params){
        Log.i("lingyilog", "insertParams: "+params);
    }

    public void replaceMethod(String params){
        System.out.println(params+" replaceMethod 我是原来的");
    }

    public String replaceMethodReturn(String params,int a){
        System.out.println(params+" replaceMethodReturn 我是原来的");
        return params;
    }
}
