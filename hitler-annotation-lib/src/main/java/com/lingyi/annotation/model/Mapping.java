package com.lingyi.annotation.model;

import com.lingyi.annotation.protocol.CmdType;
import com.lingyi.annotation.protocol.VariantsType;

import java.io.Serializable;

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

    public String targetMethod;

    public int mType;

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
            String originMethod, String targetMethod) {
        this.funType = funType;
        this.variantsType = variantsType;
        this.targetClass = targetClass;
        this.originClass = originClass;
        this.originMethod = originMethod;
        this.targetMethod = targetMethod;
        mType = funType.getValue();
        mBuildType = variantsType.getType();
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

    public String getTargetMethod() {
        return targetMethod;
    }

    public void setTargetMethod(String targetMethod) {
        this.targetMethod = targetMethod;
    }
}
