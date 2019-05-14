package com.lingyi.hitler.gradle.core.pipeline


/**
 * @author sunchuanwen
 * @time 2019/5/9.
 */
interface Pipeline{
    public Valve getFirst()
    public Valve getBasic()
    public void setBasic(Valve valve)
    public void addValve(Valve valve)
}