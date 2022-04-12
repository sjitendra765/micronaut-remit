package example.micronaut.controller;
import io.micronaut.http.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import io.micronaut.gcp.function.http.*;

import java.util.HashMap;
import java.util.Map;

public class BinificiaryFunctiontest {
    @Test
    public void testGet() throws Exception {
        try (HttpFunction function = new HttpFunction()) {
            GoogleHttpResponse response = function.invoke(HttpMethod.GET, "/beneficiary/98XXXXXX76");
            assertEquals(HttpStatus.OK, response.getStatus());
        }
    }

    @Test
    public void testPost()throws  Exception{
        try (HttpFunction function = new HttpFunction()) {
            Map<String, Object> remit = new HashMap<>();
            remit.put("mobile", "98XXXXXX76");
            remit.put("benename", "XYZ ABCD");
            remit.put("bankid", "309");
            remit.put("accno", "5XXXXXXXXXXX2");
            remit.put("ifscode", "HDFC0000XXX");
            remit.put("verified", "0");
            remit.put("gst_state", "07");
            remit.put("dob", "1990-03-02");
            remit.put("address", "New delhi");
            remit.put("pincode", "2XXXX6");
            HttpRequest request = HttpRequest.POST("/beneficiary", remit).contentType(MediaType.APPLICATION_JSON_TYPE);
            GoogleHttpResponse response = function.invoke(request);
            assertEquals(HttpStatus.OK, response.getStatus());
        }
    }
}
