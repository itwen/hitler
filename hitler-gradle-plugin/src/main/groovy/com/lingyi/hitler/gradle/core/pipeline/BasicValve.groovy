package com.lingyi.hitler.gradle.core.pipeline

import com.android.utils.FileUtils


/**
 * @author sunchuanwen
 * @time 2019/5/9.
 */
class BasicValve implements Valve{

    private Valve next
    @Override
    Valve getNext() {
        return next
    }

    @Override
    void setNext(Valve valve) {
        this.next = next
    }

    @Override
    void invoke(Handling handling) {
        handling.valveName = name()
 /*       if (handling != null){
            if (handling.inputFile != null){
                if (handling.inputFile.isFile()){
                    FileUtils.copyFile(handling.inputFile,handling.outFile)
                }else{
                    FileUtils.copyDirectory(handling.inputFile,handling.outFile)
                }
            }
        }*/
    }

    @Override
    String name() {
        return "BasicValve"
    }
}