package example.micronaut.controller;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import example.micronaut.model.Remitter;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.*;
import io.micronaut.http.HttpResponse;

import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.rxjava2.http.client.RxHttpClient;

import javax.inject.Inject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;


@Controller("/remitter")
public class RemitterController {

    private final FirbaseInitialize firebaseInitialize;
    @Client("https://paysprint.in/service-api/api/v1/service/dmt/remitter")
    @Inject
    RxHttpClient httpClient;

    public RemitterController(FirbaseInitialize firebaseInitialize) throws MalformedURLException {
        this.firebaseInitialize = firebaseInitialize;
    }

    public static final String COL_NAME="remitters";

    @Get("/{mobile}/{bank3_flag}")
    public HttpResponse  index( String mobile , String bank3_flag ) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = dbFirestore.collection(COL_NAME).whereEqualTo("mobile", mobile).whereEqualTo("bank3_flag", bank3_flag).get();

        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        if(documents.isEmpty()){
            try{

                Map<String, Object> getBeneficiaryBody = new HashMap<>();
                getBeneficiaryBody.put("mobile",mobile);
                String getBody = getBeneficiaryBody.toString();
                String result = httpClient.toBlocking().retrieve(HttpRequest.POST("/queryremitter",getBody).header("Authorisedkey","Njc1MzlkZmNkODRiNzhlMzBhM2VkNWFkYzhmYWQyODM=").header("Token","eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0aW1lc3RhbXAiOjE2NDE4ODc5NTIsInBhcnRuZXJJZCI6IlBTMDAzNTIiLCJyZXFpZCI6IjIzNDU2NCJ9.P_nhptA53SROhEbIXlysMa9oUURBsXjSUgJLpbcvb7w"));
                System.out.println(result);
                /**
                 * TODO
                 * - After getting data from query remitter we need to fetch the data to the firestore and return

                 */
            } catch (Exception e) {
                System.out.println(e);
                e.printStackTrace();
            }

            return HttpResponse.ok().body("Not found");
        }
        return HttpResponse.ok().body(documents.get(0).getData());
        //return HttpResponse.ok().body("{\"msg\":\"This is JSON test from new api\"}");
    }

    @Post
    public HttpResponse postMethod(@Body Remitter input) throws ExecutionException, InterruptedException, IOException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = dbFirestore.collection(COL_NAME).whereEqualTo("mobile", input.mobile).whereEqualTo("bank3_flag", input.bank3_flag).get();

        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        if(documents.isEmpty()){

            Map<String, Object> remit = new HashMap<>();
            remit.put("mobile", input.mobile);
            remit.put("firstname", input.firstname);
            remit.put("lastname", input.lastname);
            remit.put("address", input.address);
            remit.put("otp", input.otp);
            remit.put("pincode", input.pincode);
            remit.put("stateresp", input.stateresp);
            remit.put("bank3_flag", input.bank3_flag);
            remit.put("dob", input.dob);
            remit.put("gst_state", input.gst_state);

            String remitBody = remit.toString();
            try{
                String result = httpClient.toBlocking().retrieve(HttpRequest.POST("/registerremitter",remitBody).header("Authorisedkey","Njc1MzlkZmNkODRiNzhlMzBhM2VkNWFkYzhmYWQyODM=").header("Token","eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0aW1lc3RhbXAiOjE2NDE4ODc5NTIsInBhcnRuZXJJZCI6IlBTMDAzNTIiLCJyZXFpZCI6IjIzNDU2NCJ9.P_nhptA53SROhEbIXlysMa9oUURBsXjSUgJLpbcvb7w"));
                System.out.println(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String uniqueID = UUID.randomUUID().toString();
            ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COL_NAME).document(uniqueID).set(remit);
            System.out.println("document exist");
            return HttpResponse.ok().body(collectionsApiFuture.get().getUpdateTime().toString());
        }
        return HttpResponse.ok().body(documents.get(0).getData());
    }
}


