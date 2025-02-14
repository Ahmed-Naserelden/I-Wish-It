package client;

import connection.FriendActionPayload;
import connection.RechargePayload;
import connection.NetworkManager;
import connection.Request;
import connection.Response;
import connection.SignInPayload;
import connection.SignUpPayload;
import connection.WishlistPayload;
import java.net.Socket;
import java.util.ArrayList;
import connection.ContributionPayload;
import server.Server;
import service.*;

/**
 * ClientHandler class to manage client connections and handle requests.
 * Extends the Thread class to handle each client connection in a separate thread.
 */
public class ClientHandler extends Thread {

    public static ArrayList<ClientHandler> handlers = new ArrayList<>();
    NetworkManager networkManager;

    /**
     * Closes the client connection and removes the handler from the list.
     */
    private void closeClientConnection() {
        networkManager.closeConnection();
        ClientHandler.handlers.remove(this);
    }

    /**
     * Constructs a new ClientHandler with the specified socket.
     * Initializes the NetworkManager and adds the handler to the list.
     *
     * @param socket The socket for the client connection.
     */
    public ClientHandler(Socket socket) {
        networkManager = new NetworkManager(socket);
        Server.logInfo(networkManager.getPort() + " is established.");
        ClientHandler.handlers.add(this);
    }

    /**
     * Runs the client handler thread.
     * Continuously listens for client requests and handles them accordingly.
     */
    @Override
    public void run() {
        while (true) {
            Request request = networkManager.receive();

            if (request == null) {
                Server.logInfo(networkManager.getPort() + " lost connection.");
                closeClientConnection();
                break;
            }

            String url = request.getUrl();
            String method = request.getMethod();
            Object payload = request.getPayload();
            Server.logInfo(networkManager.getPort() + " " + method + " " + url);
            Response response;
            switch (url) {
                case "/api/client/disconnect":
                    handleClientDisconnect(method, payload);
                    return;
                case "/api/auth/signin":
                    response = handleSignIn(method, payload);
                    break;
                case "/api/auth/signup":
                    response = handleSignUp(method, payload);
                    break;
                case "/api/marketplace":
                    response = handleMarketplace(method, payload);
                    break;
                case "/api/wishlist":
                    response = handleWishlist(method, payload);
                    break;
                case "/api/friends":
                    response = handleFriends(method, payload);
                    break;
                case "/api/friends/request":
                    response = handleFriendRequests(method, payload);
                    break;
                case "/api/contribute":
                    response = handleContribution(method, payload);
                    break;
                case "/api/recharge":
                    response = handleRecharge(method, payload);
                    break;
                case "/api/notification":
                    response = handleNotification(method, payload);
                    break;
                default:
                    Server.logFail("Undefined Action: " + url);
                    response = new Response(false);
                    break;
            }
            networkManager.send(response);
            Server.logInfo(networkManager.getPort()
                    + " STATUS " + (response.isPassed() ? "OK" : "NOK")
            );
        }
    }

    /**
     * Handles client disconnect request.
     *
     * @param method The HTTP method of the request.
     * @param payload The payload of the request.
     */
    public void handleClientDisconnect(String method, Object payload) {
        networkManager.send(new Response(true));
        Server.logInfo(networkManager.getPort() + " has disconnected.");
        closeClientConnection();
    }

    /**
     * Handles sign-in request.
     *
     * @param method The HTTP method of the request.
     * @param payload The payload of the request.
     * @return A Response object containing the sign-in status and user details.
     */
    public Response handleSignIn(String method, Object payload) {
        switch (method) {
            case "POST":
                return AuthService.signIn((SignInPayload) payload);
            default:
                Server.logFail("Invalid method for authentication.");
                return new Response(false);
        }
    }

