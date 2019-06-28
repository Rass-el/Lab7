package server.russel;

import server.hash.Hash;
import server.russel.database.DBChanger;
import server.russel.database.DBLoader;
import server.russel.mail.MailSender;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicLong;

public class ClientThread implements Runnable {
    private final String collectionFile = "files/myCollection.csv";


    private Socket socket;
    private volatile Scanner clientIn = null;
    private volatile PrintWriter clientOut = null;

    public ClientThread(Socket incoming) {
        socket = incoming;
    }

    public void importFileFromClientToServer() {
        System.out.println("Прием файла от клиента...");
        try (PrintWriter fileWriter = new PrintWriter(new FileWriter(collectionFile), true)) {
            while (clientIn.hasNextLine()) {

                String line = clientIn.nextLine();
                if (line.trim().equals("END")) {
                    System.out.println("Готово!");
                    return;
                }
                fileWriter.println(line);
            }

        } catch (IOException ex) {
            System.out.println("Ошибка импорта файла");
        }

    }

    public void importFileFromServerToClient() {
        System.out.println("Передача файла клиенту...");
        try (Scanner fileReader = new Scanner(new File(collectionFile))) {
            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                clientOut.println(line);
            }
            clientOut.println("END");
            System.out.println("Готово!");
        } catch (FileNotFoundException ex) {
            System.out.println("Ошибка экспорта файла");
        }
    }

    public static String generatePassword(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random rng = new Random();
        char[] text = new char[length];
        for (int i = 0; i < length; i++)
        {
            text[i] = characters.charAt(rng.nextInt(characters.length()));
        }
        return new String(text);
    }

    private void signIn() {
        String passwordForUser = generatePassword(5);
        String hash = null;
        try {
            hash = Hash.sha384(passwordForUser);
        }catch (Exception e) {
            passwordForUser = "";
            System.out.println("Не удалось сгенерить хэш=(");
        }

        DBChanger changer;

        clientOut.println("Введите логин:");
        String login = clientIn.nextLine();
        clientOut.println("Введите почту:");
        String email = clientIn.nextLine();

        User user = new User(login, hash, email);
        DBLoader db = new DBLoader();

        try (Connection conn = db.getConnection()){
            changer = new DBChanger(conn);
            changer.insertUser(user);
            clientOut.println("Вы зарегистрированы!");
            clientOut.println("Сейчас вам на почту придет пароль. \n" +
                    "(Ваш пароль: " + passwordForUser + ")");
            clientOut.println("END");
        } catch (SQLException e) {
            System.out.println("Нельзя создать юзверя");
        }
        /*
        MailSender mailSender;
        mailSender = new MailSender("an020500@gmail.com", email, "password");
        mailSender.sendMessage("Пароль доступа", passwordForUser); */
        System.out.println("Не удалось отправить сообщение.");
    }

    @Override
    public void run() {
        try {
            System.out.println("Наблюдается подключение");

            clientOut = new PrintWriter(socket.getOutputStream(), true);
            clientIn = new Scanner(socket.getInputStream());

            try {
                while (clientIn.hasNextLine()) {
                    String command = clientIn.nextLine().trim();
                    System.out.println("Поступил новый запрос: " + command);

                    switch (command) {
                        case "registrate":
                        case "reg":
                            clientOut.println(command);
                            signIn();
                            break;
                        case "import":
                            clientOut.println(command);
                            importFileFromClientToServer();
                            break;
                        case "load":
                            clientOut.println(command);
                            importFileFromServerToClient();
                            break;
                        case "info":
                            clientOut.println(command);
                            clientOut.println(Server.myCollection.info());
                            break;
                        case "show":
                            clientOut.println(command);
                            clientOut.println(Server.myCollection.show());
                            break;
                        case "help": case "?":
                            clientOut.println(command);
                            clientOut.println(Server.myCollection.help());
                            break;
                        case "save":
                            clientOut.println(command);
                            Server.myCollection.save();
                        case "exit": case "ex":
                            clientOut.println(command);
                            System.out.println("Клиент вышел. Мы будем скучать!");
                            break;
                        default:
                            //Даем коллекции обработать команду
                            String success = Server.myCollection.start(command);
                            clientOut.println(success);
                    }
                }
            } finally {
                clientIn.close();
                clientOut.close();
                socket.close();
            }

        } catch (IOException ex) {
            System.out.println("Ошибка инициализации клиента или закрытия сокета");
        }
    }

}