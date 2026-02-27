import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class App {

    public static boolean chaosMode = false;
    public static ArrayList<App> allApps = new ArrayList<>();

    public JFrame mainWindow;
    private JTextField textPart;
    private int dx, dy;
    private boolean isChaotic = false;

    public App() {
        allApps.add(this);

        mainWindow = new JFrame();

        textPart = new JTextField();
        textPart.setEditable(false);
        textPart.setBounds(8, 10, 270, 70);
        textPart.setText("0");
        textPart.setHorizontalAlignment(JTextField.RIGHT);
        textPart.setFont(new Font("Arial", Font.BOLD, 32));
        mainWindow.setLayout(null);
        mainWindow.add(textPart);

        JPanel buttonPart = new JPanel();
        buttonPart.setLayout(new GridLayout(0, 4));
        buttonPart.setBounds(8, 90, 270, 235);

        Listener listener = new Listener(textPart);

        String[] labels = {
            "C", "/", "*", "-",
            "7", "8", "9", "+",
            "4", "5", "6", "=",
            "1", "2", "3", "."
        };

        for (String s : labels) {
            JButton b = new JButton(s);
            b.addActionListener(listener);
            buttonPart.add(b);
        }

        mainWindow.add(buttonPart);

        Random rand = new Random();
        int w = 300;
        int h = 370;

        if (chaosMode) {
            w = 260 + rand.nextInt(140);
            h = 320 + rand.nextInt(140);

            mainWindow.getContentPane().setBackground(
                new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256))
            );

            isChaotic = true;
        }

        mainWindow.setSize(w, h);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = rand.nextInt(Math.max(screen.width - w, 1));
        int y = rand.nextInt(Math.max(screen.height - h, 1));
        mainWindow.setLocation(x, y);
        mainWindow.setTitle("Normal Caclulator");
        mainWindow.setResizable(false);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setVisible(true);

        if (isChaotic) {
            dx = rand.nextInt(7) + 2;
            dy = rand.nextInt(7) + 2;
            if (rand.nextBoolean()) dx = -dx;
            if (rand.nextBoolean()) dy = -dy;
            startBouncing();
        }
    }

    private void startBouncing() {
        Timer timer = new Timer(20, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

                int x = mainWindow.getX();
                int y = mainWindow.getY();
                int w = mainWindow.getWidth();
                int h = mainWindow.getHeight();

                x += dx;
                y += dy;

                if (x < 0 || x + w > screen.width) dx = -dx;
                if (y < 0 || y + h > screen.height) dy = -dy;

                mainWindow.setLocation(x, y);
            }
        });
        timer.start();
    }

    public static void killAll() {
        for (App app : new ArrayList<>(allApps)) {
            app.mainWindow.dispose();
        }
        allApps.clear();
        chaosMode = false;
    }

    public static void main(String[] args) {
        new App();
    }
}
