package app.rdg;

import app.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Oblubene {
    private Integer id;
    private Integer id_napoj;       // zo skladu
    private Integer id_zakaznik;
    private String nazov;
    private Integer pocet;

    public Integer getId() {
        return id;
    }

    public Integer getId_zakaznik() {
        return id_zakaznik;
    }

    public Integer getId_napoj() {
        return id_napoj;
    }

    public String getNazov() {
        return nazov;
    }

    public Integer getPocet() {
        return this.pocet;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setId_zakaznik(Integer id_zakaznik) {
        this.id_zakaznik = id_zakaznik;
    }

    public void setId_napoj(Integer id_napoj) {
        this.id_napoj = id_napoj;
    }

    public void setNazov(String nazov) {
        this.nazov = nazov;
    }


    public void setPocet(Integer pocet) {
        this.pocet = pocet;
    }


    public void insert() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement(
                "INSERT INTO oblubene (id_napoj, id_zakaznik, nazov, pocet) VALUES (?, ?, ?,?)",
                Statement.RETURN_GENERATED_KEYS)) {

            s.setInt(1, id_napoj);
            s.setInt(2, id_zakaznik);
            s.setString(3, nazov);
            s.setInt(4, pocet);

            s.executeUpdate();

            try (ResultSet r = s.getGeneratedKeys()) {
                r.next();
                // todo ostatne??
                id = r.getInt(5);
            }
        }
    }

    public void update() throws SQLException {
        if (id == null) { throw new IllegalStateException("id neexistuje."); }

        try (PreparedStatement s = DbContext.getConnection().prepareStatement(
                "UPDATE oblubene SET pocet = ? WHERE id_napoj = ? and id_zakaznik = ?")) {

            s.setInt(1, pocet);
            s.setInt(2, id_napoj);
            s.setInt(3, id_zakaznik);

            s.executeUpdate();
        }
    }


    public void delete() throws SQLException {
        if (id == null) {
            throw new IllegalStateException("id nenexistuje");
        }

        try (PreparedStatement s = DbContext.getConnection().prepareStatement("DELETE FROM oblubene WHERE id = ?")) {
            s.setInt(1, id);

            s.executeUpdate();
        }
    }
}
