package com.lingyi.hitler.gradle.core.pipeline



/**
 * @author sunchuanwen
 * @time 2019/5/9.
 */
interface Valve{
    public static final def DOT_CLASS = '.class'
    public static final def DOT_JAR = '.jar'
    public static final def DOT_ZIP = '.zip'

    public Valve getNext()
    public void setNext(Valve valve)
    public void invoke(Handling handling)
    public String name()
}