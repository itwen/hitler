package com.lingyi.hitler;

import com.lingyi.annotation.protocol.Cmd;
import com.lingyi.annotation.protocol.CmdType;
import com.lingyi.annotation.protocol.VariantsType;

/**
 * @author sunchuanwen
 * @time 2019/5/8.
 */


@Cmd(targetClass = "com.lingyi.hitler.TargetMath",
        funType = CmdType.CLASSREPLACE,
        buildType = VariantsType.TYPE_RELEASE)
public class NewMath {

    public int c = 10;

    public int add(int a,int b){
        return customAdd(a,b);
    }

    public int customAdd(int a,int b){
        return  a - b + c;
    }
}
