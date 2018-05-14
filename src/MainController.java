import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by Cassidy Tarng on 5/8/2018.
 */
public class MainController {

    public static int commandCounter = 0;
    public static String endOfProgram = null;

    public static void main(String[] args){

        LinkedList<String> commands = getCommands();

        CheckerBoard board = new CheckerBoard();

        JPanel commandPanel = new JPanel();
        commandPanel.setBorder(new EmptyBorder(175,0,100,0));
        JButton nextButton = new JButton("Next");
        JButton previousButton = new JButton("Previous");

        Box box = Box.createVerticalBox();
        box.add(nextButton);
        box.createVerticalStrut(100);
        box.add(previousButton);
        commandPanel.add(box, BorderLayout.CENTER);

        CheckerBoardManager boardManager = new CheckerBoardManager(board);
        JFrame frame = new JFrame();
        frame.setLayout(new GridLayout(1, 2));
        frame.setTitle("Checkers");
        frame.setSize(850, 500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(boardManager);
        frame.add(commandPanel);

        /**
         * Handle Next Button
         */
        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                // Exit program if the end of commands is reached
                if (commandCounter >= commands.size()){
                    JOptionPane.showMessageDialog(null, "No more commands");
                    System.exit(0);
                }

                System.out.println(commands.get(commandCounter));
                int[] coord = translateCoordinate(commands.get(commandCounter));

                // Checks to see if Piece at spot exists before moving
                if (board.getBoard()[coord[0]][coord[1]] != null){
                    Color movingColor = board.getBoard()[coord[0]][coord[1]].getColor();
                    boolean isKing = board.getBoard()[coord[0]][coord[1]].isKing();
                    endOfProgram = boardManager.move(coord[0], coord[1], coord[2], coord[3], movingColor, isKing, commandCounter);
                    commandCounter++;
                    frame.repaint();

                    // Detect if alert is shown (game finished or error)
                    if (endOfProgram != null){
                        JOptionPane.showMessageDialog(null, endOfProgram);
                        System.exit(0);
                    }
                }
                // If Piece does not exist, throw an error
                else {
                    JOptionPane.showMessageDialog(null, "Error: There is no piece to move at " + commands.get(commandCounter).split("-")[0]);
                    System.exit(0);
                }

            }
        } );

        /**
         * Handle Previous Button
         */
        previousButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (commandCounter > 0){
                    commandCounter--;
                    int[] coord = translateCoordinate(commands.get(commandCounter));
                    Color movingColor = board.getBoard()[coord[2]][coord[3]].getColor();
                    boolean isKing = board.getBoard()[coord[2]][coord[3]].isKing();
                    boardManager.rewind(coord[0], coord[1], coord[2], coord[3], movingColor, isKing, commandCounter);
                    System.out.println(commands.get(commandCounter));
                    frame.repaint();
                }
                else {
                    JOptionPane.showMessageDialog(null, "Beginning of the game has been reached");
                }
            }
        } );
    }

    /**
     * Reads file commands.txt
     * @return list of commands
     */
    private static LinkedList<String> getCommands(){
        try {
            LinkedList<String> commands = new LinkedList<>();
            File file = new File("Commands.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                commands.addLast(line);
            }
            fileReader.close();
            return commands;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Translates coordinates from commands.txt into 2d-array grids
     */
    private static int[] translateCoordinate(String coord){
        String[] coordValue = coord.split("-");
        String val1 = returnTranslatedCoordinate(coordValue[0]);
        String val2 = returnTranslatedCoordinate(coordValue[1]);
        String totalVal = val1 + "-" + val2;
        int[] xyCoord = new int[4];
        String[] numCoord = totalVal.split("-");
        for(int i = 0; i < numCoord.length; i++){
            xyCoord[i] = Integer.parseInt(numCoord[i]);
        }
        return xyCoord;
    }

    /**
     * Definitions for the coordinates
     */
    private static String returnTranslatedCoordinate(String coord){
        switch (coord){
            case "1": return "0-0";
            case "2": return "0-2";
            case "3": return "0-4";
            case "4": return "0-6";
            case "5": return "1-1";
            case "6": return "1-3";
            case "7": return "1-5";
            case "8": return "1-7";
            case "9": return "2-0";
            case "10": return "2-2";
            case "11": return "2-4";
            case "12": return "2-6";
            case "13": return "3-1";
            case "14": return "3-3";
            case "15": return "3-5";
            case "16": return "3-7";
            case "17": return "4-0";
            case "18": return "4-2";
            case "19": return "4-4";
            case "20": return "4-6";
            case "21": return "5-1";
            case "22": return "5-3";
            case "23": return "5-5";
            case "24": return "5-7";
            case "25": return "6-0";
            case "26": return "6-2";
            case "27": return "6-4";
            case "28": return "6-6";
            case "29": return "7-1";
            case "30": return "7-3";
            case "31": return "7-5";
            case "32": return "7-7";
        }
        return null;
    }
}
