package com.lingyi.hitler.gradle.core.pipeline

import com.lingyi.hitler.gradle.core.ClassModifier
import com.lingyi.hitler.gradle.model.HitlerEntry
import com.lingyi.hitler.gradle.utils.LogUtils


/**
 * @author sunchuanwen
 * @time 2019/5/9.
 */
class ClassReplaceValve implements Valve{

    private Valve next
    @Override
    Valve getNext() {
        return next
    }

    @Override
    void setNext(Valve valve) {
        this.next = valve
    }

    @Override
    void invoke(Handling handling) {
        handling.valveName = name()
        if (handling != null && handling.bytes != null && handling.bytes.length > 0 &&handling.context != null){
            handling.context.mEntrySet.classReplaceList.each {HitlerEntry entry->
                String originClass = handling.classPath.replace("/",".") - DOT_CLASS
                if (needProcess(handling,entry) && handling.classPath.endsWith(DOT_CLASS) && !originClass.contains(entry.targetClass) && !originClass.contains(entry.originClass)){
                    handling.bytes = ClassModifier.modifyUTF8Constant(handling.bytes,entry.targetClass.replace(".","/"),entry.originClass.replace(".","/"),originClass)
                }
            }
        }
        getNext().invoke(handling)
    }

    @Override
    String name() {
        return "ClassReplaceValve"
    }

    private boolean needProcess(Handling handling, HitlerEntry entry){
        boolean debugSkip = handling.context.mExtension.debugSkip
        boolean releaseSkip = handling.context.mExtension.debugSkip
        String variantName = handling.context.variant.name
        if (debugSkip && variantName.contains("debug"))return false
        if (releaseSkip && variantName.contains("release"))return false
        if ("default".equals(entry.mBuildType) || variantName.contains(entry.mBuildType))
            return true
        return false
     }
}