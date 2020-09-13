package bracketAlert;

import java.util.Map;

public class AlertRuleInterpreter {
    private Expression expression;
    public AlertRuleInterpreter(String ruleExpression) {
        this.expression = new StackExpression(ruleExpression);
    }
    public boolean interpret(Map<String, Long> stats) {
        return expression.interpret(stats);
    }
}
