package com.example.maturant.maturitaScreens.testStructure

/**
 * Test
 * Test slúži na uchovávanie samotného testu, každý test má svoj vlastný rok a je rozdelený na viaceero sekcií (ako v normálnom maturitnom teste podľa ukážiek).
 * @property year - Rok testu
 * @property sections - Samotné sekcie testu
 * @constructor - Vytvára test
 */
data class Test(
    val year: String,
    val sections: List<TestSection>
)
