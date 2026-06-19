package pl.isport;

import pl.isport.model.*;
import pl.isport.model.enums.StatusRezerwacji;
import pl.isport.service.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
        RezerwacjaService rezSvc = new RezerwacjaService();
        SprzetService sprzetSvc = new SprzetService(rezSvc.wszystkie());
        KlientService klientSvc = new KlientService();

        // --- dane testowe ---
        Kategoria kat = sprzetSvc.dodajKategorie("Rowery", "Rowery górskie i miejskie");

        // HS-06: dodawanie sprzętu
        sep("HS-06: Dodawanie sprzętu");
        Sprzet rower = sprzetSvc.dodajSprzet("Rower MTB Trek", "Rower górski 29\"",
                new BigDecimal("50.00"), "dobry", kat.getId());
        Sprzet narty = sprzetSvc.dodajSprzet("Narty Rossignol", "Narty zjazdowe 170cm",
                new BigDecimal("35.00"), "dobry", kat.getId());
        System.out.println("Dodano: " + rower);
        System.out.println("Dodano: " + narty);

        // HS-09: cennik
        sep("HS-09: Cennik");
        sprzetSvc.pobierzCennik().forEach(s -> System.out.println("  " + s));

        // HS-08: edycja sprzętu
        sep("HS-08: Edycja ceny roweru");
        sprzetSvc.edytujSprzet(rower.getId(), null, null, new BigDecimal("45.00"), null);
        System.out.println("Nowa cena: " + rower.getCenaZaDobe() + " zł");

        // HS-08: walidacja błędnej ceny
        try {
            sprzetSvc.edytujSprzet(rower.getId(), null, null, new BigDecimal("-5"), null);
        } catch (IllegalArgumentException e) {
            System.out.println("Błąd walidacji: " + e.getMessage());
        }

        // HS-07: usuwanie sprzętu
        sep("HS-07: Usuwanie sprzętu");
        Sprzet doUsuniecia = sprzetSvc.dodajSprzet("Stary kask", "", new BigDecimal("5.00"), "zły", kat.getId());
        sprzetSvc.usunSprzet(doUsuniecia.getId());
        System.out.println("Cennik po usunięciu: " + sprzetSvc.pobierzCennik().stream().map(Sprzet::getNazwa).toList());

        // HS-12: rejestracja klienta
        sep("HS-12: Rejestracja");
        Klient k1 = klientSvc.zarejestruj("Jan", "Nowak", "jan@example.com", "bezpieczne1", "500000001");
        System.out.println("Zarejestrowano: " + k1);

        // HS-12: walidacje
        try { klientSvc.zarejestruj("X", "Y", "jan@example.com", "haslo123", ""); }
        catch (Exception e) { System.out.println("Email zajęty: " + e.getMessage()); }
        try { klientSvc.zarejestruj("X", "Y", "x@x.pl", "123", ""); }
        catch (Exception e) { System.out.println("Słabe hasło: " + e.getMessage()); }

        // HS-13: logowanie
        sep("HS-13: Logowanie");
        Klient zalogowany = klientSvc.zaloguj("jan@example.com", "bezpieczne1");
        System.out.println("Zalogowano: " + zalogowany);

        // HS-13: blokada po 3 próbach
        for (int i = 0; i < 3; i++) {
            try { klientSvc.zaloguj("jan@example.com", "zlehaslo"); }
            catch (Exception e) { System.out.println("Próba " + (i+1) + ": " + e.getMessage()); }
        }

        // HS-16: przeglądanie dostępnego sprzętu
        sep("HS-16: Dostępny sprzęt");
        sprzetSvc.pobierzDostepny(null).forEach(s -> System.out.println("  " + s));

        // HS-17/18: rezerwacja
        sep("HS-17/18: Tworzenie rezerwacji");
        LocalDate od = LocalDate.now().plusDays(1);
        LocalDate do_ = od.plusDays(3);
        Rezerwacja rez = rezSvc.utworz(k1, rower, od, do_);
        System.out.println("Utworzono: " + rez);

        // HS-18: zajęte terminy
        sep("HS-18: Zajęte terminy dla roweru");
        sprzetSvc.pobierzZajeteTerminy(rower.getId())
                .forEach(t -> System.out.println("  " + t[0] + " – " + t[1]));

        // HS-18: próba podwójnej rezerwacji
        try { rezSvc.utworz(k1, rower, od.plusDays(1), do_.plusDays(1)); }
        catch (Exception e) { System.out.println("Kolizja: " + e.getMessage()); }

        // HS-16: rower zarezerwowany – nie widoczny jako dostępny
        System.out.println("Dostępny sprzęt po rezerwacji: "
                + sprzetSvc.pobierzDostepny(null).stream().map(Sprzet::getNazwa).toList());

        // HS-19: anulowanie
        sep("HS-19: Anulowanie rezerwacji");
        System.out.println("Przed: " + rez.getStatus());
        rezSvc.anuluj(k1, rez.getId());
        System.out.println("Po: " + rez.getStatus());
        System.out.println("Sprzęt dostępny z powrotem: " + rower.getStatus());

        System.out.println("\n=== Demo zakończone ===");
    }

    private static void sep(String tytul) {
        System.out.println("\n─── " + tytul + " ───");
    }
}
