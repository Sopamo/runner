package de.sopamo.runner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Random;

public class Board extends JPanel implements ActionListener {

    private static Board instance;
    private Player player;
    private Timer timer;
    private double speed = 1;
    private ArrayList items;
    private ArrayList staticItems;
    private Building lastBuilding;

    public Board() {
        instance = this;
        player = new Player();

        items = new ArrayList();
        staticItems = new ArrayList();

        items.add(player);
        lastBuilding = new Building(0, 200, 1000, 300);
        staticItems.add(lastBuilding);

        setBackground(Color.BLACK);
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
        moveStaticItems();
        checkCollisions();
        createNewBuildings();
        collectGarbage();
        speed += 0.001;
        repaint();
    }

    private void checkCollisions() {
        // Check items with static items
        for (int i = 0; i < items.size(); ++i) {
            Item item = (Item) items.get(i);
            AffineTransform af = new AffineTransform();
            af.rotate(Math.toRadians(item.getRotation()), item.getCenterX(), item.getCenterY());
            Area itemArea = new Area(item);
            Area ra = itemArea.createTransformedArea(af);

            boolean intersects = false;
            for (int j = 0; j < staticItems.size(); ++j) {
                Item staticItem = (Item) staticItems.get(j);

                AffineTransform bf = new AffineTransform();
                bf.rotate(Math.toRadians(staticItem.getRotation()), staticItem.getCenterX(), staticItem.getCenterY());
                Area staticItemArea = new Area(staticItem);
                Area rb = staticItemArea.createTransformedArea(bf);
                // Check if they collide at all
                if (rb.intersects(ra.getBounds())) {
                    intersects = true;
                    if (staticItem.getRotation() != 0) {
                        //item.setY((staticItem.getPointOnTop(item.getCenterX()) - item.getWidth()/2));
                        item.setY((staticItem.getPointOnTop(player.getX() - staticItem.getX() -10 )-20));
                        item.setDx(item.getDx() + Board.getInstance().getGravity() * (staticItem.getRotation() / 180));
                        
                    } else {
                        item.setY(staticItem.getY() - item.getHeight());
                        
                    }
                    item.setMidair(false);
                }
            }
            if(!intersects) {
                item.setMidair(true);
            }
            
        }
    }

    private void moveStaticItems() {
        for (int j = 0; j < staticItems.size(); ++j) {
            Item staticItem = (Item) staticItems.get(j);
            staticItem.setX(staticItem.getX() - (1 * speed));
        }
    }

    private void createNewBuildings() {
        if (lastBuilding.getX() + lastBuilding.getWidth() < getWidth()) {
            addBuilding();
        }
    }

    private void addBuilding() {
        lastBuilding = new Building(0, 0, 0, 0);
        staticItems.add(lastBuilding);
    }

    private void collectGarbage() {
        for (int j = 0; j < staticItems.size(); ++j) {
            Item staticItem = (Item) staticItems.get(j);
            if(staticItem.getX() + staticItem.getWidth() < 0) {
                staticItems.remove(j);
            }
        }
    }

    public void paint(Graphics g) {
        super.paint(g);
        if (!isFocusOwner()) {
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
            g2d.setColor(Color.red);
            player.paint(g2d);
            g2d.setColor(Color.white);
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
        return;
        
    }

    public double getGravity() {
        return 9.81 / 100;
    }

    public static int getRandom(int start, int end) {
        if (start > end) {
            throw new IllegalArgumentException("Start cannot exceed End.");
        }
        Random random = new Random();
        //get the range, casting to long to avoid overflow problems
        long range = (long) end - (long) start + 1;
        // compute a fraction of the range, 0 <= frac < range
        long fraction = (long) (range * random.nextDouble());
        return (int) (fraction + start);
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
