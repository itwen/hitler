package com.lingyi.hitler.gradle.utils

/**
 * @author sunchuanwen
 * @time 2019/5/8.
 */
class ByteUtil{

    /**
     *  将字节码转换成int
     * @param b  字节数组
     * @param start 起始位置
     * @param len 字节长度
     * @return 返回dui应的int
     */
    public static int bytes2int(byte [] b,int start,int len){
        int sum = 0
        int end = start+len
        for(int i = start;i < end ; i++){
            int n = ((int)(b[i])) & 0xff
            n <<= (--len) * 8
            sum = n+sum
        }
        return sum
    }


    /**
     * 把一个int 类型的数字转换成长度为len的字节数组
     * @param value 要转换的数字
     * @param len 要转换成的字节长度
     * @return 返回字节数组
     */
    public static byte[] int2Bytes(int value, int len){
        byte [] b = new byte[len]
        for(int i = 0 ; i < len ; i ++){
            b[len - i - 1] = (byte)((value >> 8*i ) & 0xff)
        }
        return b
    }

    /**
     * 将一个字节数组转成字符串
     * @param b 字节数组
     * @param start 起始位置
     * @param len 字节长度
     * @return  字符串
     */
    public static String byte2String(byte [] b , int start,int len){
        return new String(b,start,len)
    }

    public static byte[] bytesReplace(byte[] originBytes,int offset,int len,byte[] replaceBytes){
        byte [] newBytes = new byte[originBytes.length+(replaceBytes.length - len)]
        System.arraycopy(originBytes,0,newBytes ,0,offset)
        System.arraycopy(replaceBytes,0,newBytes,offset,replaceBytes.length)
        System.arraycopy(originBytes,offset+len,newBytes,offset+replaceBytes.length,originBytes.length-offset-len)
        return  newBytes
    }

    public static byte[] string2Byte(String str){
        return str.getBytes()
    }
}