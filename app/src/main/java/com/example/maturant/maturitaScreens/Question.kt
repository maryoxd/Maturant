package com.example.maturant.maturitaScreens

data class Question(
    val questionText: String,
    val options: List<String>? = null,
    val correctAnswer: String,
    val type: String
)
