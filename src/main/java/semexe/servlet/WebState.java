package semexe.servlet;

import semexe.basic.CharEncUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class WebState {
    public PrintWriter out;  // Output to the response.
    public RequestParams params;
    public HttpServletRequest request;
    public HttpServletResponse response;
    public HttpSession session;

    public WebState(HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.request = request;
        this.response = response;
        this.session = request.getSession(true);
        this.params = new RequestParams(request);
        ServletLogInfo.verboseLogs("============================================================");
    }

    public OutputStream getOutputStream() throws IOException {
        return response.getOutputStream();
    }

    public PrintWriter getWriter() throws IOException {
        return response.getWriter();
    }

    // Set content-type.
    public void setRawOutput() {
    }

    public void setPlainOutput() {
        response.setContentType("text/plain; charset=" + CharEncUtils.getCharEncoding());
    }

    public void setHtmlOutput() {
        response.setContentType("text/html; charset=" + CharEncUtils.getCharEncoding());
    }

    // Input/output.
    public void initOutput() throws IOException {
        setHtmlOutput();
        this.out = getWriter();
    }

    public void endOutput() {
        out.close();
    }
}
