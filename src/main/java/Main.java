import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;

public class Main {



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





    public static String getLink(JSONArray items, int id){
        return items.getJSONObject(id).getJSONObject("selfLink").getString("href");
    }
    public static void main(String[] args) {
        int nbClick = 0;
        String link = "https://services.radio-canada.ca/hackathon/neuro/v1/future/lineups/475289?pageNumber=1";
        Lineup lineup = new Lineup(link);
        JSONArray items = lineup.getItems();
        String path = "items.csv";
        writeCSVToFile(CDL.toString(items), new File(path));

        link = getLink(items, 0);
        News news = new News(link);
//        System.out.println(news.toString());
//        System.out.println(news.getTitle());
//        System.out.println(news.getID());
//        System.out.println(news.getString("summary"));
//        System.out.println(news.getHTML());
        writeToFile("XD.html", news.getHTML());
        
        SharedCounts objClicker = new SharedCounts(news.getURL());
        nbClick = objClicker.getNbClick();
        System.out.println(nbClick);
//        News news = new News(items);
    }
}
