package app.rdg;

import app.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *  @author Natalia Mackova
 */

public class Objednavky {
    private Integer id;
    private Integer id_stol;
    private Integer id_napoj;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_stol() {
        return id_stol;
    }

    public void setId_stol(Integer id_stol) {
        this.id_stol = id_stol;
    }

    public Integer getId_napoj() {
        return id_napoj;
    }

    public void setId_napoj(Integer id_napoj) {
        this.id_napoj = id_napoj;
    }


    public void insert() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("INSERT INTO objednavky (id_stol, id_napoj) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            s.setInt(1, id_stol);
            s.setInt(2, id_napoj);

            s.executeUpdate();

            try (ResultSet r = s.getGeneratedKeys()) {
                r.next();
                id = r.getInt(1);
                id_stol = r.getInt(2);
                id_napoj = r.getInt(3);
            }
        }
    }

    public void delete() throws SQLException {
        if (id == null) {
            throw new IllegalStateException("id nenexistuje");
        }

        try (PreparedStatement s = DbContext.getConnection().prepareStatement("DELETE FROM objednavky WHERE id = ?")) {
            s.setInt(1, id);

            s.executeUpdate();
        }
    }
}
