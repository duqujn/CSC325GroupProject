import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.*;



public class registerScreen {
    private final Stage stage;
    private final FirebaseAuthService authService = new FirebaseAuthService();

    public registerScreen(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        BorderPane root = new BorderPane();
        Label title = new Label("Registration Screen");
        title.setId("titleLabel");

        VBox regInfo = new VBox();
        regInfo.setId("regInfo");
        TextField username = new TextField();
        username.setPromptText("Enter Username");
        TextField password = new PasswordField();
        password.setPromptText("Enter Password");
        TextField email = new TextField();
        email.setPromptText("Enter Email");
        TextField phone = new TextField();
        phone.setPromptText("Enter Phone Number");

        Button submit = new Button("Submit");
        submit.setId("submitButton");
        submit.setOnAction(e -> {
            registerUser registerUser = new registerUser();
            String emailString = email.getText();
            String phoneString = phone.getText();
            String usernameString = username.getText();
            String passwordString = password.getText();

            registerUser.regUser(emailString, phoneString, passwordString, usernameString);

            DietAppMainScreen dietAppMainScreen = new DietAppMainScreen(stage);
            dietAppMainScreen.show();

        });

        regInfo.getChildren().addAll(username, password, email, phone, title,submit);
        root.setCenter(regInfo);
        root.setTop(title);
        Scene scene = new Scene(root, 700, 600);
        stage.setScene(scene);
        stage.setTitle("Registration Screen");
        stage.show();

    }
}
