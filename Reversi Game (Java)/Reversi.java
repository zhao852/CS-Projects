import java.util.Scanner;

/**
 * Reversi class
 *
 * Reversi class for Reversi game.
 *
 * Abstraction of and launcher for Reversi game
 *
 * @author Edward Zhao
 *
 * @version 10/19/18
 */

public class Reversi {

    public static boolean isEmpty(Point[] p) {
        /**
         * @param P The game board to be checked
         * @return true if there are valid moves; false if there are no valid moves
         */
        for (int i = 0; i < p.length; i++) {
            if (!p[i].equals(new Point(-1, -1))) {
                return false;
            }
        } return true;
    }

    //Check whether a Point is the Points array or not

    public static boolean contains(Point[] points, Point p) {
        /**
         * @param points The game board to be checked
         * @param p The point to be checked for validity
         * @return true if the board contains the point; false if the board does not contain the point
         */

        for (int i = 0; i < points.length; i++) {
            if (points[i].equals(p)) {
                return true;
            }
        } return false;
    }

    public static void start(Game g) {
        Scanner scanner = new Scanner(System.in);
        char c = 'B';
        while (!isEmpty(g.getPlaceableLocations('B', 'W')) &&
                !isEmpty(g.getPlaceableLocations('W', 'B'))) {
            if (c == 'B') {
                g.showPlaceableLocations(g.getPlaceableLocations('B', 'W'), 'B', 'W', g);

                System.out.println("Place move (Black): row then column:");

                String input;
                input = scanner.nextLine();

                while (input.equals("exit") || input.equals("")) {
                    if (input.equals("exit")) {
                        System.out.println("Exiting!");
                        return;
                    } else if (input.equals("")) {
                        System.out.println("Invalid move!\nPlace move (Black): row then column: ");
                        input = scanner.nextLine();
                        if (input.equals("exit")) {
                            System.out.println("Exiting!");
                            return;
                        }
                    }
                }
                Point p = new Point(-1, -1);
                p.x = Integer.parseInt(input.charAt(1) + "") - 1;
                p.y = Integer.parseInt(input.charAt(0) + "") - 1;

                while (!contains(g.getPlaceableLocations('B', 'W'), p)) {
                    System.out.println("Invalid move!\nPlace move (Black): row then column: ");
                    input = scanner.nextLine();
                    while (input.equals("exit") || input.equals("")) {
                        if (input.equals("exit")) {
                            System.out.println("Exiting!");
                            return;
                        } else if (input.equals("")) {
                            System.out.println("Invalid move!\nPlace move (Black): row then column: ");
                            input = scanner.nextLine();
                            if (input.equals("exit")) {
                                System.out.println("Exiting!");
                                return;
                            }
                        }
                    }
                    p = new Point(-1, -1);
                    p.x = Integer.parseInt(input.charAt(1) + "") - 1;
                    p.y = Integer.parseInt(input.charAt(0) + "") - 1;
                }
                if (contains(g.getPlaceableLocations('B', 'W'), p)) {
                    g.placeMove(p, 'B', 'W');
                    c = 'W';
                    g.updateScores();
                }
                System.out.println("Black: " + g.bScore + " White: " + g.wScore);
                g.showPlaceableLocations(g.getPlaceableLocations('W', 'B'), 'W', 'B', g);
            }
            if (isEmpty(g.getPlaceableLocations('W', 'B'))) {
                break;
            }
            if (c == 'W') {
                System.out.println("Place move (White): row then column:");

                String input;
                input = scanner.nextLine();
                while (input.equals("exit") || input.equals("")) {
                    if (input.equals("exit")) {
                        System.out.println("Exiting!");
                        return;
                    } else if (input.equals("")) {
                        System.out.println("Invalid move!\nPlace move (White): row then column: ");
                        input = scanner.nextLine();
                        if (input.equals("exit")) {
                            System.out.println("Exiting!");
                            return;
                        }
                    }
                }
                Point p = new Point(-1, -1);
                p.x = Integer.parseInt(input.charAt(1) + "") - 1;
                p.y = Integer.parseInt(input.charAt(0) + "") - 1;
                while (!contains(g.getPlaceableLocations('W', 'B'), p)) {
                    System.out.println("Invalid move!\nPlace move (White): row then column: ");
                    input = scanner.nextLine();
                    while (input.equals("exit") || input.equals("")) {
                        if (input.equals("exit")) {
                            System.out.println("Exiting!");
                            return;
                        } else if (input.equals("")) {
                            System.out.println("Invalid move!\nPlace move (White): row then column: ");
                            input = scanner.nextLine();
                            if (input.equals("exit")) {
                                System.out.println("Exiting!");
                                return;
                            }
                        }
                    }
                    p = new Point(-1, -1);
                    p.x = Integer.parseInt(input.charAt(1) + "") - 1;
                    p.y = Integer.parseInt(input.charAt(0) + "") - 1;
                }
                if (contains(g.getPlaceableLocations('W', 'B'), p)) {
                    g.placeMove(p, 'W', 'B');
                    c = 'B';
                    g.updateScores();
                }
                System.out.println("White: " + g.wScore + " Black: " + g.bScore);
            }
        }
        /**
         * @param g The Reversi game currently in play
         */

        if (g.gameResult(g.getPlaceableLocations('B', 'W'),
                g.getPlaceableLocations('B', 'W')) == 0) {
            System.out.println("It is a draw.");
        } else if (g.gameResult(g.getPlaceableLocations('B', 'W'),
                g.getPlaceableLocations('B', 'W')) == -1) {
            System.out.println("White wins: " + g.wScore + ":" + g.bScore);
        } else if (g.gameResult(g.getPlaceableLocations('B', 'W'),
                g.getPlaceableLocations('B', 'W')) == 1) {
            System.out.println("Black wins: " + g.bScore + ":" + g.wScore);
        }

    }

    public static void main(String[] args) {
        Game b = new Game();
        start(b);
    }
}