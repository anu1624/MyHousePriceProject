import java.sql.*;
import java.util.Scanner;

public class Predictor {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter BHK: ");
        int bhk = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter Region (e.g. Andheri West): ");
        String region = sc.nextLine();

        try {
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/house_db", "root", "Beinganu@1624");

            String query = "SELECT AVG(price) FROM houses WHERE bhk=? AND region=?";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, bhk);
            ps.setString(2, region);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double avgPrice = rs.getDouble(1);

                if (avgPrice > 0) {
                    System.out.println("Estimated Price: " + avgPrice + " (check unit manually: Cr/L)");
                } else {
                    System.out.println("No data found for given input.");
                }
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}