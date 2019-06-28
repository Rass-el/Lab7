/**
 * Данный класс является классом-оболочкой для коллекции myPersonList, реализованном с помощью LinkedList.
 * @see lab.russel.Person
 * @author Bekoev Andrew
 * @version 1.0
 */
package server.russel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import server.csv.*;
import server.csv.CSVReader;
import server.csv.CSVWriter;
import server.hash.Hash;
import server.russel.database.DBChanger;
import server.russel.database.DBLoader;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

public class PersonCollection {

    private volatile CopyOnWriteArrayList<Person> myPersonList = new CopyOnWriteArrayList<>();

    private File mainFile;
    private File tempCSVFile = new File("files/tempCollection.csv");
    private String login;
    private String password;
    private DBChanger changer;

    /**
     * Переменная initializationTime показывает время, когда был инициализирован класс PersonCollection.
     * Инициализируется в конструкторе при создании экземпляра класса myCollection.
     */
    private final Date initializationTime = new Date();

    public PersonCollection(String fileName) {
        mainFile = new File(fileName);
        initializationTime.setTime(System.currentTimeMillis());
        myPersonList.sort((p, b) -> b.getName().compareTo(p.getName()));
    }

    public void saveToTable() {
        String clear = "TRUNCATE PERSON";

    }

    /**
     * Данный метод добавляет элемент типа (@code lab.russel.Person) в коллекцию.
     * Команда: add <login> <password> {name: "Cartman", "age": "23", "danger": "DEADLY", "equip": [{"name": "knife", "weight": 10}] }
     *
     * @param stringJson - массив параметров [name, age, danger]
     */

