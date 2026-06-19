package pl.isport.model;

import pl.isport.model.enums.StatusRezerwacji;
import pl.isport.model.enums.StatusSprzetu;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

// HS-17/18/19
public class Rezerwacja {
    private int id;
    private LocalDateTime dataUtworzenia;
    private LocalDate dataRozpoczecia;
    private LocalDate dataZakonczenia;
    private StatusRezerwacji status;
    private BigDecimal kosztCalkowity;
    private Klient klient;
    private Sprzet sprzet;

    public Rezerwacja(int id, Klient klient, Sprzet sprzet, LocalDate od, LocalDate do_) {
        this.id = id;
        this.klient = klient;
        this.sprzet = sprzet;
        this.dataUtworzenia = LocalDateTime.now();
        this.dataRozpoczecia = od;
        this.dataZakonczenia = do_;
        this.status = StatusRezerwacji.OCZEKUJACA;
        long dni = ChronoUnit.DAYS.between(od, do_);
        this.kosztCalkowity = sprzet.getCenaZaDobe().multiply(BigDecimal.valueOf(dni));
    }

    public void zatwierdz() {
        this.status = StatusRezerwacji.POTWIERDZONA;
        sprzet.zmienStatus(StatusSprzetu.ZAREZERWOWANY);
    }

    public void anuluj() {
        this.status = StatusRezerwacji.ANULOWANA;
        sprzet.zmienStatus(StatusSprzetu.DOSTEPNY);
    }

    public int getId() { return id; }
    public LocalDate getDataRozpoczecia() { return dataRozpoczecia; }
    public LocalDate getDataZakonczenia() { return dataZakonczenia; }
    public StatusRezerwacji getStatus() { return status; }
    public BigDecimal getKosztCalkowity() { return kosztCalkowity; }
    public Klient getKlient() { return klient; }
    public Sprzet getSprzet() { return sprzet; }

    @Override
    public String toString() {
        return "Rezerwacja#" + id + " [" + klient.getNazwisko() + " | " + sprzet.getNazwa()
                + " | " + dataRozpoczecia + "–" + dataZakonczenia
                + " | " + kosztCalkowity + " zł | " + status + "]";
    }
}
