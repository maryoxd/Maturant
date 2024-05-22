package com.example.maturant.topics.dataInfoStructure

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader


/**
 * AuthorInfo
 * AuthorInfo slúži ako dátová class pre ukladanie informácií o autoroch.
 * @property name - Meno autora
 * @property about - Informácie o autorovi
 * @property features - Znaky autora
 * @constructor - Vytvára informácie o autorovi
 */
data class AuthorInfo(
    val name: String,
    override val about: String,
    override val features: String
) : Info

/**
 * LoadAuthorsFromJson
 * LoadAuthorsFromJson slúži na načítanie informácií o autoroch z JSON súboru.
 * @param context - Parameter context slúži na získanie prístupu k súboru s autormi.
 * @return - Vracia list autorov
 */
fun loadAuthorsFromJson(context: Context): List<AuthorInfo> {
    val inputStream = context.assets.open("authors.json")
    val reader = InputStreamReader(inputStream)
    val type = object : TypeToken<List<AuthorInfo>>() {}.type
    return Gson().fromJson(reader, type)
}