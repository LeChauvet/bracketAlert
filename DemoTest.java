package bracketAlert;

import java.util.HashMap;
import java.util.Map;

public class DemoTest {

    public static void main(String[] args) {
        String rule = "key1 > 200 || (key3 < 130 || (key3 < 100 || key4 == 98))";
        AlertRuleInterpreter interpreter = new AlertRuleInterpreter(rule);
        Map<String, Long> stats = new HashMap<>();
        stats.put("key1", 101l);
        stats.put("key3", 121l);
        stats.put("key4", 88l);
        boolean alert = interpreter.interpret(stats);
        System.out.println(alert);
    }
}
