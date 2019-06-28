package server.russel.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBLoader {
    private final String url = "jdbc:postgresql://pg:5432/studs";
    private final String login = "s265938";
    private final String password = "toa763";

    private Connection connection;

    public DBLoader() {
        boolean firstCreation = true;
        while(true) {
            try {
                try {
                    Class.forName("org.postgresql.Driver");
                }catch (ClassNotFoundException ce) {
                    System.out.println("Не найден драйвер для БД.");
                }

                connection = DriverManager.getConnection(url, login, password);
                //System.out.println("\nСоединение с БД прошло успешно!");
                break;
            } catch (Exception e) {
                System.out.println("Не удалось соединиться с БД!");
                System.exit(1);
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
