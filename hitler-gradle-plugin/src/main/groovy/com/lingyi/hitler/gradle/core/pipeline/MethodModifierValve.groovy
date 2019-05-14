package com.lingyi.hitler.gradle.core.pipeline

import com.lingyi.hitler.gradle.model.HitlerEntry
import com.lingyi.hitler.gradle.utils.LogUtils
import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import javassist.CtNewMethod


/**
 * @author sunchuanwen
 * @time 2019/5/9.
 */
class MethodModifierValve implements Valve{
    private Valve next
    ClassPool classPool
    public MethodModifierValve(){
        classPool = ClassPool.default
    }
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
        if (handling != null && handling.classPath.endsWith(DOT_CLASS)){
            handling.context.mEntrySet.mEntryHashMap.each {String key,List<HitlerEntry> values->
                if (handling.classPath.replace("/",".").contains(key)){
                    CtClass ctClass =  null
                    try {
                        ctClass = classPool.get(key)
                    }catch (Exception e){
                        LogUtils.instance.log("classpool error")
                        classPool.appendClassPath(handling.inputFile.absolutePath)
                        ctClass =  classPool.get(key)
                    }
                    values.each {HitlerEntry entry->
                        insert(entry,ctClass)
                    }
                    LogUtils.instance.log("save befor")
                    handling.bytes = ctClass.toBytecode()
                    LogUtils.instance.log("save after")
                    ctClass.detach()
                }
            }

        }
        getNext().invoke(handling)
    }

    private void insert(HitlerEntry entry,CtClass ctClass){
        switch (entry.mType){
            case 0:
                insertToHead(entry,ctClass)
                break
            case 1:
                appendTail(entry,ctClass)
                break
            case 3:
                addTryCatch(entry,ctClass)
                break
        }
    }

    @Override
    String name() {
        return "MethodModifierValve"
    }

    private void insertToHead(HitlerEntry entry, CtClass ctClass){
        LogUtils.instance.log("insertToHead "+entry.targetClass)
        CtMethod method = ctClass.getDeclaredMethod(entry.targetMethod)
        LogUtils.instance.log("methodName:"+method.name+" retureType:"+method.getReturnType().name)
        String inserCode = entry.originClass+"."+entry.originMethod+"();"
        LogUtils.instance.log(inserCode)
        method.insertBefore(inserCode)
    }

    private void appendTail(HitlerEntry entry,CtClass ctClass){
        LogUtils.instance.log("appendTail "+entry.targetClass)
        CtMethod method = ctClass.getDeclaredMethod(entry.targetMethod)
        method.insertAfter(entry.originClass+"."+entry.originMethod+"();")
    }

    private void addTryCatch(HitlerEntry entry,CtClass ctClass){
        LogUtils.instance.log("addTryCatch "+entry.targetClass)
        CtMethod method = ctClass.getDeclaredMethod(entry.targetMethod)
        String originMethodName = method.name
        LogUtils.instance.log("methodName:"+method.name)
        String newName = originMethodName + "Impl"
        method.setName(newName)
        CtMethod newMethod = CtNewMethod.copy(method,originMethodName,ctClass,null)
        StringBuilder sb = new StringBuilder()
        sb.append("try{").append(newName+"(\$\$);").append("}catch(Throwable throwable){"+entry.originClass+"."+entry.originMethod+"(throwable);}")
        newMethod.setBody(sb.toString())
        ctClass.addMethod(newMethod)
        //method.addCatch()
        //method.insertBefore("try{")
       // method.insertAfter("}catch(Throwable throwable){"+entry.originClass+"."+entry.originMethod+"();}")
    }
    private boolean needProcess(){
        return true
    }
}