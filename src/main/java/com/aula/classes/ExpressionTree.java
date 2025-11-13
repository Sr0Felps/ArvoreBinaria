package main.java.com.aula.classes;

import java.util.Stack;

public class ExpressionTree {

    private ExprNode root;

    private static class ExprNode {
        String data;
        ExprNode left, right;

        public ExprNode(String data) {
            this.data = data;
            this.left = this.right = null;
        }

        public boolean isOperand() {
            if (data == null || data.isEmpty()) return false;
            return Character.isLetter(data.charAt(0)) || Character.isDigit(data.charAt(0));
        }
    }

    public ExpressionTree() {
        this.root = null;
    }

    public void buildExpressionTree(String expression) {
        Stack<ExprNode> nos = new Stack<>();
        Stack<String> operadores = new Stack<>();

        expression = expression.replaceAll("\\s+", "");

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (c == '(') {
            } else if (c == '+' || c == '-' || c == '*' || c == '/') {
                operadores.push(String.valueOf(c));
            } else if (c == ')') {
                if (operadores.isEmpty() || nos.size() < 2) {
                    throw new IllegalArgumentException("Expressão mal formada!");
                }
                String op = operadores.pop();
                ExprNode right = nos.pop();
                ExprNode left = nos.pop();

                ExprNode newRoot = new ExprNode(op);
                newRoot.right = right;
                newRoot.left = left;

                nos.push(newRoot);
            } else {
                StringBuilder operand = new StringBuilder();
                int start = i;
                while (i < expression.length() && Character.isLetterOrDigit(expression.charAt(i))) {
                    operand.append(expression.charAt(i));
                    i++;
                }
                i--;
                nos.push(new ExprNode(operand.toString()));
            }
        }

        if (nos.size() != 1 || !operadores.isEmpty()) {
            throw new IllegalArgumentException("Expressão mal formada ou não totalmente parentizada!");
        }

        this.root = nos.pop();
    }

    public void printInfix() {
        System.out.print("Expressão Infixa: ");
        printInfixRec(this.root);
        System.out.println();
    }

    private void printInfixRec(ExprNode node) {
        if (node == null) return;

        boolean isOperator = !node.isOperand();

        if (isOperator && node != this.root) System.out.print("(");

        printInfixRec(node.left);

        System.out.print(node.data);

        printInfixRec(node.right);

        if (isOperator && node != this.root) System.out.print(")");
    }

    public int evaluateExpression() {
        return evaluateExpressionRec(this.root);
    }

    private int evaluateExpressionRec(ExprNode node) {
        if (node == null) throw new IllegalArgumentException("Nó de expressão nulo.");

        if (node.isOperand()) {
            try {
                return Integer.parseInt(node.data);
            } catch (NumberFormatException e) {
                throw new UnsupportedOperationException("Não é possível calcular. A expressão contém variável ou operando não-numérico: " + node.data);
            }
        }

        int leftVal = evaluateExpressionRec(node.left);
        int rightVal = evaluateExpressionRec(node.right);

        switch (node.data.charAt(0)) {
            case '+': return leftVal + rightVal;
            case '-': return leftVal - rightVal;
            case '*': return leftVal * rightVal;
            case '/':
                if (rightVal == 0) throw new ArithmeticException("Divisão por zero");
                return leftVal / rightVal;
            default: throw new IllegalArgumentException("Operador desconhecido: " + node.data);
        }
    }
}