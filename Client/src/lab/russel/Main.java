package lab.russel;

public class Main {


    public static void main(String[] args) {
        try {

            if (args.length < 3) {
                System.out.println("Usage: java lab.russel.Main <FileName.txt> <host>");
                System.exit(1);
            }
            Client client = new Client(args);
            client.start();
        }catch (Throwable t){
            System.out.println("Неизвестная ошибка");
        }
    }
}
