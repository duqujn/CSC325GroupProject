import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.*;
import javafx.animation.*;


public class SplashScreen {
    private Stage stage;

    public SplashScreen(Stage stage) {
        this.stage = stage;
    }


    public void show() {
        Label label = new Label("Welcome to the Diet and Wellness App");
        label.setId("label1");

        StackPane root = new StackPane(label);
        Scene scene = new Scene(root, 400, 300);

        stage.setTitle("Diet and Wellness App");
        stage.setScene(scene);
        stage.show();

        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> loadLoginScreen(stage));
        pause.play();
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
    }

    private void loadLoginScreen(Stage stage) {
        LoginScreen loginScreen = new LoginScreen(stage);
        loginScreen.show();
    }
}
