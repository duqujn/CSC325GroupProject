import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.*;

/**
 * LoginScreen for users to enter their username/pass to gain access to the main screen
 * new users can also register an account if they dont already have one
 */
public class LoginScreen {
    private final Stage stage;
    private  FirebaseAuthService authService = new FirebaseAuthService();

    //constructor
    public LoginScreen(Stage stage) {
        this.stage = stage;
        try{
            authService = new FirebaseAuthService();
        }catch (Exception e){
            Platform.runLater(()->{
                new Alert(Alert.AlertType.ERROR,"Config Error: \n" + e.getMessage(), ButtonType.OK).showAndWait();
            });
        }
    }

    /**
     * public method to sow the loginScreen
     */
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

        //Login button to authenticate
        Button loginButton = new Button("Login");
        loginButton.setId("loginButton");
        loginButton.setPrefHeight(50);
        loginButton.setPrefWidth(150);

        //Register button to register a new user
        Button registerButton = new Button("Register");
        registerButton.setId("registerButton");
        registerButton.setPrefHeight(50);
        registerButton.setPrefWidth(150);

        //set functionality for a new user trying to register a new account
        registerButton.setOnAction(e -> {
         registerScreen registerScreen = new registerScreen(stage);
         registerScreen.show();
        });

        //set functionality for login to authenticate using Firebase
        loginButton.setOnAction(e -> {
            String usrname = username.getText();
            String pswd = password.getText();

            //disable the fields while authentication happens
            username.setDisable(true);
            password.setDisable(true);
            loginButton.setDisable(true);


            //create a Task to call the authService for login
            Task<String> loginTask = new Task<>() {
                @Override
                protected String call() throws Exception {
                    return authService.signIn(usrname, pswd);
                }
            };

            //on success switch screen to main screen
            loginTask.setOnSucceeded(ev -> {
                String uID = loginTask.getValue();
                new DietAppMainScreen(stage, uID).show();
            });
            //on failure display error message
            loginTask.setOnFailed(ev -> {
                Throwable exception = loginTask.getException();
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Login failed: " + exception.getMessage(),
                        ButtonType.OK);
            alert.showAndWait();

            //reenable fields
            username.setDisable(false);
            password.setDisable(false);
            loginButton.setDisable(false);
            });
            //create separate logical thread for login
            new Thread(loginTask).start();
        });
        loginInfo.getChildren().addAll(label, username, password, loginButton, registerButton);

        //set up BorderPane
        root.setCenter(loginInfo);

        //set up the scene and link the style.css
        Scene scene = new Scene(root,700, 600);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Diet Application Login");

    }
}
