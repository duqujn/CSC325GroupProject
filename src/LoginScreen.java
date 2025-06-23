import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.*;
import javafx.animation.*;
import javafx.scene.control.*;



public class LoginScreen {
    private Stage stage;

    public LoginScreen(Stage stage) {
        this.stage = stage;
    }


    public void show() {
        Label label = new Label("Login to the Diet Application");
        label.setId("label2");

        Button loginButton = new Button("Login");
        loginButton.setId("loginButton");
        loginButton.setOnAction(e -> loadMainScreen());

        VBox vBox = new VBox(20, label, loginButton);
        vBox.setId("vBox");
        Scene scene = new Scene(vBox,400, 300);
        stage.setScene(scene);
        stage.setTitle("Diet Application Login");

    }

    private void loadMainScreen() {
        DietAppMainScreen mainScreen = new DietAppMainScreen(stage);
        mainScreen.show();
    }
}
