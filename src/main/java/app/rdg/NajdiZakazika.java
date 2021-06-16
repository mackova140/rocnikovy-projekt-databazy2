package app.rdg;

// todo: HESLO pravdepodobne este nebudeme chciet zobrazovat

import app.DbContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *  @author Natalia Mackova
 */

public class NajdiZakazika {
    private static final NajdiZakazika INSTANCE = new NajdiZakazika();

    public static NajdiZakazika getInstance() {
        return INSTANCE;
    }

    private NajdiZakazika() {}

    public Zakaznik findById(int id) throws SQLException {

        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM konto_zakaznika WHERE id = ?")) {
            s.setInt(1, id);

            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {
                    Zakaznik c = new Zakaznik();

                    c.setId(r.getInt("id"));
                    c.setMeno(r.getString("meno"));
                    c.setHeslo(r.getString("heslo"));
                    c.setKredit(r.getDouble("kredit"));

                    if (r.next()) {
                        throw new RuntimeException("Neocakavana chyba");
                    }
                    return c;

                } else return null;

            }
        }
    }

    // toto zatial nepotrebne -> vylistovanie vsetkych zakaznikov
    public List<Zakaznik> findAll() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM konto_zakaznika")) {

            try (ResultSet r = s.executeQuery()) {

                List<Zakaznik> zakaznici = new ArrayList<Zakaznik>();

                while (r.next()) {
                    Zakaznik c = new Zakaznik();

                    c.setId(r.getInt("id"));
                    c.setMeno(r.getString("meno"));
                    c.setHeslo(r.getString("heslo"));
                    c.setKredit(r.getDouble("kredit"));

                    zakaznici.add(c);
                }

                return zakaznici;
            }
        }
    }
}