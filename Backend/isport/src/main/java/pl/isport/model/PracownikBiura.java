package pl.isport.model;

import java.util.List;

// HS-06, HS-07, HS-08, HS-09: zarządzanie sprzętem
public class PracownikBiura extends Uzytkownik {

    private String rola;

    public PracownikBiura(int id, String imie, String nazwisko, String email, String hasloHash, String telefon, String rola) {
        super(id, imie, nazwisko, email, hasloHash, telefon);
        this.rola = rola;
    }

    public String getRola() { return rola; }
}
