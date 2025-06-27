import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.*;


//Login screen for diet app
// Ideally this will have authentication but we might not have the time to fully implement
public class LoginScreen {
    private Stage stage;
    private FirebaseAuthService authService = new FirebaseAuthService();

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
        PasswordField password = new PasswordField();
        password.setPromptText("Enter Your Password");

        //Login button to change screens to move to the main screen
        Button loginButton = new Button("Login");
        loginButton.setId("loginButton");
        loginButton.setPrefHeight(50);
        loginButton.setPrefWidth(150);

        //set functionality for login to return to the main screen
        loginButton.setOnAction(e -> {
            String usrname = username.getText();
            String pswd = password.getText();

            username.setDisable(true);
            password.setDisable(true);
            loginButton.setDisable(true);

            Task<String> loginTask = new Task<>() {
                @Override
                protected String call() throws Exception {
                    return authService.signIn(usrname, pswd);
                }
            };

            loginTask.setOnSucceeded(ev -> {
                String idToken = loginTask.getValue();
                loadMainScreen();
            });
            loginTask.setOnFailed(ev -> {
                Throwable exception = loginTask.getException();
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Login failed: " + exception.getMessage(),
                        ButtonType.OK);
            alert.showAndWait();

            username.setDisable(false);
            password.setDisable(false);
            loginButton.setDisable(false);
            });
            new Thread(loginTask).start();
        });
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
