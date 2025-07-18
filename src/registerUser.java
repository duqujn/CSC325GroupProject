import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.beans.IntrospectionException;
import java.util.*;
import java.util.concurrent.ExecutionException;


public class registerUser {
     private TextField emailField;
     private TextField phoneField;
     private TextField usernameField;
     private TextField firstNameField;
     private TextField lastNameField;
     private TextField weightGoalField;
     private PasswordField passwordField;

    public String regUser(String email, String phoneNumber, String pass, String username,
                        String firstName, String lastName, Double weightGoal) throws FirebaseAuthException {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setEmailVerified(false)
                .setPassword(pass)
                .setPhoneNumber(phoneNumber)
                .setDisplayName(firstName + " " + lastName)
                .setDisabled(false);

        UserRecord userRecord = appLauncher.fauth.createUser(request);
        writeUserProfile(userRecord.getUid(), firstName, lastName, weightGoal);

        return userRecord.getUid();
    }

    private void writeUserProfile(String uID, String firstName, String lastName, double weightGoal){
        Firestore db = FirestoreClient.getFirestore();
        Map<String, Object> userProfile = new HashMap<>();
        userProfile.put("firstName", firstName);
        userProfile.put("lastName", lastName);
        userProfile.put("weightGoal", weightGoal);

        ApiFuture<WriteResult> writeResult = db.collection("users").document(uID).set(userProfile);

        try{
            WriteResult result = writeResult.get();
            System.out.println("Profile written successfully at " + result.getUpdateTime());
        } catch (ExecutionException | InterruptedException e){
            e.printStackTrace();
            showAlert("Failed to create user", e.getMessage());
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
