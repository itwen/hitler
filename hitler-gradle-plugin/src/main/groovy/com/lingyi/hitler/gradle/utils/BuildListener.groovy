package com.lingyi.hitler.gradle.utils

import org.gradle.BuildResult
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionListener
import org.gradle.api.initialization.Settings
import org.gradle.api.invocation.Gradle
import org.gradle.api.tasks.TaskState

/**
 * @author sunchuanwen
 * @time 2019/5/8.
 */
class BuildListener implements org.gradle.BuildListener,TaskExecutionListener{

    Map<String,Long> times = new HashMap<>()

    @Override
    void buildStarted(Gradle gradle) {}

    @Override
    void settingsEvaluated(Settings settings) {}

    @Override
    void projectsLoaded(Gradle gradle) {}

    @Override
    void projectsEvaluated(Gradle gradle) {}

    @Override
    void buildFinished(BuildResult buildResult) {
        LogUtils.instance.log("build end ------")
        times.each {String key,Long value->
            LogUtils.instance.log(":"+value+"ms :"+key)
        }
        times.clear()
        LogUtils.instance.end()
    }

    @Override
    void beforeExecute(Task task) {
        long startTime = System.currentTimeMillis()
        times[task.getName()] = startTime
    }

    @Override
    void afterExecute(Task task, TaskState taskState) {
        long endTime = System.currentTimeMillis()
        long startTime = times[task.getName()]
        times[task.getName()] = endTime-startTime
    }
}