package com.example.maturant

data class StyleInfo(
    override val about: String,
    override val features: String
) : Info

val styleDetails = mapOf<String, Info>(
    "Administratívny štýl" to StyleInfo(
        about = "Administratívny štýl sa vyznačuje formálnym jazykom, ktorý je jasne štrukturovaný a zodpovedá profesionálnemu prostrediu.",
        features = "Vecnosť, stručnosť, Adresnosť, Prehľadnosť"
    ),
    "Vedecký štýl" to StyleInfo(
        about = "Vedecký štýl je určený pre písanie vedeckých prác, odborných článkov a podobne.",
        features = "Objektívnosť, presnosť, logickosť, formálnosť"
    ),
    "Publicistický štýl" to StyleInfo(
        about = "Publicistický štýl je určený pre písanie novinových článkov, blogov a podobne.",
        features = "Aktuálnosť, zrozumiteľnosť, zaujímavosť, emotívnosť"
    ),
    "Hovorový štýl" to StyleInfo(
        about = "Hovorový štýl je určený pre bežnú komunikáciu, napríklad v rozhovoroch, dialógoch a podobne.",
        features = "Neformálnosť, prirodzenosť, emotívnosť, spontánnosť"
    ),
    "Náučný štýl" to StyleInfo(
        about = "Náučný štýl je určený pre učebnice, učebné texty a podobne.",
        features = "Jasnosť, zrozumiteľnosť, prehľadnosť, vecnosť"
    ),
    "Rečnícky štýl" to StyleInfo(
        about = "Rečnícky štýl je určený pre prednášky a verejné prejavy.",
        features = "Expresívnosť, emotívnosť, priama komunikácia"
    ),
    "Umelecký štýl" to StyleInfo(
        about = "Umelecký štýl je určený pre literárne diela, básne, prózy a podobne.",
        features = "Kreativita, emotívnosť, obraznosť, originalita"
    ),
    "Citoslovcia" to StyleInfo(
        about = "Citoslovcia sú slová, ktoré napodobňujú zvuky, ktoré vydávajú rôzne predmety, živočíchy a podobne.",
        features = "Zvuková napodobňovnosť"
    ),
)

