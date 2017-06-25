package semexe.servlet;

/**
 * An operation request params consists of
 * - op: name of operation
 * - trail: where to apply that operation
 */
public class OperationRP extends RequestParams {
    public final String op;
    public final Trail trail;
    public final UpdateSpec updateSpec;

    public OperationRP(String op, UpdateSpec updateSpec) {
        this.op = op;
        this.trail = null;
        this.updateSpec = updateSpec;
    }

    public OperationRP(String op, OperationRP params) {
        super(params);
        this.op = op;
        this.trail = params.trail;
        this.updateSpec = params.updateSpec;
    }

    public OperationRP(RequestParams params, UpdateSpec updateSpec) throws ArgumentException {
        super(params);
        this.op = getReq("op");
        this.trail = new Trail(getReq("trail"));
        this.updateSpec = updateSpec;
    }
}
