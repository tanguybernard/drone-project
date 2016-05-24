package fr.istic.sit.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author FireDroneTeam
 */

@Component
public class PushyAPI {
    public static ObjectMapper mapper = new ObjectMapper();
    public static final String PUSHY_SECRET_API_KEY = "f8f50fb45242e613a09b575e82c489fc669f67ee0254b48f53f2f5b135c1df3e";

    private final Logger log = LoggerFactory.getLogger(PushyAPI.class);


    public void sendPush( PushyPushRequest req ) throws Exception
    {

        // Get custom HTTP client
        HttpClient client = new DefaultHttpClient();

        // Create post request
        HttpPost request = new HttpPost( "https://api.pushy.me/push?api_key=" + PUSHY_SECRET_API_KEY );

        // Set content type to JSON
        request.addHeader("Content-Type", "application/json");

        // Convert API request to JSON
        String json = mapper.writeValueAsString(req);

        // Send post data as string
        request.setEntity(new StringEntity(json));

        // Execute the request
        HttpResponse response = client.execute(request, new BasicHttpContext());

        // Get response JSON as string
        String responseJSON = EntityUtils.toString(response.getEntity());

        log.debug("Response send notification "+ responseJSON);

        // Convert JSON response into HashMap
        Map<String, Object> map = mapper.readValue(responseJSON, Map.class);

        // Got an error?
        if ( map.containsKey("error") )
        {
            log.info("ERROR Sending notification...");
            // Throw it
            throw new Exception(map.get("error").toString());
        }

        log.info("Send notification OK");
    }

}
