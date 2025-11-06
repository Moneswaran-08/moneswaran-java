package server;

import java.io.*;
import java.net.*;
import java.sql.*;
import database.DBConnection;

public class BusServer {

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("🖥 Bus Server started on port 5000...");
            Connection con = DBConnection.getConnection();

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("✅ Client connected!");

                new Thread(() -> {
                    try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                         PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                        String request = in.readLine();
                        System.out.println("📩 Received: " + request);

                        if (request.equalsIgnoreCase("SHOW_BUSES")) {
                            Statement st = con.createStatement();
                            ResultSet rs = st.executeQuery("SELECT * FROM bus");
                            StringBuilder response = new StringBuilder();

                            while (rs.next()) {
                                response.append(rs.getInt("bus_id")).append(". ")
                                        .append(rs.getString("bus_name")).append(" - ")
                                        .append(rs.getString("bus_type")).append(" - ₹")
                                        .append(rs.getDouble("fare")).append("\n");
                            }
                            out.println(response.toString());
                        } else {
                            out.println("Invalid Request");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } catch (Exception e) {
            System.out.println("❌ Server Error: " + e.getMessage());
        }
    }
}
