package com.lingyi.annotation.protocol;

/**
 * @author sunchuanwen
 * @time 2019/5/7.
 */
public enum  VariantsType {

    TYPE_DEBUG("debug"),
    TYPE_RELEASE("release"),
    TYPE_DEFAULT("default"),;
    private String type;
     VariantsType(String type){
        this.type = type;
    }

    public static VariantsType typeOfValue(String type){
         for (VariantsType element: values()){
             if (element.getType() == type){
                 return element;
             }
         }
         return TYPE_DEFAULT;
    }

    public String getType(){
         return type;
    }
}
