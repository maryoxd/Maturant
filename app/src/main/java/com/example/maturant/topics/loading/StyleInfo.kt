package com.example.maturant.topics.loading

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

data class StyleInfo(
    val name: String,
    override val about: String,
    override val features: String
) : Info

fun loadStylesFromJson(context: Context): List<StyleInfo> {
    val inputStream = context.assets.open("styles.json")
    val reader = InputStreamReader(inputStream)
    val type = object : TypeToken<List<StyleInfo>>() {}.type
    return Gson().fromJson(reader, type)
}