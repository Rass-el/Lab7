package server.csv;

public class CSVSyntaxException extends RuntimeException {

    public CSVSyntaxException (String msg) {
        super(msg);
    }

    public CSVSyntaxException (String msg, Throwable cause) {
        super(msg, cause);
    }

    public CSVSyntaxException (Throwable cause) {
        super(cause);
    }
}
