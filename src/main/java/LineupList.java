import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;

public class LineupList extends JSONObject{
    public LineupList(String link){



        super(getBody(link));
    }

    public static String getBody(String link) {
        String body = "";
        try {
            HttpResponse<String> response = Unirest.get("https://services.radio-canada.ca/hackathon/neuro/v1/future/lineups/?client_key=bf9ac6d8-9ad8-4124-a63c-7b7bdf22a2ee")
                    .header("Cache-Control", "no-cache")
                    .header("Postman-Token", "54c25c17-739d-4758-898f-3c2c0673f6bc")
                    .asString();
            body = response.getBody();
        } catch (UnirestException e) {
            e.printStackTrace();
            body = "";
        }
        return body;
    }

    public JSONArray getItems() {
        return this.getJSONArray("items");
    }
}
