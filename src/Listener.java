import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.util.Stack;
import java.util.Random;

public class Listener implements ActionListener {

    private StringBuilder input = new StringBuilder();
    private JTextField display;

    private static final String[] MESSAGES = {
        "I like Windows", "Don't hit = ;)", "Nice PC bro",
        "Oops", "Meow", "I love Math", "(-_-)"
    };

    public Listener(JTextField display) {
        this.display = display;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton btn = (JButton) e.getSource();
        String text = btn.getText();

        switch (text) {
            case "C":
                input.setLength(0);
                display.setText("0");
                break;

            case "=":
                calculate();
                break;

            default:
                input.append(text);
                display.setText(input.toString());
                break;
        }
    }

    private void calculate() {
        if (input.length() == 0) return;

        String expr = input.toString();
        Random rand = new Random();

        try {
            
            if (expr.equals("999")) {  // Kill all windows
                App.killAll();
                input.setLength(0);
                return;
            }

            if (expr.equals("67")) {  
                java.net.URL imgURL = getClass().getResource("/easteregg.png");
                if (imgURL != null) {
                    ImageIcon icon = new ImageIcon(imgURL);
                    JFrame imgWindow = new JFrame("Easter Egg!");
                    imgWindow.add(new JLabel(icon));
                    imgWindow.pack();
                    imgWindow.setLocationRelativeTo(null);
                    imgWindow.setVisible(true);
                }

                input.setLength(0);
                for (int i = 0; i < 67; i++) {
                    javax.swing.SwingUtilities.invokeLater(() -> new App());
                }

                display.setText("67");
                return;
            }

            if (expr.matches("-?\\d+")) {
                int count = Integer.parseInt(expr);

                display.setText(MESSAGES[rand.nextInt(MESSAGES.length)]);

                if (count > 0) {
                    if (!App.chaosMode) App.chaosMode = true;
                    input.setLength(0);
                    for (int i = 0; i < count; i++) {
                        javax.swing.SwingUtilities.invokeLater(() -> new App());
                    }
                } else if (count < 0) {
                    int toClose = Math.min(-count, App.allApps.size());
                    for (int i = 0; i < toClose; i++) {
                        App.allApps.get(0).mainWindow.dispose();
                        App.allApps.remove(0);
                    }
                    input.setLength(0);
                    display.setText("Closed " + toClose);
                }
                return;
            }

            double result = evaluate(expr);
            String resultStr = (result == (int) result)
                    ? Integer.toString((int) result)
                    : Double.toString(result);

            display.setText(resultStr);
            input.setLength(0);
            input.append(resultStr);

            if (!App.chaosMode) App.chaosMode = true;
            javax.swing.SwingUtilities.invokeLater(() -> new App());
            javax.swing.SwingUtilities.invokeLater(() -> new App());

        } catch (Exception e) {
            display.setText("ERR");
            input.setLength(0);
        }
    }

    private double evaluate(String expr) {
        Stack<Double> numbers = new Stack<>();
        Stack<Character> ops = new Stack<>();
        int i = 0;

        while (i < expr.length()) {
            char ch = expr.charAt(i);

            if (Character.isDigit(ch) || ch == '.') {
                StringBuilder sb = new StringBuilder();
                while (i < expr.length() && (Character.isDigit(expr.charAt(i)) || expr.charAt(i) == '.')) {
                    sb.append(expr.charAt(i++));
                }
                numbers.push(Double.parseDouble(sb.toString()));
                continue;
            }

            if ("+-*/".indexOf(ch) >= 0) {
                while (!ops.isEmpty() && precedence(ops.peek()) >= precedence(ch)) {
                    double b = numbers.pop();
                    double a = numbers.pop();
                    numbers.push(applyOp(a, b, ops.pop()));
                }
                ops.push(ch);
            }
            i++;
        }

        while (!ops.isEmpty()) {
            double b = numbers.pop();
            double a = numbers.pop();
            numbers.push(applyOp(a, b, ops.pop()));
        }

        return numbers.pop();
    }

    private int precedence(char op) {
        return (op == '+' || op == '-') ? 1 : (op == '*' || op == '/') ? 2 : 0;
    }

    private double applyOp(double a, double b, char op) {
        switch (op) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/': return (b == 0) ? 0 : a / b;
        }
        return 0;
    }
}
