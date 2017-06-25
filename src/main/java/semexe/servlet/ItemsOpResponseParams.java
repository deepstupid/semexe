package semexe.servlet;

import java.util.ArrayList;
import java.util.List;

public class ItemsOpResponseParams extends ResponseParams {
    private List<String> successItems = new ArrayList<>();
    private List<String> failedItems = new ArrayList<>();
    private String op;

    public ItemsOpResponseParams(String op) {
        this.op = op;
    }

    public void setSuccess(String item, String msg) {
        successItems.add(item);
        put("msg." + item, msg);
    }

    public void setFailed(String item, String msg) {
        failedItems.add(item);
        put("msg." + item, msg);
    }

    public ResponseParams finish() {
        put("success", String.valueOf(failedItems.size() == 0));
        if (successItems.size() > 0) put("successItems", successItems);
        if (failedItems.size() > 0) put("failedItems", failedItems);
        setMsg("Operation " + op + " on " + (successItems.size() + failedItems.size()) + " items");
        return this;
    }
}
