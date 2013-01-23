import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;

public class Item extends Rectangle2D.Double {
    private double dy = 0;
    private double dx = 0;
    private double gravity;
    private boolean midair;
    protected String type;
    private double rotation = 0;
    private double m = 0;
    private double b = 0;

    /**
     * Reset x and y
     */
    public void move() {
        // Add speeds
        x += dx;
        y += dy;

        // Set gravity
        if (midair) {
            increaseGravity();
        } else {
            gravity = 0;
        }

        // Take gravity into account
        y += gravity;
    }

    public void setRotation(double r) {
        rotation = r;

        /* Recalculate m and b */

        // Get points
        AffineTransform bf = new AffineTransform();
        bf.rotate(Math.toRadians(getRotation()), getCenterX(), getCenterY());//rotate 45 degrees around bx, by
        Area staticItemArea = new Area(this);
        PathIterator pi = staticItemArea.getPathIterator(bf);
        double[] p1 = new double[6];
        double[] p2 = new double[6];
        pi.currentSegment(p1);
        pi.next();
        pi.next();
        pi.next();
        pi.currentSegment(p2);

        // Calculate y
        m = (p2[1] - p1[1]) / (p2[0] - p1[0]);
        b = p1[1] - (m * p1[0]);
    }

    public double getRotation() {
        return this.rotation;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getDx() {
        return this.dx;
    }

    public void setDx(double dx) {
        if (dx <= 5) {
            this.dx = dx;
        }
    }

    public double getDy() {
        return this.dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public void setMidair(boolean midair) {
        this.midair = midair;
    }

    public boolean getMidair() {
        return this.midair;
    }

    /**
     * Increase the gravity
     */
    public void increaseGravity() {
        gravity += Board.getInstance().getGravity();
        if (dy < 0) {
            dy += Board.getInstance().getGravity();
        }
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        if (this.rotation != 0)
            g2d.rotate(Math.toRadians(this.rotation), getX() + getWidth() / 2, getY() + getHeight() / 2);
        g2d.drawRect((int) getX(), (int) getY(), (int) getWidth(), (int) getHeight());
        if (this.rotation != 0)
            g2d.rotate(Math.toRadians(this.rotation) * -1, getX() + getWidth() / 2, getY() + getHeight() / 2);
    }

    public double getPointOnTop(double x) {
        return m * x + b;
    }
}
