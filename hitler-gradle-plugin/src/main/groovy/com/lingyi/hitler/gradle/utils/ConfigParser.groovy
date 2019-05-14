package com.lingyi.hitler.gradle.utils

import org.gradle.api.Project
import com.google.gson.Gson
import com.lingyi.hitler.gradle.model.HitlerEntrySet

/**
 * @author sunchuanwen
 * @time 2019/5/8.
 */
class ConfigParser{
    public static final def sDefualtFile = "hitler_mapping.json"

    public static HitlerEntrySet parser(Project project) {
        try {
            def configPath = project.rootDir.absolutePath + File.separator + sDefualtFile
            File file = new File(configPath)
            String json = openFileToString(file)
            Gson gson = new Gson()
            HitlerEntrySet config = gson.fromJson(json, HitlerEntrySet.class)
            config.setEntrySet(config.entrySet)
            LogUtils.instance.log("lingyilog:"+config.mEntryHashMap.size())
            return config
        } catch (Exception e) {
          //  throw new GradleException("config file can not found or  parse error !!!!")
            LogUtils.instance.log("config file can not found or parse error !!!!")
        }
    }

    public static String openFileToString(File file) {
        byte[] bytes = new byte[1024]
        String str = ""
        file.withInputStream { input ->
            int readCount = -1
            while ((readCount = input.read(bytes)) != -1) {
                str += new String(bytes, 0, readCount)
            }
        }
        return str
    }
}