package pl.isport.model;

public class Kategoria {
    private int id;
    private String nazwa;
    private String opis;

    public Kategoria(int id, String nazwa, String opis) {
        this.id = id;
        this.nazwa = nazwa;
        this.opis = opis;
    }

    public int getId() { return id; }
    public String getNazwa() { return nazwa; }
    public String getOpis() { return opis; }

    @Override
    public String toString() { return nazwa; }
}
