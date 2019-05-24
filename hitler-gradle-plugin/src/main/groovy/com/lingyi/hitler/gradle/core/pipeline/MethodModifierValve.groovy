package com.lingyi.hitler.gradle.core.pipeline

import com.lingyi.hitler.gradle.model.HitlerEntry
import com.lingyi.hitler.gradle.model.HitlerEntrySet
import com.lingyi.hitler.gradle.utils.LogUtils
import com.sun.org.apache.bcel.internal.classfile.InnerClasses
import javassist.CannotCompileException
import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import javassist.CtNewMethod
import javassist.expr.ExprEditor
import javassist.expr.MethodCall
import sun.rmi.runtime.Log


/**
 * @author sunchuanwen
 * @time 2019/5/9.
 */
class MethodModifierValve implements Valve{
    Valve next
    ClassPool classPool
    def debugSkip = false
    def releaseSkip = false
    def variantName = ""
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
                String className = handling.classPath
                debugSkip = handling.context.mExtension.debugSkip
                releaseSkip = handling.context.mExtension.releaseSkip
                variantName = handling.context.variant.name
                if (className.endsWith(DOT_CLASS)){
                    className = className - DOT_CLASS
                }
                try {
                    classPool.get("android.view.View")
                }catch (Exception e){
                    def sdkPath = "${handling.context.mProject.android.getSdkDirectory()}"+File.separator+"platforms"+File.separator+"${handling.context.mProject.android.compileSdkVersion}"+File.separator+"android.jar"
                    classPool.appendClassPath(sdkPath)
                }
                className = className.replace("/",".")
                className = getTargetClass(className,key)
                if (className != null && className.length() > 0){
                    CtClass ctClass =  null
                    try {
                        ctClass = classPool.get(className)
                    }catch (Exception e){
                        classPool.appendClassPath(handling.inputFile.absolutePath)
                        ctClass =  classPool.get(className)
                    }

                    if (ctClass == null){
                        throw new ClassNotFoundException("class:"+className+" not found")
                    }

                    values.each {HitlerEntry entry->
                        if (needProcess(entry)){
                            modifier(entry,ctClass)
                        }
                    }
                    handling.bytes = ctClass.toBytecode()
                    ctClass.detach()
                }
            }

        }
        getNext().invoke(handling)
    }

    private String getTargetClass(String className,String key){
        if (key.endsWith("*")){
            try {
                String prefix = key.substring(0,key.length()-1)
                if (className.contains(prefix)){
                    int startIndex = className.indexOf(prefix)
                    String targetClassName = className.substring(startIndex,className.length())
                    return targetClassName
                }else{
                    return null
                }
            }catch (Exception e){
                return null
            }
        }else if (className.endsWith(key)){
            return key
        }
        return null
    }

    private void modifier(HitlerEntry entry,CtClass ctClass){
        CtMethod method = getMethod(ctClass,entry)
        if (method == null){
            LogUtils.instance.log("method null at class:"+ctClass.name)
            return
        }
        switch (entry.mType){
            case HitlerEntrySet.INSERTOHEAD:
                insertToHead(entry,ctClass,method)
                break
            case HitlerEntrySet.APPENDTAIL:
                appendTail(entry,ctClass,method)
                break
            case HitlerEntrySet.ADDTRYCATCH:
                addTryCatch(entry,ctClass,method)
                break
            case HitlerEntrySet.METHODREPLACE:
                replaceMethod(entry,ctClass,method)
                break
        }
    }

    @Override
    String name() {
        return "MethodModifierValve"
    }

    private void replaceMethod(HitlerEntry entry, CtClass ctClass,CtMethod method){

        LogUtils.instance.log("replaceMethod target:"+entry.targetClass+"."+entry.methodSignature+ " origin:"+entry.originClass+"."+entry.originMethod + " paramsCount:"+ entry.paramsCount)

        method.signature
        String insertCode = insertCode(entry)
        if (!method.returnType.name.equals("void")){
            insertCode = "return " + insertCode
        }
        method.setBody(insertCode)
    }

    private CtMethod getMethod(CtClass ctClass,HitlerEntry entry){
        for (CtMethod method :ctClass.getDeclaredMethods()){
            if (method != null && method.signature.equals(entry.methodSignature) && (isEmpty(entry.targetMethodName) || entry.targetMethodName.equals(method.name))){
                return method
            }
        }
        return null
    }

    private boolean  isEmpty(String text){
        return  text == null || text.length() <= 0
    }

    private void insertToHead(HitlerEntry entry, CtClass ctClass,CtMethod method){
        LogUtils.instance.log("insertToHead target:"+entry.targetClass+"."+entry.methodSignature+ " origin:"+entry.originClass+"."+entry.originMethod + " paramsCount:"+ entry.paramsCount)
        String insertCode = insertCode(entry)
        LogUtils.instance.log("insertCode:"+insertCode)
        method.insertBefore(insertCode)
    }

    private String insertCode(HitlerEntry entry){
        String inserCode = entry.originClass+"."+entry.originMethod+"("
        if (entry.paramsCount > 0){
            inserCode +="\$\$"
        }
        inserCode+=");"

        return inserCode
    }

    private void appendTail(HitlerEntry entry,CtClass ctClass,CtMethod method){
        LogUtils.instance.log("appendTail target:"+entry.targetClass+"."+entry.methodSignature+ " origin:"+entry.originClass+"."+entry.originMethod + " paramsCount:"+ entry.paramsCount)
        String insertCode = insertCode(entry)
        LogUtils.instance.log("insertCode:"+insertCode)
        method.insertAfter(insertCode)
    }

    private void addTryCatch(HitlerEntry entry,CtClass ctClass,CtMethod method){
        boolean isVoid = method.returnType.name.equals("void")
        LogUtils.instance.log("addTryCatch: "+entry.targetClass+"."+method.name+" returnType:"+method.returnType.name)
        String originMethodName = method.name
        String newName = originMethodName + "Impl"
        method.setName(newName)
        CtMethod newMethod = CtNewMethod.copy(method,originMethodName,ctClass,null)
        StringBuilder sb = new StringBuilder()
        String firstReturnCode = isVoid?"":"return "

        sb.append("try{ "+firstReturnCode).append(newName+"(\$\$);").append("}catch(Throwable throwable){ "+firstReturnCode+entry.originClass+"."+entry.originMethod+"(throwable); }")
        String insertCode = sb.toString()
        LogUtils.instance.log("insertCode:"+insertCode)
        newMethod.setBody(insertCode)
        ctClass.addMethod(newMethod)
    }
    private boolean needProcess(HitlerEntry entry){
        if (debugSkip && variantName.contains("debug")) return false
        if (releaseSkip && variantName.contains("release")) return false
        if ("default".equals(entry.mBuildType) || variantName.contains(entry.mBuildType))
            return true
        return false
    }
}