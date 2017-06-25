package semexe.basic;

import java.io.*;

public class CharEncUtils {
    private static final int IN_BUFFER_SIZE = 32 * 1024;
    //private static String charEncoding = "ISO-8859-1";
    private static String charEncoding = "UTF-8";

    public static String getCharEncoding() {
        return charEncoding;
    }

    public static void setCharEncoding(String charEncoding) {
        if (StrUtils.isEmpty(charEncoding)) return;
        CharEncUtils.charEncoding = charEncoding;
        LogInfo.updateStdStreams();
    }

    public static BufferedReader getReader(InputStream in) throws IOException {
        return new BufferedReader(new InputStreamReader(in, getCharEncoding()), IN_BUFFER_SIZE);
    }

    public static PrintWriter getWriter(OutputStream out) throws IOException {
        return new PrintWriter(out, false);
    }
}
