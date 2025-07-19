import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.*;
import javafx.animation.*;

/**
 * SplashScreen Class that pauses for 3 seconds after start up and then transferrers the users
 * to the login screen
 */
public class SplashScreen {
    private Stage stage;
    //constructor

    /**
     * Splash screen constructor that takes in a new stage to be displayed on
     * @param stage - stage to for splash screen to be displayed one
     */
    public SplashScreen(Stage stage) {
        this.stage = stage;
    }

    /**
     * public show() method to show the splash screen
     */
    public void show() {
        //Splashscreen message
        Label label = new Label("Welcome to the Diet and Wellness App");
        label.setId("label1");

        //setting up the StackPane and scene for display
        StackPane root = new StackPane(label);
        Scene scene = new Scene(root, 400, 300);

        stage.setTitle("Diet and Wellness App");
        stage.setScene(scene);
        stage.show();

        //connecting style.css
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        //Timed transition to Login Page
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> loadLoginScreen(stage));
        pause.play();
    }

    /**
     * method to take the stage and display the login screen
     * @param stage
     */
    private void loadLoginScreen(Stage stage) {
        LoginScreen loginScreen = new LoginScreen(stage);
        loginScreen.show();
    }
}
