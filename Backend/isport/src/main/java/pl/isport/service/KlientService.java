package pl.isport.service;

import pl.isport.model.Klient;
import pl.isport.model.Uzytkownik;

import java.util.*;

// HS-12: rejestracja
// HS-13: logowanie
public class KlientService {

    private final List<Klient> klienci = new ArrayList<>();
    private int nextId = 1;

    public Klient zarejestruj(String imie, String nazwisko, String email, String haslo, String telefon) {
        Klient k = new Klient(nextId++, imie, nazwisko, email, Uzytkownik.zahaszuj(haslo), telefon);
        klienci.add(k);
        return k;
    }

    public Klient zaloguj(String email, String haslo) {
        return klienci.stream()
                .filter(k -> k.zaloguj(email, haslo))
                .findFirst()
                .orElse(null);
    }

    public Klient znajdz(int id) {
        return klienci.stream().filter(k -> k.getId() == id).findFirst().orElse(null);
    }
}
