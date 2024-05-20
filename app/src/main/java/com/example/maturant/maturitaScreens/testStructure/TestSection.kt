package com.example.maturant.maturitaScreens.testStructure

import com.example.maturant.maturitaScreens.testStructure.Question

data class TestSection(
    val sectionId: Int,
    val text: String,
    val imageUrl: String? = null,
    val questions: List<Question>
)