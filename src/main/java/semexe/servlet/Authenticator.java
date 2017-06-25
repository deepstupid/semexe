package semexe.servlet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

public class Authenticator {
    private RootItem rootItem;

    public Authenticator(RootItem rootItem) {
        this.rootItem = rootItem;
    }

    private static Cookie getAuthCookie() {
        return new Cookie("word", "bleu");
    }

    // Get permissions: check the sent cookie
    public static Permissions getPermissions(HttpServletRequest request) {
        return new Permissions(true, new File("/")); // Allow everything

    /*Cookie gold = getAuthCookie();
    Cookie[] cookies = request.getCookies();
    if(cookies != null) {
      for(Cookie c : cookies) {
        WebState.verboseLogs("Got cookie (" + c.getName() + ", " + c.getValue() + ")");
        if(c.getName().equals(gold.getName()) && c.getValue().equals(gold.getValue()))
          return new Permissions(true, new File("/")); // Allow everything
      }
    }*/
        //return new Permissions(false, null); // No access
        //return new Permissions(false, rootView.getExecViewDB().getExecsDir()); // Allow only access to execs
    }

    // Give permissions: send a cookie
    public static boolean givePermissions(String value, HttpServletResponse response) {
        if (getAuthCookie().getValue().equals(value)) {
            response.addCookie(getAuthCookie());
            return true;
        }
        return false;
    }
}
