/**
 * Point class
 *
 * Point class for Reversi game.
 *
 * @author Edward Zhao
 *
 * @version 10/19/18
 */

public class Point {

    public int x;
    public int y;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "[" + x + ", " + y + "]";
    }

    public boolean equals(Object o) {
        return o instanceof Point && ((Point)o).x == this.x && ((Point)o).y == this.y;
    }
}