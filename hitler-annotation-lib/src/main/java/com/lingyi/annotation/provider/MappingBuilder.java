package com.lingyi.annotation.provider;

import com.lingyi.annotation.model.Mapping;
import com.lingyi.annotation.protocol.CmdType;
import com.lingyi.annotation.protocol.VariantsType;

/**
 * @author sunchuanwen
 * @time 2019/5/7.
 */
public class MappingBuilder {

    public static Mapping build(CmdType funType,VariantsType buildType,String originClass,String targetClass,String originMethod,String targetMethod){
        return new Mapping(funType,buildType,targetClass,originClass,originMethod,targetMethod);
    }
}
