package app.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *  @author Natalia Mackova
 */

public abstract class Menu {
    private boolean skonci;

    public void run() throws IOException {
        skonci = false;

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (!skonci) {
            System.out.println();
            print();
            System.out.println();

            String line = br.readLine();
            if (line == null) {
                return;
            }

            System.out.println();

            handle(line);
        }
    }

    public void exit() {
        skonci = true;
    }

    public abstract void print();

    public abstract void handle(String option);
}
