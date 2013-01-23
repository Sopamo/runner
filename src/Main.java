import javax.swing.*;

public class Main extends JFrame {

    private static int width = 1000;
    private static int height = 400;
    private static Main instance;
    private Board activeBoard;

    public Main() {
        instance = this;

        activeBoard = new Board();
        add(activeBoard);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(Main.width, Main.height);
        setLocationRelativeTo(null);

        setTitle("Runner");

        setResizable(false);
        setVisible(true);
    }

    public static Main getInstance() {
        return instance;
    }

    public int getWidth() {
        return Main.width;
    }

    public int getHeight() {
        return Main.height;
    }

    public static void main(String[] args) {
        new Main();
    }
}
