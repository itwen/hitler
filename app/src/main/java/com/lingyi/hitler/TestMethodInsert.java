package com.lingyi.hitler;

import android.util.Log;

/**
 * create by sunchuanwen on 19-5-10
 * Copyright © 2018 - sunchuanwen. All Rights Reserved.
 */
public class TestMethodInsert {

    public String show(){
        String hahah = "hahahhahah_sssss";
        return hahah.split("_")[0];
    }

    public void testTryCatch(){
        int a = 0;
        int b = 3;
        int c = b/a;
        Log.i("lingyilog", "testTryCatch: c:"+c);
    }
}
