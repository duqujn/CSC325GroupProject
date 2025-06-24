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
        BorderPane root = new BorderPane();


        Label label = new Label("Login to the Diet Application");
        label.setId("label2");
        VBox loginInfo = new VBox(20);
        loginInfo.setId("loginInfo");

        TextField username = new TextField();
        username.setPromptText("Enter Your Username");
        TextField password = new TextField();
        password.setPromptText("Enter Your Password");


        Button loginButton = new Button("Login");
        loginButton.setId("loginButton");
        loginButton.setPrefHeight(50);
        loginButton.setPrefWidth(150);

        loginButton.setOnAction(e -> loadMainScreen());
        loginInfo.getChildren().addAll(label, username, password, loginButton);

        root.setCenter(loginInfo);

        Scene scene = new Scene(root,700, 600);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Diet Application Login");

    }

    private void loadMainScreen() {
        DietAppMainScreen mainScreen = new DietAppMainScreen(stage);
        mainScreen.show();
    }
}
