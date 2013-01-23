import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.util.ArrayList;

public class Board extends JPanel implements ActionListener {

    private static Board instance;
    private Player player;
    private Timer timer;
    private ArrayList items;
    private ArrayList staticItems;

    public Board() {
        instance = this;
        player = new Player();

        items = new ArrayList();
        staticItems = new ArrayList();

        items.add(player);
        Item building = new Item();
        building.setRect(-150.0, 300.0, 1000.0, 100.0);
        building.setRotation(10);
        staticItems.add(building);

        setBackground(Color.DARK_GRAY);
        setDoubleBuffered(true);
        setFocusable(true);
        setSize(Main.getInstance().getWidth(), Main.getInstance().getHeight());
        addKeyListener(new TAdapter());

        timer = new Timer(5, this);
        timer.start();
    }

    public static Board getInstance() {
        return instance;
    }

    public void addNotify() {
        super.addNotify();
    }

    public void actionPerformed(ActionEvent e) {

        player.move();
        checkCollisions();
        repaint();
    }

    public void checkCollisions() {
        // Check items with static items
        for (int i = 0; i < items.size(); ++i) {
            Item item = (Item) items.get(i);
            AffineTransform af = new AffineTransform();
            af.rotate(Math.toRadians(item.getRotation()), item.getCenterX(), item.getCenterY());//rotate 45 degrees around ax, ay
            Area itemArea = new Area(item);
            Area ra = itemArea.createTransformedArea(af);//ra is the rotated a, a is unchanged

            for (int j = 0; j < staticItems.size(); ++j) {
                Item staticItem = (Item) staticItems.get(j);

                AffineTransform bf = new AffineTransform();
                bf.rotate(Math.toRadians(staticItem.getRotation()), staticItem.getCenterX(), staticItem.getCenterY());//rotate 45 degrees around bx, by
                Area staticItemArea = new Area(staticItem);
                Area rb = staticItemArea.createTransformedArea(bf);//rb is the rotated b, b is unchanged
                // Check if they collide at all
                if (rb.intersects(ra.getBounds())) {
                    item.setMidair(false);
                    item.setY((staticItem.getPointOnTop(item.getCenterX()) - item.getHeight()));
                    item.setDx(item.getDx()+Board.getInstance().getGravity() * (staticItem.getRotation() / 180));
                } else {
                    item.setMidair(true);
                }
            }
        }
    }

    public void paint(Graphics g) {
        super.paint(g);
        if(!isFocusOwner()) {
            requestFocus();
        }
        Font small = new Font("VT323-Regular", Font.PLAIN, 22);
        FontMetrics metr = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        if (true) {

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            player.paint(g2d);
            for (int i = 0; i < staticItems.size(); ++i) {
                Item item = (Item) staticItems.get(i);
                item.paint(g2d);
            }

            //g2d.drawString("Position: " + (int)player.getX() + " - " + (int)player.getY() + " - DY: " + (int)player.getDY() + " - DX: " + (int)player.getDX() + " - Speed: " + speed, 5, 15);
            //g2d.drawString("Points: " + (int)points, getWidth()-150, 30);

        } else {
            String msg = "Game Over - You scored 0 Points - ";
            //g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
            //doPaint = false;
        }

        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    public double getGravity() {
        return 9.81 / 100;
    }

    private class TAdapter extends KeyAdapter {

        public void keyReleased(KeyEvent e) {
            player.keyReleased(e);
        }

        public void keyPressed(KeyEvent e) {
            player.keyPressed(e);
        }
    }
}
