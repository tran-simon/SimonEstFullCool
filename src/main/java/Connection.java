import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Connection {

    public static void main(String[] args){
        queryInsert(new String[]{"titre","keywords","click","lien","idArticle"}, new Object[]{"Test 3'", "test1, test'2, tes't3'", 2, "htt'p://thing.com", 10100});
    }

    /**
     *
     *
     *
     * @param objs :
     */
    public static void queryInsert(String[] inputs, Object[] objs) {
        String hostName = "testai1.database.windows.net";
        String dbName = "leo";
        String user = "leo";
        String password = "qwerty1234!";
        String url = String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;", hostName, dbName, user, password);
    java.sql.Connection connection = null;

        String[] colsName = inputs;
        String textCols = "";
        String textVals = "";
        for(int i = 0; i < colsName.length; i++) {
            textCols += colsName[i];
            if(objs[i] instanceof Integer ){
                textVals += objs[i].toString();
            }
            else
                textVals += "\'" + objs[i].toString().replace("'", "''") + "\'";

            if(i != colsName.length-1){
                textVals += ",";
                textCols += ",";
            }
        }
        System.out.println(textVals +"    "+ textCols);

        try {
            connection = DriverManager.getConnection(url);

            // Create and execute a SELECT SQL statement.
            String selectSql = String.format("INSERT INTO data(%s) VALUES(%s);", textCols, textVals);

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
