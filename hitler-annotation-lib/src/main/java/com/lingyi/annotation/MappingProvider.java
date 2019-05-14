package com.lingyi.annotation;

import com.google.gson.Gson;
import com.lingyi.annotation.model.Mapping;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author sunchuanwen
 * @time 2019/5/7.
 */
public class MappingProvider {

    public static List<Mapping> mMappings = new CopyOnWriteArrayList<>();

    public static void addMapping(Mapping mapping){
         mMappings.add(mapping);
    }

    public static void createMappingJson(String filePath){
        System.out.println("filePath:"+filePath);
        if (filePath == null || filePath.length() <= 0)return;

        File file = new File(filePath);
        if (!file.exists()){
            try {
                file.getParentFile().createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            Gson gson = new Gson();
            Map<String,Object> map = new HashMap<>();
            map.put("entrySet",mMappings);
            String jsonStr = gson.toJson(map);
            fos.write(jsonStr.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (fos != null)
                fos.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
