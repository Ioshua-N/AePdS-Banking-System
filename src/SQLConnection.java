import java.sql.Connection;
import java.sql.DriverManager;

public class SQLConnection
{
    private static final String user = "root";
    private static final String password = "2002";
    private static final String url = "jdbc:mysql://localhost:3306/tabajaraBankingSystem";

    public static Connection open() throws Exception
    {
        // open connection
        Connection connection = DriverManager.getConnection(url, user, password);
        return connection;
    }
}