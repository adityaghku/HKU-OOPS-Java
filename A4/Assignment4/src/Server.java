import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The Server class creates a server for playing TicTacToe with 2 clients.
 * It initiates a session between two clients by waiting for two connections to be established.
 * Once two clients are successfully connected, two separate threads are created to facilitate communication between each client and the server.
 * The class implements the Runnable interface, and each thread runs the `run()` method of the Server class.
 * 
 * @author Aditya
 */

public class Server implements Runnable {
  
  /**
   * A static instance of the Server class.
   */
  private static Server server;

  /**
   * A boolean value indicating whether the server is alive or not.
   */
  private boolean alive;

  /**
   * The socket for the first client.
   */
  private Socket sock1;

  /**
   * The socket for the second client.
   */
  private Socket sock2;

  /**
   * The PrintWriter object for the first client's output stream.
   */
  private PrintWriter writer1;

  /**
   * The PrintWriter object for the second client's output stream.
   */
  private PrintWriter writer2;

  /**
   * The BufferedReader object for the first client's input stream.
   */
  private BufferedReader reader1;

  /**
   * The BufferedReader object for the second client's input stream.
   */
  private BufferedReader reader2;

  /**
   * The Gameplay object for the game.
   */
  private Gameplay game;

  /**
   * The main method of the Server class.
   * It creates an instance of the Server class and calls the `go()` method to start the game.
   * @param args The command line arguments.
   */
  public static void main(String[] args) {
    server = new Server();
    server.go();
  }

  /**
   * The `go()` method of the Server class.
   * It starts a new game by creating a new Gameplay object and waiting for two connections to be established.
   * Once two clients are successfully connected, two separate threads are created to facilitate communication between each client and the server.
   * It also starts the threads and waits for them to finish.
   */
  public void go() {
    game = new Gameplay();

    try {
      ServerSocket serverSock = new ServerSocket(2000);

      sock1 = serverSock.accept();
      writer1 = new PrintWriter(sock1.getOutputStream(), true);
      reader1 = new BufferedReader(new InputStreamReader(sock1.getInputStream()));

      sock2 = serverSock.accept();
      writer2 = new PrintWriter(sock2.getOutputStream(), true);
      reader2 = new BufferedReader(new InputStreamReader(sock2.getInputStream()));

      Thread player1 = new Thread(server);
      Thread player2 = new Thread(server);
      player1.setName("1");
      player2.setName("2");

      alive = true;

      player1.start();
      player2.start();

      player1.join();
      player2.join();

      serverSock.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * The `run()` method of the Server class.
   * It is called by each thread to facilitate communication between each client and the server.
   * It reads input from the client and processes it.
   */
  public void run() {
    boolean isPlayer1 = (Thread.currentThread().getName().equals("1"));

    String response;

    while (alive) {
      try {
        response = read(isPlayer1);
        process(isPlayer1, response);

      } catch (IOException e) {
        write("over", "Game Ends. One of the players left.");
        alive = false;
      }
    }
  }

  /**
   * The `process()` method of the Server class.
   * It processes the input received from the client and updates the game accordingly.
   * @param isPlayer1 A boolean value indicating whether the client is player 1 or not.
   * @param response The input received from the client.
   * @throws IOException If an I/O error occurs.
   */
  private void process(boolean isPlayer1, String response) throws IOException {

    if (response.equals("ok")){
      return;
    }

    String[] input = response.split(",");
    int row = Integer.parseInt(input[0]);
    int col = Integer.parseInt(input[1]);

    boolean valid = game.makeMove(isPlayer1, row, col);

    if (valid) {
      write("game", game.getBoardString());

      int check = game.checkEndGame();

      if (check == Gameplay.checkConditions[0]) {
        if (isPlayer1) {
          write1("output", "Valid move, wait for your opponent.");
          write2("output", "Your opponent has moved, now is your turn.");
        } else {
          write2("output", "Valid move, wait for your opponent.");
          write1("output", "Your opponent has moved, now is your turn.");
        }
      } else if (check == Gameplay.checkConditions[1]) {
        write1("over", "Congratulations. You Win.");
        write2("over", "You lose.");
        alive = false;
      } else if (check == Gameplay.checkConditions[2]) {
        write2("over", "Congratulations. You Win.");
        write1("over", "You lose.");
        alive = false;
      } else if (check == Gameplay.checkConditions[3]) {
        write("over", "Draw.");
        alive = false;
      } else {
        throw new IllegalArgumentException("Unexpected value: " + Integer.toString(check));
      }
    } else {
      write("invalid", "");
    }
  }

  /**
   * The `write1()` method of the Server class.
   * It writes a message to the output stream of the first client.
   * @param type The type of message.
   * @param argument The message to be written.
   */
  private void write1(String type, String argument) {

    writer1.println(type);
    writer1.println(argument);
  }

  /**
   * The `write2()` method of the Server class.
   * It writes a message to the output stream of the second client.
   * @param type The type of message.
   * @param argument The message to be written.
   */
  private void write2(String type, String argument) {

    writer2.println(type);
    writer2.println(argument);
  }

  /**
   * The `write()` method of the Server class.
   * It writes a message to the output stream of both clients.
   * @param type The type of message.
   * @param argument The message to be written.
   */
  private void write(String type, String argument) {

    writer1.println(type);
    writer1.println(argument);

    writer2.println(type);
    writer2.println(argument);
  }

  /**
   * The `read()` method of the Server class.
   * It reads input from the input stream of the client.
   * @param isPlayer1 A boolean value indicating whether the client is player 1 or not.
   * @return The input received from the client.
   * @throws IOException If an I/O error occurs.
   */
  private String read(boolean isPlayer1) throws IOException {

    String response;

    if (isPlayer1)
      response = reader1.readLine();
    else
      response = reader2.readLine();

    return response;
  }
}