    /**
     * Handles sign-up request.
     *
     * @param method The HTTP method of the request.
     * @param payload The payload of the request.
     * @return A Response object containing the sign-up status.
     */
    public Response handleSignUp(String method, Object payload) {
        switch (method) {
            case "POST":
                return AuthService.signUp((SignUpPayload) payload);
            default:
                Server.logFail("Invalid method for signup.");
                return new Response(false);
        }
    }

    /**
     * Handles contribution request.
     *
     * @param method The HTTP method of the request.
     * @param payload The payload of the request.
     * @return A Response object containing the status of the contribution.
     */
    public Response handleContribution(String method, Object payload) {
        switch (method) {
            case "POST":
                return TransactionService.contribute((ContributionPayload) payload);
            default:
                Server.logFail("Invalid method for contribution.");
                return new Response(false);
        }
    }

    /**
     * Handles marketplace request.
     *
     * @param method The HTTP method of the request.
     * @param payload The payload of the request.
     * @return A Response object containing the list of products in the marketplace.
     */
    public Response handleMarketplace(String method, Object payload) {
        switch (method) {
            case "GET":
                return ProductService.getMarketplace();
            default:
                Server.logFail("Invalid method for products.");
                return new Response(false);
        }
    }

    /**
     * Handles wishlist request.
     *
     * @param method The HTTP method of the request.
     * @param payload The payload of the request.
     * @return A Response object containing the status of the wishlist operation.
     */
    public Response handleWishlist(String method, Object payload) {
        switch (method) {
            case "GET":
                return WishlistService.getWishlist((int) payload);
            case "POST":
                return WishlistService.addToWishlist((WishlistPayload) payload);
            case "DELETE":
                return WishlistService.deleteFromWishlist((WishlistPayload) payload);
            default:
                Server.logFail("Invalid method for wishlist.");
                return new Response(false);
        }
    }

    /**
     * Handles friends request.
     *
     * @param method The HTTP method of the request.
     * @param payload The payload of the request.
     * @return A Response object containing the status of the friends operation.
     */
    public Response handleFriends(String method, Object payload) {
        switch (method) {
            case "GET":
                return FriendService.getFriends((int) payload);
            case "POST":
                return FriendService.sendFriendRequest((FriendActionPayload) payload);
            case "DELETE":
                return FriendService.removeFriend((FriendActionPayload) payload);
            default:
                Server.logFail("Invalid method for friends.");
                return new Response(false);
        }
    }

    /**
     * Handles friend requests.
     *
     * @param method The HTTP method of the request.
     * @param payload The payload of the request.
     * @return A Response object containing the status of the friend request operation.
     */
    public Response handleFriendRequests(String method, Object payload) {
        switch (method) {
            case "GET":
                return FriendService.getFriendRequests((int) payload);
            case "POST":
                return FriendService.acceptFriendRequest((FriendActionPayload) payload);
            case "DELETE":
                return FriendService.declineFriendRequest((FriendActionPayload) payload);
            default:
                Server.logFail("Invalid method for friend requests.");
                return new Response(false);
        }
    }

    /**
     * Handles recharge request.
     *
     * @param method The HTTP method of the request.
     * @param payload The payload of the request.
     * @return A Response object containing the status of the recharge operation.
     */
    public Response handleRecharge(String method, Object payload) {
        switch (method) {
            case "GET":
                return TransactionService.getBalance((int) payload);
            case "PATCH":
                return TransactionService.chargeBalance((RechargePayload) payload);
            default:
                Server.logFail("Invalid method for recharge.");
                return new Response(false);
        }
    }

    /**
     * Handles notification request.
     *
     * @param method The HTTP method of the request.
     * @param payload The payload of the request.
     * @return A Response object containing the status of the notification operation.
     */
    public Response handleNotification(String method, Object payload) {
        switch (method) {
            case "GET":
                return NotificationService.getNotifications((int) payload);
            case "DELETE":
                return NotificationService.deleteNotification((int) payload);
            default:
                Server.logFail("Invalid method for notifications.");
                return new Response(false);
        }
    }
}
