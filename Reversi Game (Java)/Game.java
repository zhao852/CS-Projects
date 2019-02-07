/**
 * Game class
 *
 * Game class for Reversi game.
 *
 * Implementation of the game mechanics in Reversi
 *
 * @author Edward Zhao
 *
 * @version 10/19/18
 */

public class Game {


    private final char[][] board;
    public int wScore;
    public int bScore;
    public int remaining;
    private final char[] boardX = new char[]{'1', '2', '3', '4', '5', '6', '7', '8'};

    public Game() {
        board = new char[][]{
                {'_', '_', '_', '_', '_', '_', '_', '_'},
                {'_', '_', '_', '_', '_', '_', '_', '_'},
                {'_', '_', '_', '_', '_', '_', '_', '_'},
                {'_', '_', '_', 'W', 'B', '_', '_', '_'},
                {'_', '_', '_', 'B', 'W', '_', '_', '_'},
                {'_', '_', '_', '_', '_', '_', '_', '_'},
                {'_', '_', '_', '_', '_', '_', '_', '_'},
                {'_', '_', '_', '_', '_', '_', '_', '_'}};
    }

    public void displayBoard(Game b) {
        /**
         * @param b The game board to be displayed
         */
        System.out.print("\n  ");
        for (int i = 0; i < 8; i++) {
            System.out.print(boardX[i] + " ");
        }
        System.out.println();
        for (int i = 0; i < 8; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < 8; j++)
                System.out.print(board[i][j] + " ");
            System.out.println();
        }
        System.out.println();
    }

    //There are three cases: black win = -1, white win = 1, draw = 0

    public int gameResult(Point[] whitePlaceableLocations, Point[] blackPlaceableLocations) {
        /**
         * @param whitePlaceableLocations Array of possible moves for white
         * @param blackPlaceableLocations Array of possible moves for white
         * @return The integer corresponding to the game result: -1 for black win, 0 for draw, 1 for white win
         */

        updateScores();
        if (wScore > bScore) {
            return -1;
        } else if (bScore > wScore) {
            return 1;
        } else {
            return 0;
        }
    }

    private static boolean isInBounds(int x, int y) {
        return (x >= 0 && x <= 7 && y >= 0 && y <= 7);
    }

    public Point[] getPlaceableLocations(char player, char opponent) {
        /**
         * @param player Current player
         * @param opponent player's opponent
         * @return placeablePositions array, with corresponding point for a valid
         * location and (-1,-1) for an invalid location
         */

        int a = 0;
        Point[] placeablePositions = new Point[64];

        for (int i = 0; i < placeablePositions.length; i++) {
            placeablePositions[i] = new Point(-1, -1);
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                for (int k = 0; k < board[i].length; k++) {
                    if (isInBounds(i, j + k + 1)) {
                        if (board[i][j] == player && board[i][j + k] == opponent) {
                            if (board[i][j + k + 1] == '_') {
                                placeablePositions[a] = new Point(j + k + 1, i);
                                a++;
                            }
                        }
                    } else {
                        break;
                    }
                }
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                for (int k = 0; k < board[i].length; k++) {
                    if (isInBounds(i, j - k - 1)) {
                        if (board[i][j] == player && board[i][j - k] == opponent) {
                            if (board[i][j - k - 1] == '_') {
                                placeablePositions[a] = new Point(j - k - 1, i);
                                a++;
                            }
                        }
                    } else {
                        break;
                    }
                }
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                for (int k = 0; k < board[i].length; k++) {
                    if (isInBounds(i + k + 1, j)) {
                        if (board[i][j] == player && board[i + k][j] == opponent) {
                            if (board[i + k + 1][j] == '_') {
                                placeablePositions[a] = new Point(j, i + k + 1);
                                a++;
                            }
                        }
                    } else {
                        break;
                    }
                }
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                for (int k = 0; k < board[i].length; k++) {
                    if (isInBounds(i - k - 1, j)) {
                        if (board[i][j] == player && board[i - k][j] == opponent) {
                            if (board[i - k - 1][j] == '_') {
                                placeablePositions[a] = new Point(j, i - k - 1);
                                a++;
                            }
                        }
                    } else {
                        break;
                    }
                }
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                for (int k = 0; k < board[i].length; k++) {
                    if (isInBounds(i + k + 1, j + k + 1)) {
                        if (board[i][j] == player && board[i + k][j + k] == opponent) {
                            if (board[i + k + 1][j + k + 1] == '_') {
                                placeablePositions[a] = new Point(j + k + 1, i + k + 1);
                                a++;
                            }
                        }
                    } else {
                        break;
                    }
                }
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                for (int k = 0; k < board[i].length; k++) {
                    if (isInBounds(i - k - 1, j - k - 1)) {
                        if (board[i][j] == player && board[i - k][j - k] == opponent) {
                            if (board[i - k - 1][j - k - 1] == '_') {
                                placeablePositions[a] = new Point(j - k - 1, i - k - 1);
                                a++;
                            }
                        }
                    } else {
                        break;
                    }
                }
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                for (int k = 0; k < board[i].length; k++) {
                    if (isInBounds(i - k - 1, j + k + 1)) {
                        if (board[i][j] == player && board[i - k][j + k] == opponent) {
                            if (board[i - k - 1][j + k + 1] == '_') {
                                placeablePositions[a] = new Point(j + k + 1, i - k - 1);
                                a++;
                            }
                        }
                    } else {
                        break;
                    }
                }
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                for (int k = 0; k < board[i].length; k++) {
                    if (isInBounds(i + k + 1, j - k - 1)) {
                        if (board[i][j] == player && board[i + k][j - k] == opponent) {
                            if (board[i + k + 1][j - k - 1] == '_') {
                                placeablePositions[a] = new Point(j - k - 1, i + k + 1);
                                a++;
                            }
                        }
                    } else {
                        break;
                    }
                }
            }
        }
        return placeablePositions;
    }

    public void showPlaceableLocations(Point[] locations, char player, char opponent, Game b) {
        /**
         * @param locations Array containing placeable locations for the player
         * @param player Current player
         * @param opponent player's opponent
         */

        for (int i = 0; i < locations.length; i++) {
            if (locations[i].x != -1 && locations[i].y != -1) {
                board[locations[i].y][locations[i].x] = '*';
            }
        }
        displayBoard(b);
        for (int i = 0; i < locations.length; i++) {
            if (locations[i].x != -1 && locations[i].y != -1) {
                board[locations[i].y][locations[i].x] = '_';
            }

        }
    }

    public void placeMove(Point p, char player, char opponent) {
        /**
         * @param p Point corresponding to the location of the piece to be placed
         * @param player Current player
         * @param opponent player's opponent
         */

        for (int i = 1; i < 8; i++) {
            if (isInBounds(p.y + i + 1, p.x + i + 1)) {
                if (board[p.y][p.x] != player && board[p.y][p.x] != opponent &&
                        board[p.y][p.x + i] == opponent && board[p.y][p.x + i + 1] == player) {
                    for (int j = p.x; j < i + p.x + 1; j++) {
                        board[p.y][j] = player;
                    }
                }
            }
        }
        for (int i = 0; i < 8; i++) {
            if (isInBounds(p.y - i - 1, p.x - i - 1)) {
                if (board[p.y][p.x] != player && board[p.y][p.x] != opponent &&
                        board[p.y][p.x - i] == opponent && board[p.y][p.x - i - 1] == player) {
                    for (int j = p.x; j > p.x - i - 1; j--) {
                        board[p.y][j] = player;
                    }
                }
            }
        }
        for (int i = 0; i < 8; i++) {
            if (isInBounds( p.y + i + 1, p.x)) {
                if (board[p.y][p.x] != player && board[p.y][p.x] != opponent &&
                        board[p.y + i][p.x] == opponent && board[p.y + i + 1][p.x] == player) {
                    for (int j = p.y; j < i + p.y + 1; j++) {
                        board[j][p.x] = player;
                    }
                }
            }
        }
        for (int i = 0; i < 8; i++) {
            if (isInBounds(p.y - i - 1, p.x)) {
                if (board[p.y][p.x] != player && board[p.y][p.x] != opponent &&
                        board[p.y - i][p.x] == opponent && board[p.y - i - 1][p.x] == player) {
                    for (int j = p.y; j > p.y - i - 1; j--) {
                        board[j][p.x] = player;
                    }
                }
            }
        }
        for (int i = 0; i < 8; i++) {
            if (isInBounds(p.y + i + 1, p.x + i + 1)) {
                if (board[p.y][p.x] != player && board[p.y][p.x] != opponent &&
                        board[p.y + i][p.x + i] == opponent & board[p.y + i + 1][p.x + i + 1] == player) {
                    for (int j = 0; j < i + 1; j++) {
                        board[p.y + j][p.x + j] = player;
                    }
                }
            }
        }
        for (int i = 0; i < 8; i++) {
            if (isInBounds(p.y - i - 1, p.x - i - 1)) {
                if (board[p.x][p.y] != player && board[p.x][p.y] != opponent &&
                        board[p.y - i][p.x - i] == opponent && board[p.y - i - 1][p.x - i - 1] == player) {
                    for (int j = 0; j < i + 1; j++) {
                        board[p.y - j][p.x - j] = player;
                    }
                }
            }
        }
        for (int i = 0; i < 8; i++) {
            if (isInBounds( p.y - i - 1, p.x + i + 1)) {
                if (board[p.y][p.x] != player && board[p.y][p.x] != opponent &&
                        board[p.y - i][p.x + i] == opponent && board[p.y - i - 1][p.x + i + 1] == player) {
                    for (int j = 0; j < i + 1; j++) {
                        board[p.y - j][p.x + j] = player;
                    }
                }
            }
        }
        for (int i = 0; i < 8; i++) {
            if (isInBounds( p.y + i + 1, p.x - i - 1)) {
                if (board[p.y][p.x] != player && board[p.y][p.x] != opponent &&
                        board[p.y + i][p.x - i] == opponent && board[p.y + i + 1][p.x - i - 1] == player) {
                    for (int j = 0; j < i + 1; j++) {
                        board[p.y + j][p.x - j] = player;
                    }
                }
            }
        }
        board[p.y][p.x] = player;
    }

    public void updateScores() {
        wScore = 0;
        bScore = 0;
        remaining = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == 'W') {
                    wScore++;
                } else if (board[i][j] == 'B') {
                    bScore++;
                } else {
                    remaining++;
                }
            }
        }
    }
}