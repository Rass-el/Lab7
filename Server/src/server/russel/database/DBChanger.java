package server.russel.database;

import server.russel.Person;
import server.russel.User;

import java.sql.*;

public class DBChanger {

    private static Connection connection;
    private static Statement statement = null;

    public DBChanger(Connection c) throws SQLException {
        connection = c;
        statement = connection.createStatement();
    }

    public static boolean checkUser(String login, String password) {
        return true;
    }

    public void createCollectionTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS PERSONS "
                + "(USERNAME VARCHAR(30) NOT NULL,"
                + "PERSONNAME VARCHAR(30) NOT NULL,"
                + "AGE INTEGER NOT NULL,"
                + "DANGER VARCHAR(30) NOT NULL)";
        try {
            statement.executeUpdate(createTableSQL);
            System.out.println("Таблица \"PERSONS\" создана!");
        } catch (SQLException e) {
            System.out.println("Не удалось создать таблицу(");
        }
    }

    public void createUserTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS USERS "
                + "(USERNAME VARCHAR(30) NOT NULL,"
                + "PASSWORD VARCHAR(100) NOT NULL,"
                + "EMAIL VARCHAR(35) NOT NULL)";
        try {

            statement.executeUpdate(createTableSQL);
            System.out.println("Таблица \"USERS\" создана!");
        } catch (SQLException e) {
            System.out.println("Не удалось создать таблицу(");
        }
    }

    public void closeConn() {
        if (statement != null) {
            try {
                statement.close();
            }catch (SQLException ex) {
                System.out.println("Проблема с закрытием");
            }

        }
        if (connection != null) {
            try {
                connection.close();
            }catch (SQLException ex) {
                System.out.println("Проблема с закрытием");
            }
        }
    }



    public void insertPerson(Person p, User user) {
        String insertTableSQL = "INSERT INTO PERSONS VALUES ('" + user.getLogin() + "','" + p.getName() + "'," + p.getAge() + ",'" + p.getDanger().toValue() + "')";

        System.out.println("Новый запрос: " + insertTableSQL);
        try {
            statement.execute(insertTableSQL);
        } catch (SQLException e) {
            System.out.println("Неверный юзер.");
        }

    }


    public void deletePerson(Person p) {
        String deleteTableSQL = "DELETE FROM PERSONS WHERE PERSONNAME = '" + p.getName() + "'";
            try {
                statement.execute(deleteTableSQL);
            } catch (SQLException e) {
                System.out.println("Неудалось удалить персонажа");
            }
    }




    public void insertUser(User user) {
        String insertTableSQL = "INSERT INTO USERS "  //(USERNAME, PASSWORD, EMAIL)
                + "VALUES "
                + "('" + user.getLogin() + "','" + user.getPassword() + "','" + user.getEmail() + "')";
        System.out.println("Новый запрос: " + insertTableSQL);
        try {
            statement.execute(insertTableSQL); // todo чекнуть, был ли уже такой юзер
        } catch (SQLException e) {
            System.out.println("Неверный юзер.");
        }
    }
}
