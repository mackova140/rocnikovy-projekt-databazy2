package app.rdg;

import app.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NajdiPrevadz {
    private static final NajdiPrevadz INSTANCE = new NajdiPrevadz();

    public static NajdiPrevadz getInstance() {
        return INSTANCE;
    }

    private NajdiPrevadz() {}

    public Prevadzkovatel find() throws SQLException {

        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM konto_prevadzkovatel")) {

            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {
                    Prevadzkovatel c = load(r);

                    if (r.next()) { throw new RuntimeException("Neocakavana chyba"); }

                    return c;

                } else return null;
            }
        }
    }

// ostatne nam netreba
    protected Prevadzkovatel load(ResultSet r) throws SQLException {
        Prevadzkovatel c = new Prevadzkovatel();
        c.setKredit(r.getDouble("kredit"));

        return c;
    }
}
