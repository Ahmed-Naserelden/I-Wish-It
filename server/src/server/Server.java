package server;

import client.ClientHandler;
import dal.DbManager;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    static final int PORT = 5005;

    public static void main(String[] args) {
        DbManager.openConnection();
        Server.start();
    }
    
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
            Server.logExit("Address localhost:" + PORT + " aleady in use.");
        }
    }
    


    public static void logFail(String string) {
        System.out.println("[\u001B[31m\u001B[1mFAIL\u001B[0m] " + string);
    }

    public static void logPass(String string) {
        System.out.println("[\u001B[32m\u001B[1mPASS\u001B[0m] " + string);
    }

    public static void logInfo(String string) {
        System.out.println("[\u001B[34m\u001B[1mINFO\u001B[0m] " + string);
    }

    public static void logExit(String string) {
        System.out.println("[\u001B[35m\u001B[1mEXIT\u001B[0m] " + string);
    }

    public static void logInfo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
