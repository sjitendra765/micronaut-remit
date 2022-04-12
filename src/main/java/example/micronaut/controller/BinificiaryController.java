package example.micronaut.controller;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import example.micronaut.model.Beneficiary;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.HttpClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Controller("/beneficiary")
public class BinificiaryController {
    private final FirbaseInitialize firebaseInitialize;

    private HttpClient httpClient;

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
    public HttpResponse postMethod(@Body Beneficiary input) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = dbFirestore.collection(COL_NAME).whereEqualTo("mobile", input.mobile).get();

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

            String uniqueID = UUID.randomUUID().toString();
            ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COL_NAME).document(uniqueID).set(remit);
            System.out.println("document exist");
            return HttpResponse.ok().body(collectionsApiFuture.get().getUpdateTime().toString());
        }
        return HttpResponse.ok().body(documents.get(0).getData());
    }
}
