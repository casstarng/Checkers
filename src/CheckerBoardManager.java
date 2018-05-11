import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Cassidy Tarng on 5/8/2018.
 */
public class CheckerBoardManager extends JPanel {

    String test = "";
    CheckerBoard board = new CheckerBoard();
    Color nextTurn;
    boolean initialTurn = true;
    ArrayList<String> nextChain = null;

    public CheckerBoardManager(CheckerBoard board){
        test = "this is a test";
        this.board = board;
        System.out.println(test);
    }

    /**
     * Draw the board
     */
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        // Paint CheckerBoard
        int y = 10;
        for(int i = 0; i < 8; i++){
            int x = 10;
            for(int k = 0; k < 8; k++){
                if ((k + i) % 2 == 0) g.setColor(Color.LIGHT_GRAY);
                else g.setColor(Color.GRAY);
                g.fillRect(x, y, 50, 50);

                x += 50;
            }
            y += 50;
        }

        // Paint CheckerPiece
        for(int i = 0; i < board.getBoard().length; i++){
            for(int k = 0; k < board.getBoard()[i].length; k++){
                if (board.getBoard()[k][i] != null){
                    paintPiece(g, i, k, board.getBoard()[k][i].getColor(), board.getBoard()[k][i].isKing());
                }
            }
        }
    }

    /**
     * Paints the CheckerPiece given an (x, y) coordinate and color
     * @param g Graphics
     * @param x coordinate
     * @param y coordinate
     * @param color to paint
     */
    public void paintPiece(Graphics g, int x, int y, Color color, boolean king){
        g.setColor(color);
        g.fillOval(10 + (x * 50), 10 + (y * 50), 50, 50);

        // If piece is king, draw a K
        if (king){
            g.setFont(new Font("TimesRoman", Font.PLAIN, 25));
            g.setColor(Color.WHITE);
            g.drawString("K", 27 + (x * 50),  45 + (y * 50));
        }
    }

    //TODO make sure jumps need to be taken
    //TODO make previous
    public String move(int y, int x, int g, int h, Color color, boolean isKing){

        if (nextChain != null){
            String currentCommand = y + "-" + x + "-" + g + "-" + h;
            if (nextChain.contains(currentCommand)){
                nextChain = null;
            }
            else {
                return "Illegal Move: Another jump must be made";
            }
        }

        // Check if player's turn is correct
        if (color != nextTurn && !initialTurn) return "Wrong turn";

        // If piece reaches end, change piece to king
        if (color == Color.BLACK && g == 7) isKing = true;
        if (color == Color.RED && g == 0) isKing = true;

        // Check if piece moves 1 space
        if (Math.abs(y - g) == 1 && Math.abs(x - h) == 1){

            // If piece is a king, then move
            if (isKing){
                board.movePiece(y, x, g, h, color, isKing);
            }
            // If piece is black, check if move goes down
            else if (color == Color.BLACK && g > y){
                board.movePiece(y, x, g, h, color, isKing);
            }
            // If piece is red, check if move goes up
            else if (color == Color.RED && g < y){
                board.movePiece(y, x, g, h, color, isKing);
            }
            else{
                return "Illegal Move";
            }
        }
        // Check if piece moves 2 spaces (needs to delete opposing piece)
        else if (Math.abs(y - g) == 2 && Math.abs(x - h) == 2){
            // If piece is a king
            if (isKing){
                // If piece moves up-left
                if (y - g > 0 && x - h > 0 && board.getBoard()[y - 1][x - 1].getColor() != color){
                    board.movePiece(y, x, g, h, color, isKing);
                    board.deletePiece(y - 1, x - 1);
                }
                // If piece moves up-right
                else if (y - g > 0 && x - h < 0 && board.getBoard()[y - 1][x + 1].getColor() != color){
                    board.movePiece(y, x, g, h, color, isKing);
                    board.deletePiece(y - 1, x + 1);
                }
                // If piece moves down-left
                else if (y - g < 0 && x - h > 0 && board.getBoard()[y + 1][x - 1].getColor() != color){
                    board.movePiece(y, x, g, h, color, isKing);
                    board.deletePiece(y + 1, x - 1);
                }
                // If piece moves down-right
                else if (y - g < 0 && x - h < 0 && board.getBoard()[y + 1][x + 1].getColor() != color){
                    board.movePiece(y, x, g, h, color, isKing);
                    board.deletePiece(y + 1, x + 1);
                }

                // Check if next step is chainable
                nextChain = checkForChain(g, h, color, isKing);
                if (nextChain != null) return null;
            }
            // If piece is black, check if move goes down
            else if (color == Color.BLACK && g > y){
                // check if case moves left and that the piece will skip over a red piece
                if (x - h > 0 && board.getBoard()[y + 1][x - 1].getColor() != color){
                    board.movePiece(y, x, g, h, color, isKing);
                    board.deletePiece(y + 1, x - 1);

                    // Check if next step is chainable
                    nextChain = checkForChain(g, h, color, isKing);
                    if (nextChain != null) return null;
                }
                // check if case moves right and that the piece will skip over a red piece
                else if (x - h < 0 && board.getBoard()[y + 1][x + 1].getColor() != color){
                    board.movePiece(y, x, g, h, color, isKing);
                    board.deletePiece(y + 1, x + 1);

                    // Check if next step is chainable
                    nextChain = checkForChain(g, h, color, isKing);
                    if (nextChain != null) return null;
                }
                else {
                    return "Illegal Move";
                }
            }
            // If piece is red, check if move goes up
            else if (color == Color.RED && g < y){
                // check if case moves left and that the piece will skip over a black piece
                if (x - h > 0 && board.getBoard()[y - 1][x - 1].getColor() != color){
                    board.movePiece(y, x, g, h, color, isKing);
                    board.deletePiece(y - 1, x - 1);

                    // Check if next step is chainable
                    nextChain = checkForChain(g, h, color, isKing);
                    if (nextChain != null) return null;
                }
                // check if case moves right and that the piece will skip over a black piece
                else if (x - h < 0 && board.getBoard()[y - 1][x + 1].getColor() != color){
                    board.movePiece(y, x, g, h, color, isKing);
                    board.deletePiece(y - 1, x + 1);

                    // Check if next step is chainable
                    nextChain = checkForChain(g, h, color, isKing);
                    if (nextChain != null) return null;
                }
                else {
                    return "Illegal Move";
                }
            }
            else{
                return "Illegal Move";
            }
        }
        else{
            return "Illegal Move";
        }

        // Change turns
        initialTurn = false;
        if (color == Color.RED) nextTurn = Color.BLACK;
        else if (color == Color.BLACK) nextTurn = Color.RED;

        // Check for a winner
        String win = checkWinner();
        if (win != null){
            return win;
        }
        else return null;
    }

    public String checkWinner(){
        int redCount = 0;
        int blackCount = 0;
        for(int i = 0; i < board.getBoard().length; i++){
            for(int k = 0; k < board.getBoard()[i].length; k++){
                if (board.getBoard()[k][i] != null){
                    if (board.getBoard()[k][i].getColor() == Color.RED){
                        redCount++;
                    }
                    else if (board.getBoard()[k][i].getColor() == Color.BLACK){
                        blackCount++;
                    }
                }
            }
        }
        if (redCount == 0){
            return "Black Wins!";
        }
        else if (blackCount == 0){
            return "Red Wins!";
        }
        else return null;
    }

    public ArrayList<String> checkForChain(int y, int x, Color color, boolean isKing){
        ArrayList<String> chainOptions = new ArrayList<>();
        // If Color is black
        if (color == Color.BLACK || isKing){
            // If piece can jump down-right
            if (y + 2 < 8 && x + 2 < 8 && board.getBoard()[y + 1][x + 1] != null
                    && board.getBoard()[y + 1][x + 1].getColor() != color
                    && board.getBoard()[y + 2][x + 2] == null){
                chainOptions.add(y + "-" + x + "-" + (y + 2) + "-" + (x + 2));
            }
            // If piece can jump down-left
            if (y + 2 < 8 && x - 2 >= 0 && board.getBoard()[y + 1][x - 1] != null
                    && board.getBoard()[y + 1][x - 1].getColor() != color
                    && board.getBoard()[y + 2][x - 2] == null){
                chainOptions.add(y + "-" + x + "-" + (y + 2) + "-" + (x - 2));
            }
        }
        // If Color is red
        if (color == Color.RED || isKing){
            // If piece can jump up-right
            if (y - 2 >= 0 && x + 2 < 8 && board.getBoard()[y - 1][x + 1] != null
                    && board.getBoard()[y - 1][x + 1].getColor() != color
                    && board.getBoard()[y - 2][x + 2] == null){
                chainOptions.add(y + "-" + x + "-" + (y - 2) + "-" + (x + 2));
            }
            // If piece can jump up-left
            if (y - 2 >= 0 && x - 2 >= 0 && board.getBoard()[y - 1][x - 1] != null
                    && board.getBoard()[y - 1][x - 1].getColor() != color
                    && board.getBoard()[y - 2][x - 2] == null){
                chainOptions.add(y + "-" + x + "-" + (y - 2) + "-" + (x - 2));
            }
        }
        if (chainOptions.size() > 0) return chainOptions;
        else return null;
    }

}

