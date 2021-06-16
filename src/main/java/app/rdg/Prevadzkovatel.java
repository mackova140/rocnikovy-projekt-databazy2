package app.rdg;

import app.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Prevadzkovatel {
    private String meno;
    private String heslo;
    private double kredit;

    public void setKredit(double kredit) {
        this.kredit = kredit;
    }

    public double getKredit() {
        return kredit;
    }

    public void update() throws SQLException {

        try (PreparedStatement s = DbContext.getConnection().prepareStatement("UPDATE konto_prevadzkovatel SET kredit = ?")) {
            s.setDouble(1, kredit);

            s.executeUpdate();
        }
    }
}
