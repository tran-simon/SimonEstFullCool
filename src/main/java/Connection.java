import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Connection {

    public static void queryInsert(String keywords, String titre, int click) {
        String hostName = "testai1.database.windows.net";
        String dbName = "leo";
        String user = "leo";
        String password = "qwerty1234!";
        String url = String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;", hostName, dbName, user, password);
        java.sql.Connection connection = null;

        try {
            connection = DriverManager.getConnection(url);

            // Create and execute a SELECT SQL statement.
            String selectSql = String.format("INSERT INTO data(keywords, click, titre) VALUES(\'%s\', %d, \'%s\');", keywords, click, titre);

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(selectSql)) {

                // Print results from select statement
                while (resultSet.next()) {
                    System.out.println(resultSet.getString(1) + " "
                            + resultSet.getString(2));
                }
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
