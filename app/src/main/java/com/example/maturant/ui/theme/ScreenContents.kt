package com.example.maturant.ui.theme

import android.content.Context

object ScreenContents {

    fun loadContent(context: Context, resourceId: Int): List<String> {
        val inputStream = context.resources.openRawResource(resourceId)
        return inputStream.bufferedReader().use { it.readLines()}
    }
}