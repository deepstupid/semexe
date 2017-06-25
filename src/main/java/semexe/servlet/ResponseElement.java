package semexe.servlet;

import semexe.html.HtmlElement;

import java.io.IOException;

public class ResponseElement implements ResponseObject {
    private HtmlElement element;

    public ResponseElement(HtmlElement element) {
        this.element = element;
    }

    public void dump(WebState state) throws IOException {
        state.setHtmlOutput();
        state.getWriter().println(element.toString());
    }
}
