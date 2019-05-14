package com.lingyi.hitler.gradle.transform

import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.lingyi.hitler.gradle.core.Context

/**
 * @author sunchuanwen
 * @time 2019/5/8.
 */
class TransformProxy extends Transform{


    public TransformProxy(Transform base,Context context){
        this.base = base
        this.context = context
    }
    final Transform base
    Context context

    @Override
    String getName() {
        return base.getName()
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return base.getInputTypes()
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return base.getScopes()
    }

    @Override
    boolean isIncremental() {
        return false
    }
}