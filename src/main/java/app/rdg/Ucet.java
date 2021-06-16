package app.rdg;

import app.DbContext;

import java.sql.*;

public class Ucet {
    private Integer id;
    private double cena;
    private Date datum;
    private Integer id_podnik;
    private Integer id_napoj;
    private Integer id_zakaznik;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_napoj() {
        return id_napoj;
    }

    public void setId_napoj(Integer id_napoj) {
        this.id_napoj = id_napoj;
    }

    public double getCena() {
        return cena;
    }

    public Date getDatum() {
        return datum;
    }

    public Integer getId_podnik() {
        return id_podnik;
    }

    public Integer getId_zakaznik() {
        return id_zakaznik;
    }

    public void setCena(double cena) {
        this.cena = cena;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public void setId_podnik(Integer id_podnik) {
        this.id_podnik = id_podnik;
    }

    public void setId_zakaznik(Integer id_zakaznik) {
        this.id_zakaznik = id_zakaznik;
    }

    public void insert() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement(
                "INSERT INTO ucet (datum, cena, id_podnik,id_zakaznik, id_napoj) " +
                        "VALUES (?, ?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            s.setDate(1, datum);
            s.setDouble(2, cena);
            s.setInt(3, id_podnik);
            s.setInt(4, id_zakaznik);
            s.setInt(5, id_napoj);

            s.executeUpdate();

            try (ResultSet r = s.getGeneratedKeys()) {
                r.next();
                id = r.getInt(1);
            }
        }
    }

    // nepotrebujeme
    public void update() throws SQLException {
        if (id == null) { throw new IllegalStateException("id uctu nenexistuje"); }

        try (PreparedStatement s = DbContext.getConnection().prepareStatement(
                "UPDATE ucet SET datum = ?, cena = ?, id_podnik = ?,id_zakaznik = ?, id_napoj = ?" +
                        " WHERE id = ?")) {
            s.setDate(1, datum);
            s.setDouble(2, cena);
            s.setInt(3, id_podnik);
            s.setInt(4, id_zakaznik);
            s.setInt(5, id_napoj);
            s.setInt(6, id);

            s.executeUpdate();
        }
    }

    public void delete() throws SQLException {
        if (id == null) { throw new IllegalStateException("id zakaznika nenexistuje"); }

        try (PreparedStatement s = DbContext.getConnection().prepareStatement("DELETE FROM ucet WHERE id = ?")) {
            s.setInt(1, id);
            s.executeUpdate();
        }
    }
}
