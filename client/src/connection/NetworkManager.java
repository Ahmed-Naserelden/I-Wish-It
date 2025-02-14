package connection;

import client.Client;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * NetworkManager class to manage network connections and communication.
 * Handles opening and closing connections, sending requests, and receiving responses.
 */
public class NetworkManager {

    private static final String HOSTNAME = "localhost";
    private static final int PORT = 5005;

    private static Socket socket;
    private static ObjectOutputStream out;
    private static ObjectInputStream in;

    /**
     * Opens a connection to the server.
     * Initializes the socket, output stream, and input stream.
     */
    public static void openConnection() {
        try {
            socket = new Socket(HOSTNAME, PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            Client.logInfo(socket.getLocalPort() + "→" + socket.getPort() + " has started");
        } catch (IOException e) {
            Client.logFail(e.getMessage());
            Client.logExit("Server is down!");
            System.exit(1);
        }
    }

    /**
     * Closes the connection to the server.
     * Closes the output stream, input stream, and socket.
     */
    public static void closeConnection() {
        try {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (socket != null) {
                socket.close();
            }
            Client.logInfo("Connection closed.");
        } catch (IOException e) {
            Client.logFail(e.getMessage());
        }
    }

    /**
     * Gets the input stream for reading objects from the server.
     *
     * @return The input stream for reading objects from the server.
     */
    public static ObjectInputStream getIn() {
        return in;
    }

    /**
     * Gets the output stream for writing objects to the server.
     *
     * @return The output stream for writing objects to the server.
     */
    public static ObjectOutputStream getOut() {
        return out;
    }

    /**
     * Sends a request to the server.
     *
     * @param request The request to send.
     * @return True if the request was sent successfully, false otherwise.
     */
    public static boolean send(Request request) {
        try {
            out.writeObject(request);
            Client.logInfo(request.getMethod() + " " + request.getUrl());
            return true;
        } catch (IOException e) {
            Client.logFail(e.getMessage());
            return false;
        }
    }

    /**
     * Receives a response from the server.
     *
     * @return The response received from the server.
     */
    public static Response receive() {
        try {
            Response response = (Response) in.readObject();
            Client.logInfo("STATUS " + (response.isPassed() ? "OK" : "NOK"));
            return response;
        } catch (IOException | ClassNotFoundException e) {
            Client.logFail(e.getMessage());
            return new Response(false);
        }
    }
}
