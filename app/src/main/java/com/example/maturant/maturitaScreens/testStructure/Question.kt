package com.example.maturant.maturitaScreens.testStructure

/**
 * Question
 * Question slúži ako samotná otázka testu, ktorá obsahuje text otázky, možné odpovede, správnu odpoveď a typ otázky.
 * @property questionText - Text otázky
 * @property options - Možné odpovede otázky
 * @property correctAnswer - Správna odpoveď otázky
 * @property type - Typ otázky (FILL_IN, CHOICE)
 * @constructor - Vytvára otázku
 */
data class Question(
    val questionText: String,
    val options: List<String>? = null,
    val correctAnswer: String,
    val type: String
)
