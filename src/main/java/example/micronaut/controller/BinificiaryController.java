package example.micronaut.controller;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import example.micronaut.model.Beneficiary;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;

import io.micronaut.http.client.annotation.Client;
import io.micronaut.rxjava2.http.client.*;
import io.micronaut.http.client.HttpClient;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Controller("/beneficiary")
public class BinificiaryController {
    private final FirbaseInitialize firebaseInitialize;

    @Client("https://paysprint.in/service-api/api/v1/service/dmt/beneficiary/registerbeneficiary")
    @Inject
    RxHttpClient httpClient;

    public BinificiaryController(FirbaseInitialize firebaseInitialize) {
        this.firebaseInitialize = firebaseInitialize;
    }

    public static final String COL_NAME="beneficiaries";

    @Get("/{mobile}")
    public HttpResponse index(String mobile ) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = dbFirestore.collection(COL_NAME).whereEqualTo("mobile", mobile).get();

        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        if(documents.isEmpty()){
            try{

                Map<String, Object> getBeneficiaryBody = new HashMap<>();
                getBeneficiaryBody.put("mobile",mobile);
                String getBody = getBeneficiaryBody.toString();
                String result = httpClient.toBlocking().retrieve(HttpRequest.POST("/fetchbeneficiary",getBody).header("Authorisedkey","Njc1MzlkZmNkODRiNzhlMzBhM2VkNWFkYzhmYWQyODM=").header("Token","eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0aW1lc3RhbXAiOjE2NDE4ODc5NTIsInBhcnRuZXJJZCI6IlBTMDAzNTIiLCJyZXFpZCI6IjIzNDU2NCJ9.P_nhptA53SROhEbIXlysMa9oUURBsXjSUgJLpbcvb7w"));
                /**
                 * TODO
                 * - After getting data from query beneficiary we need to fetch the data to the firestore and return

                 */
                System.out.println(result);
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
    public HttpResponse postMethod(@Body Beneficiary input) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = dbFirestore.collection(COL_NAME).whereEqualTo("mobile", input.mobile).get();

        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        if(documents.isEmpty()){
           
            System.out.println("empty");
            Map<String, Object> remit = new HashMap<>();
            remit.put("mobile", input.mobile);
            remit.put("benename", input.benename);
            remit.put("bankid", input.bankid);
            remit.put("accno", input.accno);
            remit.put("ifscode", input.ifscode);
            remit.put("verified", input.verified);
            remit.put("gst_state", input.gst_state);
            remit.put("dob", input.dob);
            remit.put("address", input.address);
            remit.put("pincode", input.pincode);

            String remitBody = remit.toString();
            try{
                String result = httpClient.toBlocking().retrieve(HttpRequest.POST("/",remitBody).header("Authorisedkey","Njc1MzlkZmNkODRiNzhlMzBhM2VkNWFkYzhmYWQyODM=").header("Token","eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0aW1lc3RhbXAiOjE2NDE4ODc5NTIsInBhcnRuZXJJZCI6IlBTMDAzNTIiLCJyZXFpZCI6IjIzNDU2NCJ9.P_nhptA53SROhEbIXlysMa9oUURBsXjSUgJLpbcvb7w"));
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
