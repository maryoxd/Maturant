[SK]
Hra s kockami
Tento projekt je jednoduchá hra v jazyku C++ zahŕňajúca hráčov a kocky, kde je každému hráčovi pridelená kocka a súťaží jej hodom. Hra simuluje 10 kôl a po každom kole vyhráva hráč s najvyšším hodom. Ak je nerozhodný výsledok, na určenie víťaza sa použije priorita kocky. Po dokončení všetkých kôl sa zobrazí celkový počet výhier každého hráča.

Štruktúra projektu
Hra pozostáva z troch hlavných tried:

Hra: Riadi priebeh hry vrátane hráčov, kociek a logiky porovnávania.
Hrac: Predstavuje hráča, ukladá jeho meno, pridelené kocky a počet výhier.
Kocka: Predstavuje kocku, ukladá jej farbu a prioritu.

Popisy tried

Hra
Metódy:
pridajHracovAkocky(): Pridá štyroch hráčov a pridelí im farebné kocky s rôznymi prioritami.
porovnajKocky(): Porovná hody dvoch hráčov a určí víťaza. Ak sú hody nerozhodné, priorita kocky sa použije ako nerozhodný výsledok.
hrajHru(): Prebehne hru na 10 kôl. Hráčom sú náhodne priradené kocky, hodí sa nimi a určí sa víťaz každého kola. Na konci je vytlačený celkový počet výhier každého hráča.

Hrac
Vlastnosti:
Meno: Uloží meno hráča.
kocka: Ukladá kocky priradené hráčovi.
pocetVyhier: Sleduje počet kôl, ktoré hráč vyhral.
Metódy:
getMeno(): Vráti meno hráča.
getPocetVyhier(): Vráti počet výhier.
pridajVyhru(): Zvyšuje počet výhier hráča.
priradKocku(Kocka): Pridelí hráčovi kocku.
dajKocku(): Vráti aktuálnu kocku hráča.
hodKockou(): Simuluje hod kockou (generuje náhodné číslo medzi 0 a 6).
getHodeneCislo(): Vráti výsledok posledného hodu hráča.

Kocka
Vlastnosti:
Farba: Farba kocky.
Priorita: Priorita kociek (používa sa na prerušenie remíz).
Metódy:
getFarba(): Vráti farbu kocky.
getPriorita(): Vráti prioritu kocky.
Ako hra funguje
Hra začína pridaním štyroch hráčov.
Každému hráčovi je pridelená náhodná kocka s farbami od bielej po zlatú a prioritami od 1 do 4.
V každom kole hráči hodia kockami a víťaz je určený buď hodom alebo prioritou kociek.
Po 10 kolách sa zobrazia celkové výhry každého hráča.

Budúce vylepšenia
Umožnite hráčom zadať svoje mená.
Pridajte ďalších hráčov a kocky s rôznymi vlastnosťami.
Implementujte používateľské rozhranie pre lepšiu vizualizáciu toku hry.

[EN]
Dice Game
This project is a simple C++ game involving players and dice, where each player is assigned a dice and competes by rolling it. The game simulates 10 rounds, and after each round, the player with the highest roll wins the round. If there's a tie, the dice's priority is used to determine the winner. After all rounds are completed, the total number of wins for each player is displayed.

Project Structure
The game consists of three main classes:

Hra: Manages the game flow, including players, dice, and the comparison logic.
Hrac: Represents a player, storing their name, assigned dice, and the number of wins.
Kocka: Represents a dice, storing its color and priority.

Class Descriptions

Hra
Methods:
pridajHracovAkocky(): Adds four players and assigns them colored dice with different priorities.
porovnajKocky(): Compares two players' dice rolls and determines the winner. If rolls are tied, the dice's priority is used as a tiebreaker.
hrajHru(): Runs the game over 10 rounds. Players are randomly assigned dice, roll them, and the winner of each round is determined. At the end, the total number of wins for each player is printed.

Hrac
Attributes:
Meno: Stores the player's name.
kocka: Stores the dice assigned to the player.
pocetVyhier: Tracks the number of rounds won by the player.
Methods:
getMeno(): Returns the player's name.
getPocetVyhier(): Returns the number of wins.
pridajVyhru(): Increments the player's win count.
priradKocku(Kocka): Assigns a dice to the player.
dajKocku(): Returns the player's current dice.
hodKockou(): Simulates rolling a dice (generating a random number between 0 and 6).
getHodeneCislo(): Returns the result of the player's last roll.

Kocka
Attributes:
Farba: The color of the dice.
Priorita: The priority of the dice (used to break ties).
Methods:
getFarba(): Returns the dice's color.
getPriorita(): Returns the dice's priority.
How the Game Works
The game starts by adding four players.
Each player is assigned a random dice, with colors ranging from white to gold, and priorities from 1 to 4.
In each round, players roll their dice, and the winner is determined either by the roll or by the priority of the dice.
After 10 rounds, the total wins for each player are displayed.

Future Improvements
Allow players to input their names.
Add more players and dice with varying attributes.
Implement a user interface for better visualization of the game flow.
