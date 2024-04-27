import javax.swing.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Serveur {
    private ServerSocket serverSocket;
    public List    <Discussion> discussions = new ArrayList<>();
    public List<String> chatHistory = new ArrayList<>(); // Chat history


    public Serveur() {
        try {
            InetAddress inetAddress = InetAddress.getByName("127.0.0.1");
            int port = 9000;
            serverSocket = new ServerSocket(port, 50, inetAddress);
            System.out.println("Server started on " + inetAddress.getHostAddress() + " at port " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                Discussion discussion = new Discussion(clientSocket, this);
                discussions.add(discussion);
                discussion.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getChatHistory() {
        return chatHistory;
    }


    public synchronized void broadcast(String message) {
        chatHistory.add(message); // Add message to chat history
        for (Discussion discussion : discussions) {
            discussion.sendMessage(message);
        }
    }

    public synchronized void removeDiscussion(Discussion discussion) {
        discussions.remove(discussion);
    }

    public static void main(String[] args) {
        new Serveur();
    }
}

