package Client;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class Client {
    private static Socket clientSocket;
    private static ObjectInputStream in;
    private static ObjectOutputStream out;

    public static void startConnection() throws IOException {
        clientSocket = new Socket("localhost", 3345);
        in = new ObjectInputStream(clientSocket.getInputStream());
        out = new ObjectOutputStream(clientSocket.getOutputStream());
    }

    public static void sendToServer(List<Object> userData) throws IOException {
        try {
            if (!clientSocket.isClosed()) {
                out.writeObject(userData);
                out.flush();
            } else
                System.out.println("SOcket is close");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Object> getFromServer() throws IOException {
        try {
            if (!clientSocket.isClosed()) {
                List<Object> getData = (List<Object>) in.readObject();
                return getData;
            } else {
                System.out.println("SOcket is close");
                return null;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static void closeConnection() throws IOException {
        clientSocket.close();
        in.close();
        out.close();
    }
}