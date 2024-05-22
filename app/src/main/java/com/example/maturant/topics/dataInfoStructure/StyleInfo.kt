package com.example.maturant.topics.dataInfoStructure

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

/**
 * StyleInfo
 * StyleInfo slúži ako dátová class pre ukladanie informácií o štýloch.
 * @property name - Názov štýlu
 * @property about - Informácie o štýle
 * @property features - Znaky štýlu
 * @constructor - Vytvára informácie o štýle
 */
data class StyleInfo(
    val name: String,
    override val about: String,
    override val features: String
) : Info

/**
 * LoadStylesFromJson
 * LoadStylesFromJson slúži na načítanie informácií o štýloch z JSON súboru.
 * @param context - Parameter context slúži na získanie prístupu k súboru so štýlmi.
 * @return - Vracia list štýlov
 */
fun loadStylesFromJson(context: Context): List<StyleInfo> {
    val inputStream = context.assets.open("styles.json")
    val reader = InputStreamReader(inputStream)
    val type = object : TypeToken<List<StyleInfo>>() {}.type
    return Gson().fromJson(reader, type)
}