package com.example.maturant.Topics.Loading


data class AuthorInfo(
    override val about: String,
    override val features: String
) : Info

val authorDetails = mapOf<String, Info>(
    "ANDREJ SLÁDKOVIČ" to AuthorInfo(
        about = "Andrej Sládkovič bol slovenský básnik, ktorý sa narodil v roku 1820 a zomrel v roku 1872.",
        features = "Romantizmus, láska, príroda"
    ),
    "DOBROSLAV CHROBÁK" to AuthorInfo(
        about = "Dobroslav Chrobák bol slovenský spisovateľ, ktorý sa narodil v roku 1928 a zomrel v roku 2010.",
        features = "Príbehy, poviedky, rozprávky"
    ),
    "DUŠAN DUŠEK" to AuthorInfo(
        about = "Dušan Dušek bol slovenský spisovateľ, ktorý sa narodil v roku 1928 a zomrel v roku 2007."
        + "Bol známy svojimi príbehmi, poviedkami a rozprávkami."
        + "Narodil sa v roku 1928 a zomrel v roku 2007."
        +"Bol známy svojimi príbehmi, poviedkami a rozprávkami." +
        "Dušan Dušek bol slovenský spisovateľ, ktorý sa narodil v roku 1928 a zomrel v roku 2007."
                + "Bol známy svojimi príbehmi, poviedkami a rozprávkami."
                + "Narodil sa v roku 1928 a zomrel v roku 2007."
                +"Bol známy svojimi príbehmi, poviedkami a rozprávkami.",
        features = "Príbehy, poviedky, rozprávky"
    ),
    "EMIL BOLESLAV LUKÁČ" to AuthorInfo(
        about = "Emil Boleslav Lukáč bol slovenský spisovateľ, ktorý sa narodil v roku 1928 a zomrel v roku 2007.",
        features = "Príbehy, poviedky, rozprávky"
    ),
    "EŽO VLKOLÍNSKY" to AuthorInfo(
        about = "Ežo Vlkolínsky bol slovenský spisovateľ, ktorý sa narodil v roku 1928 a zomrel v roku 2007.",
        features = "Príbehy, poviedky, rozprávky"
    ),
    "IVAN KRASKO"  to AuthorInfo(
        about = "Ivan Krasko bol slovenský básnik, ktorý sa narodil v roku 1876 a zomrel v roku 1958.",
        features = "Romantizmus, láska, príroda"
    ),
    "JANKO KRÁĽ" to AuthorInfo(
        about = "Janko Kráľ bol slovenský básnik, ktorý sa narodil v roku 1822 a zomrel v roku 1876.",
        features = "Romantizmus, láska, príroda"
    ),
    "JOZEF CÍGER HRONSKÝ" to AuthorInfo(
        about = "Jozef Cíger Hronský bol slovenský spisovateľ, ktorý sa narodil v roku 1896 a zomrel v roku 1960.",
        features = "Príbehy, poviedky, rozprávky"
    ),

)


