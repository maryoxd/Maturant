[SK]
1. Prehľad <br>
Aplikácia „MATURANT“ bola vyvinutá v rámci semestrálneho projektu na Žilinskej univerzite, konkrétne v rámci Fakulty riadenia a informatiky. Primárnym cieľom aplikácie je pomôcť študentom stredných škôl pripraviť sa na maturitnú skúšku zo slovenského jazyka. Aplikácia simuluje skutočné maturitné testy a ponúka interaktívne skúsenosti s testovaním a študijné zdroje.

2. Definícia problému <br>
Identifikovaným problémom je nedostatok interaktívnych vzdelávacích nástrojov pre študentov pripravujúcich sa na maturitu. Tradičné študijné materiály neposkytujú okamžitú spätnú väzbu, čo môže viesť k neefektívnym vzdelávacím procesom. Navyše príprava manuálnych testov (ako je tlač a ručná kontrola odpovedí) je časovo náročná a zastaraná. Aplikácia "MATURANT" sa snaží tieto problémy vyriešiť poskytovaním:
Interaktívne testy: Okamžitá spätná väzba a hodnotenie otázok s možnosťou výberu z viacerých odpovedí a otázok na doplnenie prázdnych miest.
Sledovanie pokroku: Schopnosť ukladať výsledky testov a sledovať priebeh v priebehu času.
Prostredie simulovanej skúšky: Aplikácia obsahuje časovač na odpočítavanie, ktorý simuluje 100-minútový limit, ktorému študenti čelia počas skutočného testu.

3. Funkcie aplikácie <br>
Aplikácia ponúka niekoľko kľúčových funkcií:
Študijné materiály: Obsahuje témy z gramatiky a literatúry, ktoré študentom pomôžu pripraviť sa na test.
Simulácia maturitného testu: Poskytuje súbor testov založených na skutočných maturitných skúškach zo slovenského jazyka, doplnený o časovač a automatické známkovanie.
Výsledky testov: Používatelia môžu ukladať a kontrolovať svoje výsledky testov, čo im umožňuje sledovať ich zlepšovanie v priebehu času.
Konfigurácia časovača: Predvolená dĺžka trvania testu je 100 minút, no používatelia môžu čas podľa potreby upraviť.

4. Porovnanie s podobnými aplikáciami <br>
Počas vývoja projektu bolo preverených niekoľko ďalších aplikácií, ako napr. Maturita vo vrecku, Maturita, Autoškola. Medzi hlavné rozdiely patria:
Maturita vo vrecku sa viac zameriava na prehľady gramatiky a literatúry bez poskytovania úplných maturitných testov. Obsahuje kvízový systém, ale chýba mu plná interaktivita a hĺbka aplikácie „MATURANT“.
Maturita poskytuje teoretický obsah naprieč širšou škálou predmetov, chýba však funkcia kvízu alebo testovania.
Autoškola, hoci je určená na prípravu na vodičské skúšky, inšpirovala niekoľko nápadov na interaktívne testovanie a sledovanie výsledkov v "MATURANT".

5. Technická implementácia <br>
Aplikácia je vytvorená pomocou vývojových nástrojov pre Android a sleduje rámec Jetpack Compose na vytváranie komponentov používateľského rozhrania. Implementácia je štruktúrovaná nasledovne:
MainActivity and Navigation: Využíva NavHost a NavController na správu navigácie na rôznych obrazovkách.
Komponenty obrazovky: Jednotlivé obrazovky ovládajú rôzne funkcie, ako napríklad hlavnú obrazovku pre navigáciu, GrammarTopicsScreen pre zoznam gramatických tém, LiteratureTopicsScreen pre témy z literatúry a TestScreen pre simuláciu testov.
Spracovanie údajov: Aplikácia používa súbory JSON na ukladanie a získavanie testovacích údajov pomocou GSON na analýzu JSON. TestLoader je pomocná trieda, ktorá riadi načítanie testovacích dát do aplikácie.
SharedViewModel: Ústredný prvok udržiavania stavu aplikácie, ktorý zabezpečuje konzistentnosť navigácie a načítania údajov na všetkých obrazovkách.
Časovač a vyhodnotenie testov: Integrované do MaturitaViewModel, spracováva logiku časovania a hodnotí test po dokončení.

6. JSON štruktúra a spracovanie súborov <br>
Aplikácia používa dobre štruktúrovaný formát JSON na načítanie testovacích otázok a ďalších relevantných údajov. To zaisťuje, že aktualizácie možno jednoducho vykonávať nahradením alebo úpravou súborov JSON podľa potreby. Flexibilita JSON umožňuje budúce rozšírenie, napríklad pridanie ďalších predmetov alebo rôznych typov testov.

