package com.example.maturant.maturitaScreens

data class TestSection(
    val sectionId: Int,
    val text: String,
    val imageUrl: String? = null, // Pridaný voliteľný atribút imageUrl
    val questions: List<Question>
)