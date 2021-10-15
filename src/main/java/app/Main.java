package app;

import app.ui.MainMenu;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import org.postgresql.ds.PGSimpleDataSource;

/**
 *
 * @author Natalia Mackova
 */
public class Main {

    public static void main(String[] args) throws SQLException, IOException {

        PGSimpleDataSource dataSource = new PGSimpleDataSource();

        dataSource.setServerName("www.fk-it.org");
        dataSource.setPortNumber(5432);
        dataSource.setDatabaseName("skusanie_mackova");
        dataSource.setUser("mackova");
        dataSource.setPassword("*******");

        // System.out.println("helo");

        try (Connection connection = dataSource.getConnection()) {
            DbContext.setConnection(connection);

            MainMenu mainMenu = new MainMenu();
            mainMenu.run();


        } finally {
            DbContext.clear();
        }
    }
}
