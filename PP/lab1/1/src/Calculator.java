import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.util.*;

class TreeNode {
    String value;
    TreeNode left, right;

    TreeNode(String value) {
        this.value = value;
        this.left = this.right = null;
    }
}

public class Calculator extends JFrame {
    JButton digits[] = {
            new JButton(" 0 "),
            new JButton(" 1 "),
            new JButton(" 2 "),
            new JButton(" 3 "),
            new JButton(" 4 "),
            new JButton(" 5 "),
            new JButton(" 6 "),
            new JButton(" 7 "),
            new JButton(" 8 "),
            new JButton(" 9 ")
    };

    JButton operators[] = {
            new JButton(" + "),
            new JButton(" - "),
            new JButton(" * "),
            new JButton(" / "),
            new JButton("("),       //added open paranthesis
            new JButton(")"),       //added closed paranthesis
            new JButton(" = "),
            new JButton(" C ")
    };

    String oper_values[] = {"+", "-", "*", "/", "(", ")", "=", ""};

    char operator;

    JTextArea area = new JTextArea(3, 5);

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        calculator.setSize(250, 240);
        calculator.setTitle(" Java-Calc, PP Lab1 ");
        calculator.setResizable(false);
        calculator.setVisible(true);
        calculator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public Calculator() {
        add(new JScrollPane(area), BorderLayout.NORTH);
        JPanel buttonpanel = new JPanel();
        buttonpanel.setLayout(new FlowLayout());

        for (int i=0;i<10;i++)
            buttonpanel.add(digits[i]);

        for (int i=0;i<8;i++)
            buttonpanel.add(operators[i]);

        add(buttonpanel, BorderLayout.CENTER);
        area.setForeground(Color.BLACK);
        area.setBackground(Color.WHITE);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);

        for (int i=0;i<10;i++) {
            int finalI = i;
            digits[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    area.append(Integer.toString(finalI));
                }
            });
        }

        for (int i=0;i<8;i++){
            int finalI = i;
            operators[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    if (finalI == 7)
                        area.setText("");
                    else
                    if (finalI == 6) {
                        String expression = area.getText();
                        try {
                            TreeNode root = buildTree(expression);

                            // Convert the tree to Postfix notation
                            String postfixNotation = toPostfix(root).trim();

                            // Build the tree from the postfix expression
                            TreeNode root_postfix = buildTreeFromPostfix(postfixNotation);

                            // Evaluate the tree and get the result
                            double result = evaluateTree(root_postfix);

                            //double result = evaluate(root);
                            area.append(" = " + result);

                        } catch (Exception e) {
                            area.setText(" !!!Probleme!!! ");
                        }
                    }
                    else {
                        area.append(oper_values[finalI]);
                        operator = oper_values[finalI].charAt(0);
                    }
                }
            });
        }
    }

    // Precedence of operators for infix to tree conversion
    private static int precedence(String op) {
        if (op.equals("+") || op.equals("-")) return 1;
        if (op.equals("*") || op.equals("/")) return 2;
        return 0;
    }

    // Function to build the expression tree
    private static TreeNode buildTree(String expression) {
        Stack<TreeNode> operandStack = new Stack<>();
        Stack<String> operatorStack = new Stack<>();


        String[] tokens = expression.split("(?=[-+*/()])|(?<=[-+*/()])");

        for (String token : tokens) {
            token = token.trim();

            if (token.isEmpty()) continue;

            if (isOperand(token)) {
                operandStack.push(new TreeNode(token));
            } else if (token.equals("(")) {
                operatorStack.push(token);
            } else if (token.equals(")")) {
                while (!operatorStack.isEmpty() && !operatorStack.peek().equals("(")) {
                    operandStack.push(createTreeNode(operatorStack.pop(), operandStack.pop(), operandStack.pop()));
                }
                operatorStack.pop();  // Remove the '(' from the stack
            } else {
                while (!operatorStack.isEmpty() && precedence(token) <= precedence(operatorStack.peek())) {
                    operandStack.push(createTreeNode(operatorStack.pop(), operandStack.pop(), operandStack.pop()));
                }
                operatorStack.push(token);
            }
        }

        while (!operatorStack.isEmpty()) {
            operandStack.push(createTreeNode(operatorStack.pop(), operandStack.pop(), operandStack.pop()));
        }

        return operandStack.pop();
    }

    // Helper function to create an operator node
    private static TreeNode createTreeNode(String operator, TreeNode right, TreeNode left) {
        TreeNode node = new TreeNode(operator);
        node.left = left;
        node.right = right;
        return node;
    }

    // Function to check if a string is a number (operand)
    private static boolean isOperand(String token) {
        try {
            Double.parseDouble(token); // Try to parse as number
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Function to perform postorder traversal to generate Postfix notation
    private static String toPostfix(TreeNode root) {
        if (root == null) return "";
        return toPostfix(root.left) + toPostfix(root.right) + root.value + " ";
    }

    // Function to evaluate an expression tree
    public static double evaluateTree(TreeNode root) {
        if (root == null) {
            return 0;
        }

        // If it's a leaf node, it must be a number
        if (root.left == null && root.right == null) {
            return Double.parseDouble(root.value);
        }

        // Evaluate the left and right subtrees
        double leftValue = evaluateTree(root.left);
        double rightValue = evaluateTree(root.right);

        // Apply the operator at the current node
        return applyOperator(leftValue, rightValue, root.value);
    }

    // Function to apply an operator to two operands
    private static double applyOperator(double leftOperand, double rightOperand, String operator) {
        switch (operator) {
            case "+":
                return leftOperand + rightOperand;
            case "-":
                return leftOperand - rightOperand;
            case "*":
                return leftOperand * rightOperand;
            case "/":
                if (rightOperand == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                return leftOperand / rightOperand;
            default:
                throw new UnsupportedOperationException("Operator " + operator + " not supported");
        }
    }

    // Function to build the expression tree from postfix expression
    public static TreeNode buildTreeFromPostfix(String postfixExpression) {
        Stack<TreeNode> stack = new Stack<>();

        // Split the postfix expression into tokens
        String[] tokens = postfixExpression.split(" ");

        for (String token : tokens) {
            token = token.trim();

            if (isOperand(token)) {
                // If the token is a number, create a leaf node and push it to the stack
                stack.push(new TreeNode(token));
            } else if (isOperator(token)) {
                // If the token is an operator, pop two operands from the stack
                TreeNode rightOperand = stack.pop();
                TreeNode leftOperand = stack.pop();

                // Create an internal node with the operator, and push it to the stack
                TreeNode operatorNode = new TreeNode(token);
                operatorNode.left = leftOperand;
                operatorNode.right = rightOperand;

                stack.push(operatorNode);
            }
        }

        // The final element in the stack is the root of the expression tree
        return stack.pop();
    }

    // Helper function to check if a string is an operator
    private static boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/");
    }

}