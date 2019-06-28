package lab.russel;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Client {

    private final String collectionFileString;
    private final String HOST;
    private final int PORT;

    private static Socket clientSocket = null;

    private static PrintWriter serverOut = null;
    private static Scanner serverIn = null;

    Scanner stdInReader = null;


    // Принимает на вход параметр с именем файла на котором хранится коллекция, хост и порт для подключения.
    public Client(String[] args) {
        collectionFileString = args[0];
        HOST = args[1];
        PORT = Integer.parseInt(args[2]);
    }

    public void start() {
            try {
                clientSocket = new Socket(HOST, PORT);

                serverOut = new PrintWriter(clientSocket.getOutputStream(), true);
                serverIn = new Scanner(clientSocket.getInputStream());

                stdInReader = new Scanner(System.in);
            } catch (IOException e) {
                try {
                    System.out.println("Извините! Ошибка подключения");
                    serverIn.close();
                    serverOut.close();
                    clientSocket.close();
                } catch (IOException ex) {
                    System.out.println("Небольшие проблемы с закрытием, но в целом все замурчательно!");
                } finally {
                    try {
                        System.out.println("Пробую подключиться к серверу...");
                        TimeUnit.SECONDS.sleep(4);
                    }catch (InterruptedException exep) {}
                    start();
                }

            }

            System.out.println("Вы подключены к серверу!\nВведите команду или \"help\" для получения справки: ");
            try {
                while (stdInReader.hasNextLine()) {
                    String command = stdInReader.nextLine();
                    serverOut.println(command);

                    // Читаем ответ от сервера
                    if (serverIn.hasNextLine()) {
                        String answer = serverIn.nextLine();

                        switch (answer) {
                            case "import":
                                importFileFromClientToServer(); break;
                            case  "load":
                                importFileFromServerToClient(); break;
                            case "reg":
                            case "registrate":
                                signIn(); break;
                            case "info":
                            case "show":
                            case "help":
                            case "?":
                                readInfo(); break;
                            case "ex":
                            case "exit":
                                System.out.println("Отключение от сервера...");
                                System.exit(0);
                            case "true":
                                System.out.println("Команда выполнена!");
                                break;
                            case "false":
                                System.out.println("Команда не выполнена=(");
                                break;
                            default:
                                System.out.println("Ответ от сервера: " + answer);
                            }
                    } else {
                        System.out.println("Сервер упал=(");
                        break;
                    }
                    System.out.println("Введите команду: ");
                }
            } finally {
                try {
                    serverIn.close();
                    serverOut.close();
                    clientSocket.close();
                } catch (IOException e) {
                    System.out.println("Небольшие проблемы с закрытием, но в целом все замурчательно!");
                }

            }
            try {
                System.out.println("Пробую подключиться к серверу...");
                TimeUnit.SECONDS.sleep(4);
            }catch (InterruptedException exep) {}
            start();
    }

    public void signIn() {
        String msg = serverIn.nextLine();
        System.out.println(msg); //Дай логин

        String login = stdInReader.nextLine();
        serverOut.println(login);

        msg = serverIn.nextLine();
        System.out.println(msg); //Дай e-mail
        String email = stdInReader.nextLine();
        serverOut.println(email);

        readInfo();
    }



    public void readInfo() {
            while (serverIn.hasNextLine()) {
                String line = serverIn.nextLine();
                if (line.trim().equals("END")) {
                    return;
                }
                System.out.println(line);
            }
    }

    public void importFileFromClientToServer() {
        System.out.println("Передача файла серверу...");
        try (Scanner fileReader = new Scanner(new File(collectionFileString))){

            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                serverOut.println(line);
            }
            serverOut.println("END");
            System.out.println("Готово!");
        } catch (FileNotFoundException e) {
            System.out.println("Ошибка импорта файла");
        }
    }

    public void importFileFromServerToClient() {
        System.out.println("Прием файла от сервера...");
        try (PrintWriter fileWriter = new PrintWriter(new FileWriter(collectionFileString), true)) {

            while (serverIn.hasNextLine()) {
                String line = serverIn.nextLine();
                if (line.trim().equals("END")) {
                    System.out.println("Готово!");
                    return;
                }
                fileWriter.println(line);
            }
        } catch (IOException ex) {
            System.out.println("Ошибка экспорта файла");
        }
    }

}
