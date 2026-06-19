package pl.isport.service;

import pl.isport.model.*;
import pl.isport.model.enums.StatusSprzetu;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

// HS-06: dodawanie sprzętu
// HS-07: usuwanie sprzętu
// HS-08: edytowanie sprzętu
// HS-09: cennik
// HS-16: dostępny sprzęt dla klienta
// HS-17/18: zajęte terminy
public class SprzetService {

    private final List<Sprzet> sprzety = new ArrayList<>();
    private final List<Kategoria> kategorie = new ArrayList<>();
    private final List<Rezerwacja> rezerwacje;
    private int nextSprzetId = 1;
    private int nextKatId = 1;

    public SprzetService(List<Rezerwacja> rezerwacje) {
        this.rezerwacje = rezerwacje;
    }

    public Sprzet dodajSprzet(String nazwa, String opis, BigDecimal cenaZaDobe,
                               String stanTechniczny, int kategoriaId) {
        Kategoria kat = znajdzKategorie(kategoriaId);
        Sprzet s = new Sprzet(nextSprzetId++, nazwa, opis, cenaZaDobe, stanTechniczny, kat);
        sprzety.add(s);
        return s;
    }

    public void usunSprzet(int sprzetId) {
        znajdzSprzet(sprzetId).setUsuniety(true);
    }

    public void edytujSprzet(int sprzetId, String nazwa, String opis, BigDecimal cena, String stan) {
        znajdzSprzet(sprzetId).aktualizujDane(nazwa, opis, cena, stan);
    }

    public List<Sprzet> pobierzCennik() {
        return sprzety.stream()
                .filter(s -> !s.isUsuniety())
                .sorted(Comparator.comparing(Sprzet::getNazwa))
                .collect(Collectors.toList());
    }

    public List<Sprzet> pobierzDostepny(String nazwaKategorii) {
        return sprzety.stream()
                .filter(s -> !s.isUsuniety())
                .filter(s -> s.getStatus() == StatusSprzetu.DOSTEPNY)
                .filter(s -> nazwaKategorii == null || nazwaKategorii.isBlank()
                        || s.getKategoria().getNazwa().equalsIgnoreCase(nazwaKategorii))
                .collect(Collectors.toList());
    }

    public List<LocalDate[]> pobierzZajeteTerminy(int sprzetId) {
        Sprzet s = znajdzSprzet(sprzetId);
        return rezerwacje.stream()
                .filter(r -> r.getSprzet().equals(s))
                .filter(r -> r.getStatus() != pl.isport.model.enums.StatusRezerwacji.ANULOWANA)
                .map(r -> new LocalDate[]{r.getDataRozpoczecia(), r.getDataZakonczenia()})
                .collect(Collectors.toList());
    }

    public Sprzet znajdzSprzet(int id) {
        return sprzety.stream().filter(s -> s.getId() == id).findFirst().orElse(null);
    }

    public Kategoria dodajKategorie(String nazwa, String opis) {
        Kategoria k = new Kategoria(nextKatId++, nazwa, opis);
        kategorie.add(k);
        return k;
    }

    private Kategoria znajdzKategorie(int id) {
        return kategorie.stream().filter(k -> k.getId() == id).findFirst().orElse(null);
    }

    public List<Kategoria> pobierzKategorie() { return List.copyOf(kategorie); }
}
