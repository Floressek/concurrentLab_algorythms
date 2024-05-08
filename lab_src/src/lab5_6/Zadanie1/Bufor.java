package src.lab5_6.Zadanie1;

import java.util.concurrent.Semaphore;

public class Bufor {
    int rozmiar;
    int odczyt;

    String buff[] = new String[500];

    public Bufor() {
        rozmiar = 0;
        odczyt = -1;
    }

    public void dodaj(String dodana, double wartosc) {
        dodana = dodana + rozmiar + ", " + (int) wartosc + "]";
        buff[rozmiar] = dodana;
        rozmiar++;
    }

    public String odczyt() {
        odczyt++;
        return buff[odczyt];
    }

    public int pozycja() {
        return odczyt + 1;
    }
}
