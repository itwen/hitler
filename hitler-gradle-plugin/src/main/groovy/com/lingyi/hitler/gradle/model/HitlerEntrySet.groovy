package com.lingyi.hitler.gradle.model

import com.lingyi.hitler.gradle.utils.LogUtils

/**
 * @author sunchuanwen
 * @time 2019/5/8.
 */
class HitlerEntrySet{
    public static final int INSERTOHEAD = 0x0
    public static final int APPENDTAIL = 0x01
    public static final int CLASS = 0x02
    public static final int ADDTRYCATCH = 0x03
    public static final int UNKONW = 0x04


    public List<HitlerEntry> entrySet


    public HashMap<String,List<HitlerEntry>> mEntryHashMap =  new HashMap<>()

    public List<HitlerEntry> classReplaceList  =  new ArrayList<>()

    public void setEntrySet(List<HitlerEntry> entries){
        this.entrySet = entries
        mEntryHashMap.clear()
        LogUtils.instance.log("parser,......"+entrySet.size())
        if (entrySet != null){
            for (HitlerEntry entry:entrySet){
                String key = entry.targetClass
                if (entry.mType == CLASS){
                    classReplaceList.add(entry)
                }else{
                    if (!mEntryHashMap.containsKey(key)){
                        mEntryHashMap.put(key,new ArrayList<>())
                    }
                    mEntryHashMap.get(key).add(entry)
                }
                LogUtils.instance.log("log...."+key+" size:"+mEntryHashMap.size())
            }
        }
    }


}