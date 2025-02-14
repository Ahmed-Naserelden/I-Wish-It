package connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import server.Server;

/**
 * NetworkManager class to manage network connections and communication.
 * Handles opening and closing connections, sending responses, and receiving requests.
 */
public class NetworkManager {

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    /**
     * Constructs a new NetworkManager with the specified socket.
     * Initializes the output stream and input stream.
     *
     * @param socket The socket for the network connection.
     */
    public NetworkManager(Socket socket) {
        this.socket = socket;

        try {
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            Server.logFail(e.getMessage());
        }
    }

    /**
     * Closes the network connection.
     * Closes the output stream, input stream, and socket.
     */
    public void closeConnection() {
        try {
            if (this.out != null) {
                this.out.close();
            }
            if (this.in != null) {
                this.in.close();
            }
            if (this.socket != null) {
                this.socket.close();
            }
        } catch (IOException e) {
            Server.logFail(e.getMessage());
        }
    }

    /**
     * Gets the input stream for reading objects from the client.
     *
     * @return The input stream for reading objects from the client.
     */
    public ObjectInputStream getIn() {
        return this.in;
    }

    /**
     * Gets the output stream for writing objects to the client.
     *
     * @return The output stream for writing objects to the client.
     */
    public ObjectOutputStream getOut() {
        return this.out;
    }

    /**
     * Sends a response to the client.
     *
     * @param response The response to send.
     * @return True if the response was sent successfully, false otherwise.
     */
    public boolean send(Response response) {
        try {
            this.out.writeObject(response);
            return true;
        } catch (IOException e) {
            Server.logFail(e.getMessage());
        }
        return false;
    }

    /**
     * Receives a request from the client.
     *
     * @return The request received from the client, or null if an error occurred.
     */
    public Request receive() {
        try {
            return (Request) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            Server.logFail(e.getMessage());
            return null;
        }
    }

    /**
     * Gets the port information of the socket.
     *
     * @return A string representing the local and remote port of the socket.
     */
    public String getPort() {
        return socket.getLocalPort() + "→" + socket.getPort();
    }
}
