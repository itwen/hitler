package com.lingyi.annotation.protocol;

/**
 * @author sunchuanwen
 * @time 2019/5/7.
 */
public enum CmdType {
    INSERTHEAD(0),
    APPENDTAIL(1),
    REPLACE(2),
    ADDTRYCATCH(3),
    STATEUNKONW(4);

    private int value;

    CmdType(int value){
        this.value = value;
    }

    public static CmdType stateOfVaule(int value){
        for (CmdType cmdType:values()){
            if (cmdType.getValue() ==  value){
                return cmdType;
            }
        }

        return STATEUNKONW;
    }

    public int getValue(){
        return value;
    }
}
