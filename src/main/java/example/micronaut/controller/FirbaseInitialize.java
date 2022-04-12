package example.micronaut.controller;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import io.micronaut.context.annotation.Bean;
import io.micronaut.core.annotation.Introspected;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
//import javax.inject.Singleton;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

@jakarta.inject.Singleton
public class FirbaseInitialize {
    Logger logger = LoggerFactory.getLogger(FirbaseInitialize.class);

    @PostConstruct
    public void initialize() {


        try {
            File directory = new File("./");
            System.out.println("document exist"+ directory.getAbsolutePath());
            FileInputStream serviceAccount =
                    new FileInputStream("./serviceAccount.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://micronaut-remit-default-rtdb.firebaseio.com/")
                    .build();

            FirebaseApp.initializeApp(options);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

