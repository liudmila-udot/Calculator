package com.mila;

import java.util.*;
import java.util.function.BiFunction;

/**
 * https://leetcode.com/problems/basic-calculator/
 * https://leetcode.com/problems/basic-calculator-ii/
 * https://leetcode.com/problems/basic-calculator-iii/
 */
public class CalculatorShuntingYard {

    public static final Map<Character, Integer> OPERATOR_TO_PRECEDENCE = new HashMap<>();
    public static final Map<Character, BiFunction<Integer, Integer, Integer>> OPERATOR_TO_OPERATION = new HashMap<>();

    static {
        OPERATOR_TO_PRECEDENCE.put('+', 1);
        OPERATOR_TO_PRECEDENCE.put('-', 1);
        OPERATOR_TO_PRECEDENCE.put('*', 2);
        OPERATOR_TO_PRECEDENCE.put('/', 2);

        OPERATOR_TO_OPERATION.put('+', (a, b) -> a + b);
        OPERATOR_TO_OPERATION.put('-', (a, b) -> a - b);
        OPERATOR_TO_OPERATION.put('*', (a, b) -> a * b);
        OPERATOR_TO_OPERATION.put('/', (a, b) -> a / b);
    }

    public static int calculate(String s) {
        List<String> shuntingYard = shuntingYard(s);
        System.out.println(shuntingYard);
        return evaluateRpn(shuntingYard);
    }

    public static List<String> shuntingYard(String s) {
        List<String> rpn = new ArrayList<>();
        Stack<Character> operators = new Stack<>();
        int currentNumber = 0;
        // to calculate expressions like -(2+3)
        // the expression will be transformed to 0-(2+3)
        boolean checkUnary = true;
        for (int i = 0; i <= s.length() - 1; i++) {
            if (s.charAt(i) == ' ') {
                continue;
            }
            if (checkUnary) {
                checkUnary = false;
                if (s.charAt(i) == '+' || s.charAt(i) == '-') {
                    rpn.add("0");
                }
            }
            if (Character.isDigit(s.charAt(i))) {
                currentNumber = 10 * currentNumber + s.charAt(i) - '0';
                if (i == s.length() - 1 || !Character.isDigit(s.charAt(i + 1))) {
                    rpn.add(String.valueOf(currentNumber));
                    currentNumber = 0;
                }
            } else {
                char operator = s.charAt(i);
                switch (operator) {
                    case '(':
                        checkUnary = true;
                        operators.push(operator);
                        break;
                    case ')':
                        while (!(operators.peek() == '(')) {
                            rpn.add(String.valueOf(operators.pop()));
                        }
                        operators.pop();
                        break;
                    default:
                        if (operators.isEmpty() || operators.peek() == '(') {
                            operators.push(operator);
                        } else if (OPERATOR_TO_PRECEDENCE.get(operator) > OPERATOR_TO_PRECEDENCE.get(operators.peek())) {
                            operators.push(operator);
                        } else {
                            while (!operators.isEmpty() && operators.peek() != '(' && OPERATOR_TO_PRECEDENCE.get(operator) <= OPERATOR_TO_PRECEDENCE.get(operators.peek())) {
                                rpn.add(String.valueOf(operators.pop()));
                            }
                            operators.push(operator);
                        }
                        break;
                }
            }
        }
        while (!operators.isEmpty()) {
            rpn.add(String.valueOf(operators.pop()));
        }
        return rpn;
    }

    public static int evaluateRpn(List<String> rpn) {
        Stack<Integer> values = new Stack<>();

        for (String token : rpn) {
            if (token.length() == 1 && !Character.isDigit(token.charAt(0))) {
                int operand2 = values.pop();
                int operand1 = values.pop();

                char operator = token.charAt(0);
                values.push(OPERATOR_TO_OPERATION.get(operator).apply(operand1, operand2));
            } else {
                values.push(Integer.parseInt(token));
            }
        }
        return values.size() == 1 ? values.get(0) : -1;
    }

    /**
     * https://www.geeksforgeeks.org/expression-evaluation/
     *
     * @param s expression to parse
     */
    public static int calculateOneTraversal(String s) {
        Stack<Integer> values = new Stack<>();
        Stack<Character> operators = new Stack<>();
        int currentNumber = 0;
        // to calculate expressions like -(2+3)
        // the expression will be transformed to 0-(2+3)
        boolean checkUnary = true;
        for (int i = 0; i <= s.length() - 1; i++) {
            if (s.charAt(i) == ' ') {
                continue;
            }
            if (checkUnary) {
                checkUnary = false;
                if (s.charAt(i) == '+' || s.charAt(i) == '-') {
                    values.add(0);
                }
            }
            if (Character.isDigit(s.charAt(i))) {
                currentNumber = 10 * currentNumber + s.charAt(i) - '0';
                if (i == s.length() - 1 || !Character.isDigit(s.charAt(i + 1))) {
                    values.add(currentNumber);
                    currentNumber = 0;
                }
            } else {
                char operator = s.charAt(i);
                switch (operator) {
                    case '(':
                        checkUnary = true;
                        operators.push(operator);
                        break;
                    case ')':
                        while (!(operators.peek() == '(')) {
                            applyOperation(values, operators);
                        }
                        operators.pop();
                        break;
                    default:
                        if (operators.isEmpty() || operators.peek() == '(') {
                            operators.push(operator);
                        } else if (OPERATOR_TO_PRECEDENCE.get(operator) > OPERATOR_TO_PRECEDENCE.get(operators.peek())) {
                            operators.push(operator);
                        } else {
                            while (!operators.isEmpty() && operators.peek() != '(' && OPERATOR_TO_PRECEDENCE.get(operator) <= OPERATOR_TO_PRECEDENCE.get(operators.peek())) {
                                applyOperation(values, operators);
                            }
                            operators.push(operator);
                        }
                        break;
                }
            }
        }
        while (!operators.isEmpty()) {
            applyOperation(values, operators);
        }
        return values.get(0);
    }

    private static void applyOperation(Stack<Integer> values, Stack<Character> operators) {
        int operand2 = values.pop();
        int operand1 = values.pop();
        values.add(OPERATOR_TO_OPERATION.get(operators.pop()).apply(operand1, operand2));
    }

    public static void main(String[] args) {
        System.out.println(calculate("2*(5+5*2)/3+(6/2+8)"));
        System.out.println(calculateOneTraversal("2*(5+5*2)/3+(6/2+8)"));
    }
}
