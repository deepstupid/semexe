package semexe.servlet;

import semexe.basic.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class ResponseStream implements ResponseObject {
    private InputStream rawData;

    public ResponseStream(InputStream rawData) {
        this.rawData = rawData;
    }

    public void dump(WebState state) throws IOException {
        IOUtils.copy(rawData, state.getOutputStream());
    }
}
