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
import javafx.scene.image.Image;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

//main Diet App Page to be displayed after the splashscreen and loginscreen
public class DietAppMainScreen{
    private TextField meal  = new TextField();
    private TextField cal  = new TextField();
    private TextField pro = new TextField();
    private TextField carb = new TextField();
    private TextField fat = new TextField();
    private firebaseDBController db = new firebaseDBController();
    private DatePicker datePicker = new DatePicker();
    private final Stage stage;
    private final TableView<MealEntry> tableView = new TableView<>();

    public DietAppMainScreen(Stage stage) {
        this.stage = stage;
    }

    //public method to show the screen
    public void show() {
        //setting up the MenuBar
        MenuBar menuBar = new MenuBar();
        menuBar.setId("menuBar");
        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");
        MenuItem profEdit = new MenuItem("Profile");
        MenuItem exitMenuItem = new MenuItem("Exit");

        //MenuItems: Exit with exit the program
        //loadProfileScreen will pull up the profile screen class

        exitMenuItem.setOnAction(e -> System.exit(0));
        profEdit.setOnAction(e -> loadProfileScreen());

        fileMenu.getItems().addAll(exitMenuItem);
        editMenu.getItems().addAll(profEdit);
        menuBar.getMenus().addAll(fileMenu, editMenu);

        //user profile image
        Image profileImage = new Image("/images/userIcon.jpg");
        ImageView profileImageView = new ImageView(profileImage);
        profileImageView.setFitHeight(100);
        profileImageView.setFitWidth(100);
        VBox leftbox = new VBox(profileImageView);
        leftbox.setId("leftbox");
        //Ideally these datapoints will be loaded from a database but we might not have time to fully implement
        Label nameLabel = new Label("Name: Users First Name");
        nameLabel.setId("nameLabel");

        Label goalLabel = new Label("Goal: Users Goal Weight");
        goalLabel.setId("goalLabel");

        Label calorieLabel = new Label("Total Calories Available Today");
        calorieLabel.setId("calorieLabel");

        leftbox.getChildren().addAll(nameLabel, goalLabel, calorieLabel);

        //setting up the TableView for displaying past meals.
        //Center column layout to display meals entered in rightbox
        //needs to read from the Firebase db

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

        //adding everything to the TableView
        tableView.getColumns().addAll(dateCol, mealCol, calCol, proteinCol, carbCol,fatCol);
        db.loadData(tableView);

        //setting up the right box with editable text fields that will populate the central TableView when save is clicked
        //ideally these will be saved in a database and will be loaded when the mainscreen is loaded

        VBox rightbox = new VBox();
        Label rightBoxLabel = new Label("Enter Meal Info");
        rightBoxLabel.setId("rightBoxLabel");
        rightbox.setId("rightbox");

        datePicker.setPromptText("Select a Date");
        meal.setPromptText("Enter Meal");
        cal.setPromptText("Enter Calories");
        pro.setPromptText("Enter Protein");
        carb.setPromptText("Enter Carbs");
        fat.setPromptText("Enter Fat");
        Button saveButton = new Button("Save");

        //setting up the save button functionality
        //needs to be updated to write to the Firebase db
        //and then displayed in the table view
        saveButton.setOnAction(event -> handleSave());
        db.loadData(tableView);
        //adding fields created to rightbox
        rightbox.getChildren().addAll(rightBoxLabel, datePicker, meal, cal, pro, carb,fat, saveButton);

        //setting up borderpane layout and attaching MenuBar/VBoxes/TableView
        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setLeft(leftbox);
        root.setCenter(tableView);
        root.setRight(rightbox);

        //create the scene to display the MainAppScreen
        Scene scene = new Scene(root, 1100, 700);
        //connecting style.css
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Diet App Main Screen");
    }

    //private method to change the scene to the Profile Screen
    private void loadProfileScreen() {
        ProfileScreen profileScreen = new ProfileScreen(stage);
        profileScreen.show();
    }

    private void handleSave(){
        try{
            //Validate data
            String name = meal.getText().trim();
            String caloriesStr = cal.getText().trim();
            String proteinStr = pro.getText().trim();
            String carbStr = carb.getText().trim();
            String fatsStr = fat.getText().trim();
            String id = UUID.randomUUID().toString();
            LocalDate datePicked = datePicker.getValue();
            //validate the date
            if(datePicked == null){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a valid date", ButtonType.OK);
                alert.setTitle("Error");
                alert.showAndWait();
                return;
            }
            String dateEntered = datePicked.format(DateTimeFormatter.ISO_LOCAL_DATE);
            //build mealEntry
            MealEntry entry = new MealEntry(id, dateEntered, name, caloriesStr, proteinStr, carbStr, fatsStr);

            db.addData(entry);

            tableView.getItems().add(entry);
            meal.clear();
            cal.clear();
            pro.clear();
            carb.clear();
            fat.clear();
            datePicker.setValue(null);

        } catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a valid data for calories, protein, carbs and fat",
                    ButtonType.OK);
            alert.setTitle("Error");
            alert.showAndWait();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}

