package com.lingyi.hitler.gradle

import com.android.build.api.transform.Transform
import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.internal.pipeline.TransformTask
import com.android.builder.model.Version
import com.lingyi.hitler.gradle.core.Context
import com.lingyi.hitler.gradle.model.HitlerEntrySet
import com.lingyi.hitler.gradle.model.HitlerExtension
import com.lingyi.hitler.gradle.transform.ProcessBaseTransform
import com.lingyi.hitler.gradle.transform.ProcessClassTransform
import com.lingyi.hitler.gradle.utils.BuildListener
import com.lingyi.hitler.gradle.utils.ConfigParser
import com.lingyi.hitler.gradle.utils.LogUtils
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionGraph
import org.gradle.api.execution.TaskExecutionGraphListener

import java.lang.reflect.Field

/**
 * @author sunchuanwen
 * @time 2019/5/8.
 */
public class HitlerPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        LogUtils.instance.setLogLevel(2)
        project.gradle.addListener(new BuildListener())
        project.extensions.create("hitler", HitlerExtension)
        project.android.registerTransform(new ProcessBaseTransform())

        if (Version.ANDROID_GRADLE_PLUGIN_VERSION.compareTo("3.0.0") >= 0) {
            //禁止掉aapt2
            reflectAapt2Flag()
            //禁止掉dex archive任务
            reflectDexArchive()

            reflectPredex(project)
        }

        project.afterEvaluate {
            HitlerExtension extension = project.extensions.getByName("hitler")
            HitlerEntrySet entrySet = ConfigParser.parser(extension.configFilePath,project)
            project.android.applicationVariants.all{ ApplicationVariant variant->
                LogUtils.instance.log("variant:"+variant.name)
                process(project, variant, entrySet,extension)
            }
        }

    }
    static Field getFieldByName(Class<?> aClass, String name) {
        Class<?> currentClass = aClass
        while (currentClass != null) {
            try {
                return currentClass.getDeclaredField(name)
            } catch (NoSuchFieldException e) {
                // ignored.
            }
            currentClass = currentClass.getSuperclass()
        }
        return null
    }

    def process(project,variant,entrySet,extension){

        Context context = new Context()
        context.mEntrySet = entrySet
        context.mExtension = extension
        context.mProject = project
        context.variant = variant

        project.gradle.getTaskGraph().addTaskExecutionGraphListener(new TaskExecutionGraphListener() {
            @Override
            void graphPopulated(TaskExecutionGraph taskExecutionGraph) {
                for (Task task : taskExecutionGraph.getAllTasks()) {
                    if (task.getProject().equals(project) && task instanceof TransformTask &&
                            task.name.startsWith("transform") &&
                            task.name.endsWith("For" + variant.name.capitalize())) {
                        Transform transform = ((TransformTask) task).getTransform()
                        LogUtils.instance.log("HitlerProcess-------------------------HitlerProcess:"+variant.name)
                        if (transform instanceof ProcessBaseTransform) {
                            LogUtils.instance.log("inject process class start")
                            ProcessClassTransform processClassTransform = new ProcessClassTransform(transform, context)
                            Field field = getFieldByName(task.getClass(), 'transform')
                            field.setAccessible(true)
                            field.set(task, processClassTransform)
                        }
                    }
                }
            }
        })
    }

    //禁掉aapt2
    def reflectAapt2Flag() {
        try {
            def booleanOptClazz = Class.forName('com.android.build.gradle.options.BooleanOption')
            def enableAAPT2Field = booleanOptClazz.getDeclaredField('ENABLE_AAPT2')
            enableAAPT2Field.setAccessible(true)
            def enableAAPT2EnumObj = enableAAPT2Field.get(null)
            def defValField = enableAAPT2EnumObj.getClass().getDeclaredField('defaultValue')
            defValField.setAccessible(true)
            defValField.set(enableAAPT2EnumObj, false)
        } catch (Throwable thr) {
            LogUtils.instance.log("relectAapt2Flag error: ${thr.getMessage()}.")
        }
    }

    //禁止掉dex archive任务
    def reflectDexArchive() {
        try {
            def booleanOptClazz = Class.forName('com.android.build.gradle.options.BooleanOption')

            def enableDexArchiveField = booleanOptClazz.getDeclaredField('ENABLE_DEX_ARCHIVE')
            enableDexArchiveField.setAccessible(true)
            def enableDexArchiveObj = enableDexArchiveField.get(null)
            def defValField = enableDexArchiveObj.getClass().getDeclaredField('defaultValue')
            defValField.setAccessible(true)
            defValField.set(enableDexArchiveObj, false)
        } catch (Throwable thr) {
            LogUtils.instance.log("reflectDexArchive error: ${thr.getMessage()}.")
        }
    }


    //禁掉 predex 任务
    def reflectPredex(Project project){
        try{
            project.extensions.android.dexOptions.preDexLibraries = false
            LogUtils.instance.log("reflectPredex preDexLibraries:"+ project.extensions.android.dexOptions.preDexLibraries )
        }catch (Throwable thr){
            LogUtils.instance.log(" reflect predex error")
        }
    }
}