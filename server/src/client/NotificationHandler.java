//package client;
//
//
//import model.Notification;
//import connection.NetworkManager;
//import connection.Request;
//import connection.Response;
//import dal.NotificationRepository;
//import java.net.Socket;
//import java.util.List;
//import java.util.Vector;
//import server.Server;
//
//public class NotificationHandler extends Thread {
//    
//    public static Vector<NotificationHandler> handlers = new Vector<>();
//    NetworkManager networkManager;
//    
//    public NotificationHandler(Socket socket) {
//        networkManager = new NetworkManager(socket);
//        Server.logInfo(networkManager.getPort() + " is established.");
//        NotificationHandler.handlers.add(this);
//    }
//    
//    @Override
//    public void run() {
//        while (true) {
//            try {
//                
//                Request request = networkManager.receive();
//                    int userId = (int) request.getPayload();
//                    
//                    List<Notification> notifications = NotificationRepository.getNewNotifications(userId);
//
//                    if (!notifications.isEmpty()) {
//                        for (Notification notification : notifications) {
//                            Response response = new Response(true, notification);
//                            networkManager.send(response);
//                        }
//                    }
//
//                }
//
//                Thread.sleep(5000); // Sleep for 5 seconds before checking again.
//            } catch (InterruptedException e) {
//                Server.logFail("Notification handler thread interrupted");
//                break;
//            } catch (Exception e) {
//                Server.logFail("Error while checking notifications: " + e.getMessage());
//            }
//        }
//    }
//}
