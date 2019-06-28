package server.russel;

import server.csv.CSVReader;
import server.russel.database.DBChanger;
import server.russel.database.DBLoader;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {
    private static int PORT;      // you may change this
    private static final String collectionFileString = "files/myCollection.csv";

    public static volatile PersonCollection myCollection;

    public static void main (String[] args) {
        /*
        Connection connection = new DBLoader().getConnection();
        DBChanger db = new DBChanger(connection);
        db.createCollectionTable();
        */
        if (args.length < 1) {
            System.out.println("Usage: java server.russel.Server <port>");
            System.exit(1);
        } else
            PORT = Integer.parseInt(args[0]);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            CSVReader input = new CSVReader(new File(collectionFileString));
            myCollection = new PersonCollection(collectionFileString);
            myCollection.initializationFromCSV(input);

            DBLoader db = new DBLoader();
            try (Connection conn = db.getConnection()){
                DBChanger changer = new DBChanger(conn);
                changer.createCollectionTable();
                changer.createUserTable();
                changer.closeConn();
            }

            ExecutorService executor = Executors.newCachedThreadPool();
            while (true) {
                Socket msocket = serverSocket.accept();
                executor.execute(new ClientThread(msocket));
            }
        }catch (Throwable t) {
            System.out.println("Неизвестная ошибка");
        }
    }
}

