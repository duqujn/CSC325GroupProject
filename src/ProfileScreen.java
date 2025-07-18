import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.SetOptions;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import java.util.HashMap;
import java.util.Map;


/**
 * Profile screen that displays current user values and allows user to update these values
 */
public class ProfileScreen {
    private Stage stage;
    private final String uID;
    private final Firestore db = FirestoreClient.getFirestore();
    private TextField firstName = new TextField();
    private TextField lastName = new TextField();
    private TextField username = new TextField();
    private TextField goalWeight = new TextField();
    private Label successLabel;

    /**
     * contructor that takes in a stage to be displayed on
     * and uID for specific user data retrieval
     * @param stage stage to be passed upon creation
     * @param uID uID to be passed for specific user data retrieval
     */
    ProfileScreen(Stage stage, String uID) {
        this.stage = stage;
        this.uID = uID;
    }

    /**
     * public method to show the profile screen
     */
    public void show(){
        //set up borderpane
        BorderPane root = new BorderPane();

        //create cheer label for save button
        successLabel = new Label("Saved Successfully");
        successLabel.setId("successLabel");
        successLabel.setVisible(false);

        //load user profile image
        Image profileImage = new Image("/images/userIcon.jpg");
        ImageView profileImageView = new ImageView(profileImage);
        profileImageView.setFitHeight(100);
        profileImageView.setFitWidth(100);

        //set up leftbox for ImageView
        VBox leftbox = new VBox(profileImageView);
        leftbox.setId("profileLeftbox");

        //set up rightbox for editable text fields
        VBox rightbox = new VBox();
        loadProfile();
        firstName = new TextField();
        lastName = new TextField();
        goalWeight = new TextField();
        firstName.setPromptText("First Name");
        lastName.setPromptText("Last Name");
        username.setPromptText("Username");
        goalWeight.setPromptText("Goal Weight");

        //set up the buttons
        Button saveButton = new Button("Save");
        Button backButton = new Button("Back to Main Screen");
        rightbox.getChildren().addAll(firstName, lastName, username, goalWeight, saveButton, backButton);

        //save button cheer functionality
        saveButton.setOnAction(event -> saveProfile());

        //set up backButton Action to return to the mainscreen
        backButton.setOnAction(e ->{new DietAppMainScreen(stage, uID).show();
        });
        //attach vbox and label to the borderpane
        root.setLeft(leftbox);
        root.setCenter(rightbox);
        root.setBottom(successLabel);

        //set up the scene and link the style.css
        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Profile Screen");
    }

    /**
     * private method that is called when the save button is pressed
     * This method saves the user edits to the database and then displays a success or failure message
     */
    private void saveProfile(){
        Map<String, Object> profileUpdate = new HashMap<>();
        profileUpdate.put("firstName", firstName.getText().trim());
        profileUpdate.put("lastName", lastName.getText().trim());

        try{
            double goal = Double.parseDouble(goalWeight.getText().trim());
            profileUpdate.put("weightGoal", goal);
        }catch (NumberFormatException e){
            showAlert(e.getMessage(), "Invalid weight goal");
        }

        //create a task to be run as a separate thread for performance
        //this overrides the writeResult in order to get the specific instance of the db, as well ass
        //access to the correct collection in the firestore db
        Task<WriteResult> updateProfileTask = new Task<WriteResult>() {
            @Override
            protected WriteResult call() throws Exception {
                Firestore db = FirestoreClient.getFirestore();
                ApiFuture<WriteResult> future = db
                        .collection("users")
                        .document(uID)
                        .set(profileUpdate, SetOptions.merge());
                return future.get();
            }
        };
        //upon success display the success label
        updateProfileTask.setOnSucceeded(event -> {
            successLabel.setVisible(true);
        });
        //upon failure throw an exception
        updateProfileTask.setOnFailed(event -> {
            Throwable exception = updateProfileTask.getException();
            showAlert(exception.getMessage(), exception.getCause().getMessage());
        });

        new Thread(updateProfileTask).start();
    }

    /**
     * private method to be called to load the specific user data already in the database
     */
    private void loadProfile(){
        ApiFuture<DocumentSnapshot> future = db.collection("users").document(uID).get();

        try{
            DocumentSnapshot doc = future.get();
            if(doc.exists()){
                Platform.runLater(() -> {
                    firstName.setText(doc.get("firstName").toString());
                    lastName.setText(doc.get("lastName").toString());
                    Object gw = doc.get("weightGoal");
                    goalWeight.setText(gw.toString());
                });
            }
        }catch (Exception e){
            showAlert(e.getMessage(), "Failed to Load Profile");
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
