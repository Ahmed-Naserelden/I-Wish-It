package connection;

import client.Client;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NetworkManager {

    private static final String HOSTNAME = "localhost";
    private static final int PORT = 5005;

    private static Socket socket;
    private static ObjectOutputStream out;
    private static ObjectInputStream in;

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

    public static ObjectInputStream getIn() {
        return in;
    }

    public static ObjectOutputStream getOut() {
        return out;
    }

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
