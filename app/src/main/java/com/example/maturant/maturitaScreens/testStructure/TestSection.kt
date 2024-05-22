package com.example.maturant.maturitaScreens.testStructure


/**
 * TestSection
 * TestSection slúži na uchovávanie samotnej sekcie testu, ktorá obsahuje text sekcie, obrázok sekcie a otázky.
 * @property sectionId - Id sekcie
 * @property text - Text sekcie
 * @property imageUrl - Url obrázku sekcie (nemusí byť)
 * @property questions - Otázky sekcie
 * @constructor - Vytvára sekciu testu
 */
data class TestSection(
    val sectionId: Int,
    val text: String,
    val imageUrl: String? = null,
    val questions: List<Question>
)