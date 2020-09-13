package bracketAlert;

import java.util.*;

public class StackExpression implements Expression {

    private List<String> expList;

    public StackExpression(String ruleExpression) {
        expList = ruleToExpList(ruleExpression);
    }

    @Override
    public boolean interpret(Map<String, Long> stats) {
        Deque<Boolean> boolStack = new LinkedList<>();
        Deque<String> logicStack = new LinkedList<>();
        logicStack.push("#");

        for (String strExpr : expList) {
            if (strExpr.contains(">")) {
                Expression expression = new GreaterExpression(strExpr);
                boolStack.push(expression.interpret(stats));
            } else if (strExpr.contains("<")) {
                Expression expression = new LessExpression(strExpr);
                boolStack.push(expression.interpret(stats));
            } else if (strExpr.contains("==")) {
                Expression expression = new EqualExpression(strExpr);
                boolStack.push(expression.interpret(stats));
            } else {
                String top = logicStack.getFirst();
                int icp = icp(strExpr);
                int isp = isp(top);
                if (icp < isp) {
                    Boolean b1 = boolStack.pop();
                    Boolean b2 = boolStack.pop();
                    logicStack.pop();
                    boolean b3;
                    switch (top) {
                        case "||":
                            b3 = b1 || b2;
                            break;
                        case "&&":
                            b3 = b1 && b2;
                            break;
                        default:
                            throw new RuntimeException("invalid logic: \"" + top + "\"");
                    }
                    boolStack.push(b3);
                } else if (icp == isp) {
                    logicStack.pop();
                } else {
                    logicStack.push(strExpr);
                }
            }
        }
        return boolStack.pop();
    }

    private List<String> ruleToExpList(String ruleExpression) {
        List<String> expList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        char[] chars = ruleExpression.toCharArray();
        int i = 0;
        while (i < chars.length) {
            char c = chars[i];
            if (c == '(' || c == ')') {
                if (!sb.toString().trim().isEmpty()) {
                    expList.add(sb.toString());
                    sb.setLength(0);
                }
                expList.add(String.valueOf(c));
                i++;
            } else if (c == '&') {
                if (!sb.toString().trim().isEmpty()) {
                    expList.add(sb.toString());
                    sb.setLength(0);
                }
                expList.add("&&");
                i += 2;
            } else if (c == '|') {
                if (!sb.toString().trim().isEmpty()) {
                    expList.add(sb.toString());
                    sb.setLength(0);
                }
                expList.add("||");
                i += 2;
            } else {
                sb.append(c);
                i++;
            }
        }
        if (!sb.toString().trim().isEmpty()) {
            expList.add(sb.toString());
            sb.setLength(0);
        }
        expList.add("#");
        return expList;
    }

    // 栈内优先级
    private int isp(String ch) {
        switch (ch) {
            case "#":
                return 0;
            case "(":
                return 1;
            case "||":
                return 3;
            case "&&":
                return 5;
            case ")":
                return 8;
            default:
                throw new RuntimeException("invalid op: \"" + ch + "\"");
        }
    }

    // 栈外优先级
    private int icp(String ch) {
        switch (ch) {
            case "#":
                return 0;
            case "(":
                return 8;
            case "||":
                return 2;
            case "&&":
                return 4;
            case ")":
                return 1;
            default:
                throw new RuntimeException("invalid op: \"" + ch + "\"");
        }
    }

}
