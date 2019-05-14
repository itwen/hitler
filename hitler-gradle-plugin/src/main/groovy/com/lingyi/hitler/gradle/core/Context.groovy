package com.lingyi.hitler.gradle.core

import com.android.build.gradle.api.ApplicationVariant
import com.lingyi.hitler.gradle.model.HitlerEntrySet
import com.lingyi.hitler.gradle.model.HitlerExtension
import org.gradle.api.Project

/**
 * @author sunchuanwen
 * @time 2019/5/8.
 */
class Context{

    public HitlerEntrySet mEntrySet

    public HitlerExtension mExtension

    public Project mProject

    public ApplicationVariant variant

}