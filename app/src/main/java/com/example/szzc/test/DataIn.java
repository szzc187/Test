package com.example.szzc.test;

public interface DataIn {
    long whenStopLong = 1490983727574L;
    // kiedy ma przestać działać ( getTime ) 1490983727574L - 31Marca  //  7 marca 22.00 1488920400000L
    String number = "695414641"; // nr telefonu 71100 695414641 603684054
    String message = "TEST"; // Wiadomość

    long firstDelay = 60000; // za ile pierwsza wiadomość w milisekundach getTime + milis
    // przy losowaniu tylko firstDelay

    //losowanie  wylosowana razy firstDelay daje czas opużnienia smsa w milisekundach
    int low = 1; //przedział losowania niska
    int high = 5; // przedział losowania wysoka

    //+ 10 sekund dla pewności
    long tenSecond = 10000;

    long repeatDelay = 1200000; // co ile następna wiadomość w sekundach tylko dla interwału
    //nie istotne od kiedy jest service
}
