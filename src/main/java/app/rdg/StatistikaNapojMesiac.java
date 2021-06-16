package app.rdg;

public class StatistikaNapojMesiac {
    public String getPodnik_napoj() {
        return podnik_napoj;
    }

    public String getDatum() {
        return datum;
    }

    public int getPocet() {
        return pocet;
    }

    public void setPodnik_napoj(String podnik_napoj) {
        this.podnik_napoj = podnik_napoj;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public void setPocet(int pocet) {
        this.pocet = pocet;
    }

    private String podnik_napoj;
    private String datum;
    private int pocet;

}
