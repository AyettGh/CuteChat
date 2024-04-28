import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private InterfaceClient interfaceClient;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public Client(InterfaceClient interfaceClient) {
        this.interfaceClient = interfaceClient;
    }

    public void connectToServer() {
        try {
            String serverAddress = "127.0.0.1"; // Server's IP address
            socket = new Socket(serverAddress, 9000); // Connect to port 9000

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            String pseudo = JOptionPane.showInputDialog(interfaceClient, "saisir votre pseudo:", "Pseudo", JOptionPane.INFORMATION_MESSAGE);

            writer.println(pseudo);

            new Thread(this::receiveMessages).start();
        //receiveMessages() lié  mise à jour de l'interface utilisateur en réponse aux messages reçu du serveur.
            //permet à l'interface client de rester RTOS aux  données provenant du serveur.
        } catch (Exception e) {
            System.out.println("Socket Connection error: " + e);
        }
    }

    public void sendMessage(String message) {
        writer.println(message);
    }

    public void receiveMessages() {
        try {
            String message;
            while ((message = reader.readLine()) != null) {
                if (message.startsWith("..//./../")) {
                    // Extract chat history from the message
                    String[] history = new String[]{message};
                    interfaceClient.showChatHistory(history);
                } else {
                    interfaceClient.appendMessage(message);
                }
            }
        } catch (Exception e) {
            System.out.println("Client run fail: " + e);
        }
    }


}