7. Budúce úvahy <br>
Aktualizácie obsahu: Aplikácia by sa mala každoročne aktualizovať, aby odrážala nové maturitné testy.
Dostupnosť naprieč platformami: Rozšírenie dostupnosti aplikácie mimo Android by ju mohlo sprístupniť širšiemu okruhu študentov.
Vylepšenia UI/UX: Rozhranie by malo zostať jednoduché, čisté a ľahko ovládateľné pre študentov, čím sa zabezpečí efektívne študijné prostredie.

8. Záver <br>
Aplikácia „MATURANT“ poskytuje efektívne interaktívne riešenie pre študentov stredných škôl pripravujúcich sa na maturitu zo slovenského jazyka. Simuláciou testovacieho prostredia a poskytnutím okamžitej spätnej väzby zefektívňuje proces štúdia a pomáha študentom sledovať ich pokrok

[EN]
1. Overview <br>
The "MATURANT" application was developed as part of a semester project at the University of Žilina, specifically within the Faculty of Management and Informatics. The primary objective of the application is to aid high school students preparing for the Slovak language graduation exam (maturita). The application simulates real graduation tests, offering both interactive test-taking experiences and study resources.

2. Problem Definition <br>
The problem identified is the lack of interactive learning tools for students preparing for graduation exams. Traditional study materials do not provide instant feedback, which can lead to inefficient learning processes. Additionally, manual test preparations (such as printing and checking answers by hand) are time-consuming and outdated. The "MATURANT" application seeks to solve these problems by providing:
Interactive tests: Immediate feedback and grading of multiple-choice and fill-in-the-blank questions.
Progress tracking: The ability to save test results and monitor progress over time.
Simulated exam environment: The app includes a countdown timer, simulating the 100-minute limit students face during the real test.

3. Application Features <br>
The application offers several key functionalities:
Study Materials: Contains grammar and literature topics to help students prepare for the test.
Graduation Test Simulation: Provides a set of tests based on real Slovak language graduation exams, complete with a timer and automatic grading.
Test Results: Users can save and review their test results, allowing them to track their improvement over time.
Timer Configuration: The default test duration is 100 minutes, but users can adjust the time as needed.

4. Comparison with Similar Applications <br>
Several other applications, such as Maturita vo vrecku, Maturita, and Autoškola, were reviewed during the project development. Key differences include:
Maturita vo vrecku focuses more on grammar and literature overviews without providing full graduation tests. It includes a quiz system but lacks the full interactivity and depth of the "MATURANT" app.
Maturita provides theoretical content across a wider range of subjects but lacks any quiz or test functionality.
Autoškola, though designed for driving test preparation, inspired some ideas for interactive testing and result tracking in "MATURANT."

5. Technical Implementation <br>
The application is built using Android development tools and follows the Jetpack Compose framework for creating UI components. The implementation is structured as follows:
MainActivity and Navigation: Utilizes NavHost and NavController to manage navigation across different screens.
Screen Components: Individual screens handle various functionalities, such as the MainScreen for navigation, GrammarTopicsScreen for listing grammar topics, LiteratureTopicsScreen for literature topics, and TestScreen for simulating the tests.
Data Handling: The application uses JSON files to store and retrieve test data, using GSON for JSON parsing. TestLoader is a helper class that manages the loading of test data into the application.
SharedViewModel: Central to maintaining the state of the app, ensuring that navigation and data loading are consistent across screens.
Timer and Test Evaluation: Integrated into the MaturitaViewModel, handling timing logic and grading the test upon completion.

6. JSON Structure and File Handling <br>
The app uses a well-structured JSON format to load test questions and other relevant data. This ensures that updates can be easily made by replacing or modifying the JSON files as needed. The flexibility of JSON allows for future expansion, such as adding more subjects or different types of tests.

7. Future Considerations <br>
Content Updates: The application should be updated annually to reflect new graduation tests.
Cross-Platform Availability: Expanding the app's availability beyond Android could make it more accessible to a broader range of students.
UI/UX Enhancements: The interface should remain simple, clean, and easy to navigate for students, ensuring an efficient study environment.

8. Conclusion <br>
The "MATURANT" application provides an efficient, interactive solution for high school students preparing for their Slovak language graduation exam. By simulating the test environment and offering immediate feedback, it streamlines the study process and helps students track their progress
