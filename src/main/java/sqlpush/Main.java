package sqlpush;

import com.google.gson.Gson;

import java.awt.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import javax.swing.*;

import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONObject;


public class Main {

//    final static String API_KEY = "feb29daae1dc4f2eb888862eb560400d";
    final static String API_KEY = "7e5bcd096c4a4749818fdb0fa3aea0d3";
    final static String HOST = "https://westcentralus.api.cognitive.microsoft" +
            ".com";
    final static String PATH = "/text/analytics/v2.0/keyPhrases";

    public static String getKeyPhrases(Documents documents) throws Exception {
        String text = new Gson().toJson(documents);
        byte[] encodedText = text.getBytes("UTF-8");
        URL url = new URL(HOST + PATH);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "text/json");
        connection.setRequestProperty("Ocp-Apim-Subscription-Key", API_KEY);
        connection.setDoOutput(true);

        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.write(encodedText, 0, encodedText.length);
        wr.flush();
        wr.close();

        StringBuilder response = new StringBuilder();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();

        return response.toString();
    }

    public static String getCSVKeyWords(Documents documents) {
        String response = "";
        try {
            response = getKeyPhrases(documents);
        } catch (Exception e) {
            System.err.println("Error: Couldn't get keywords from APi.");
            System.exit(0);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < documents.documents.size(); ++i) {
                sb.append(documents.documents.get(i).id + '\n');
            }
            System.err.print("IDs: " + sb);
            return "";
        }

        JSONObject jsonResponse = new JSONObject(response);
        JSONObject docs = jsonResponse.getJSONArray("documents")
                .optJSONObject(0);
        JSONArray keyWords = docs.getJSONArray("keyPhrases");

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keyWords.length(); ++i) {
            sb.append(keyWords.getString(i) + ',');
        }

        // remove last comma
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public static void writeCSVToFile(String csv, File file) {
        file.delete();

        try {
            PrintWriter out = new PrintWriter(file.getPath());
            out.println(csv);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String readFile(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));

            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                contentBuilder.append(sCurrentLine).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }

    public static void writeToFile(String filePath, String content) {
        try {
            PrintWriter writer = new PrintWriter(filePath, "UTF-8");
            writer.println(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String getCSVString(JSONArray object) {
        return CDL.toString(object);
    }

    public static String getLink(JSONArray items, int id) {
        return items.getJSONObject(id).getJSONObject("selfLink").getString("href");
    }

    public static void pushToSQL(String titre, String keywords, int nbClick, String lien, int articleID){



        Connection.queryInsert(new String[]{"titre","keywords","click","lien","idArticle"}, new Object[]{titre, keywords, nbClick, lien, articleID});

    }
    public static void pushItems(JSONArray items) {
        for (int j = 0; j < items.length(); j++) {
            try {
                JSONObject item = items.getJSONObject(j);




                String lien = item.getJSONObject("canonicalWebLink").getString("href");
                SharedCounts objClicker = new SharedCounts(lien);

                int nbClick = objClicker.getNbClick();


                String titre = item.getString("title");

                int articleID = Integer.parseInt(item.getString("id"));
                Documents kwDoc = new Documents();
                String summary = item.getString("summary");

                kwDoc.add(articleID + "", "fr", summary );
                String keywordsXD = Main.getCSVKeyWords(kwDoc);


                System.out.println( " --Pushing ID: " + articleID + " Title: " + titre + " URL: " + lien + " lenght: " + items.length());

                pushToSQL(titre, keywordsXD, nbClick, lien, articleID);
            } catch (Exception e) {

            }
        }
    }

    public static void main(String[] args) throws Exception {

        LineupList lineupList = new LineupList("https://services.radio-canada.ca/hackathon/neuro/v1/future/lineups/");
        JSONArray lineupArray = lineupList.getItems();

        int i1 = 0;
        try {
            i1 = Integer.parseInt(args[0]);
        } catch (Exception e) {

        }
        i1=0;
        System.out.println("I1: " + i1);
        for (int i = i1; i < lineupArray.length(); i++) {

            try {
                Lineup lineup = new Lineup(getLink(lineupArray, i));
                System.out.println("\ni: " + i + " Name: " + lineup.getString("name") + " Lenght: " + lineupArray.length() + " id: " + lineup.getString("id") );
                pushItems(lineup.getItems());


                System.out.println("FINISHED PUSING LINEUP ID: " + i);
                i++;
            } catch (Exception e) {
            }
        }


    }


    public static int okcancel(String theMessage) {
        int result = JOptionPane.showConfirmDialog((Component) null, theMessage,
                "alert", JOptionPane.OK_CANCEL_OPTION);
        //OK:0, cancel:2
        return result;
    }


}
