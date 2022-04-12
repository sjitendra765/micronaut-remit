package example.micronaut.controller;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import example.micronaut.model.Remitter;
import io.micronaut.http.annotation.*;
import io.micronaut.http.HttpResponse;

import io.micronaut.http.client.HttpClient;

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
    private HttpClient httpClient;

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
            /*&
                TODO:
                    -call pay-sprint to see if there is data
                    - if there is data then save it to firestore and return
             */
            System.out.println("empty");
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
            /**
            HttpRequest request;
            request = HttpRequest.POST("https://paysprint.in/service-api/api/v1/service/dmt/remitter/registerremitter", input.toString());
            try {
                String response =  httpClient.toBlocking().retrieve(request, String.class);
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            /**
             * /*&
             *                 TODO:
             *                     -save data to the pay-sprint
             *
             *              */


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

            String uniqueID = UUID.randomUUID().toString();
            ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COL_NAME).document(uniqueID).set(remit);
            System.out.println("document exist");
            return HttpResponse.ok().body(collectionsApiFuture.get().getUpdateTime().toString());
        }
        return HttpResponse.ok().body(documents.get(0).getData());
    }
}


