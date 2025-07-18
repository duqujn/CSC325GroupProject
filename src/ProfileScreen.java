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
import javafx.util.*;
import javafx.animation.*;
import javafx.scene.image.Image;

import javax.swing.text.Document;
import java.util.HashMap;
import java.util.Map;


//user profile screen. Ideally preloaded with values from a database, might not have the time to full implement this tho
public class ProfileScreen {
    private Stage stage;
    private final String uID;
    private final Firestore db = FirestoreClient.getFirestore();
    private TextField firstName = new TextField();
    private TextField lastName = new TextField();
    private TextField username = new TextField();
    private TextField goalWeight = new TextField();
    private Label successLabel;


    ProfileScreen(Stage stage, String uID) {
        this.stage = stage;
        this.uID = uID;
    }

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
        updateProfileTask.setOnSucceeded(event -> {
            WriteResult result = updateProfileTask.getValue();
            successLabel.setVisible(true);
        });
        updateProfileTask.setOnFailed(event -> {
            Throwable exception = updateProfileTask.getException();
            showAlert(exception.getMessage(), exception.getCause().getMessage());
        });

        new Thread(updateProfileTask).start();
    }

    private void loadProfile(){
        ApiFuture<DocumentSnapshot> future = db.collection("users").document(uID).get();

        try{
            DocumentSnapshot doc = future.get();
            if(doc.exists()){
                Platform.runLater(() -> {
                    firstName.setText(doc.get("firstName").toString());
                    lastName.setText(doc.get("lastName").toString());
                    Object gw = doc.get("goalWeight");
                    goalWeight.setText(gw.toString());
                });
            }
        }catch (Exception e){
            showAlert(e.getMessage(), "Failed to Load Profile");
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
