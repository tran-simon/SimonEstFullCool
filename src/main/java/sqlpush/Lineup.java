package sqlpush;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Lineup extends JSONObject{
    public Lineup(String link){
        super(getBody(link));
    }

    public static String getBody(String link) {
        String bodyString = "";
        try {
            HttpResponse<String> response = Unirest.get(link)
                    .header("Authorization", "Client-Key bf9ac6d8-9ad8-4124-a63c-7b7bdf22a2ee")
                    .header("Cache-Control", "no-cache")
                    .header("Postman-Token", "8064e4a8-c861-48d8-9fd5-bc8ce25e0960")
                    .asString();
            bodyString = response.getBody();

        } catch (Exception e) {
            bodyString = "";
        }
        if (bodyString.equals("")) {
            System.out.println("USING TESTING BODY");
            bodyString = Main.readFile("testingBody.txt");
        }
        return bodyString;
    }

    public JSONArray getItems() {
        JSONObject pagedList = this.getJSONObject("pagedList");
        JSONArray jsonArray = pagedList.getJSONArray("items");
        ArrayList<JSONObject> objectList = new ArrayList();
        for (int i = 0; i < jsonArray.length(); i++) {
            objectList.add(jsonArray.getJSONObject(i));
        }

        return jsonArray;
    }
}
