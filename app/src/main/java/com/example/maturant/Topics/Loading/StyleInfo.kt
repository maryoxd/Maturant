package com.example.maturant.Topics.Loading

data class StyleInfo(
    override val about: String,
    override val features: String
) : Info

val styleDetails = mapOf<String, Info>(
    "ADMINISTRATÍVNY ŠTÝL" to StyleInfo(
        about = "Administratívny štýl sa vyznačuje formálnym jazykom, ktorý je jasne štrukturovaný a zodpovedá profesionálnemu prostrediu." +
                "Je určený pre písanie oficiálnych dokumentov, správ, zápisníc a podobne."
                + "Administratívny štýl je určený pre písanie oficiálnych dokumentov, správ, zápisníc a podobne.",
        features = "Vecnosť, stručnosť, Adresnosť, Prehľadnosť"
    ),
    "VEDECKÝ ŠTÝL" to StyleInfo(
        about = "Vedecký štýl je určený pre písanie vedeckých prác, odborných článkov a podobne.",
        features = "Objektívnosť, presnosť, logickosť, formálnosť"
    ),
    "PUBLICISTICKÝ ŠTÝL" to StyleInfo(
        about = "Publicistický štýl je určený pre písanie novinových článkov, blogov a podobne.",
        features = "Aktuálnosť, zrozumiteľnosť, zaujímavosť, emotívnosť"
    ),
    "HOVOROVÝ ŠTÝL" to StyleInfo(
        about = "Hovorový štýl je určený pre bežnú komunikáciu, napríklad v rozhovoroch, dialógoch a podobne.",
        features = "Neformálnosť, prirodzenosť, emotívnosť, spontánnosť"
    ),
    "NÁUČNÝ ŠTÝL" to StyleInfo(
        about = "Náučný štýl je určený pre učebnice, učebné texty a podobne.",
        features = "Jasnosť, zrozumiteľnosť, prehľadnosť, vecnosť"
    ),
    "REČNÍCKY ŠTÝL" to StyleInfo(
        about = "Rečnícky štýl je určený pre prednášky a verejné prejavy.",
        features = "Expresívnosť, emotívnosť, priama komunikácia"
    ),
    "UMELECKÝ ŠTÝL" to StyleInfo(
        about = "Umelecký štýl je určený pre literárne diela, básne, prózy a podobne.",
        features = "Kreativita, emotívnosť, obraznosť, originalita"
    ),
    "CITOSLOVCIA" to StyleInfo(
        about = "Citoslovcia sú slová, ktoré napodobňujú zvuky, ktoré vydávajú rôzne predmety, živočíchy a podobne.",
        features = "Zvuková napodobňovnosť"
    ),
)

