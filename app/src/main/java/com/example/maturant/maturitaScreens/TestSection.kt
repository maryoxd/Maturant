package com.example.maturant.maturitaScreens

data class TestSection(
    val sectionId: Int,
    val text: String,
    val questions: List<Question>
)
