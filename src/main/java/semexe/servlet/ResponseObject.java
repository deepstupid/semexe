package semexe.servlet;

import java.io.IOException;

public interface ResponseObject {
    void dump(WebState state) throws IOException;
}
