package app.rdg;

import app.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Podnik {
    private Integer id;
    private String nazov;

    public Integer getId() {
        return id;
    }

    public String getNazov() {
        return nazov;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNazov(String nazov) {
        this.nazov = nazov;
    }


    public void insert() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("INSERT INTO podnik (nazov) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
            s.setString(1, nazov);

            s.executeUpdate();

            try (ResultSet r = s.getGeneratedKeys()) {
                r.next();
                nazov = r.getString(1);
                id = r.getInt(2);
            }
        }
    }

    public void delete() throws SQLException {
        if (id == null) {
            throw new IllegalStateException("id podniku nenexistuje");
        }

        try (PreparedStatement s = DbContext.getConnection().prepareStatement("DELETE FROM podnik WHERE id = ?")) {
            s.setInt(1, id);

            s.executeUpdate();
        }
    }
}
