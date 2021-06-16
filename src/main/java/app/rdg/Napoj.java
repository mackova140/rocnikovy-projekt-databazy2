package app.rdg;

import app.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *  @author Natalia Mackova
 */

public class Napoj {
    private Integer id;
    private Integer id_podnik;
    private Integer id_napoj;       // zo skladu
    private String nazov;
    private double predajna_cena;
    private Integer mnozstvo;

    public Integer getId() {
        return id;
    }

    public Integer getId_podnik() {
        return id_podnik;
    }

    public Integer getId_napoj() {
        return id_napoj;
    }

    public String getNazov() {
        return nazov;
    }

    public double getPredajna_cena() {
        return predajna_cena;
    }

    public double getObjem() {
        return objem;
    }

    public Integer getMnozstvo() {
        return this.mnozstvo;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setId_podnik(Integer id_podnik) {
        this.id_podnik = id_podnik;
    }

    public void setId_napoj(Integer id_napoj) {
        this.id_napoj = id_napoj;
    }

    public void setNazov(String nazov) {
        this.nazov = nazov;
    }

    public void setPredajna_cena(double predajna_cena) {
        this.predajna_cena = predajna_cena;
    }

    public void setObjem(double objem) {
        this.objem = objem;
    }

    public void setMnozstvo(Integer mnozstvo) {
        this.mnozstvo = mnozstvo;
    }

    private double objem;

    public void insert() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("INSERT INTO napojovy_listok (id_podnik, id_napoj, nazov, predajna_cena, objem, mnozstvo) VALUES (?, ?, ?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            s.setInt(1, id_podnik);
            s.setInt(2, id_napoj);
            s.setString(3, nazov);
            s.setDouble(4, predajna_cena);
            s.setDouble(5, objem);
            s.setInt(6, mnozstvo);

            s.executeUpdate();

            try (ResultSet r = s.getGeneratedKeys()) {
                r.next();
                id = r.getInt(1);
            }
        }
    }

    public void update() throws SQLException {
        if (id == null) {
            throw new IllegalStateException("id napoja nenexistuje");
        }

        try (PreparedStatement s = DbContext.getConnection().prepareStatement("UPDATE napojovy_listok SET predajna_cena = ?, mnozstvo = ?  WHERE id = ? ")) {
            s.setDouble(1, predajna_cena);
            s.setDouble(2, mnozstvo);
            s.setInt(3, id);

            s.executeUpdate();
        }
    }


    public void delete() throws SQLException {
        if (id == null) {
            throw new IllegalStateException("id zakaznika nenexistuje");
        }

        try (PreparedStatement s = DbContext.getConnection().prepareStatement("DELETE FROM napojovy_listok WHERE id = ?")) {
            s.setInt(1, id);

            s.executeUpdate();
        }
    }
}
