import com.google.firebase.auth.FirebaseAuthException;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.*;

/**
 * RegisterScreen to be accessed from the login Screen for new users to register a new account
 * in the firebase auth
 */

public class registerScreen {
    private final Stage stage;
    registerUser regController = new registerUser();


    public registerScreen(Stage stage) {
        this.stage = stage;
    }

    /**
     * public method to show the registerScreen
     */
    public void show() {
        BorderPane root = new BorderPane();
        Label title = new Label("Registration Screen");
        title.setId("titleLabel");

        VBox regInfo = new VBox();
        regInfo.setId("regInfo");
        TextField firstName = new TextField();
        firstName.setPromptText("First Name");
        TextField lastName = new TextField();
        lastName.setPromptText("Last Name");
        TextField weightGoalField = new TextField();
        weightGoalField.setPromptText("Weight Goal");
        TextField password = new PasswordField();
        password.setPromptText("Enter Password");
        TextField email = new TextField();
        email.setPromptText("Enter Email");
        TextField phone = new TextField();
        phone.setPromptText("Enter Phone Number");
        //+1 is preset to help users pass validation
        phone.setText("+1");

        Button submit = new Button("Submit");
        submit.setId("submitButton");
        //set functionality to validate and register new user upon clicking submit
        submit.setOnAction(e -> {
            String emailString = email.getText().trim();
            String phoneString = phone.getText().trim();
            String passwordString = password.getText().trim();
            String firstNameString = firstName.getText().trim();
            String lastNameString = lastName.getText().trim();
            //validating before sending to regUser
            if(!emailString.contains("@")) {
                showAlert("Invalid email address", "Please enter a valid email address");
                return;
            }
            if(passwordString.length() < 6 || passwordString.length() > 16) {
                showAlert("Invalid password", "Please enter a valid password");
                return;
            }
            if(!isValidPhoneNumber(phoneString))  {
                showAlert("Invalid phone number", "Please enter a valid phone number");
                return;
            }

            double weightGoal;
            try{
                weightGoal = Double.parseDouble(weightGoalField.getText());
            } catch (NumberFormatException ex) {
                showAlert("invalid goal", "Please enter a valid weight goal");
                return;
            }
            try {
                String uID = regController.regUser(emailString, phoneString, passwordString, firstNameString, lastNameString, weightGoal);
                new DietAppMainScreen(stage, uID).show();

            }catch (FirebaseAuthException ex){
                showAlert("registration error", ex.getMessage());
            }

        });

        regInfo.getChildren().addAll(email, password, phone, firstName, lastName, weightGoalField, title, submit);
        root.setCenter(regInfo);
        root.setTop(title);
        Scene scene = new Scene(root, 700, 600);
        stage.setScene(scene);
        stage.setTitle("Registration Screen");
        stage.show();

    }

    /**
     * public method for other methods to call when an error is thrown
     * @param title pass in title of the alert pane
     * @param message the error log that was thrown
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    /**
     * public method to be called from validation to validate and valid phone number
     * @param phoneNumber number input from the user
     * @return return true/false if phone number is valid
     */
    public boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("^\\+?\\d{10,15}$");
    }
}
