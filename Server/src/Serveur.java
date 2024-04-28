import javax.swing.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Serveur {
    private ServerSocket serverSocket;
    public List <Discussion> discussions = new ArrayList<>();
    public List<String> chatHistory = new ArrayList<>(); // Chat history


    public Serveur() {
        try {
            InetAddress inetAddress = InetAddress.getByName("127.0.0.1");
            int port = 9000;
            serverSocket = new ServerSocket(port, 50, inetAddress);
            // 50 spécifie la taille maximale de la file d'attente des connexions entrantes.Si 50> block the newer ones
            System.out.println("le serveur  " + inetAddress.getHostAddress() + "  a démarré avec succès au port " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                //bloque l'exécution jusqu'à ce qu'une nouvelle connexion cliente soit acceptée.
                Discussion discussion = new Discussion(clientSocket, this);
                discussions.add(discussion);
                discussion.start();//Démarre thread de discu en lanceant le traitm asynchrone de communication avec le client spécifique.
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
            discussion.sendMessage(message);//SM==>envoie le message au client associé
            //DSM.=> le message est diffusé à tous les clients en même temps
        }
    }

    public synchronized void removeDiscussion(Discussion discussion) {
        discussions.remove(discussion);
    }

    public static void main(String[] args) {
        new Serveur();
    }
}

