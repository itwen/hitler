package com.lingyi.hitler.gradle.utils


/**
 * @author sunchuanwen
 * @time 2019/5/9.
 */
class FilePathUtil {
    public static final String HITLER_BUILD_FOLDER = File.separator+"hitler-build"

    public static String getProjectPath(){
        String path  = System.getProperty("user.dir")
        if(path != null){
            if(path.endsWith(File.separator + "app")){
                path = path.substring(0,path.length() - 4)
            }
        }

        return path
    }


    public static String getHitlerBuildPath(){
        return getProjectPath()+File.separator+"app"+File.separator+"build"+HITLER_BUILD_FOLDER
    }
}