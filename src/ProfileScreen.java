import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.*;
import javafx.animation.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import java.awt.*;


public class ProfileScreen {
    private Stage stage;

    ProfileScreen(Stage stage) {
        this.stage = stage;
    }

    public void show(){
        BorderPane root = new BorderPane();

        Label successLabel = new Label("Saved Successfully");
        successLabel.setId("successLabel");
        successLabel.setVisible(false);

        Image profileImage = new Image("/images/userIcon.jpg");
        ImageView profileImageView = new ImageView(profileImage);
        profileImageView.setFitHeight(100);
        profileImageView.setFitWidth(100);
        VBox leftbox = new VBox(profileImageView);
        leftbox.setId("profileLeftbox");

        VBox rightbox = new VBox();
        TextField firstName = new TextField();
        firstName.setPromptText("First Name");
        TextField lastName = new TextField();
        lastName.setPromptText("Last Name");
        TextField username = new TextField();
        username.setPromptText("Username");
        TextField goalWeight = new TextField();
        goalWeight.setPromptText("Goal Weight");
        Button saveButton = new Button("Save");
        Button backButton = new Button("Back to Main Screen");
        rightbox.getChildren().addAll(firstName, lastName, username, goalWeight, saveButton, backButton);

        saveButton.setOnAction(event -> {
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(e -> successLabel.setVisible(true));
            pause.play();
        });
        backButton.setOnAction(e -> loadMainScreen());
        root.setLeft(leftbox);
        root.setCenter(rightbox);
        root.setBottom(successLabel);

        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Profile Screen");
    }
    private void loadMainScreen() {
        DietAppMainScreen mainScreen = new DietAppMainScreen(stage);
        mainScreen.show();
    }
}
