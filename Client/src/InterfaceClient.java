import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InterfaceClient extends JFrame implements ActionListener {
    private JTextArea chatArea = new JTextArea();
    private JTextField messageField = new JTextField();
    private Client client;
    private JButton historyButton = new JButton("Chat History");
    private String[] history;

    public InterfaceClient() {
        super("Cute Chat");
        UIInterfaceClient();
        client = new Client(this);
        client.connectToServer();
    }

    private void UIInterfaceClient() {
        //setTitle("Cute Chat");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);

        chatArea.setEditable(false);
        chatArea.setFont(new Font("Arial", Font.PLAIN, 14));
        chatArea.setForeground(new Color(50, 50, 50)); // Darker text color
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setBackground(new Color(240, 240, 240)); // Lighter background

        JScrollPane chatScrollPane = new JScrollPane(chatArea);

        messageField.addActionListener(this);
        messageField.setFont(new Font("Arial", Font.PLAIN, 14));
        messageField.setBackground(new Color(255, 255, 153)); // Yellowish background
        messageField.setForeground(new Color(50, 50, 50)); // Darker text color

        historyButton.setPreferredSize(new Dimension(120, 30)); // Smaller button
        historyButton.setBackground(new Color(255, 182, 193)); // Light pink background
        historyButton.setFocusPainted(false); // Remove focus border
        historyButton.addActionListener(this);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(200, 134, 234)); // Light purple background
        buttonPanel.add(historyButton);

        setLayout(new BorderLayout());
        add(chatScrollPane, BorderLayout.CENTER);
        add(messageField, BorderLayout.SOUTH);
        add(buttonPanel, BorderLayout.NORTH);

    }

    public void appendMessage(String message) {
        chatArea.append(message + "\n");//ajouter du texte à la fin de la zone de texte
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == messageField) {
            String message = messageField.getText();
            if (!message.isEmpty()) {
                client.sendMessage(message);
                messageField.setText("");
            }
        } else if (e.getSource() == historyButton) {
            showChatHistory(history);
        }
    }

    public void showChatHistory(String[] history) {
        JFrame historyFrame = new JFrame("Chat History");//crée une nouvelle fenêtre avec le titre "Chat History"
        JTextArea historyArea = new JTextArea(chatArea.getText());
        historyFrame.setResizable(true);
        historyArea.setEditable(false);
        JScrollPane historyScrollPane = new JScrollPane(historyArea);
        historyFrame.setLayout(new BorderLayout());
        historyFrame.add(historyScrollPane, BorderLayout.CENTER);
        historyFrame.setSize(300, 250);
        historyFrame.setLocationRelativeTo(this);//centre la fenêtre de l'historique % la fenêtre parente
        historyFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InterfaceClient().setVisible(true));
    }
}
