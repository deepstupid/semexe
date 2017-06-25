package semexe.servlet;

import java.io.File;
import java.io.IOException;

public class FileException extends MyException {
    private static final long serialVersionUID = 42;
    private File file;
    private IOException exception;

    public FileException(IOException exception) {
        this.file = null;
        this.exception = exception;
    }

    public FileException(File file, IOException exception) {
        this.file = file;
        this.exception = exception;
    }

    public FileException(File file, String msg) {
        this.file = file;
        this.exception = new IOException(msg);
    }

    public FileException(String file, IOException exception) {
        this.file = new File(file);
        this.exception = exception;
    }
    public FileException(String file, String msg) {
        this.file = new File(file);
        this.exception = new IOException(msg);
    }
}
