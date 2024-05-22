package com.example.maturant.maturitaScreens.testStructure


/**
 * TestContainer
 * TestContainer slúži na uchovávanie viacerých testov, ktoré sú uložené v jednom liste.
 * @property tests - Uchované testy v liste
 * @constructor - Vytvára testový kontajner
 */
data class TestContainer(
    val tests: List<Test>
)
