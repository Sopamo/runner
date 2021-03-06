package de.sopamo.runner;

public class Building extends Item {

    public Building(double x, double y, double width, double height) {
        if (x == 0 && y == 0 && width == 0 && height == 0) {
            x = Board.getInstance().getWidth() - 1;
            y = Board.getRandom(200, 250);
            width = Board.getRandom(500, 1000);
            height = 300;
        }
        this.setRect(x, y, width, height);
        if (Board.getRandom(0,5) == 4) {
            this.setRotation(Board.getRandom(5,10));
        }
    }
}
