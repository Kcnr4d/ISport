package pl.isport.model;

import pl.isport.model.enums.StatusSprzetu;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

// HS-06/07/08/09/16/17/18
public class Sprzet {
    private int id;
    private String nazwa;
    private String opis;
    private BigDecimal cenaZaDobe;
    private String stanTechniczny;
    private StatusSprzetu status;
    private Kategoria kategoria;
    private boolean usuniety;

    public Sprzet(int id, String nazwa, String opis, BigDecimal cenaZaDobe, String stanTechniczny, Kategoria kategoria) {
        this.id = id;
        this.nazwa = nazwa;
        this.opis = opis;
        this.cenaZaDobe = cenaZaDobe;
        this.stanTechniczny = stanTechniczny;
        this.kategoria = kategoria;
        this.status = StatusSprzetu.DOSTEPNY;
    }

    public void zmienStatus(StatusSprzetu s) { this.status = s; }

    public boolean czyDostepny(LocalDate od, LocalDate do_, List<Rezerwacja> rezerwacje) {
        if (status == StatusSprzetu.NIEDOSTEPNY) return false;
        return rezerwacje.stream()
                .filter(r -> r.getSprzet().equals(this))
                .filter(r -> r.getStatus() != pl.isport.model.enums.StatusRezerwacji.ANULOWANA)
                .noneMatch(r -> !od.isAfter(r.getDataZakonczenia()) && !do_.isBefore(r.getDataRozpoczecia()));
    }

    public void aktualizujDane(String nazwa, String opis, BigDecimal cena, String stan) {
        if (nazwa != null) this.nazwa = nazwa;
        if (opis != null) this.opis = opis;
        if (cena != null) this.cenaZaDobe = cena;
        if (stan != null) {
            this.stanTechniczny = stan;
            if (stan.equalsIgnoreCase("w serwisie")) this.status = StatusSprzetu.NIEDOSTEPNY;
        }
    }

    public int getId() { return id; }
    public String getNazwa() { return nazwa; }
    public String getOpis() { return opis; }
    public BigDecimal getCenaZaDobe() { return cenaZaDobe; }
    public String getStanTechniczny() { return stanTechniczny; }
    public StatusSprzetu getStatus() { return status; }
    public Kategoria getKategoria() { return kategoria; }
    public boolean isUsuniety() { return usuniety; }
    public void setUsuniety(boolean usuniety) { this.usuniety = usuniety; }

    @Override
    public String toString() {
        return "[" + id + "] " + nazwa + " – " + cenaZaDobe + " zł/dobę (" + status + ")";
    }
}
