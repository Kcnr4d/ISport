package pl.isport.service;

import pl.isport.model.*;

import java.time.LocalDate;
import java.util.*;

// HS-17/18: tworzenie rezerwacji
// HS-19: anulowanie
public class RezerwacjaService {

    private final List<Rezerwacja> rezerwacje = new ArrayList<>();
    private int nextId = 1;

    public Rezerwacja utworz(Klient klient, Sprzet sprzet, LocalDate od, LocalDate do_) {
        Rezerwacja r = new Rezerwacja(nextId++, klient, sprzet, od, do_);
        r.zatwierdz();
        klient.dodajRezerwacje(r);
        rezerwacje.add(r);
        return r;
    }

    public void anuluj(Klient klient, int rezerwacjaId) {
        klient.getRezerwacje().stream()
                .filter(r -> r.getId() == rezerwacjaId)
                .findFirst()
                .ifPresent(Rezerwacja::anuluj);
    }

    public List<Rezerwacja> rezerwacjeKlienta(Klient klient) {
        return klient.getRezerwacje();
    }

    public List<Rezerwacja> wszystkie() { return List.copyOf(rezerwacje); }
}
