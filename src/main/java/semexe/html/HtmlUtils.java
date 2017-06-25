package semexe.html;

/**
 * Useful methods for constructing HTML elements.
 */
public class HtmlUtils {
    public static HtmlElement elem(String tag) {
        return new HtmlElement(tag);
    }

    public static HtmlElement html() {
        return elem("html");
    }

    public static HtmlElement title(String value) {
        return elem("title").child(value);
    }

    public static HtmlElement link() {
        return elem("link");
    }

    public static HtmlElement script() {
        return elem("script");
    }

    public static HtmlElement head() {
        return elem("head");
    }

    public static HtmlElement body() {
        return elem("body");
    }

    public static HtmlElement span() {
        return elem("span");
    }

    public static HtmlElement span(String value) {
        return elem("span").child(value);
    }

    public static HtmlElement span(HtmlElement value) {
        return elem("span").child(value);
    }

    public static HtmlElement div() {
        return elem("div");
    }

    public static HtmlElement table() {
        return elem("table");
    }

    public static HtmlElement tr() {
        return elem("tr");
    }

    public static HtmlElement td(String value) {
        return elem("td").child(value);
    }

    public static HtmlElement td(HtmlElement value) {
        return elem("td").child(value);
    }

    public static HtmlElement h1() {
        return elem("h1");
    }

    public static HtmlElement a() {
        return elem("a");
    }

    public static HtmlElement form() {
        return elem("form");
    }

    public static HtmlElement input() {
        return elem("input");
    }

    public HtmlElement text(String value) {
        return input().type("text").value(value);
    }

    public HtmlElement button(String value) {
        return input().type("submit").value(value);
    }

    public static HtmlElement ul() {
        return elem("ul");
    }

    public static HtmlElement ol() {
        return elem("ol");
    }

    public static HtmlElement li() {
        return elem("li");
    }

    public static HtmlElement img() {
        return elem("img");
    }

    public static HtmlElement b(String value) {
        return elem("b").child(value);
    }
}
