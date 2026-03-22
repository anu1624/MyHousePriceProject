import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class PredictorGUI {
    public static void main(String[] args) {

        JFrame frame = new JFrame("🏠 House Price Predictor");
        frame.setSize(450, 350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        frame.getContentPane().setBackground(new Color(245, 245, 255));

        // Title
        JLabel title = new JLabel("House Price Predictor", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setBounds(80, 20, 300, 30);
        title.setForeground(new Color(80, 0, 120));
        frame.add(title);

        // BHK
        JLabel bhkLabel = new JLabel("Select BHK:");
        bhkLabel.setBounds(50, 80, 120, 25);
        frame.add(bhkLabel);

        JComboBox<Integer> bhkBox = new JComboBox<>();
        bhkBox.setBounds(180, 80, 150, 30);
        frame.add(bhkBox);

        // Region
        JLabel regionLabel = new JLabel("Select Region:");
        regionLabel.setBounds(50, 130, 120, 25);
        frame.add(regionLabel);

        JComboBox<String> regionBox = new JComboBox<>();
        regionBox.setBounds(180, 130, 180, 30);
        frame.add(regionBox);

        // Button
        JButton button = new JButton("Predict Price");
        button.setBounds(130, 190, 170, 40);
        button.setBackground(new Color(120, 80, 200));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        frame.add(button);

        // Result
        JLabel resultLabel = new JLabel("", SwingConstants.CENTER);
        resultLabel.setBounds(50, 250, 350, 30);
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        frame.add(resultLabel);

        // 🔄 Load data from DB
        try {
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/house_db", "root", "Beinganu@1624");

            Statement st1 = con.createStatement();
            ResultSet rs1 = st1.executeQuery("SELECT DISTINCT bhk FROM houses");

            while (rs1.next()) {
                bhkBox.addItem(rs1.getInt("bhk"));
            }

            Statement st2 = con.createStatement();
            ResultSet rs2 = st2.executeQuery("SELECT DISTINCT region FROM houses");

            while (rs2.next()) {
                regionBox.addItem(rs2.getString("region"));
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 🔮 Prediction logic
        button.addActionListener(e -> {
            try {
                int bhk = (int) bhkBox.getSelectedItem();
                String region = (String) regionBox.getSelectedItem();

                Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/house_db", "root", "Beinganu@1624");

                String query = "SELECT AVG(price) FROM houses WHERE bhk=? AND region=?";
                PreparedStatement ps = con.prepareStatement(query);

                ps.setInt(1, bhk);
                ps.setString(2, region);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    double price = rs.getDouble(1);

                    if (price > 0) {
                        resultLabel.setForeground(new Color(0, 128, 0));
                        resultLabel.setText("Estimated Price: " + price + " (Cr/L)");
                    } else {
                        resultLabel.setForeground(Color.RED);
                        resultLabel.setText("No data found!");
                    }
                }

                con.close();

            } catch (Exception ex) {
                resultLabel.setText("Error!");
                ex.printStackTrace();
            }
        });

        frame.setVisible(true);
    }
}