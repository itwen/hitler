package com.lingyi.annotation.model;

import com.lingyi.annotation.protocol.CmdType;
import com.lingyi.annotation.protocol.VariantsType;

import java.io.Serializable;
import java.util.List;

/**
 * @author sunchuanwen
 * @time 2019/5/7.
 */
public class Mapping  implements Serializable{

    public transient CmdType funType;

    public transient VariantsType variantsType;

    public String targetClass;

    public String originClass;

    public String originMethod;

    public int paramsCount = 0;

    public int mType;

    public String methodSignature;

    public String targetMethodName;

    public String getTargetMethodName() {
        return targetMethodName;
    }

    public void setTargetMethodName(String targetMethodName) {
        this.targetMethodName = targetMethodName;
    }

    public String getMethodSignature() {
        return methodSignature;
    }

    public void setMethodSignature(String methodSignature) {
        this.methodSignature = methodSignature;
    }

    public int getType() {
        return funType.getValue();
    }

    public void setType(int type) {
        mType = type;
    }

    public String mBuildType;

    public String getBuildType() {
        return variantsType.getType();
    }

    public void setBuildType(String buildType) {
        mBuildType = buildType;
    }

    public Mapping(CmdType funType, VariantsType variantsType, String targetClass, String originClass,
            String originMethod, String methodSignature,String targetMethodName,int paramsCount) {
        this.funType = funType;
        this.variantsType = variantsType;
        this.targetClass = targetClass;
        this.originClass = originClass;
        this.originMethod = originMethod;
        this.paramsCount = paramsCount;
        this.methodSignature = methodSignature;
        this.targetMethodName = targetMethodName;
        mType = funType.getValue();
        mBuildType = variantsType.getType();
    }

    public int getParamsCount() {
        return paramsCount;
    }

    public void setParamsCount(int paramsCount) {
        this.paramsCount = paramsCount;
    }

    public CmdType getFunType() {
        return funType;
    }

    public void setFunType(CmdType funType) {
        this.funType = funType;
    }

    public VariantsType getVariantsType() {
        return variantsType;
    }

    public void setVariantsType(VariantsType variantsType) {
        this.variantsType = variantsType;
    }

    public String getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(String targetClass) {
        this.targetClass = targetClass;
    }

    public String getOriginClass() {
        return originClass;
    }

    public void setOriginClass(String originClass) {
        this.originClass = originClass;
    }

    public String getOriginMethod() {
        return originMethod;
    }

    public void setOriginMethod(String originMethod) {
        this.originMethod = originMethod;
    }

}
