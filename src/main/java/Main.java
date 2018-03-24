import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;

public class Main {

    public static JSONArray getItems(String bodyString) {
        JSONObject body = new JSONObject(bodyString);
        JSONObject pagedList = body.getJSONObject("pagedList");
        JSONArray jsonArray = pagedList.getJSONArray("items");
        ArrayList<JSONObject> objectList = new ArrayList();
        for (int i = 0; i < jsonArray.length(); i++) {
            objectList.add(jsonArray.getJSONObject(i));
        }

        return jsonArray;
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

    private static String readFile(String filePath) {
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

    public static String getBodyString() {
        String bodyString = "";
        try {
            HttpResponse<String> response = Unirest.get("https://services.radio-canada.ca/hackathon/neuro/v1/future/lineups/475289?pageNumber=1")
                    .header("Authorization", "Client-Key bf9ac6d8-9ad8-4124-a63c-7b7bdf22a2ee")
                    .header("Cache-Control", "no-cache")
                    .header("Postman-Token", "8064e4a8-c861-48d8-9fd5-bc8ce25e0960")
                    .asString();
            bodyString = response.getBody();

        } catch (Exception e) {
            System.out.println("USING TESTING BODY");
            bodyString = "";
        }


        if (bodyString.equals("")) {
            bodyString = readFile("testingBody.txt");
        }
        return bodyString;
    }




    public static String getLink(JSONArray items, int id){
        return items.getJSONObject(id).getJSONObject("selfLink").getString("href");
    }
    public static void main(String[] args) {
        JSONArray items = getItems(getBodyString());
        String path = "items.csv";
        writeCSVToFile(CDL.toString(items), new File(path));
        System.out.println(getLink(items, 1));
//        News news = new News(items);
    }
}
