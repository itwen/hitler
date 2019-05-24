package com.lingyi.hitler.gradle.core

import com.lingyi.hitler.gradle.utils.ByteUtil
import com.lingyi.hitler.gradle.utils.LogUtils

/**
 * @author sunchuanwen
 * @time 2019/5/8.
 */
class ClassModifier{
    /**
     * class文件中常量池的起始偏移
     */
    private static final def CONSTANT_POLL_COUNT_INDEX = 8

    /**
     * CONSTANT_utf8_info 常量的tag标志
     */
    private static final def CONSTANT_utf8_info = 1

    /**
     * java 1.7以下11种　　java1.7以后新增了三种动态调用
     * 常量池中14中常量所占的长度,constant_utf8_info型常量除外,因为他是不定长的
     */
    private static final def CONSTANT_ITEM_LENGTH  = [0,0,0,5,5,9,9,3,3,5,5,5,5,0,0,4,3,0,5]

    /**
     * u1 u2 类型分别的字节数
     */
    private static final def u1 = 1
    private static final def u2 = 2

    /**
     * 修改常量池中constant_utf8_info 常量内容
     * @param oldStr 修改前的字符串
     * @param newStr 修改后的字符串
     * @return 修改后的字节码字节数组
     */
    public static byte [] modifyUTF8Constant(byte [] classByte,String oldStr , String newStr,String className){
        def constantCount = getContantPoolCount(classByte)
        def offset = CONSTANT_POLL_COUNT_INDEX + u2
        try {
            //常量池计数从１开始　第0个常量为预留 所以常量池个数　= constantCount - 2
            for(def i = 0 ; i < constantCount -1 ; i ++){
                def tag = ByteUtil.bytes2int(classByte,offset,u1)
                if(tag == CONSTANT_utf8_info){
                    def len = ByteUtil.bytes2int(classByte,offset+u1,u2)
                    offset += (u1+u2)
                    String str = ByteUtil.byte2String(classByte,offset,len)
                    if(str.equalsIgnoreCase(oldStr)){
                        LogUtils.instance.log("replaceClass[className:"+className+"] "+"oldStr:"+oldStr+" newStr:"+newStr)
                        byte [] strByte = ByteUtil.string2Byte(newStr)
                        byte [] strlen = ByteUtil.int2Bytes(newStr.length(),u2)
                        classByte = ByteUtil.bytesReplace(classByte,offset - u2,u2,strlen)
                        classByte = ByteUtil.bytesReplace(classByte,offset,len,strByte)
                    }else{
                        offset +=len
                    }
                }else{
                    offset +=CONSTANT_ITEM_LENGTH[tag]
                }
            }
        }catch (Exception e){
            LogUtils.instance.log("replace error:"+" className:"+className)
        }
        return classByte
    }

    /**
     * 获取常量池中常量的数量
     * @return 常量池数量
     */
    private static def getContantPoolCount(byte [] classByte){
        return ByteUtil.bytes2int(classByte,CONSTANT_POLL_COUNT_INDEX,u2)
    }
}