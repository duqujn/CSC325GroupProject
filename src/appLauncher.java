import javafx.application.Application;
import javafx.stage.Stage;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.firestore.Firestore;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

//appLaucher class to set up a splashscreen for starting up the app
public class appLauncher extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        //call the splashscreen class constructor to show.
        initializeFireBase();
        Firestore db = FirestoreClient.getFirestore();
        SplashScreen splash = new SplashScreen(primaryStage);
        splash.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
    private void initializeFireBase() throws IOException {
        InputStream serviceAccount = getClass().getResourceAsStream("/key.json");
        FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();
        FirebaseApp.initializeApp(options);
        System.out.println("Firebase initialized");

    }
}
