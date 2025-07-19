import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Class with methods to register a new user
 */
public class registerUser {
    /**
     * public class that calls the private writeUserProfile method
     * this class is accessed as a controller to write new users to the database
     * @param email valid user email
     * @param phoneNumber valid user phone number
     * @param pass valid user password
     * @param firstName valid first name
     * @param lastName valid last name
     * @param weightGoal valid weight goal
     * @return the uID in order to keep users data siloed
     * @throws FirebaseAuthException error of db is unreachable
     */
    public String regUser(String email, String phoneNumber, String pass,
                        String firstName, String lastName, Double weightGoal) throws FirebaseAuthException {
        //build user record from inputs
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

    /**
     * private method to called within this class to write the new user to the database
     * @param uID user ID passed in
     * @param firstName valid first name
     * @param lastName valid last name
     * @param weightGoal valid goal weight
     */
    private void writeUserProfile(String uID, String firstName, String lastName, double weightGoal){
        //get the firestore db instance and create a map for addition to the database
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
}
