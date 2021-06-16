package app.rdg;

import app.DbContext;

import java.sql.*;

public class Nakup {
    private Integer id;
    private Date datum;

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public void setSuma(double suma) {
        this.suma = suma;
    }

    private double suma;

    public Date getDatum() {
        return datum;
    }

    public double getSuma() {
        return suma;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public void insert() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("INSERT INTO nakupy (datum, suma) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            s.setDate(1, datum);
            s.setDouble(2, suma);

            s.executeUpdate();

            try (ResultSet r = s.getGeneratedKeys()) {
                r.next();
                id = r.getInt(1);
            }
        }
    }

}
