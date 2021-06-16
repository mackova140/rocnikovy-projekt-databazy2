package app;

import java.sql.Connection;

/**
 *
 * @author Natalia Mackova
 */

public class DbContext {

    private static Connection connection;

    public static void setConnection(Connection connection) {
        if (connection == null) {
            throw new NullPointerException("Spojenie nesmie byt null");
        }

        DbContext.connection = connection;
    }

    public static Connection getConnection() {
        if (connection == null) {
            throw new IllegalStateException("Spojenie nebolo nadstavene. Vytvorte ho pred volanim tejto funkcie.");
        }

        return connection;
    }

    public static void clear() {
        connection = null;
    }

}
