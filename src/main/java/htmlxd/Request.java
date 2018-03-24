package htmlxd;

import javax.xml.ws.Response;
import java.net.URL;
import java.util.concurrent.Callable;

public class Request implements Callable<Response> {
    private URL url;

    public Request(URL url) {
        this.url = url;
    }

    @Override
    public Response call() throws Exception {
        return null;
    }
}

