package app.rdg;

import app.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Karta {
    private Integer id;
    private Integer id_zakaznik;
    private String kategoria;
    private double zlava_percenta;

    public Integer getId() {
        return id;
    }

    public Integer getId_zakaznik() {
        return id_zakaznik;
    }

    public String getKategoria() {
        return kategoria;
    }

    public double getZlava_percenta() {
        return zlava_percenta;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setId_zakaznik(Integer id_zakaznik) {
        this.id_zakaznik = id_zakaznik;
    }

    public void setKategoria(String kategoria) {
        this.kategoria = kategoria;
    }

    public void setZlava_percenta(double zlava_percenta) {
        this.zlava_percenta = zlava_percenta;
    }


    public void insert() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement(
                "INSERT INTO karta (id_zakaznik, kategoria, zlava_percenta) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            s.setInt(1, id_zakaznik);
            s.setString(2, kategoria);
            s.setDouble(3, zlava_percenta);

            s.executeUpdate();

            try (ResultSet r = s.getGeneratedKeys()) {
                r.next();
                id = r.getInt(1);
                id_zakaznik = r.getInt(2);
                kategoria = r.getString(3);
                zlava_percenta = r.getDouble(4);
            }
        }
    }
}
