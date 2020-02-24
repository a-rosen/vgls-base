package com.vgleadsheets.resources

import android.content.res.Resources

internal class RealResourceProvider(private val resources: Resources) : ResourceProvider {
    override fun getString(id: Int, vararg formatArgs: Any?) = resources.getString(id, *formatArgs)
}
