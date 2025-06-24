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

public class DietAppMainScreen {

    private Stage stage;

    public DietAppMainScreen(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        MenuBar menuBar = new MenuBar();
        menuBar.setId("menuBar");
        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");
        MenuItem profEdit = new MenuItem("Profile");

        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(e -> System.exit(0));
        profEdit.setOnAction(e -> loadProfileScreen());

        fileMenu.getItems().addAll( exitMenuItem);
        editMenu.getItems().addAll(profEdit);
        menuBar.getMenus().addAll(fileMenu, editMenu);

        //left box with profile image and basic information like first name, weight goal and diet name
        Image profileImage = new Image("/images/userIcon.jpg");
        ImageView profileImageView = new ImageView(profileImage);
        profileImageView.setFitHeight(100);
        profileImageView.setFitWidth(100);
        VBox leftbox = new VBox(profileImageView);
        leftbox.setId("leftbox");
        Label nameLabel = new Label("Name: Users First Name");
        nameLabel.setId("nameLabel");
        Label goalLabel = new Label("Goal: Users Goal Weight");
        goalLabel.setId("goalLabel");
        Label calorieLabel = new Label("Total Calories Available Today");
        calorieLabel.setId("calorieLabel");
        leftbox.getChildren().addAll(nameLabel, goalLabel, calorieLabel);

        //Center column layout to display meals entered
        TableView tableView = new TableView();
        TableColumn<MealEntry, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("dateEntered"));
        TableColumn<MealEntry, String> mealCol = new TableColumn<>("Meal");
        mealCol.setCellValueFactory(new PropertyValueFactory<>("mealName"));
        TableColumn<MealEntry, String> calCol = new TableColumn<>("Calories");
        calCol.setCellValueFactory(new PropertyValueFactory<>("calories"));
        TableColumn<MealEntry, String> proteinCol = new TableColumn<>("Protein");
        proteinCol.setCellValueFactory(new PropertyValueFactory<>("protein"));
        TableColumn<MealEntry, String> carbCol = new TableColumn<>("Carbs");
        carbCol.setCellValueFactory(new PropertyValueFactory<>("carbs"));
        TableColumn<MealEntry,String> fatCol = new TableColumn<>("Fat");
        fatCol.setCellValueFactory(new PropertyValueFactory<>("fat"));

        tableView.getColumns().addAll(dateCol, mealCol, calCol, proteinCol, carbCol,fatCol);


        VBox rightbox = new VBox();
        Label rightBoxLabel = new Label("Enter Meal Info");
        rightBoxLabel.setId("rightBoxLabel");
        rightbox.setId("rightbox");  //profile screen with target weight and time frame, which can be displayed in the main screen
        TextField date  = new TextField();
        TextField meal  = new TextField();
        TextField cal  = new TextField();
        TextField protein = new TextField();
        TextField carb = new TextField();
        TextField fat = new TextField();
        date.setPromptText("Enter Date");
        meal.setPromptText("Enter Meal");
        cal.setPromptText("Enter Calories");
        protein.setPromptText("Enter Protein");
        carb.setPromptText("Enter Carbs");
        fat.setPromptText("Enter Fat");
        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> {
            String dateEntered = date.getText();
            String mealEntered = meal.getText();
            String caloriesEntered = cal.getText();
            String proteinEntered = protein.getText();
            String carbEntered = carb.getText();
            String fatEntered = fat.getText();

            MealEntry entry = new MealEntry(dateEntered, mealEntered, caloriesEntered, proteinEntered, carbEntered, fatEntered);
            tableView.getItems().add(entry);

            date.clear();
            meal.clear();
            cal.clear();
            protein.clear();
            carb.clear();
            fat.clear();
        });

        rightbox.getChildren().addAll(rightBoxLabel, date, meal, cal, protein, carb,fat, saveButton);

        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setLeft(leftbox);
        root.setCenter(tableView);
        root.setRight(rightbox);

        Scene scene = new Scene(root, 1100, 700);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Diet App Main Screen");
    }
    private void loadProfileScreen() {
        ProfileScreen profileScreen = new ProfileScreen(stage);
        profileScreen.show();
    }
}

