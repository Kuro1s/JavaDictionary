package bin.Dictionary;
import java.sql.*;
public class DatabaseConnection {
    public static Connection getConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection("jdbc:sqlite:D:/Github/Dictonarydemo/DictonaryDemo/src/data/dict_hh.db");
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}