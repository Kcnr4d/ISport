package pl.isport.model;

import java.util.ArrayList;
import java.util.List;

// HS-12: rejestracja klienta
// HS-13: logowanie
// HS-19: anulowanie rezerwacji
public class Klient extends Uzytkownik {

    private List<Rezerwacja> rezerwacje = new ArrayList<>();

    public Klient(int id, String imie, String nazwisko, String email, String hasloHash, String telefon) {
        super(id, imie, nazwisko, email, hasloHash, telefon);
    }

    public void dodajRezerwacje(Rezerwacja r) { rezerwacje.add(r); }
    public List<Rezerwacja> getRezerwacje() { return List.copyOf(rezerwacje); }
}
