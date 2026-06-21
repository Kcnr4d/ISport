package pl.isport.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.isport.model.Sprzet;
import pl.isport.model.enums.StatusSprzetu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// HS-06/07/08/09/16
class SprzetServiceTest {

    private SprzetService svc;
    private int katId;

    @BeforeEach
    void setUp() {
        svc = new SprzetService(new ArrayList<>());
        katId = svc.dodajKategorie("Rowery", "").getId();
    }

    @Test
    void dodajSprzet_statusDomyslnieDostepny() {
        Sprzet s = svc.dodajSprzet("Rower", "", new BigDecimal("50.00"), "dobry", katId);
        assertEquals(StatusSprzetu.DOSTEPNY, s.getStatus());
    }

    @Test
    void dodajSprzet_widocznyWCenniku() {
        Sprzet s = svc.dodajSprzet("Rower", "", new BigDecimal("50.00"), "dobry", katId);
        assertTrue(svc.pobierzCennik().contains(s));
    }

    @Test
    void usunSprzet_nieWidocznyWCenniku() {
        Sprzet s = svc.dodajSprzet("Rower", "", new BigDecimal("50.00"), "dobry", katId);
        svc.usunSprzet(s.getId());
        assertFalse(svc.pobierzCennik().contains(s));
    }

    @Test
    void edytujSprzet_aktualizujeCene() {
        Sprzet s = svc.dodajSprzet("Rower", "", new BigDecimal("50.00"), "dobry", katId);
        svc.edytujSprzet(s.getId(), null, null, new BigDecimal("40.00"), null);
        assertEquals(new BigDecimal("40.00"), s.getCenaZaDobe());
    }

    @Test
    void edytujSprzet_wSerwisie_statusNiedostepny() {
        Sprzet s = svc.dodajSprzet("Rower", "", new BigDecimal("50.00"), "dobry", katId);
        svc.edytujSprzet(s.getId(), null, null, null, "w serwisie");
        assertEquals(StatusSprzetu.NIEDOSTEPNY, s.getStatus());
    }

    @Test
    void pobierzCennik_posortowanyAlfabetycznie() {
        svc.dodajSprzet("Rower", "", new BigDecimal("50.00"), "dobry", katId);
        svc.dodajSprzet("Kask", "", new BigDecimal("10.00"), "dobry", katId);
        List<Sprzet> cennik = svc.pobierzCennik();
        assertEquals("Kask", cennik.get(0).getNazwa());
        assertEquals("Rower", cennik.get(1).getNazwa());
    }

    @Test
    void pobierzDostepny_nieZawieraZarezerwowanych() {
        Sprzet s1 = svc.dodajSprzet("Rower", "", new BigDecimal("50.00"), "dobry", katId);
        Sprzet s2 = svc.dodajSprzet("Kask", "", new BigDecimal("10.00"), "dobry", katId);
        s2.zmienStatus(StatusSprzetu.ZAREZERWOWANY);
        List<Sprzet> dostepne = svc.pobierzDostepny(null);
        assertTrue(dostepne.contains(s1));
        assertFalse(dostepne.contains(s2));
    }

    @Test
    void pobierzDostepny_filtrujePoKategorii() {
        int katId2 = svc.dodajKategorie("Narty", "").getId();
        svc.dodajSprzet("Rower", "", new BigDecimal("50.00"), "dobry", katId);
        svc.dodajSprzet("Narty", "", new BigDecimal("35.00"), "dobry", katId2);
        List<Sprzet> wynik = svc.pobierzDostepny("Narty");
        assertEquals(1, wynik.size());
        assertEquals("Narty", wynik.get(0).getNazwa());
    }
}
