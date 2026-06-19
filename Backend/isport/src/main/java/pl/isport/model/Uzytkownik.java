package pl.isport.model;

// Klasa abstrakcyjna – wspólne cechy klienta i pracownika (diagram klas)
public abstract class Uzytkownik {
    protected int id;
    protected String imie;
    protected String nazwisko;
    protected String email;
    protected String hasloHash;
    protected String telefon;

    public Uzytkownik(int id, String imie, String nazwisko, String email, String hasloHash, String telefon) {
        this.id = id;
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.email = email;
        this.hasloHash = hasloHash;
        this.telefon = telefon;
    }

    // HS-13
    public boolean zaloguj(String email, String haslo) {
        return this.email.equals(email) && this.hasloHash.equals(zahaszuj(haslo));
    }

    public void wyloguj() {}

    public static String zahaszuj(String haslo) {
        return Integer.toHexString(java.util.Objects.hashCode(haslo));
    }

    public int getId() { return id; }
    public String getImie() { return imie; }
    public String getNazwisko() { return nazwisko; }
    public String getEmail() { return email; }
    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }

    @Override
    public String toString() { return imie + " " + nazwisko + " (" + email + ")"; }
}
