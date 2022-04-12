package example.micronaut.controller;
import io.micronaut.http.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import io.micronaut.gcp.function.http.*;

import java.util.HashMap;
import java.util.Map;

public class RemitterFunctionTest {
    @Test
    public void testGet() throws Exception {
        try (HttpFunction function = new HttpFunction()) {
            GoogleHttpResponse response = function.invoke(HttpMethod.GET, "/remitter/98XXXXXX76/yes");
            assertEquals(HttpStatus.OK, response.getStatus());
        }
    }

    @Test
    public void testPost()throws  Exception{
        try (HttpFunction function = new HttpFunction()) {
            Map<String, Object> remit = new HashMap<>();
            remit.put("mobile", "98XXXXXX76");
            remit.put("firstname", "XYZ ");
            remit.put("lastname", "ABCD");
            remit.put("address", "delhi");
            remit.put("otp", "112233");
            remit.put("pincode", "242424");
            remit.put("stateresp", "ad811ec6-073c-4402-98b1-a0a43a3e973d");
            remit.put("bank3_flag", "yes");
            remit.put("dob", "1990-03-02");
            remit.put("gst_state", "07");
            HttpRequest request = HttpRequest.POST("/remitter", remit).contentType(MediaType.APPLICATION_JSON_TYPE);
            GoogleHttpResponse response = function.invoke(request);
            assertEquals(HttpStatus.OK, response.getStatus());
        }
    }
}
