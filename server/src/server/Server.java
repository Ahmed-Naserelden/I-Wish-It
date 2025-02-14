package server;

import client.ClientHandler;
import dal.DbManager;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server class to manage the server operations.
 * Handles starting the server, accepting client connections, and logging messages.
 */
public class Server {

    static final int PORT = 5005;

    /**
     * Main method to start the server.
     * Opens the database connection and starts the server.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        DbManager.openConnection();
        Server.start();
    }
    
    /**
     * Starts the server and waits for client connections.
     * Accepts client connections and starts a new thread for each client.
     */
    public static void start() {
        Socket socket;

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            Server.logPass("Server started.");
            Server.logInfo("Waiting for clients...");

            while (true) {
                socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
                clientHandler.start(); // start thread
            }
        } catch (IOException e) {
            Server.logExit("Address localhost:" + PORT + " already in use.");
        }
    }

    /**
     * Logs a failure message to the console.
     *
     * @param string The failure message to log.
     */
    public static void logFail(String string) {
        System.out.println("[\u001B[31m\u001B[1mFAIL\u001B[0m] " + string);
    }

    /**
     * Logs a success message to the console.
     *
     * @param string The success message to log.
     */
    public static void logPass(String string) {
        System.out.println("[\u001B[32m\u001B[1mPASS\u001B[0m] " + string);
    }

    /**
     * Logs an informational message to the console.
     *
     * @param string The informational message to log.
     */
    public static void logInfo(String string) {
        System.out.println("[\u001B[34m\u001B[1mINFO\u001B[0m] " + string);
    }

    /**
     * Logs an exit message to the console and exits the application.
     *
     * @param string The exit message to log.
     */
    public static void logExit(String string) {
        System.out.println("[\u001B[35m\u001B[1mEXIT\u001B[0m] " + string);
        System.exit(1);
    }
}
