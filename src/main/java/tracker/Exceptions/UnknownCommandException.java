package main.java.tracker.Exceptions;

public class UnknownCommandException extends Exception{
    public UnknownCommandException(String message) {
        super(message);
    }
}
