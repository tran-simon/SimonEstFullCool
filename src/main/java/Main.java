import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONArray;
import org.json.JSONObject;

class Document {
    public String id, language, text;

    public Document(String id, String language, String text){
        this.id = id;
        this.language = language;
        this.text = text;
    }
}

class Documents {
    public List<Document> documents;

    public Documents() {
        this.documents = new ArrayList<Document> ();
    }
    public void add(String id, String language, String text) {
        this.documents.add (new Document (id, language, text));
    }
}

public class Main {

    final static String API_KEY = "feb29daae1dc4f2eb888862eb560400d";
    final static String HOST = "https://westcentralus.api.cognitive.microsoft" +
                               ".com";
    final static String PATH = "/text/analytics/v2.0/keyPhrases";

    public static  String getKeyPhrases (Documents documents) throws Exception {
        String text = new Gson().toJson(documents);
        byte[] encodedText = text.getBytes ("UTF-8");
        URL                url        = new URL(HOST + PATH);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "text/json");
        connection.setRequestProperty("Ocp-Apim-Subscription-Key", API_KEY);
        connection.setDoOutput(true);

        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.write(encodedText, 0, encodedText.length);
        wr.flush();
        wr.close();

        StringBuilder response = new StringBuilder ();
        BufferedReader in = new BufferedReader(
            new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();

        return response.toString();
    }

    public static String prettify(String json_text) {
        JsonParser parser = new JsonParser ();
        JsonObject json   = parser.parse(json_text).getAsJsonObject();
        Gson       gson   = new GsonBuilder ().setPrettyPrinting().create();
        return gson.toJson(json);
    }

    public static String getCSVKeyWords(Documents documents) {
        String response = "";
        try {
            response = getKeyPhrases (documents);
        } catch (Exception e) {
            System.err.println ("Error: Couldn't get keywords from APi.");
            StringBuilder sb = new StringBuilder ();
            for (int i = 0; i < documents.documents.size (); ++i) {
                sb.append (documents.documents.get (i).id + '\n');
            }
            System.err.print ("IDs: " + sb);
            return "";
        }

        JSONObject jsonResponse = new JSONObject (response);
        JSONObject docs = jsonResponse.getJSONArray ("documents")
            .optJSONObject (0);
        JSONArray keyWords = docs.getJSONArray ("keyPhrases");

        StringBuilder sb = new StringBuilder ();
        for (int i = 0; i < keyWords.length (); ++i) {
            sb.append (keyWords.getString (i) + ',');
        }

        // remove last comma
        sb.deleteCharAt (sb.length () - 1);
        return sb.toString ();
    }

    public static JSONArray getItems(HttpResponse<String> response) {
        JSONObject jsonObject = new JSONObject(response);

        Iterator iterator = jsonObject.keys();

        while(iterator.hasNext ()) {
            System.out.println (iterator.next ());
        }

        JSONObject body = new JSONObject(jsonObject.getString("body"));
        JSONObject pagedList = body.getJSONObject("pagedList");
//        System.out.println(pagedList.get("items").getClass());
        JSONArray jsonArray = pagedList.getJSONArray("items");

        return jsonArray;
    }

    public static void main(String[] args) throws Exception {
        // use example
        Documents documents = new Documents ();
        documents.add ("1", "en", "I really enjoy the new XBox One S. It has a clean look, it has 4K/HDR resolution and it is affordable.");
        System.out.println (getCSVKeyWords (documents));


        try {
            HttpResponse<String> response = Unirest
                .get("https://services.radio-canada.ca/hackathon/neuro/v1/future/lineups/475289?pageNumber=1")
                    .header("Authorization", "Client-Key bf9ac6d8-9ad8-4124-a63c-7b7bdf22a2ee")
                    .header("Cache-Control", "no-cache")
                    .header("Postman-Token", "8064e4a8-c861-48d8-9fd5-bc8ce25e0960")
                    .asString();


            JSONArray items = getItems(response);

            System.out.println(items.getJSONObject(0).get("selfLink"));

        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }
}
