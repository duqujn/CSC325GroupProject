import com.google.firebase.auth.FirebaseAuth;
import javafx.application.Application;
import javafx.stage.Stage;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.IOException;
import java.io.InputStream;

/**
 * appLauncher initalizes the firebaseAuth and starts the splash screen
 */
public class appLauncher extends Application {
    public static FirebaseAuth fauth;

    /**
     *
     * @param primaryStage stage to display the splashscreen
     * @throws IOException exception thrown if any errors occur during start();
     */
    @Override
    public void start(Stage primaryStage) throws IOException {

        initializeFireBase();
        fauth = FirebaseAuth.getInstance();
        SplashScreen splash = new SplashScreen(primaryStage);
        splash.show();
    }

    /**
     * main method to start the app
     * @param args - arguments to start the app
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * initizalise the firebase instance from the key.json file that includes all service account related
     * points for authentication
     * @throws IOException
     */
    private void initializeFireBase() throws IOException {
        InputStream serviceAccount = getClass().getResourceAsStream("/key.json");
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
        FirebaseApp.initializeApp(options);
        System.out.println("Firebase initialized");

    }
}
