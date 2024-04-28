package com.example.maturant


data class AuthorInfo(
    override val about: String,
    override val features: String
) : Info

val authorDetails = mapOf<String, Info>(
    "Andrej Sládkovič" to AuthorInfo(
        about = "Andrej Sládkovič bol slovenský básnik, ktorý sa narodil v roku 1820 a zomrel v roku 1872.",
        features = "Romantizmus, láska, príroda"
    ),
    "Dobroslav Chrobák" to AuthorInfo(
        about = "Dobroslav Chrobák bol slovenský spisovateľ, ktorý sa narodil v roku 1928 a zomrel v roku 2010.",
        features = "Príbehy, poviedky, rozprávky"
    ),
    "Dušan Dušek" to AuthorInfo(
        about = "Dušan Dušek bol slovenský spisovateľ, ktorý sa narodil v roku 1928 a zomrel v roku 2007.",
        features = "Príbehy, poviedky, rozprávky"
    ),
    "Emil Boleslav Lukáč" to AuthorInfo(
        about = "Emil Boleslav Lukáč bol slovenský spisovateľ, ktorý sa narodil v roku 1928 a zomrel v roku 2007.",
        features = "Príbehy, poviedky, rozprávky"
    ),
    "Ežo Vlkolínsky" to AuthorInfo(
        about = "Ežo Vlkolínsky bol slovenský spisovateľ, ktorý sa narodil v roku 1928 a zomrel v roku 2007.",
        features = "Príbehy, poviedky, rozprávky"
    ),
    "Ivan Krasko"  to AuthorInfo(
        about = "Ivan Krasko bol slovenský básnik, ktorý sa narodil v roku 1876 a zomrel v roku 1958.",
        features = "Romantizmus, láska, príroda"
    ),
    "Janko Kráľ" to AuthorInfo(
        about = "Janko Kráľ bol slovenský básnik, ktorý sa narodil v roku 1822 a zomrel v roku 1876.",
        features = "Romantizmus, láska, príroda"
    ),
    "Jozef Cíger Hronský" to AuthorInfo(
        about = "Jozef Cíger Hronský bol slovenský spisovateľ, ktorý sa narodil v roku 1896 a zomrel v roku 1960.",
        features = "Príbehy, poviedky, rozprávky"
    ),

)


