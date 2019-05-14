package com.lingyi.hitler.gradle.transform

import com.android.build.api.transform.*
import com.android.utils.FileUtils
import com.lingyi.hitler.gradle.core.Context
import com.lingyi.hitler.gradle.core.pipeline.BasicValve
import com.lingyi.hitler.gradle.core.pipeline.ClassProcessPipeline
import com.lingyi.hitler.gradle.core.pipeline.ClassReplaceValve
import com.lingyi.hitler.gradle.core.pipeline.FirstValve
import com.lingyi.hitler.gradle.core.pipeline.Handling
import com.lingyi.hitler.gradle.core.pipeline.MethodModifierValve
import com.lingyi.hitler.gradle.utils.LogUtils


/**
 * @author sunchuanwen
 * @time 2019/5/8.
 */
class ProcessClassTransform extends TransformProxy{

    private static final def DOT_CLASS = '.class'
    private static final def DOT_JAR = '.jar'

    private ClassProcessPipeline pipeline

    ProcessClassTransform(Transform base,
            Context context) {
        super(base, context)
        pipeline = new ClassProcessPipeline()
        BasicValve basicValve = new BasicValve()
        pipeline.setBasic(basicValve)

        FirstValve firstValve = new FirstValve()
        ClassReplaceValve classReplaceValve = new ClassReplaceValve()
        MethodModifierValve methodModifierValve = new MethodModifierValve()
        pipeline.addValve(firstValve)
        pipeline.addValve(classReplaceValve)
        pipeline.addValve(methodModifierValve)


    }

    @Override
    void transform(TransformInvocation transformInvocation)
            throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)
        def jarIndex = 0
        ArrayList<Handling> list = new ArrayList<>()
        transformInvocation.inputs.each {TransformInput transformInput->
            transformInput.jarInputs.each {JarInput jarInput->
                LogUtils.instance.log("jar name："+jarInput.file.getAbsoluteFile().name)
                def jarName = jarInput.name
                if (jarName.endsWith(DOT_JAR)){
                    jarName = jarName.substring(0,jarName - DOT_JAR.length())
                }
              def dest = transformInvocation.outputProvider.getContentLocation(jarName+jarIndex++,jarInput.contentTypes,jarInput.scopes,Format.JAR)
                Handling entry = new Handling()
                entry.inputFile = jarInput.file
                entry.outFile = dest
                entry.context = context
                list.add(entry)
            }


            transformInput.directoryInputs.each {DirectoryInput directoryInput->
                def dest = transformInvocation.outputProvider.getContentLocation(directoryInput.name,directoryInput.contentTypes,directoryInput.scopes,Format.DIRECTORY)
                Handling entry = new Handling()
                entry.inputFile = directoryInput.file
                entry.outFile = dest
                entry.context = context
                list.add(entry)
            }
        }

        for (Handling handling:list){
           pipeline.getFirst().invoke(handling)
            if (handling.inputFile.isFile()){
                FileUtils.copyFile(handling.inputFile,handling.outFile)
            }else{
                FileUtils.copyDirectory(handling.inputFile,handling.outFile)
            }
        }
    }
}