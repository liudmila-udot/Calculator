package com.mila;

import java.util.Stack;

/**
 * https://leetcode.com/problems/evaluate-reverse-polish-notation/
 */
public class EvaluateReversePolishNotation {

    public static int evalRPN(String[] tokens) {
        Stack<Integer> values = new Stack<>();

        for (String token : tokens) {
            if (token.length() == 1 && !Character.isDigit(token.charAt(0))) {
                int op2 = values.pop();
                int op1 = values.pop();

                char operator = token.charAt(0);
                switch (operator) {
                    case '+':
                        values.push(op1 + op2);
                        break;
                    case '-':
                        values.push(op1 - op2);
                        break;
                    case '*':
                        values.push(op1 * op2);
                        break;
                    case '/':
                        values.push(op1 / op2);
                        break;
                    default:
                        break;
                }
            } else {
                values.push(Integer.parseInt(token));
            }
        }
        return values.size() == 1 ? values.get(0) : -1;
    }

    public static void main(String[] args) {
        System.out.println(evalRPN(new String[]{"10","6","9","3","+","-11","*","/","*","17","+","5","+"}));
    }
}