    public synchronized boolean add(String stringJson, User user) throws JsonSyntaxException, IOException {
        this.upLoadFromCSV(new CSVReader(tempCSVFile));
        try {
            if (stringJson == null || stringJson.equals(""))
                throw new JsonSyntaxException("");
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Person.class, new PersonDeserializer())
                    .create();

            Person person = gson.fromJson(stringJson, Person.class);

            changer.insertPerson(person, user);
            myPersonList.add(person);
            myPersonList.sort((p, b) -> b.getName().compareTo(p.getName()));
            this.tempSaveAsCSV();
            return true;
        } catch (JsonSyntaxException je) {
            System.out.println("Неверный формат Json!");
            return false;
        }
    }

    /**
     * Данный метод удаляет из коллекции элемент.
     * Команда: remove <login> <password> {"name": "Kenny", "age": "23", "danger": "DEADLY", "equip": [{"weight": 10, "rarity": "RARE"}] }
     *
     * @param stringJson - массив параметров [name, age, danger]
     */
    public synchronized boolean remove(String stringJson) throws IOException {
        this.upLoadFromCSV(new CSVReader(tempCSVFile));
        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Person.class, new PersonDeserializer())
                    .create();
            Person person = gson.fromJson(stringJson, Person.class);
            changer.deletePerson(person);
            for (Person p : myPersonList) {
                if (p.getName().equals(person.getName())) {
                    myPersonList.remove(person);
                    this.tempSaveAsCSV();
                    return true;
                }
            }
        } catch (JsonSyntaxException je) {
            System.out.println("Неверный формат Json!");
            return false;
        }
        return false;
    }

    /**
     * Этот метод удаляет все элементы из коллекции, эквивалентные заданному элементу.
     * Команда: remove_all <login> <password> {"name": "Kenny", "age": "23", "danger": "DEADLY", "equip": [{"weight": 10, "rarity": "RARE"}] }
     *
     * @param person
     */
    public synchronized void remove_all(String person, User user) throws IOException {
        this.upLoadFromCSV(new CSVReader(tempCSVFile));
        while (this.remove(person)) {}
        this.tempSaveAsCSV();
    }

    /**
     * Метод удаляет из коллекции первый элемент.
     * Команда: remove_first <login> <password>
     */
    public synchronized boolean remove_first() throws IOException {
        this.upLoadFromCSV(new CSVReader(tempCSVFile));
        if (myPersonList.size() > 0) {
            myPersonList.remove(0);
            this.tempSaveAsCSV();
            return true;
        }
        this.tempSaveAsCSV();
        return false;
    }

    /**
     * Этот метод возвращает все элементы коллекции в строковом представлении.
     * Команда: show
     */
    public String show() {
        StringBuffer sb = new StringBuffer();
        for (Person p : myPersonList) {
            sb.append(p.toString());
        }
        sb.append("\n\nEND");
        return new String(sb);
    }


    /**
     * Данный метод сохраняет коллекцию в файл fileName. Данные берутся непосредственно из коллекции.
     * Команда: save fileName
     */
    public synchronized boolean save(String fileName) {
        try (CSVWriter file = new CSVWriter(fileName)) {
            for (Person person : myPersonList)
                file.writeAsCSV(person.asArray());
            return true;
        } catch (IOException e) {
            System.out.println("Не могу сохранить в файл " + fileName);
            return false;
        }
    }

    public void tempSaveAsCSV() {
        try (CSVWriter file = new CSVWriter("files/tempCollection.csv")) {
            for (Person person : myPersonList)
                file.writeAsCSV(person.asArray());
        } catch (IOException e) {
            System.out.println("Ошибка динамического сохранения!");
        }
    }

    /**
     * Данный метод сохраняет коллекцию в файл
     * Команда: save
     */
    public synchronized boolean save() {
        return save(mainFile.getPath());
    }

    /**
     * Метод возвращает информацию о коллекции (тип, дата инициализации, количество элементов)
     * Команда: info
     */
    public String info() {
        return "LinkedList<Person>:" +
                "\nИмя: myPersonList" +
                "\nТип элемента: Person" +
                "\nВремя первой инициализации: " + initializationTime.toString() +
                "\nКол-во элементов: " + myPersonList.size() +
                "\nEND";
    }

    /**
     * Данная команда возвращает список доступных команд и операций над коллекцией myCollection
     * Команда help или ?
     */
    public String help() {
        return "Available commands: " +
                "\n{registrate|reg} - registrate a new user." +
                "\nadd <login> <password> {name: \"Cartman\", \"age\": \"23\", \"danger\": \"DEADLY\", \"equip\": [{\"name\": \"knife\", \"weight\": 10}] } - add in collection new item in Json format" +
                "\nremove <login> <password> {name: \"Cartman\", \"age\": \"23\", \"danger\": \"DEADLY\", \"equip\": [{\"name\": \"knife\", \"weight\": 10}] } - remove from the collection item equivalent to the given" +
                "\nremove_all <login> <password> {name: \"Cartman\", \"age\": \"23\", \"danger\": \"DEADLY\", \"equip\": [{\"name\": \"knife\", \"weight\": 10}] } - remove from the collection all items equivalent to the given" +
                "\n{remove_first|r_f} <login> <password> - remove first item from collection" +
                "\nshow - show current state of collection" +
                "\nsave - save collection in file" +
                "\ninfo - show information about collection" +
                "\nex[it] - kill program" +
                "\n{help|?} - show available commands " +
                "\nEND";
    }

    public void upLoadFromCSV(CSVReader csvFile) {
        myPersonList.clear();
        while (csvFile.hasNextLine()) {
            myPersonList.add(new Person(csvFile.parseNextLine()));
        }
    }

    public void initializationFromCSV(CSVReader csvFile) {
        mainFile = csvFile.getFile();
        myPersonList.clear();
        while (csvFile.hasNextLine()) {
            myPersonList.add(new Person(csvFile.parseNextLine()));
        }
        this.tempSaveAsCSV();
    }

    /**
     * Start the work with collection.
     *
     * @throws IOException
     */
    public String start(String command) throws IOException {
        if (command == null) {
            System.out.println("Команды не поступило...");
            return "null";
        }

        User user = new User();
        String[] parsedCommand = command.split(" ");


        if (parsedCommand.length < 4)
            return "false";
        user.setLogin(parsedCommand[1]);
        try {
            user.setPassword(Hash.sha384(parsedCommand[2]));
        } catch (Exception e) {
            System.out.println("PersColl = sha lol");
        }


        DBLoader db = new DBLoader();

        Connection conn = db.getConnection();

        try (Statement stat = conn.createStatement()) {
            changer = new DBChanger(conn);

            ResultSet rs = stat.executeQuery("SELECT * FROM USERS");
            while (rs.next()) {
                if (rs.getString(1).equals(user.getLogin()) &&
                    rs.getString(2).equals(user.getPassword())) {
                    user.setRegistrated(true);
                }
            }

            if (!user.isRegistrated()) {
                return "Недостаточно прав.";
            }

            command = "";
            for (int i = 3; i < parsedCommand.length; ++i) {
                command = command + parsedCommand[i];
            }

            switch (parsedCommand[0].trim()) {
                case "add":
                    return String.valueOf(add(command, user));
                case "remove_all":
                    remove_all(command, user);
                    return "true";
                case "remove":
                    return String.valueOf(remove(command));

                case "r_f":
                case "remove_first": {
                    return String.valueOf(remove_first());
                }
                case "save":
                    return String.valueOf(save());
                case "show":
                    show();
                    return "show";
                case "info":
                    info();
                    return "info";
                case "ex":
                case "exit":
                    System.out.println("Клиент вышел. Мы будем скучать!");
                    return "exit";
                case "help":
                case "?":
                    help();
                    return "help";
                default:
                    return "Введите \"help\" или \"?\" для ознакомления со списком команд.";
            }


        } catch (SQLException e) {
            System.out.println("Нельзя выполнить команду.");
        } finally {
            try {
                conn.close();
            }catch (SQLException e) { System.out.println("Проблемы с закрытием чи шо");}
        }

        return "PersonCollection.java = kekos";
    }
}




