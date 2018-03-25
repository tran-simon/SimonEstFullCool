package sqlpush;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Connection {

    public static void main(String[] args) {
//        queryInsert(new String[]{"titre","keywords","click","lien","idArticle"}, new Object[]{"Test 3'", "test1, test'2, tes't3'", 2, "htt'p://thing.com", 10100});
    }

    /**
     * @param objs :
     */
    public static void queryInsert(String[] inputs, Object[] objs, int lineupIndex, String titre, String keywords, int nbClick) {
        String hostName = "testai1.database.windows.net";
        String dbName = "leo";
        String user = "leo";
        String password = "qwerty1234!";
        String url = String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;", hostName, dbName, user, password);
        java.sql.Connection connection = null;

        String[] colsName = inputs;
        String textCols = "";
        String textVals = "";

        String colAndtype = "";
        for (int i = 0; i < colsName.length; i++) {
            if (colsName[i].equals("$type")) {
                colsName[i] = "TYPE";
            }
            textCols += colsName[i];
            colAndtype += colsName[i];


            if (objs[i] instanceof Integer) {
                textVals += objs[i].toString();
                colAndtype += " INT";
            }
            else {
                textVals += "\'" + objs[i].toString().replace("'", "''") + "\'";
                colAndtype += " TEXT";
            }

            if (i != colsName.length - 1) {
                textVals += ",";
                textCols += ",";
                colAndtype += ",";
            }
        }
//        System.out.println(textVals +"    "+ textCols);

        try {

//            colAndtype += ", lineupIndex INTEGER";
            textCols += "," + "lineupIndex,titre,keywords,click";
            textVals += "," + lineupIndex + ",'" + titre + "','" + keywords + "'," + nbClick;
//            textCols += "," + "\'" + lineup.

            connection = DriverManager.getConnection(url);

            // Create and execute a SELECT SQL statement.
            String selectSql = String.format("INSERT INTO simonLeCool(%s) VALUES(%s);", textCols, textVals);
//            System.out.println(selectSql);

//            String createTable = String.format("CREATE TABLE simonLeCool(id INT identity(1,1) not null, %s)", colAndtype);
//            System.out.println(createTable);
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
        }
    }
}
