import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


public  class registerUser {
     private TextField emailField;
     private TextField phoneField;
     private TextField usernameField;
     private PasswordField passwordField;


    private void regRecord(ActionEvent event) {
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String password = passwordField.getText().trim();
        String name = usernameField.getText().trim();
        registerUser(email, phone, password, name);
    }

    public boolean registerUser(String usrname, String pass, String phoneNumber, String email) {
        if(email.isEmpty() || !email.contains("@")) {
            showAlert("Invalid email address", "Please enter a valid email address");
            return false;
        }
        if(pass.length() < 6 || pass.length() > 16) {
            showAlert("Invalid password", "Please enter a valid password");
            return false;
        }
        email = email.trim();
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setEmailVerified(false)
                .setPassword(pass)
                .setPhoneNumber(phoneNumber)
                .setDisplayName(usrname)
                .setDisabled(false);

        UserRecord userRecord;
        try {
            userRecord = appLauncher.fauth.createUser(request);
            showAlert("Success!", "User created successfully");
            return true;

        } catch (FirebaseAuthException ex) {
            showAlert("Failed to create user", ex.getMessage());
            return false;
        }

    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
