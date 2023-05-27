import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import javax.swing.*;
import javax.swing.border.Border;

/**
 * This is the Client class which communicates with the server to play Tic Tac Toe game.
 * It provides a graphical user interface for the player to play the game.
 * The class implements the Runnable interface to enable it to run in a separate thread.
 * 
 * @author Aditya
 */
public class Client implements Runnable {

    Socket sock; // the socket used to connect to the server
    static JFrame frame; // the main window of the game
    static PrintWriter writer; // output stream to send messages to the server
    static BufferedReader reader; // input stream to receive messages from the server
    static JLabel broadcastLabel; // label to display messages sent by the server
    static JLabel[] board; // array to hold the labels representing the game board

    /**
     * Connects to the server and starts the conversation to play the game.
     */
    public void go() {
        try {
            // connect to the server
            sock = new Socket("127.0.0.1", 2000); // random socket number

            // create input and output streams
            InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
            reader = new BufferedReader(streamReader);
            writer = new PrintWriter(sock.getOutputStream(), true);

            // enable the game board labels
            for (int i = 0; i < board.length; i++) {
                board[i].setEnabled(true);
            }

            String type, arg;

            // listen for messages from the server and act accordingly
            while (true) {
                type = reader.readLine();
                arg = reader.readLine();

                if (type.equals("game")){
                    displayBoard(arg); // display the new game board
                }

                else if (type.equals("output")){
                    broadcastLabel.setText(arg); // display a message sent by the server
                }

                else if (type.equals("over")) {
                    JOptionPane.showMessageDialog(frame, arg); // display the game result sent by the server
                    break; // end the game
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Runs the go() method in a separate thread.
     */
    public void run() {
        this.go();
    }

    /**
     * Parses the game board string sent by the server and displays it on the GUI.
     * 
     * @param str the string representing the game board
     */
    private void displayBoard(String str) {
        String[] boardStr = str.split(",");

        if (boardStr.length != 9) {
            System.err.println("Invalid input string: " + str);
            return;
        }

        // update the game board labels based on the board string
        for (int i = 0; i < 9; i++) {
            if (boardStr[i].equals("1")) {
                board[i].setForeground(Color.RED);
                board[i].setText("X");

            }
            if (boardStr[i].equals("0")) {
                board[i].setText("");
            }
            if (boardStr[i].equals("-1")) {
                board[i].setForeground(Color.GREEN);
                board[i].setText("O");
            }
        }
        return;
    }

    /**
     * The main method to start the game.
     * It creates the GUI and sets up the event listeners for buttons and menu items.
     * 
     * @param args command-line arguments (not used in this program)
     */
    public static void main(String[] args) {
        // create the GUI components
        JPanel LayoutPanel = new JPanel();
        LayoutPanel.setLayout(new BorderLayout());

        broadcastLabel = new JLabel();
        broadcastLabel.setText("Enter your player name...");

        JPanel GameArea = new JPanel();
        GameArea.setLayout(new GridLayout(3, 3));

        board = new JLabel[9];

        Font font = new Font("TimesRoman", 1, 100);
        Border border = BorderFactory.createLineBorder(Color.BLACK, 4);

        // create the game board labels and add mouse listeners to them
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JLabel label = new JLabel();
                label.setPreferredSize(new Dimension(100, 100));

                label.setFont(font);

                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setBackground(Color.WHITE);
                label.setBorder(border);
                label.setEnabled(false);

                board[i + 3 * j] = label;

                final int _i = i;
                final int _j = j;

                label.addMouseListener(new MouseListener() {
                    public void mouseClicked(MouseEvent e) {
                        if (writer != null) {
                            writer.println(Integer.toString(_i) + "," + Integer.toString(_j)); // send the player's move to the server
                        }
                    }

                    public void mousePressed(MouseEvent e) {
                    }

                    public void mouseReleased(MouseEvent e) {
                    }

                    public void mouseEntered(MouseEvent e) {
                    }

                    public void mouseExited(MouseEvent e) {
                    }
                });
                GameArea.add(label);
            }
        }

        // create the player name input field and submit button
        JPanel inputLayout = new JPanel(new GridLayout(1, 2));
        JTextField txtInput_name = new JTextField(0);
        JButton btn_submit = new JButton("Submit");

        // set up the event listener for the submit button
        btn_submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                txtInput_name.setEnabled(false);
                btn_submit.setEnabled(false);
                broadcastLabel.setText("WELCOME " + txtInput_name.getText()); // display a welcome message
                frame.setTitle("Tic Tac Toe - Player: " + txtInput_name.getText()); // update the window title
                Client client = new Client();
                Thread T = new Thread(client);
                T.start(); // start the game in a separate thread
            }

        });

        inputLayout.add(txtInput_name);
        inputLayout.add(btn_submit);

        // add all the GUI components to the main panel
        LayoutPanel.add(broadcastLabel, BorderLayout.NORTH);
        LayoutPanel.add(GameArea, BorderLayout.CENTER);
        LayoutPanel.add(inputLayout, BorderLayout.SOUTH);

        // create the menu bar and menu items
        JMenuItem menuItem_Exit = new JMenuItem("Exit");
        JMenuItem menuItem_Instruction = new JMenuItem("Instruction");

        JMenu menu_control = new JMenu("Control");
        JMenu menu_help = new JMenu("Help");
        menu_control.add(menuItem_Exit);
        menu_help.add(menuItem_Instruction);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menu_control);
        menuBar.add(menu_help);

        // create the main window and set its properties
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(LayoutPanel);
        frame.setJMenuBar(menuBar);
        frame.setTitle("Tic Tac Toe");
        frame.setSize(500, 600);

        // set up the event listeners for the menu items
        menuItem_Exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                frame.dispose(); // exit the game
            }
        });

        menuItem_Instruction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame,
                        "Some information about the game\n" + "Criteria for a valid move:\n"
                                + "- The move is not occupied by any mark.\n" + "- The move is made in the players turn.\n"
                                + "- The move is made within the 3 x 3 board.\n"
                                + "The game would continue and switch among the opposite player until it reaches either one of the following conditions:\n"
                                + "- Player 1 wins.\n" + "- Player 2 wins.\n" + "- Draw."); // display the game instructions
            }
        });

        frame.setVisible(true);
    }
}