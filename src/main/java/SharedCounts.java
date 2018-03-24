
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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
        
        HttpURLConnection connection = null;

        try {
          //Create connection
          URL url = new URL(DOMAIN);
          connection = (HttpURLConnection) url.openConnection();
          connection.setRequestMethod("POST");
          connection.setRequestProperty("Content-Type", 
              "application/x-www-form-urlencoded");

          connection.setRequestProperty("Content-Length", 
              Integer.toString(urlParameters.getBytes().length));
          connection.setRequestProperty("Content-Language", "en-US");  

          connection.setUseCaches(false);
          connection.setDoOutput(true);

          //Send request
          DataOutputStream wr = new DataOutputStream (
              connection.getOutputStream());
          wr.writeBytes(urlParameters);
          wr.close();

          //Get Response  
          InputStream is = connection.getInputStream();
          BufferedReader rd = new BufferedReader(new InputStreamReader(is));
          StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
          String line;
          while ((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
          }
          rd.close();
          return response.toString();
        } catch (Exception e) {
          e.printStackTrace();
          return null;
        } finally {
          if (connection != null) {
            connection.disconnect();
          }
        }
        
        
        return 0;
    }
}
