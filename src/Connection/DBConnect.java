package Connection;
import java.sql.*;
public class DBConnect {
    public Connection conn;
    public Statement stat;
    public ResultSet result;
    public PreparedStatement pstat;

    public DBConnect(){
        try{
            //String url = "jdbc:sqlserver://localhost;database=PRG3_KEL09;encrypt=false;user=sa;password=robby";
            //String url = "jdbc:sqlserver://localhost;database=PRG2_KEL09;encrypt=false;user=sa;password=polman";
            String url = "jdbc:sqlserver://10.8.9.99;database=HonorariumDosenEksternal;encrypt=false;user=sa;password=polman";
            conn = DriverManager.getConnection(url);
            stat = conn.createStatement();
            System.out.println("Connection Berhasil "+stat);
        }catch (Exception e){
            System.out.println("Error saat connect Database: "+e);
        }
    }
    public static  void  main (String[] args){
        DBConnect connect = new DBConnect();
    }
}