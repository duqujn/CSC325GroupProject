import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.*;
import javafx.animation.*;
import javafx.scene.control.*;


//Login screen for diet app
// Ideally this will have authentication but we might not have the time to fully implement
public class LoginScreen {
    private Stage stage;

    //constructor
    public LoginScreen(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        BorderPane root = new BorderPane();


        Label label = new Label("Login to the Diet Application");
        label.setId("label2");
        VBox loginInfo = new VBox(20);
        loginInfo.setId("loginInfo");

        TextField username = new TextField();
        username.setPromptText("Enter Your Username");
        TextField password = new TextField();
        password.setPromptText("Enter Your Password");

        //Login button to change screens to move to the main screen
        Button loginButton = new Button("Login");
        loginButton.setId("loginButton");
        loginButton.setPrefHeight(50);
        loginButton.setPrefWidth(150);

        //set functionality for login to return to the main screen
        loginButton.setOnAction(e -> loadMainScreen());
        loginInfo.getChildren().addAll(label, username, password, loginButton);

        //set up BorderPane
        root.setCenter(loginInfo);

        //set up the scene and link the style.css
        Scene scene = new Scene(root,700, 600);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Diet Application Login");

    }

    //private method to return to the main screen
    private void loadMainScreen() {
        DietAppMainScreen mainScreen = new DietAppMainScreen(stage);
        mainScreen.show();
    }
}
