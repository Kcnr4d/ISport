package pl.isport.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.isport.model.Klient;

import static org.junit.jupiter.api.Assertions.*;

// HS-12: rejestracja
// HS-13: logowanie
class KlientServiceTest {

    private KlientService svc;

    @BeforeEach
    void setUp() {
        svc = new KlientService();
    }

    @Test
    void rejestracja_zwracaKlientaZPoprawnymEmailem() {
        Klient k = svc.zarejestruj("Jan", "Nowak", "jan@example.com", "haslo123", "500000001");
        assertEquals("jan@example.com", k.getEmail());
    }

    @Test
    void rejestracja_dwaKlienci_majaRozneId() {
        Klient k1 = svc.zarejestruj("Jan", "Nowak", "jan@example.com", "haslo123", "");
        Klient k2 = svc.zarejestruj("Anna", "Kowalska", "anna@example.com", "haslo456", "");
        assertNotEquals(k1.getId(), k2.getId());
    }

    @Test
    void logowanie_poprawneHaslo_zwracaKlienta() {
        svc.zarejestruj("Jan", "Nowak", "jan@example.com", "haslo123", "");
        Klient k = svc.zaloguj("jan@example.com", "haslo123");
        assertNotNull(k);
        assertEquals("jan@example.com", k.getEmail());
    }

    @Test
    void logowanie_zleHaslo_zwracaNull() {
        svc.zarejestruj("Jan", "Nowak", "jan@example.com", "haslo123", "");
        assertNull(svc.zaloguj("jan@example.com", "zlehaslo"));
    }

    @Test
    void logowanie_nieistniejacyEmail_zwracaNull() {
        assertNull(svc.zaloguj("brak@example.com", "haslo123"));
    }

    @Test
    void znajdz_istniejacyId_zwracaKlienta() {
        Klient k = svc.zarejestruj("Jan", "Nowak", "jan@example.com", "haslo123", "");
        assertEquals(k, svc.znajdz(k.getId()));
    }

    @Test
    void znajdz_nieistniejacyId_zwracaNull() {
        assertNull(svc.znajdz(999));
    }
}
