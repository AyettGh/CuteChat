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

            String nickname = JOptionPane.showInputDialog(interfaceClient, "Enter your nickname:", "Nickname", JOptionPane.INFORMATION_MESSAGE);

            writer.println(nickname);

            new Thread(this::receiveMessages).start();
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
                if (message.startsWith("/history")) {
                    // Extract chat history from the message
                    String[] history = message.substring(8).split(":");
                    interfaceClient.showChatHistory(history);
                } else {
                    interfaceClient.appendMessage(message);
                }
            }
        } catch (Exception e) {
            System.out.println("Client run fail: " + e);
        }
    }

    public void requestChatHistory() {
        try {
            // Send a special command to the server to request chat history
            writer.println("/history");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
