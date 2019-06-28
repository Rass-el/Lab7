package server.csv;

import java.io.Closeable;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CSVWriter implements Closeable {
    /**
     * Имя файла
     */
    private String fileName;

    private OutputStream fileWriter;
    /**
     * Используемый разделитель. В конструкторе без переданного параметра разделителя по умолчанию используется
     * запятая ( , ).
     * Пример: Name, Surname, Age, Character
     */
    private char delimiter;

    public CSVWriter(String fileName) throws IOException {
        this(fileName, ',');
    }

    public CSVWriter(String fileName, char delimiter) throws IOException {
        this.fileName = fileName;
        this.delimiter = delimiter;
        fileWriter = new FileOutputStream(fileName, false);
    }

    public void writeAsCSV(String[] line) throws IOException {
        int i = 0;
        for (String element: line) {
            // Если это Редкость...
            if (i == 4) {
                element = element + '"' + '\n';  // Перевод строки
                byte[] bytes = element.getBytes();
                fileWriter.write(bytes, 0, bytes.length);
            }

            // Если это вес...
            if (i == 3) {
                element = '"' + element + delimiter + ' ';
                byte[] bytes = element.getBytes();
                fileWriter.write(bytes, 0, bytes.length);
            }

            // Иначе...
            if (i < 3) {
                element = '"' + element + '"' + delimiter + ' ';
                byte[] bytes = element.getBytes();
                fileWriter.write(bytes, 0, bytes.length);
            }
            i++;
        }

    }

    @Override
    public void close() throws IOException {
        fileWriter.close();
    }


}
