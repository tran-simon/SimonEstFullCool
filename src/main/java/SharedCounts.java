
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author root
 */
public class SharedCounts {
    private String url;
    private final String DOMAIN = "https://api.sharedcount.com/v1.0/";
    private final String APIKEY = "83fcad976cc23fc8868c7d4e9d78994f8b887fce";
    
    
    public SharedCounts(String url) {
        this.url = url;
        
    }
    
    public int getNbClick() {
        int count = 0;
        JSONObject responseJSON;
        
        responseJSON = new JSONObject(postOnShared());
        
        count = responseJSON.getInt("StumbleUpon");
        count += responseJSON.getJSONObject("Facebook").getInt("total_count");
        count += responseJSON.getInt("Pinterest");
        
        
        return count;
    }
    
    private String postOnShared() { //return le id de la requête
        
        int count = 0;
        
        String bodyString = "";
        String url = DOMAIN + "?" + "apikey=" + APIKEY + "&" + "url=" + this.url;
        try {
            HttpResponse<String> response = Unirest.get(url).asString();
            bodyString = response.getBody();

        } catch (Exception e) {
            bodyString = "";
        }
        
        /*
        if(responseJSON.get("LinkedIn") != null){
            count += responseJSON.getInt("LinkedIn");
        }*/
        
        return bodyString;
    }
    
}
