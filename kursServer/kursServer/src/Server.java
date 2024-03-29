import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static int numConnections = 0;
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(3345);
            System.out.println("Сервер запущен!");
            while (true) {
                Socket client = server.accept();
                Thread t = new MonoThreadClientHandler(client);
                t.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}