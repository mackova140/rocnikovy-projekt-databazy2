package app.rdg;

import app.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NajdiKarta {
    private static final NajdiKarta INSTANCE = new NajdiKarta();

    public static NajdiKarta getInstance() {
        return INSTANCE;
    }

    private NajdiKarta() {}

    public Karta findByZakaznik(int i) throws SQLException {

        try (PreparedStatement s = DbContext.getConnection().prepareStatement(
                "SELECT * FROM karta where id_zakaznik = ?")) {
            s.setInt(1, i);

            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {
                    Karta c = load(r);
                    if (r.next()) { throw new RuntimeException("Neocakavana chyba"); }
                    return c;
                } else return null;
            }
        }
    }

    // ostatne nam netreba
    protected Karta load(ResultSet r) throws SQLException {
        Karta c = new Karta();
        c.setId(r.getInt("id"));
        c.setId_zakaznik(r.getInt("id_zakaznik"));
        c.setKategoria(r.getString("kategoria"));
        c.setZlava_percenta(r.getDouble("zlava_percenta"));

        return c;
    }
}
