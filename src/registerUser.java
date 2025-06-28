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

    public void registerUser(String email, String phoneNumber, String pass, String username) {
        if(email.isEmpty() || !email.contains("@")) {
            showAlert("Invalid email address", "Please enter a valid email address");
            return;
        }
        if(pass.length() < 6 || pass.length() > 16) {
            showAlert("Invalid password", "Please enter a valid password");
            return;
        }
        if(phoneNumber.isEmpty() || !isValidPhoneNumber(phoneNumber))  {
            showAlert("Invalid phone number", "Please enter a valid phone number");
            return;
        }

        email = email.trim();
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setEmailVerified(false)
                .setPassword(pass)
                .setPhoneNumber(phoneNumber)
                .setDisplayName(username)
                .setDisabled(false);

        UserRecord userRecord;
        try {
            userRecord = appLauncher.fauth.createUser(request);
            showAlert("Success!", "User created successfully");


        } catch (FirebaseAuthException ex) {
            showAlert("Failed to create user", ex.getMessage());
        }

    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    public boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("^\\+?\\d{10,15}$");
    }
}
