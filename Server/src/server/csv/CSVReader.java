 /**
 * Класс для чтения данных в формате CSV
 * @author Bekoev Andrew
 * @since 1.0
 */

package server.csv;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

 public class CSVReader implements Closeable {

     /**
      * Считываемый файл.
      */
     private File file;

     /**
      * Считыватель файла file.
      */
     private Scanner fileReader;

     /**
      * Используемый разделитель. В конструкторе без переданного параметра разделителя по умолчанию используется
      * запятая ( , ).
      * Пример: Name, Surname, Age, Character
      */
     private char delimiter;
     public CSVReader(File file) throws FileNotFoundException {
         this(file, ',');
     }

     public CSVReader(File file, char delimiter) throws FileNotFoundException {
         this.file = file;
         fileReader = new Scanner(file);
         this.delimiter = delimiter;
     }

     public File getFile() {
         return file;
     }

     /**
      * @return Возвращает значения строки в виде массива строк.
      */

     public ArrayList<String> parseNextLine() {
         return parseNextLine(this.delimiter);
     }

     public ArrayList<String> parseNextLine(char delimiter) {
         ArrayList<String> parsedArray = new ArrayList<>();
         String line = fileReader.nextLine();
         char[] str = line.toCharArray();

         for (int i = 0; i < str.length; ++i) {
             boolean isQuotes = false;
             String word = "";

             while ( i < str.length && (str[i] != delimiter || isQuotes == true) ) {
                 if (str[i] == '"') {
                     isQuotes = !isQuotes;
                     i++;
                     continue;
                 }
                 word = word + str[i];
                 i++;
             }
             parsedArray.add(word.replace(" ", ""));

         }
         if (IsWrongCSVFormat(parsedArray)) {
             throw new CSVSyntaxException("Wrong format for Person!");
         }
         return parsedArray;
     }

     private boolean IsWrongCSVFormat(ArrayList<String> str) {
         // Synopsys
         // csvPrim = "" | string
         // csvElem = (prim | "csvElem"), + csvElem
         if (str.size() != 4) return true;
         if (!str.get(1).matches("\\d+")) return true;
         String[] s = str.get(3).split(String.valueOf(delimiter));
         if (s.length != 2) return true;
         if (!s[0].matches("\\d+")) return true;
         return false;
     }

     public boolean hasNextLine() {
         return fileReader.hasNextLine();
     }

     @Override
     public void close(){
         fileReader.close();
     }
 }
