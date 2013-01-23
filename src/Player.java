import java.awt.event.KeyEvent;

public class Player extends Item {
    public Player() {
        setRect(10, 10, 10, 20);
        setMidair(true);
        this.type = "player";
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_SPACE && !getMidair()) {
            setDy(-6);
            setMidair(true);
        }

        if (key == KeyEvent.VK_LEFT) {
            setDx(-2);
        }

        if (key == KeyEvent.VK_RIGHT) {
            setDx(2);
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            setDx(0);
        }

        if (key == KeyEvent.VK_RIGHT) {
            setDx(0);
        }
    }
}
