package Connection;
import java.sql.*;
import io.github.cdimascio.dotenv.Dotenv;

public class DBConnect {
    public Connection conn;
    public Statement stat;
    public ResultSet result;
    public PreparedStatement pstat;

    public DBConnect(){
        try{
            // Load environment variables from the .env file
            Dotenv dotenv = Dotenv.load();

            // Get the values from the .env file
            String url = dotenv.get("DB_URL");
            String user = dotenv.get("DB_USER");
            String password = dotenv.get("DB_PASSWORD");

            // Establish the connection
            conn = DriverManager.getConnection(url, user, password);
            stat = conn.createStatement();
            System.out.println("Connection Berhasil " + stat);
        } catch (Exception e) {
            System.out.println("Error saat connect Database: " + e);
        }
    }

    public static void main(String[] args){
        DBConnect connect = new DBConnect();
    }
}