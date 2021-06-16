package app.rdg;

/**
 *  @author Natalia Mackova
 */

public class MesTrzba {
    private Integer id_podnik;
    private String year_month;
    private double cena;
    private int pocet;


    public Integer getId_podnik() {
        return id_podnik;
    }

    public void setId_podnik(Integer id_podnik) {
        this.id_podnik = id_podnik;
    }

    public double getCena() {
        return cena;
    }

    public void setCena(double cena) {
        this.cena = cena;
    }

    public String getYear_month() {
        return year_month;
    }

    public void setYear_month(String year_month) {
        this.year_month = year_month;
    }

    public int getPocet() {
        return pocet;
    }

    public void setPocet(int pocet) {
        this.pocet = pocet;
    }
}
