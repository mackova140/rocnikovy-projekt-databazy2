package app.rdg;

import app.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Sklad {
    private Integer id;
    private String nazov;
    private double nakupna_cena;
    private double objem;
    private String merna_jednotka;

    public double getNakupna_cena() { return nakupna_cena; }

    public String getMerna_jednotka() { return merna_jednotka; }

    public Integer getId() {
        return id;
    }

    public String getNazov() {
        return nazov;
    }

    public double getPredajna_cena() {
        return nakupna_cena;
    }

    public double getObjem() {
        return objem;
    }


    public void setId(Integer id) {
        this.id = id;
    }


    public void setNazov(String nazov) {
        this.nazov = nazov;
    }

    public void setNakupnaCena(double nakupna_cena) {
        this.nakupna_cena = nakupna_cena;
    }

    public void setObjem(double objem) {
        this.objem = objem;
    }



    public void insert() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("INSERT INTO velkosklad (nazov, nakupna_cena, objem, merna_jednotka) VALUES (?, ?, ?, 2)", Statement.RETURN_GENERATED_KEYS)) {
            s.setString(1, nazov);
            s.setDouble(2, nakupna_cena);
            s.setDouble(3, objem);
            s.setString(4, merna_jednotka);

            s.executeUpdate();

            try (ResultSet r = s.getGeneratedKeys()) {
                r.next();
                nazov = r.getString(1);
                nakupna_cena = r.getDouble(2);
                objem = r.getDouble(3);
                merna_jednotka = r.getString(4);
                id = r.getInt(5);
            }
        }
    }

}
