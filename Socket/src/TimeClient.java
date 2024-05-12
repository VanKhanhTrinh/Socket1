import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeClient extends JFrame {
    private JLabel timeLabel;

    public TimeClient() {
        setTitle("Clock");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(200, 100);
        setLocationRelativeTo(null);

        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(timeLabel);

        setVisible(true);

        Timer timer = new Timer(1000, event -> {
            updateTime();
            requestTimeFromServer();
        });
        timer.start();
    }

    private void updateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentTime = new Date();
        String timeString = dateFormat.format(currentTime);
        timeLabel.setText(timeString);
    }

    private void requestTimeFromServer() {
        String serverAddress = "localhost";
        int serverPort = 12342;

        try (Socket socket = new Socket(serverAddress, serverPort)) {
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            writer.println("time");

            String response = reader.readLine();
            if (response != null) {
                System.out.println(response);
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TimeClient::new);
    }
}