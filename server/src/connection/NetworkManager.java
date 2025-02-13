package connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import server.Server;

public class NetworkManager {

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public NetworkManager(Socket socket) {
        this.socket = socket;

        try {
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            Server.logFail(e.getMessage());
        }
    }

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

    public ObjectInputStream getIn() {
        return this.in;
    }

    public ObjectOutputStream getOut() {
        return this.out;
    }

    public boolean send(Response response) {
        try {
            this.out.writeObject(response);
            return true;
        } catch (IOException e) {
            Server.logFail(e.getMessage());
        }
        return false;
    }

    public Request receive() {
        try {
            return (Request) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            Server.logFail(e.getMessage());
            return null;
        }
    }

    public String getPort() {
        return socket.getLocalPort() + "→" + socket.getPort();
    }
}
