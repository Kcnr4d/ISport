package pl.isport.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.isport.model.*;
import pl.isport.model.enums.StatusRezerwacji;
import pl.isport.model.enums.StatusSprzetu;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

// HS-17/18/19
class RezerwacjaServiceTest {

    private RezerwacjaService svc;
    private Klient klient;
    private Sprzet sprzet;
    private final LocalDate OD = LocalDate.now().plusDays(1);
    private final LocalDate DO = OD.plusDays(3);

    @BeforeEach
    void setUp() {
        svc = new RezerwacjaService();
        klient = new Klient(1, "Jan", "Nowak", "jan@example.com", "hash", "");
        sprzet = new Sprzet(1, "Rower", "", new BigDecimal("50.00"), "dobry",
                new Kategoria(1, "Rowery", ""));
    }

    @Test
    void utworz_statusPotwierdzona() {
        Rezerwacja r = svc.utworz(klient, sprzet, OD, DO);
        assertEquals(StatusRezerwacji.POTWIERDZONA, r.getStatus());
    }

    @Test
    void utworz_sprzetZarezerwowany() {
        svc.utworz(klient, sprzet, OD, DO);
        assertEquals(StatusSprzetu.ZAREZERWOWANY, sprzet.getStatus());
    }

    @Test
    void utworz_kosztCalkowity3Dni() {
        Rezerwacja r = svc.utworz(klient, sprzet, OD, DO);
        assertEquals(new BigDecimal("150.00"), r.getKosztCalkowity());
    }

    @Test
    void utworz_dodajeRezerwacjeDoKlienta() {
        svc.utworz(klient, sprzet, OD, DO);
        assertEquals(1, klient.getRezerwacje().size());
    }

    @Test
    void anuluj_statusAnulowana() {
        Rezerwacja r = svc.utworz(klient, sprzet, OD, DO);
        svc.anuluj(klient, r.getId());
        assertEquals(StatusRezerwacji.ANULOWANA, r.getStatus());
    }

    @Test
    void anuluj_sprzetDostepnyZPowrotem() {
        Rezerwacja r = svc.utworz(klient, sprzet, OD, DO);
        svc.anuluj(klient, r.getId());
        assertEquals(StatusSprzetu.DOSTEPNY, sprzet.getStatus());
    }

    @Test
    void anuluj_nieistniejacaRezerwacja_nicNieRobi() {
        svc.utworz(klient, sprzet, OD, DO);
        assertDoesNotThrow(() -> svc.anuluj(klient, 999));
    }

    @Test
    void rezerwacjeKlienta_zwracaRezerwacjeKlienta() {
        svc.utworz(klient, sprzet, OD, DO);
        assertEquals(1, svc.rezerwacjeKlienta(klient).size());
    }
}
