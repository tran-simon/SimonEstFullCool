import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class News {
    String body = "";

    public News(String link) {
        getBody(link);
    }
    public void getBody(String link){
        try {
            HttpResponse<String> response = Unirest.get(link)
                    .header("Authorization", "Client-Key bf9ac6d8-9ad8-4124-a63c-7b7bdf22a2ee")
                    .header("Cache-Control", "no-cache")
                    .header("Postman-Token", "06ec1375-dab7-46f1-87b6-c3250c9acbe5")
                    .asString();
            body = response.getBody();
        } catch (UnirestException e) {
            System.out.println("USING TESTING BODY");
            body = "";
        }
        if(body.equals("")){
            System.out.println("BODY EMPTY");
            System.exit(0);
        }


    }
}
