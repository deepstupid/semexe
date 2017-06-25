package semexe.record;

import semexe.basic.Exceptions;
import semexe.basic.Utils;

public class NumMatcher implements Matcher {
    private Op op;

    private String targetStr;
    private double target;
    public NumMatcher(String opStr, String targetStr) {
        this.op = getOp(opStr);
        this.targetStr = targetStr;
        if (!VarBindingList.containsVar(targetStr))
            this.target = convert(targetStr);
        else
            this.target = Double.NaN;
    }

    private static Op getOp(String s) {
        if (s.equals("==")) return Op.equals;
        if (s.equals("<")) return Op.lessThan;
        if (s.equals(">")) return Op.greaterThan;
        if (s.equals("<=")) return Op.lessThanEquals;
        if (s.equals(">=")) return Op.greaterThanEquals;
        throw Exceptions.unknownCase;
    }

    private static double convert(String targetStr) {
        return Utils.parseDoubleEasy(targetStr);
    }

    public boolean matches(String s, VarBindingList bindings) {
        double x = Double.parseDouble(s);
        double target = this.target;
        if (Double.isNaN(target))
            target = convert(bindings.substitute(targetStr));
        switch (op) {
            case equals:
                return Math.abs(x - target) < 1e-10;
            case lessThan:
                return x < target;
            case greaterThan:
                return x > target;
            case lessThanEquals:
                return x <= target;
            case greaterThanEquals:
                return x >= target;
            default:
                throw new RuntimeException("Unknown case");
        }
    }

    public String toString() {
        return op + "(" + targetStr + ')';
    }

    public enum Op {equals, lessThan, greaterThan, lessThanEquals, greaterThanEquals}
}
