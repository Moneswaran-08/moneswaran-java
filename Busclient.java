package client;

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BusClient extends JFrame {

    private JTextArea displayArea;
    private JButton viewBusButton;

    public BusClient() {
        setTitle("🚌 Bus Ticket Booking System - Client");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        viewBusButton = new JButton("View Available Buses");

        viewBusButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewBuses();
            }
        });

        add(new JScrollPane(displayArea), BorderLayout.CENTER);
        add(viewBusButton, BorderLayout.SOUTH);
    }

    private void viewBuses() {
        try (Socket socket = new Socket("localhost", 5000);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println("SHOW_BUSES");

            String response;
            StringBuilder buses = new StringBuilder();
            while ((response = in.readLine()) != null) {
                buses.append(response).append("\n");
            }

            displayArea.setText(buses.toString());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Server not reachable!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BusClient().setVisible(true));
    }
}
