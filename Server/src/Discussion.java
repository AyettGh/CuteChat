import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Discussion extends Thread {
    private Socket clientSocket;
    private Serveur server;
    private BufferedReader reader;
    private PrintWriter writer;
    private String clientName;

    public Discussion(Socket clientSocket, Serveur server) {
        this.clientSocket = clientSocket;
        this.server = server;
        try {
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer = new PrintWriter(clientSocket.getOutputStream(), true);
            clientName = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String message;
            while ((message = reader.readLine()) != null) {
                server.broadcast("["+clientName+"]" + ": " + message);
            }
        } catch (IOException e) {
            // Handle socket exception
            System.out.println("Client disconnected: " + clientName);
        } finally {
            //Nettoyage et fermeture des ressources
            server.removeDiscussion(this);
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        writer.println(message);
    }

}
